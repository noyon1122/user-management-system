package com.noyon.main.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String token;
	
	private boolean logout;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private OurUsers user;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean isLogout() {
		return logout;
	}
	public void setLogout(boolean logout) {
		this.logout = logout;
	}
	public OurUsers getOurUsers() {
		return user;
	}
	public void setOurUsers(OurUsers user) {
		this.user = user;
	}
	public Token(int id, String token, boolean logout, OurUsers user) {
		super();
		this.id = id;
		this.token = token;
		this.logout = logout;
		this.user = user;
	}
	public Token() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
