package com.catlbattery.alm.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.catlbattery.alm.dao.GroupMapper;
import com.catlbattery.alm.entity.GroupVO;
import com.catlbattery.alm.service.IGroupService;

@Service("groupService")
public class GroupServiceImpl implements IGroupService {

	@Resource
	private GroupMapper groupDao;

	@Override
	public GroupVO findById(int id) {
		return groupDao.findById(id);
	}

	@Override
	public List<GroupVO> findList() {
		return groupDao.findList();
	}

	@Override
	public int insert(GroupVO group) {
		return groupDao.insert(group);
	}

	@Override
	public int insertAll(List<GroupVO> list) {
		return groupDao.insertAll(list);
	}

	@Override
	public int delete(int id) {
		return groupDao.delete(id);
	}

	@Override
	public int update(GroupVO group) {
		return groupDao.update(group);
	}

	@Override
	public void updateList(List<GroupVO> list) {
		groupDao.updateList(list);
	}

	@Override
	public int deleteAll(List<GroupVO> list) {
		return groupDao.deleteAll(list);
	}

	@Override
	public List<GroupVO> findGroups(List<String> groups) {
		return groupDao.findGroups(groups);
	}

}
