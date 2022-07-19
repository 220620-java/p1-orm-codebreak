package com.revature.bankapp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.bankapp.ds.List;
import com.revature.bankapp.models.User;
import com.revature.bankapp.services.ConnectionUtil;


public class UserPostgres implements UserDAO {

	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();
	
	@Override
	public User create(User user) {

		try (Connection conn = connUtil.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "insert into users " 
					+ "(user_id, firstname, lastname, username, passwd)"
					+ "VALUES (default, ?, ?, ?, ?)";
			
			String[] keys = {"user_id"};
			PreparedStatement st = conn.prepareStatement(sql, keys);
			st.setString(1, user.getFirstname());
			st.setString(2, user.getLastname());
			st.setString(3, user.getUsername());
			st.setString(4, user.getPassword());
			
			int rowsAffected = st.executeUpdate();
			ResultSet result = st.getGeneratedKeys();
			if (result.next() && rowsAffected == 1) {
				user.setId(result.getInt("user_id"));
				conn.commit();
			} else {
				//conn.rollback();
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(User t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(User user) {
		
		try (Connection conn = connUtil.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "delete from users where users_id = ?";
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, user.getId());
			
			int rowsAffected = st.executeUpdate();
			if (rowsAffected <= 1) {
				//conn.commit();
			} else {
				//conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public User findByUsername(String username) {
		
		User user = null;
		try (Connection conn = connUtil.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "select * from users where username = ?";
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, username);
			
			ResultSet result = st.executeQuery();
			
			if(result.next()) {
				 user = new User();
				 user.setId(result.getInt("user_id"));
				 user.setFirstname(result.getString("firstname"));
				 user.setLastname(result.getString("lastname"));
				 user.setUsername(result.getString("username"));
				 user.setPassword(result.getString("passwd"));
			}
			//conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

}
