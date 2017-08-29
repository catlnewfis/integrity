package com.catlbattery.alm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.catlbattery.alm.entity.RoleVO;

@Repository
public interface RoleMapper {

	RoleVO findById(int id);

	List<RoleVO> findList();

	int insert(RoleVO role);

	int insertAll(List<RoleVO> list);

	int delete(int id);

	int deleteAll(List<RoleVO> list);

	int update(RoleVO role);

	void updateList(List<RoleVO> list);

}
