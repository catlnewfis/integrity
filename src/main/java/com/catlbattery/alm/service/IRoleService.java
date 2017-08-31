package com.catlbattery.alm.service;

import java.util.List;

import com.catlbattery.alm.entity.RoleVO;

public interface IRoleService {

	RoleVO findById(int id);

	List<RoleVO> findList();

	int insert(RoleVO role);

	int insertAll(List<RoleVO> list);

	int delete(int id);

	int deleteAll(List<RoleVO> list);

	int update(RoleVO role);

	void updateList(List<RoleVO> list);

}
