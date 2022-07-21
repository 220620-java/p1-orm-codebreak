package com.revature.orm;

import java.sql.SQLException;
import java.util.List;

public interface ORMQuery<T> {

	public T findbyId(Object id) throws SQLException;
	
	public T findOneBy(String field, Object value) throws SQLException;
	
	public List<T> findAllBy(String field, Object value) throws SQLException;
	
	public List<T> findAll() throws SQLException;
	
}
