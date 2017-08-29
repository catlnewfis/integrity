package com.catlbattery.alm.dao;

import java.util.List;

import com.catlbattery.alm.entity.ConstraintVO;

public interface ConstraintMapper {

	ConstraintVO findById(int id);

	List<ConstraintVO> findList();

}
