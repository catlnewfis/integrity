package com.catlbattery.alm.vo;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IMItem {

	private String id;

	private String type;

	private Map<String, String> data = Collections.synchronizedMap(new LinkedHashMap<>());

	private Map<String, String> metaData = Collections.synchronizedMap(new LinkedHashMap<>());

	private Map<String, File> attachments = Collections.synchronizedMap(new LinkedHashMap<>());

	private Map<String, List<String>> relationships = Collections.synchronizedMap(new LinkedHashMap<>());

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public Map<String, String> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

	public Map<String, File> getAttachments() {
		return attachments;
	}

	public void setAttachments(Map<String, File> attachments) {
		this.attachments = attachments;
	}

	public Map<String, List<String>> getRelationships() {
		return relationships;
	}

	public void setRelationships(Map<String, List<String>> relationships) {
		this.relationships = relationships;
	}

}
