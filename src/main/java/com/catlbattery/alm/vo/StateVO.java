package com.catlbattery.alm.vo;

import java.util.List;
import java.util.Map;

/**
 * Aug-27 2017
 * @author huangfeihu
 *
 */
public class StateVO {

	private String state;

	private List<String> targetStates;

	private List<Map<String, List<String>>> groups;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<String> getTargetStates() {
		return targetStates;
	}

	public void setTargetStates(List<String> targetStates) {
		this.targetStates = targetStates;
	}

	public List<Map<String, List<String>>> getGroups() {
		return groups;
	}

	public void setGroups(List<Map<String, List<String>>> groups) {
		this.groups = groups;
	}

}
