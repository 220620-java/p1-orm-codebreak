package com.revature.orm.connection;

import java.sql.Connection;

public interface ConnectionManager {

	public Connection getConnection();
	
	public boolean releaseConnection(Connection conn);
}
