package com.catlbattery.alm.entity;

import java.util.List;

public class RuleVO extends ValueObject {

	private String fieldName;

	private String fieldValue;

	private String targetField;

	private boolean negation;

	private String operator;

	private Integer parentId;

	private List<RuleVO> rules;

	private int level;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getTargetField() {
		return targetField;
	}

	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}

	public boolean isNegation() {
		return negation;
	}

	public void setNegation(boolean negation) {
		this.negation = negation;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<RuleVO> getRules() {
		return rules;
	}

	public void setRules(List<RuleVO> rules) {
		this.rules = rules;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
