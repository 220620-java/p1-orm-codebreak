package com.revature.bankapp.data;

import com.revature.bankapp.ds.List;
import com.revature.bankapp.models.Account;
import com.revature.bankapp.models.User;

public interface AccountDAO extends DataAccessObject<Account> {

	public void updateBalance(Account account);
	
	public Account getDetail(Account account, User user);
	
	public List<Account> findAllAccounts(User user);
	
}
