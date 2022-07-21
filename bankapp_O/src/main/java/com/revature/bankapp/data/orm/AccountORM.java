package com.revature.bankapp.data.orm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.bankapp.data.AccountDAO;
import com.revature.bankapp.models.Account;
import com.revature.bankapp.models.User;
import com.revature.orm.ORMQuery;
import com.revature.orm.ORMSession;
import com.revature.orm.ORMTransaction;
import com.revature.orm.session.SessionImp;

public class AccountORM implements AccountDAO {

	@Override
	public Account create(Account account) throws SQLException {
		ORMTransaction<Account> tx = null;
		try {
			ORMSession session = new SessionImp();
			tx = session.beginTransaction(Account.class);
			tx = tx.addStatement("INSERT", account);
			tx.execute();
			tx.commit();
			int id = (int) tx.getGeneratedKeys().get(0);
			account.setId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	@Override
	public Account findById(int id) {
		Account account = null;
		try {
			ORMSession session = new SessionImp();
			ORMQuery<Account> query = session.createQuery(Account.class);
			account = query.findbyId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	@Override
	public List<Account> findAll() {
		List<Account> account = new ArrayList<>();
		try {
			ORMSession session = new SessionImp();
			ORMQuery<Account> query = session.createQuery(Account.class);
			account = query.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	@Override
	public void update(Account account) {
		try {
			ORMSession session = new SessionImp();
			ORMTransaction<Account> tx = session.beginTransaction(Account.class);
			tx.addStatement("UPDATE", account).execute();
			tx.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void delete(Account account) {
		try {
			ORMSession session = new SessionImp();
			ORMTransaction<Account> tx = session.beginTransaction(Account.class);
			tx.addStatement("DELETE", account).execute();
			tx.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateBalance(Account account) {
		try {
			ORMSession session = new SessionImp();
			ORMTransaction<Account> tx = session.beginTransaction(Account.class);
			tx.addStatement("UPDATE", account).execute();
			tx.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Account getDetail(Account account, User user) {
		
		return account;
	}

	@Override
	public List<Account> findAllAccounts(User user) {
		List<Account> account = new ArrayList<>();
		try {
			ORMSession session = new SessionImp();
			ORMQuery<Account> query = session.createQuery(Account.class);
			account = query.findAllBy("accounts", user.getAccounts());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	
}
