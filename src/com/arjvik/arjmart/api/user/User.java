package com.arjvik.arjmart.api.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
	private int ID;
	private String email;
	private String password;
	private List<String> roles;
	
	public User() {
		roles = new ArrayList<>();
	}
	public User(int ID, String email, String password, List<String> roles) {
		this();
		this.ID = ID;
		this.email = email;
		this.password = password;
		this.roles.addAll(roles);
	}
	public int getID() {
		return ID;
	}
	public void setID(int ID) {
		this.ID = ID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getRoles() {
		return Collections.unmodifiableList(roles);
	}
	public void setRoles(List<String> roles) {
		this.roles = new ArrayList<>(roles);
	}
	public void addRole(String role){
		roles.add(role);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}
	@Override
	public String toString() {
		return "Item [ID=" + ID + ", email=" + email + ", password=" + password + ", roles=" + roles + "]";
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
		if (ID != other.ID)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		return true;
	}
}
