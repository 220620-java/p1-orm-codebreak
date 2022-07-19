package com.revature.bankapp.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Scanner;

import com.revature.bankapp.ds.List;
import com.revature.bankapp.exceptions.UsernameAlreadyExistsException;
import com.revature.bankapp.models.Account;
import com.revature.bankapp.models.User;
import com.revature.bankapp.services.AccountService;
import com.revature.bankapp.services.ConnectionUtil;
import com.revature.bankapp.services.UserService;

public class BankKiosk {

	private static Scanner sc = new Scanner(System.in);
	private static UserService userService = new UserService();
	private static AccountService accountService = new AccountService();
	
	private static Locale us = new Locale("en", "US");
	Currency usDollar = Currency.getInstance(us);
	private static NumberFormat dollar = NumberFormat.getCurrencyInstance(us);
	
	public static User user = new User();
	public static Account account = new Account();
	
	public static void main(String[] args) {	
		boolean usingKiosk = true;
		
		System.out.println("*********************************");
		System.out.println("=================================");
		System.out.println("    $                       $    ");
		System.out.println("  (($)) Banking Made Easy (($))  ");
		System.out.println("    $                       $    ");
		System.out.println("=================================");
		System.out.println("*********************************");
		
		User user = null;
		
		while(usingKiosk) {
			if(user == null) {
				System.out.println("                                  ");
				System.out.println("<<<<<<<<<<<<<Welcome>>>>>>>>>>>>>>");
				System.out.println("Log in to Your Account or Register\n"
						+ "1. Log In\n"
						+ "2. Register\n"
						+ "3. Exit");
				System.out.println("<<<<<<<<<<<<<<<<$>>>>>>>>>>>>>>>>>");
				System.out.println("                                  ");
				String input = sc.nextLine();
				
				switch(input) {
				case "1":
					user = userlogIn();
					break;
				case "2":
					registerUser();
					break;
				default:
					usingKiosk = false;
					System.out.println("Thank You for your Service!");
				}
			}
			if(user != null) {
				System.out.println("******************************");
				System.out.println("Welcome " + user.getFirstname() + " " + user.getLastname());
				System.out.println("1. Open New Account\n"
						+ "2. Account Details\n"
						+ "3. View All Accounts\n"
						+ "4. Deposit\n"
						+ "5. Withdraw\n"
						+ "6. Log out");
				System.out.println("******************************");
				
				String input = sc.nextLine();
				switch (input) {
				case "1":
					//System.out.println("open account");
					openAccount(user);
					break;
				case "2":
					//System.out.println("account detail");
					accountDetails(account, user);
					break;
				case "3":
					//System.out.println("view all accounts");
					allAccounts(user);
					break;
				case "4":
					//System.out.println("deposit");
					deposit(account, user);
					break;
				case "5":
					//System.out.println("withdraw");
					withdraw(account, user);
					break;
				default:
					System.out.println("Log Out");
					user = null;
				}
			}	
		}
		sc.close();
	}
	
	private static User userlogIn() {
		boolean loggingIn = true;
		
		while(loggingIn) {
			System.out.println("Enter username");
			String username = sc.nextLine();
			
			System.out.println("Enter password");
			String password = sc.nextLine();
			
			User user = userService.logIn(username, password);
			
			if(user == null) {
				System.out.println("Must be a glitch in the Matrix, try again!");
				loggingIn = false;
			} else {
				return user;
			}
		}
		//return user = userlogIn();
		return null;
	}
	
	private static void registerUser() {
		boolean registering = true;
		
		while(registering) {
			System.out.println("Please fill out all information");
			
			System.out.println("Enter First Name:");
			String firstname = sc.nextLine();
			
			System.out.println("Enter Last Name:");
			String lastname = sc.nextLine();
			
			System.out.println("Enter username:");
			String username = sc.nextLine();
			
			System.out.println("Enter password:");
			String password = sc.nextLine();
			
			System.out.println("Type \"y\" to confirm\n"
					+ "Type \"n\" to try again\n"
					+ "Press any key to go back to the main menu");
			
			String yes = sc.nextLine().toLowerCase();
			
			switch(yes) {
			case "y":
				User user = new User(firstname, lastname, username, password);
				try {
					userService.registerUser(user);
					registering = false;
					System.out.println("Registration successful!");
				} catch (UsernameAlreadyExistsException e) {
					System.out.println("More glitches in the Matrix, that username already exists! Try again?!");
				}
				break;
			case "n":
				System.out.println("Try again??!");
				break;
			default:
				System.out.println("Back to main menu");
				registering = false;
			}
		}
	}
	
	private static User openAccount(User user) {
		boolean newAccount = true;
		
		while(newAccount) {
			String accountType = "";
			System.out.println("What type of account would you like to open?\n"
					+ "Checking\n"
					+ "Savings\n");
			
			String type = sc.nextLine();
			
			switch(type) {
			case "1":
				accountType = "Checking";
				break;
			case "2":
				accountType = "Savings";
				break;
			default:
				newAccount = false;
			}
			System.out.println("Please enter the deposit amount: (format X.XX)");
			
			double accountBal = sc.nextDouble();
			sc.nextLine();
			Account nAccount = new Account(accountType, accountBal);
			nAccount.setUser_id(user.getId());
			nAccount.setAccountType(type);
			accountService.createAccount(nAccount);
			
			System.out.println("Successfully opened " + accountType + "account");
			
			newAccount = false;		
		}
		return null;	
	}
	
	private static Account accountDetails(Account account, User user) {
		accountService.accountDetail(account, user);
		System.out.println("******************************");
		System.out.println(account.getAccountType() + " Account Details");
		System.out.println("Account Number: " + account.getId());
		System.out.println("User ID: " + user.getId());
		System.out.println("Account Type: " + account.getAccountType());
		System.out.println("Account Balance: " + dollar.format(account.getAccountBal()));
		System.out.println("******************************");
		return account;	
	}
	
	private static User allAccounts(User user) {
		List<Account> accounts = accountService.getAllAccounts(user);
		System.out.println("******************************");
		System.out.println("    All Available Accounts    ");
		System.out.println("******************************");
		for (int i = 0; i < accounts.size(); i++) {
			System.out.println("Account Type: " + accounts.get(i).getAccountType());
			System.out.println("Account Id: " + accounts.get(i).getId());
			System.out.println("Balance: " + dollar.format(accounts.get(i).getAccountBal()));
			System.out.println("******************************");
		}
		return user;
	}
	
	private static Account deposit(Account account, User user) {
		accountService.accountDetail(account, user);
		System.out.println("*************************");
		System.out.println(account.getAccountType() + " Account\n" + "Account ID: " + account.getId());
		System.out.println("Balance: " + dollar.format(account.getAccountBal()));
		System.out.println("Enter amount to deposit: ");
		double depositAmt = sc.nextDouble();
		sc.nextLine();
		accountService.deposit(account, depositAmt);
		System.out.println("Updated Account Balance: " + dollar.format(account.getAccountBal()));
		System.out.println("*************************");
		return account;
	}
	
	private static Account withdraw(Account account, User user) {
		accountService.accountDetail(account, user);
		System.out.println("*************************");
		System.out.println(account.getAccountType() + " Account\n" + "Account ID: " + account.getId());
		System.out.println("Balance: " + dollar.format(account.getAccountBal()));
		System.out.println("Enter amount to withdraw: ");
		double withdrawAm = sc.nextDouble();
		sc.nextLine();
		accountService.withdraw(account, withdrawAm);
		System.out.println("Updated Account Balance: " + dollar.format(account.getAccountBal()));
		System.out.println("*************************");
		return account;
	}
	
}
