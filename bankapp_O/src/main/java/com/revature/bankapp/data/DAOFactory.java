package com.revature.bankapp.data;

import com.revature.bankapp.data.orm.AccountORM;
import com.revature.bankapp.data.orm.UserORM;

public class DAOFactory {

	public static UserDAO getUserDAO() {
		return new UserORM();
	}
	
	public static AccountDAO getAccountDAO() {
		return new AccountORM();
	}
}
