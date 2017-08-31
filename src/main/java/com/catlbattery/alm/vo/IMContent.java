package com.catlbattery.alm.vo;

import java.util.ArrayList;
import java.util.List;

public class IMContent extends IMItem {

	private String parentID;

	private List<IMContent> nodes = new ArrayList<>();

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public List<IMContent> getNodes() {
		return nodes;
	}

	public void setNodes(List<IMContent> nodes) {
		this.nodes = nodes;
	}

}
