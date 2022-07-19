package com.revature.bankapp.data;

import com.revature.bankapp.models.User;

public interface UserDAO extends DataAccessObject<User> {

	public User findByUsername(String username);
}
