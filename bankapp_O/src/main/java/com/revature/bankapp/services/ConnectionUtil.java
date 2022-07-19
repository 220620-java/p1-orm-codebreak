package com.revature.bankapp.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConnectionUtil {

	private static ConnectionUtil connUtil;
	private Properties props;
	
	private ConnectionUtil() {
		/*
		 * InputStream propsFile = ConnectionUtil.class.getClassLoader()
		 * .getResourceAsStream("config.properties"); try { props.load(propsFile); }
		 * catch (IOException e) { e.printStackTrace();
		 *	}
		 */
	}
	
	public static synchronized ConnectionUtil getConnectionUtil() {
		if (connUtil == null) {
			connUtil = new ConnectionUtil();
		}
		return connUtil;
	}
	
	public Connection getConnection() {
		Connection conn = null;
		
		String dbUrl = System.getenv("DB_URL");
		String dbUser = System.getenv("DB_USER");
		String dbPass = System.getenv("DB_PASS");
		
		/*
		 * String dbUrl = System.getenv("DB_URL"); 
		 * String dbUser = System.getenv("DB_USER"); 
		 * String dbPass = System.getenv("DB_PASS");
		 */	
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(
					// jdbc:postgresql://pet-app.cziwys5p2mwa.us-east-2.rds.amazonaws.com:5432/postgres?currentSchema=pet_app0
					"jdbc:postgresql://localhost:5432/postgres?currentSchema=bank_app",
					dbUrl,
					dbUser);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return conn;
	}
}
