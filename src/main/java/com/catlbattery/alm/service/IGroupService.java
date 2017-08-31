package com.catlbattery.alm.service;

import java.util.List;

import com.catlbattery.alm.entity.GroupVO;

public interface IGroupService {

	GroupVO findById(int id);

	List<GroupVO> findList();

	List<GroupVO> findGroups(List<String> groups);

	int insert(GroupVO group);

	int insertAll(List<GroupVO> list);

	int update(GroupVO group);

	void updateList(List<GroupVO> list);

	int delete(int id);

	int deleteAll(List<GroupVO> list);

}
