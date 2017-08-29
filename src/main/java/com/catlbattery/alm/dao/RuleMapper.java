package com.catlbattery.alm.dao;

import java.util.List;

import com.catlbattery.alm.entity.RuleVO;

public interface RuleMapper {

	RuleVO findById(int id);

	List<RuleVO> findTree(int id);

	List<RuleVO> findList();

}
