package com.catlbattery.alm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.catlbattery.alm.entity.GroupRoleVO;

@Repository
public interface GroupRoleMapper {

	int insert(GroupRoleVO vo);

	int insertAll(List<GroupRoleVO> list);

	int delete(GroupRoleVO vo);

	int deleteAll(List<GroupRoleVO> list);

}
