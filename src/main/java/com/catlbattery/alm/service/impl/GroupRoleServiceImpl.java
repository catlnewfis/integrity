package com.catlbattery.alm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catlbattery.alm.dao.GroupRoleMapper;
import com.catlbattery.alm.entity.GroupRoleVO;
import com.catlbattery.alm.service.IGroupRoleService;

@Service
public class GroupRoleServiceImpl implements IGroupRoleService {

	@Autowired
	private GroupRoleMapper grMapper;

	@Override
	public int insert(GroupRoleVO vo) {
		return grMapper.insert(vo);
	}

	@Override
	public int insertAll(List<GroupRoleVO> list) {
		return grMapper.insertAll(list);
	}

	@Override
	public int delete(GroupRoleVO vo) {
		return grMapper.delete(vo);
	}

	@Override
	public int deleteAll(List<GroupRoleVO> list) {
		return grMapper.deleteAll(list);
	}

}
