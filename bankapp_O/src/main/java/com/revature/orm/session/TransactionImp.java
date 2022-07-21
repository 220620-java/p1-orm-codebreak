package com.revature.orm.session;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.LinkedList;
import java.util.List;

import com.revature.orm.ORMTransaction;
import com.revature.orm.ParsedObject;
import com.revature.orm.annotations.Relationship;
import com.revature.orm.connection.ConnectionManager;
import com.revature.orm.connection.RevConnectionManager;
import com.revature.orm.connection.StatementWriter;
import com.revature.orm.exceptions.InvalidKeywordException;
import com.revature.orm.exceptions.UnsupportedModelException;

public class TransactionImp<T> implements ORMTransaction<T> {

	private Connection conn;
	private final StatementWriter<T> writer;
	private List<Object> sts;
	private List<Savepoint> savepoint = new LinkedList<>();
	private List<Object> generatedKeys = new LinkedList<>();
	
	TransactionImp(Connection conn, StatementWriter<T> writer, List<Object> sts) {
		this.conn = conn;
		this.writer = writer;
		this.sts = sts;
	}
	
	@Override
	public ORMTransaction<T> addStatement(String keyword, Object obj) throws SQLException {
		keyword = keyword.toUpperCase();
		PreparedStatement st = null;
		switch (keyword) {
		case "INSERT":
			st = writer.insert((T) obj, conn);
			st = setInsertValues(obj, st);
			break;
		case "UPDATE":
			st = writer.update((T) obj, conn);
			st = setUpdateValues(obj, st);
			break;
		case "DELETE":
			st = writer.delete((T) obj, conn);
			Object primaryKeyValue2 = getPkValue(obj);
			st.setObject(1, primaryKeyValue2);
			break;
		default:
			throw new InvalidKeywordException();
		}
		if (st != null) {
			sts.add(st);
		}
		return new TransactionImp<T>(conn, writer, sts);
	}
	
	private PreparedStatement setUpdateValues(Object obj, PreparedStatement st) throws SQLException {
		int parameterIndex = 1;
		ParsedObject parsedObj = new ParsedObject(obj.getClass());
		try {
			for (String fieldName : parsedObj.getColumns().keySet()) {
				if (!fieldName.equals(parsedObj.getPrimaryKeyField())) {
					st = setValue(fieldName, obj, st, parameterIndex++, parsedObj);
				}
			}
			st.setObject(parameterIndex++, getPkValue(obj));
		} catch (Exception e) {
			UnsupportedModelException e1 = new UnsupportedModelException("Model has error in fields");
			e1.initCause(e);
			throw e1;
		}
		return st;
	}
	
	private PreparedStatement setInsertValues(Object obj, PreparedStatement st) throws SQLException {
		int parameterIndex = 1;
		ParsedObject parsedObj = new ParsedObject(obj.getClass());
		try {
			for (String fieldName : parsedObj.getColumns().keySet()) {
				if (!fieldName.equals(parsedObj.getPrimaryKeyField())) {
					st = setValue(fieldName, obj, st, parameterIndex++, parsedObj);
				}
			}
		} catch (Exception e) {
			UnsupportedModelException e1 = new UnsupportedModelException("Model has error in fields");
			e1.initCause(e);
			throw e1;
		}
		return st;
	}
	
	private PreparedStatement setValue(String fieldName, Object obj, PreparedStatement st, int parameterIndex, ParsedObject parsedObj)
		throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, InvocationTargetException,
		NoSuchMethodException, SQLException {
		Object value = null;
		if (fieldExists(obj, fieldName) && obj.getClass().getDeclaredField(fieldName).isAccessible()) {
			value = obj.getClass().getDeclaredField(fieldName).get(obj);
		} else {
			String methodName = "get" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
			value = obj.getClass().getDeclaredMethod(methodName).invoke(obj);
		}
		if (fieldExists(obj, fieldName) && obj.getClass().getDeclaredField(fieldName).isAnnotationPresent(Relationship.class)) {
			value = getPkValue(value);
		}
		st.setObject(parameterIndex, value);
		return st;
	}
	
	private Object getPkValue(Object obj) {
		Object primaryKeyValue = null;
		try {
			ParsedObject parsedObj = new ParsedObject(obj.getClass());
			String pkFieldName = parsedObj.getPrimaryKeyField();
			if (fieldExists(obj, pkFieldName) && obj.getClass().getDeclaredField(pkFieldName).isAccessible()) {
				primaryKeyValue = obj.getClass().getDeclaredField(pkFieldName).get(obj);
			} else {
				String methodName = "get" + pkFieldName.toUpperCase().charAt(0) + pkFieldName.substring(1);
				primaryKeyValue = obj.getClass().getDeclaredMethod(methodName).invoke(obj);
			} 
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			UnsupportedModelException e1 = new UnsupportedModelException("Model has error in fields");
			e1.initCause(e);
			throw e1;
		}
		return primaryKeyValue;
 	}
	
	@Override
	public int execute() throws SQLException {
		conn.setAutoCommit(false);
		
		int rowsUpdated = 0;
		for (Object stOrSvpt : sts) {
			if (stOrSvpt instanceof PreparedStatement) {
				PreparedStatement st = (PreparedStatement) stOrSvpt;
				rowsUpdated += st.executeUpdate();
				ResultSet result = st.getGeneratedKeys();
				if (result.next()) {
					generatedKeys.add(result.getObject(1));
				}
			} else if (stOrSvpt instanceof String) {
				Savepoint svpt = conn.setSavepoint(stOrSvpt.toString());
				savepoint.add(svpt);
			}
		}
		return rowsUpdated;
	}
	
	@Override
	public List<Object> getGeneratedKeys() {
		return generatedKeys;
	}
	
	@Override
	public void commit() throws SQLException {
		conn.commit();
		stopConn();
		
	}
	
	@Override
	public void rollback() throws SQLException {
		conn.rollback();
		sts = new LinkedList<>();
		
	}
	
	@Override
	public void rollbackToSavepoint(String name) throws SQLException {
		int index = sts.indexOf(name);
		for (int i = index; i < sts.size(); i++) {
			sts.remove(i);
		}
		for (Savepoint svpt : savepoint) {
			if (svpt.getSavepointName().equals(name)) {
				conn.rollback(svpt);
				return;
			}
		}
		throw new SQLException("No savepoint found");
	}
	
	@Override
	public ORMTransaction<T> addSavepoint(String name) {
		sts.add(name);
		return new TransactionImp<T>(conn, writer, sts);
	}
	
	private void stopConn() {
		ConnectionManager manager = RevConnectionManager.getConnectionManager();
		manager.releaseConnection(conn);
		conn = null;
	}
	
	@Override
	public void close() throws SQLException {
		stopConn();
		
	}
	
	private boolean fieldExists(Object object, String fieldName) {
		try {
			object.getClass().getDeclaredField(fieldName);
			return true;
		} catch (NoSuchFieldException e) {
			return false;
		}
	}
	
}
