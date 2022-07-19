package com.revature.bankapp.services;

import com.revature.bankapp.data.UserDAO;
import com.revature.bankapp.data.UserPostgres;
import com.revature.bankapp.exceptions.UsernameAlreadyExistsException;
import com.revature.bankapp.models.User;

public class UserService {

	private UserDAO userDao = new UserPostgres();
	
	public User registerUser(User user) throws UsernameAlreadyExistsException {
		user = userDao.create(user);
		
		if(user == null) {
			throw new UsernameAlreadyExistsException();
		}
		return user;
	}

	
	public User logIn(String username, String password) {
		User user = userDao.findByUsername(username);
		
		if(user != null && (password != null && password.equals(user.getPassword()))) {
			return user;
		} else {
			return null;
		}
	}
	
}