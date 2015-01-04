package org.arong.lucene;

import java.util.Date;

public class User {

	private int id;
	private String name;
	private String password;
	private Date regdate;

	public User() {
	}
	
	public User(int id, String name, String password, Date regdate) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.regdate = regdate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
}
