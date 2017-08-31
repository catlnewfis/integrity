package com.catlbattery.alm.service;

import java.util.List;

import com.catlbattery.alm.entity.GroupRoleVO;

public interface IGroupRoleService {

	int insert(GroupRoleVO vo);

	int insertAll(List<GroupRoleVO> list);

	int delete(GroupRoleVO vo);

	int deleteAll(List<GroupRoleVO> list);

}
