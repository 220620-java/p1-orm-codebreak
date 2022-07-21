package com.revature.bankapp.data.orm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.bankapp.data.UserDAO;
import com.revature.bankapp.models.User;
import com.revature.orm.ORMQuery;
import com.revature.orm.ORMSession;
import com.revature.orm.ORMTransaction;
import com.revature.orm.session.SessionImp;

public class UserORM implements UserDAO {

	@Override
	public User create(User user) throws SQLException {
		ORMTransaction<User> tx = null;
		try {
			ORMSession session = new SessionImp();
			tx = session.beginTransaction(User.class);
			tx = tx.addStatement("INSERT", user);
			tx.execute();
			tx.commit();
			int id = (int) tx.getGeneratedKeys().get(0);
			user.setId(id);
		} catch (SQLException e) {
			if (e.getMessage().contains("unique constraint")) {
				throw e;
			} else {
				if (tx != null) {
					tx.rollback();
					tx.close();
				}
				e.printStackTrace();
			}
		}
		return user;
	}

	@Override
	public User findById(int id) {
		User user = null;
		try {
			ORMSession session = new SessionImp();
			ORMQuery<User> query = session.createQuery(User.class);
			user = query.findbyId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public List<User> findAll() {
		List<User> users = new ArrayList<>();
		try {
			ORMSession session = new SessionImp();
			ORMQuery<User> query = session.createQuery(User.class);
			users = query.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public void update(User user) {
		try {
			ORMSession session = new SessionImp();
			ORMTransaction<User> tx = session.beginTransaction(User.class);
			tx.addStatement("UPDATE", user).execute();
			tx.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(User user) {
		try {
			ORMSession session = new SessionImp();
			ORMTransaction<User> tx = session.beginTransaction(User.class);
			tx.addStatement("DELETE", user).execute();
			tx.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public User findByUsername(String username) {
		User user = null;
		try {
			ORMSession session = new SessionImp();
			ORMQuery<User> query = session.createQuery(User.class);
			user = query.findOneBy("username", username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	
}
