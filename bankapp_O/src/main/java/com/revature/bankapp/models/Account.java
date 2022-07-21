package com.revature.bankapp.models;

import java.util.Objects;

import com.revature.orm.annotations.PrimaryKey;
import com.revature.orm.annotations.Relationship;
import com.revature.orm.enums.RelationshipType;

public class Account {
	@PrimaryKey
	private int account_id;
	@Relationship(type=RelationshipType.MANY_TO_ONE, ownerJoinColumn="user_id")
	private int user_id;
	private String accountType;
	private double accountBal;
	
	public Account() {
		this.account_id = 0;
		this.user_id = 0;
		this.accountType = "";
		this.accountBal = 0;
	}
	
	public Account(int user_id) {
		this.user_id = user_id;
	}
	
	public Account(String accountType, double accountBal) {
		this.accountType = accountType;
		this.accountBal = accountBal;
	}
	
	public Account(int account_id, String accountType, double accountBal) {
		this.account_id = account_id;
		this.accountType = accountType;
		this.accountBal = accountBal;
	}
	
	public Account(int account_id, String accountType, double accountBal, int user_id) {
		this.account_id = account_id;
		this.accountType = accountType;
		this.accountBal = accountBal;
		this.user_id = user_id;
	}
	
//	void depost (int amount) {
//		if(amount != 0) {
//			accountBal = accountBal + amount;
//			previousTransaction = amount;
//		}
//	}
//	
//	void withdraw(int amount) {
//		if(amount != 0) {
//			accountBal = accountBal - amount;
//			previousTransaction = -amount;
//		}
//	}
//	
//	void getPreviousTransaction() {
//		if(previousTransaction > 0) {
//			System.out.println("Deposited: " + previousTransaction);
//		}
//		if(previousTransaction < 0) {
//			System.out.println("Withdrawn: " + Math.abs(previousTransaction));
//		}
//		else {
//			System.out.println("No transaction occured");
//		}
//	}

	public int getId() {
		return account_id;
	}

	public void setId(int account_id) {
		this.account_id = account_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getAccountBal() {
		return accountBal;
	}

	public void setAccountBal(double accountBal) {
		this.accountBal = accountBal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountBal, accountType, account_id, user_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return Double.doubleToLongBits(accountBal) == Double.doubleToLongBits(other.accountBal)
				&& Objects.equals(accountType, other.accountType) && account_id == other.account_id
				&& user_id == other.user_id;
	}

	@Override
	public String toString() {
		return "Account [id=" + account_id + ", user_id=" + user_id + ", accountType=" + accountType + ", accountBal="
				+ accountBal + ", previousTransaction=" + "]";
	}
	
}
