package com.revature.bankapp.services;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import com.revature.bankapp.data.AccountDAO;
import com.revature.bankapp.data.AccountPostgres;
import com.revature.bankapp.models.Account;
import com.revature.bankapp.models.User;

public class AccountService {

	private AccountDAO accountDao = new AccountPostgres();
	private static Locale us = new Locale("en", "US");
	Currency usDollar = Currency.getInstance(us);
	private static NumberFormat dollar = NumberFormat.getCurrencyInstance(us);
	
	public Account createAccount(Account account) {
//		account = accountDao.create(account);
//		
//		if(account == null) {
//			System.out.println("Must be a glitch, try again");
//		}
//		return account;
		try {
			account = accountDao.create(account);
		} catch (SQLException e) {
			System.out.println("Must be a glitch, try again");
		}
		return account;
	}
	
	public Account accountDetail(Account account, User user) {
		accountDao.getDetail(account, user);
		return account;
	}
	
	public List<Account> getAllAccounts(User user) {
		return accountDao.findAllAccounts(user);
	}
	
	public Account deposit(Account account, double amount) {
		
		if(amount >= 0) {
			double deposit = account.getAccountBal() + amount;
			account.setAccountBal(deposit);
			accountDao.updateBalance(account);
			System.out.println("Deposit: " + dollar.format(amount) + "\nDeposit Successful!!");
		} else {
			System.out.println("Another glitch in the Matrix, Invalid deposit amount!");
		}
		return account;
	}
	
	public Account withdraw(Account account, double amount) {
		
		if(account.getAccountBal() >= amount) {
			double withdraw = account.getAccountBal() - amount;
			account.setAccountBal(withdraw);
			accountDao.updateBalance(account);
			System.out.println("Withdraw: " + dollar.format(amount) + "\nWithdraw Successful!!");
		} else {
			System.out.println("Money doesn't grown in the Matrix, Insufficient funds unavailable");
		}
		return account;
	}
}
