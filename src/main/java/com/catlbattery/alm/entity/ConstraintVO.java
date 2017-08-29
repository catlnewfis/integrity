package com.catlbattery.alm.entity;

public class ConstraintVO extends ValueObject {

	private String type;

	private String cnstraintMethod;

	private Integer ruleId;

	private String constrainedField;

	private String allowedValues;

	private RuleVO rule;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCnstraintMethod() {
		return cnstraintMethod;
	}

	public void setCnstraintMethod(String cnstraintMethod) {
		this.cnstraintMethod = cnstraintMethod;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public String getConstrainedField() {
		return constrainedField;
	}

	public void setConstrainedField(String constrainedField) {
		this.constrainedField = constrainedField;
	}

	public String getAllowedValues() {
		return allowedValues;
	}

	public void setAllowedValues(String allowedValues) {
		this.allowedValues = allowedValues;
	}

	public RuleVO getRule() {
		return rule;
	}

	public void setRule(RuleVO rule) {
		this.rule = rule;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		ConstraintVO vo = (ConstraintVO) obj;
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getId() == vo.getId()) {
			equals = true;
		}
		return equals;
	}

}
