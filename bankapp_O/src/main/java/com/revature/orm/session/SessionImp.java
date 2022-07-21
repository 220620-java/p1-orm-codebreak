package com.revature.orm.session;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.revature.orm.ORMQuery;
import com.revature.orm.ORMSession;
import com.revature.orm.ORMTransaction;
import com.revature.orm.ParsedObject;
import com.revature.orm.connection.ConnectionManager;
import com.revature.orm.connection.RevConnectionManager;
import com.revature.orm.connection.StatementWriter;
import com.revature.orm.connection.WriterFactory;

public class SessionImp implements ORMSession {

	private Properties prop;
	private ConnectionManager connManager;
	
	public SessionImp() throws SQLException {
		prop = new Properties();
		
		InputStream propsFile = SessionImp.class.getClassLoader().getResourceAsStream("database.properties");
		try {
			prop.load(propsFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String dbUrl = prop.getProperty("DB_URL");
		String dbUser = prop.getProperty("DB_USER");
		String dbPass = prop.getProperty("DB_PASS");
		String dbDriver = prop.getProperty("DB_DRIVER");
		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		connManager = RevConnectionManager.getConnectionManager(dbUrl, dbUser, dbPass);
	}
	
	@Override
	public <T> ORMTransaction<T> beginTransaction(Class<T> type) {
		StatementWriter<T> writer = WriterFactory.getSQLWriter(prop.getProperty("DB_DRIVER"), type);
		List<Object> st = new LinkedList<>();
		return new TransactionImp<T>(connManager.getConnection(), writer, st);
	}

	@Override
	public <T> ORMQuery<T> createQuery(Class<T> type) {
		StatementWriter<T> writer = WriterFactory.getSQLWriter(prop.getProperty("DB_DRIVER"), type);
		ParsedObject parsedObj = new ParsedObject(type);
		return new QueryImp<T>(connManager.getConnection(), writer, parsedObj);
	}

	
}
