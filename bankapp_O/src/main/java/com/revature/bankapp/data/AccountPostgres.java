package com.revature.bankapp.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.bankapp.ds.ArrayList;
import com.revature.bankapp.ds.List;
import com.revature.bankapp.models.Account;
import com.revature.bankapp.models.User;
import com.revature.bankapp.services.ConnectionUtil;

public class AccountPostgres implements AccountDAO {

	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();
	
	@Override
	public Account create(Account account) {
		
		try (Connection conn = connUtil.getConnection()) {
			conn.setAutoCommit(false);
			
			String sql = "insert into accounts "
					+ "(account_id, acounttype, accountbal, user_id)"
					+ "values (default, ?, ?, ?)";
			
			String[] keys = {"account_id"};
			PreparedStatement st = conn.prepareStatement(sql, keys);
			st.setString(1, account.getAccountType());
			st.setDouble(2, account.getAccountBal());
			st.setInt(3, account.getUser_id());
			
			int rowsAffected = st.executeUpdate();
			ResultSet result = st.getGeneratedKeys();
			if (result.next() && rowsAffected == 1) {
				account.setId(result.getInt("account_id"));
				conn.commit();
			} else {
				conn.rollback();
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	@Override
	public Account findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Account account) {
		try (Connection conn = connUtil.getConnection()) {
			conn.setAutoCommit(false);
			
			String sql = "update accounts"
					+ "set acounttype=?"
					+ "set accountbal=?"
					+ "where account_id=?";
					
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, account.getAccountType());
			st.setDouble(2, account.getAccountBal());
			st.setInt(3, account.getId());
			
			int rowsAffected = st.executeUpdate();
			if (rowsAffected <= 1) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Account account) {
		try (Connection conn = connUtil.getConnection()) {
			conn.setAutoCommit(false);
			
			String sql = "delete from accounts where account_id = ?";
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, account.getId());
			
			int rowsAffected = st.executeUpdate();
			if (rowsAffected <= 1) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateBalance(Account account) {
		try (Connection conn = connUtil.getConnection()) {
			conn.setAutoCommit(false);
			
			String sql = "update accounts "
					+ "set accountbal = ?"
					+ "where account_id = ?";
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, account.getAccountBal());
			st.setInt(2, account.getId());
			
			int rowsAffected = st.executeUpdate();
			if (rowsAffected <= 1) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Account getDetail(Account account, User user) {
		try (Connection conn = connUtil.getConnection()) {
			conn.setAutoCommit(false);
			
			String sql = "select * from accounts where user_id = ?";
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, user.getId());
			ResultSet result = st.executeQuery();
			if(result.next()) {
				int account_id = result.getInt("account_id");
				String accountType = result.getString("acounttype");
				double accountBal = result.getDouble("accountbal");
				account.setId(account_id);
				account.setAccountType(accountType);
				account.setAccountBal(accountBal);			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	@Override
	public List<Account> findAllAccounts(User user) {
		List<Account> allAccounts = new ArrayList<>();
		
		try (Connection conn = connUtil.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "select * from accounts where user_id = ?";
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, user.getId());
			
			ResultSet result = st.executeQuery();
			while (result.next()) {
				//int user_id = result.getInt("accounts.user_id");
				int account_id = result.getInt("account_id");
				String accountType = result.getString("acounttype");
				double accountBal = result.getDouble("accountbal");
				Account account = new Account(account_id, accountType, accountBal);
				allAccounts.add(account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allAccounts;
	}
}
