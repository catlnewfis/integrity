package com.catlbattery.alm.vo;

import java.io.Serializable;

public class UserSession implements Serializable {

	private static final long serialVersionUID = 1L;

	private String address;

	private String sessionId;

	private String username;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
