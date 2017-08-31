package com.catlbattery.alm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.catlbattery.alm.dao.RoleMapper;
import com.catlbattery.alm.entity.RoleVO;
import com.catlbattery.alm.service.IRoleService;

@Service("roleService")
public class RoleServiceImpl implements IRoleService {

	@Resource
	private RoleMapper roleDao;

	@Override
	public RoleVO findById(int id) {
		return roleDao.findById(id);
	}

	@Override
	public List<RoleVO> findList() {
		return roleDao.findList();
	}

	@Override
	public int insert(RoleVO role) {
		return roleDao.insert(role);
	}

	@Override
	public int insertAll(List<RoleVO> list) {
		return roleDao.insertAll(list);
	}

	@Override
	public int delete(int id) {
		return roleDao.delete(id);
	}

	@Override
	public int deleteAll(List<RoleVO> list) {
		return roleDao.deleteAll(list);
	}

	@Override
	public int update(RoleVO role) {
		return roleDao.update(role);
	}

	@Override
	public void updateList(List<RoleVO> list) {
		roleDao.updateList(list);
	}

}
