package com.catlbattery.alm.entity;

import java.util.List;

public class GroupVO extends ValueObject {

	private String name;

	private String groupAdmin;

	private List<RoleVO> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupAdmin() {
		return groupAdmin;
	}

	public void setGroupAdmin(String groupAdmin) {
		this.groupAdmin = groupAdmin;
	}

	public List<RoleVO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVO> roles) {
		this.roles = roles;
	}

}
