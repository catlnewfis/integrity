package com.catlbattery.alm.entity;

import java.util.List;

public class RoleVO extends ValueObject {

	private String name;

	private String description;

	private List<GroupVO> groups;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<GroupVO> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupVO> groups) {
		this.groups = groups;
	}

}
