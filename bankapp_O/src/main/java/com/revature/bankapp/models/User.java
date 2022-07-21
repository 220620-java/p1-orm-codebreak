package com.revature.bankapp.models;

import java.util.Objects;

import com.revature.bankapp.ds.ArrayList;
import com.revature.bankapp.ds.List;
import com.revature.orm.annotations.PrimaryKey;
import com.revature.orm.annotations.Relationship;
import com.revature.orm.annotations.Table;
import com.revature.orm.enums.RelationshipType;

@Table(name="user")
public class User {
	@PrimaryKey
	private int id;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	@Relationship(type=RelationshipType.ONE_TO_MANY, ownedJoinColumn="user_id")
	private List<Account> accounts;
	
	public User() {
		this.id = 0;
		this.firstname = "";
		this.lastname = "";
		this.username = "";
		this.password = "";
		this.accounts = new ArrayList<>();
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.accounts = new ArrayList<>();
	}
	
	public User(String firstname, String lastname, String username, String password ) {
		this.id = 0;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.password = password;
		this.accounts = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accounts, firstname, id, lastname, password, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(accounts, other.accounts) && Objects.equals(firstname, other.firstname) && id == other.id
				&& Objects.equals(lastname, other.lastname) && Objects.equals(password, other.password)
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", username=" + username
				+ ", password=" + password + ", accounts=" + accounts + "]";
	}
}
