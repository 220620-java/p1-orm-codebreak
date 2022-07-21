package com.revature.orm.session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.orm.ORMQuery;
import com.revature.orm.ParsedObject;
import com.revature.orm.connection.ConnectionManager;
import com.revature.orm.connection.RevConnectionManager;
import com.revature.orm.connection.StatementWriter;
import com.revature.orm.exceptions.UnsupportedModelException;

public class QueryImp<T> implements ORMQuery<T> {

	private final Connection conn;
	private final StatementWriter<T> writer;
	private final ParsedObject parsedObj;
	
	QueryImp(Connection conn, StatementWriter<T> writer, ParsedObject parsedObj) {
		this.conn = conn;
		this.writer = writer;
		this.parsedObj = parsedObj;
	}
	
	@Override
	public T findbyId(Object id) throws SQLException {
		T obj = null;
		
		try (PreparedStatement st = writer.findById(id, conn)) {
			st.setObject(1, id);
			ResultSet result = st.executeQuery();
			if(result.next()) {
				try {
					obj = (T) setValues(this.parsedObj, result);
				} catch (Exception e) {
					throw new UnsupportedModelException("Model issue, please check constructor");
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		stopConn();
		return obj;
	}

	@Override
	public T findOneBy(String field, Object value) throws SQLException {
		T obj = null;
		
		try(PreparedStatement st = writer.findBy(field, value, conn)) {
			st.setObject(1, value);
			ResultSet result = st.executeQuery();
			if(result.next()) {
				try {
					obj = (T) setValues(this.parsedObj, result);
				} catch (IllegalAccessException | InstantiationException e) {
					throw new UnsupportedModelException("Model issue, please check constructor");
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		stopConn();
		return obj;
	}

	@Override
	public List<T> findAllBy(String field, Object value) throws SQLException {
		List<T> list = new ArrayList<>();

		try (PreparedStatement st = writer.findBy(field, value, conn)) {
			st.setObject(1, value);
			ResultSet result = st.executeQuery();

			while (result.next()) {
				try {
					T obj = (T) setValues(this.parsedObj, result);
					list.add(obj);
				} catch (IllegalAccessException | InstantiationException e) {
					throw new UnsupportedModelException("Model issue, please check constructor");
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		stopConn();
		return list;
	}

	@Override
	public List<T> findAll() throws SQLException {
		List<T> list = new ArrayList<>();

		try (Statement st = conn.createStatement()) {
			ResultSet result = st.executeQuery(writer.findAll());

			while (result.next()) {
				try {
					T obj = (T) setValues(this.parsedObj, result);
					list.add(obj);
				} catch (IllegalAccessException | InstantiationException e) {
					throw new UnsupportedModelException("Your model is missing a no-arguments constructor.");
				}
			}
		} catch (SQLException e) {
			throw e;
		}
		stopConn();
		return list;
	}
	
	private Object setValues(ParsedObject parsedObj, ResultSet result) throws SQLException, InstantiationException, IllegalAccessException {
		Object obj = parsedObj.getOriginalType().newInstance();
		try {
			for (String fieldName : parsedObj.getColumns().keySet()) {
				String columnName = parsedObj.getColumns().get(fieldName);
				Object columnValue = result.getObject(parsedObj.getTableName() + "_" + columnName);
				String methodName = "set" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
				if (fieldExists(obj, fieldName) && obj.getClass().getDeclaredField(fieldName).isAccessible()) {
					obj.getClass().getDeclaredField(fieldName).set(obj, columnValue);
				} else if (methodExists(obj, methodName, columnValue.getClass())) {
					obj.getClass().getDeclaredMethod(methodName, columnValue.getClass()).invoke(obj, columnValue);
				} else {
					for (ParsedObject relationshipField : parsedObj.getRelationships().keySet()) {
						if (fieldName.equals(getCamelCase(relationshipField.getTableName()))) {
							Object sObj = setValues(relationshipField, result);
							if (methodExists(obj, methodName, sObj.getClass())) {
								obj.getClass().getDeclaredMethod(methodName, sObj.getClass()).invoke(obj, sObj);
							}
						}
					}
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			UnsupportedModelException e1 = new UnsupportedModelException("Check your model's fields");
			e1.initCause(e);
			throw e1;
		}
		return obj;
	}
	
	private void stopConn() {
		ConnectionManager manager = RevConnectionManager.getConnectionManager();
		manager.releaseConnection(conn);
	}
	
	private boolean fieldExists(Object object, String fieldName) {
		try {
			object.getClass().getDeclaredField(fieldName);
			return true;
		} catch (NoSuchFieldException e) {
			return false;
		}
	}
	
	private boolean methodExists(Object object, String methodName, Class<?> type) {
		for (Method method : object.getClass().getDeclaredMethods()) {
			if (method.getName().contains(methodName)) {
				if (method.getParameterTypes().length==1 && method.getParameterTypes()[0].equals(type)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private Object getCamelCase(String snakeCase) {
		String[] words = snakeCase.toString().split("[_]");

		StringBuilder finalFieldName = new StringBuilder(words[0]);
		for (int i = 1; i < words.length; i++) {
			finalFieldName.append(words[i].toUpperCase().charAt(0) + words[i].substring(1));
		}
		return finalFieldName.toString();
	}
}
