package com.catlbattery.alm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.catlbattery.alm.caches.Caches;
import com.catlbattery.alm.caches.Constants;
import com.catlbattery.alm.connect.Connection;
import com.catlbattery.alm.vo.StateVO;
import com.mks.api.Command;
import com.mks.api.MultiValue;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.ItemList;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;

public class TypeUtil {

	private static Log log = LogFactory.getLog(FieldUtil.class);

	private static TypeUtil instance;

	private static Connection conn = (Connection) Caches.objects.get(Constants.CONECTION_KEY);

	private TypeUtil() {
	}

	public synchronized static TypeUtil getInstance() {
		if (instance == null) {
			instance = new TypeUtil();
		}
		return instance;
	}

	public List<String> getObjectList(String command) throws APIException {
		List<String> list = new ArrayList<>();
		Command cmd = new Command("im", command);
		cmd.addOption(new Option("noAsAdmin"));
		if(command.equals("types")) {
			MultiValue mValue = new MultiValue(",");
			mValue.add("name");
			mValue.add("typeClass");
			cmd.addOption(new Option("fields", mValue));
		} else {
			cmd.addOption(new Option("fields", "name"));
		}
		Response res = conn.execute(cmd);
		if(res == null) {
			System.out.println("was null");
		} else {
			WorkItemIterator wit = res.getWorkItems();
			while(wit.hasNext()) {
				WorkItem wi = wit.next();
				String fieldName = wi.getField("").getValueAsString();
				if(command.equals("types")) {
					Field typeclass = wi.getField("typeClass");
					Caches.typeClass.put(fieldName, typeclass.getValueAsString());
				}
				list.add(fieldName);
			}
		}
		return list;
	}

	public static String contentType(String documentType) throws APIException {
		String contentType = null;
		Command command = new Command(Command.IM, Constants.VIEW_TYPE);
		command.addSelection(documentType);
		Response res = conn.execute(command);
		WorkItem wi = res.getWorkItem(documentType);
		Field field = wi.getField("associatedtype");
		if (field == null) {
			String message = "Parameter [" + documentType + "] is not a Document Type.";
			log.info(message);
			throw new APIException(message);
		}
		contentType = field.getItem().getId();
		return contentType;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> projects() throws APIException {
		List<Map<String, String>> projects = new ArrayList<>();
		Command cmd = new Command(Command.IM, "projects");
		MultiValue mv = new MultiValue(",");
		mv.add("permittedAdministrators");
		mv.add("permittedGroups");
		mv.add("isActive");
		mv.add("name");
		mv.add("parent");
		mv.add("backingIssueID");
		cmd.addOption(new Option("fields", mv));
		Response res = conn.execute(cmd);
		WorkItemIterator wit = res.getWorkItems();
		while (wit.hasNext()) {
			WorkItem wi = wit.next();
			Map<String, String> map = new HashMap<>();
			Iterator<Field> it = wi.getFields();
			while (it.hasNext()) {
				Field field = it.next();
				String fieldName = field.getName();
				if ("com.mks.api.response.ItemList".equals(field.getDataType())) {
					StringBuilder sb = new StringBuilder();
					ItemList il = (ItemList) field.getList();
					for (int i = 0; i < il.size(); i++) {
						Item item = (Item) il.get(i);
						if (i > 0) {
							sb.append(",");
						}
						sb.append(item.getId());
					}
					map.put(fieldName, sb.toString());
				} else {
					map.put(fieldName, field.getValueAsString());
				}
				Boolean active = new Boolean(map.get("isActive"));
				if (active) {
					projects.add(map);
				}
			}
		}
		return projects;
	}

	@SuppressWarnings("unchecked")
	public List<String> getVisibleFields(String type) throws APIException {
		List<String> visibleFieldList = new ArrayList<>();
		visibleFieldList.add("ID");
		visibleFieldList.add("Created Date");
		visibleFieldList.add("Created By");
		Command viewType = new Command("im", "types");
		viewType.addOption(new Option("noAsAdmin"));
		viewType.addOption(new Option("fields", "visibleFieldsForMe"));
		viewType.addSelection(type);
		Response res = conn.execute(viewType);
		WorkItem wi = res.getWorkItem(type);
		if (wi != null) {
			Field visibleField = wi.getField("visibleFieldsForMe");
			List<Item> itemList = visibleField.getList();
			if (itemList != null) {
				for (Item item : itemList) {
					String field = item.getId();
					if (!visibleFieldList.add(field)) {
						visibleFieldList.add(field);
					}
				}
			}
		}
		if (visibleFieldList.size() > 0) {
			setSpecialFields(visibleFieldList);
		}
		Collections.sort(visibleFieldList);
		return visibleFieldList;
	}

	public List<StateVO> viewState(String type) throws APIException {
		List<StateVO> states = new ArrayList<StateVO>();
		Command cmd = new Command(Command.IM, "viewtype");
		WorkItem wi = null;
		Response res = conn.execute(cmd);
		wi = res.getWorkItem(type);
		Field field = wi.getField("statetransitions");
		if("com.mks.api.response.ItemList".equals(field.getDataType())) {
			ItemList il = (ItemList) field.getList();
			for (int i = 0; i < il.size(); i++) {
				StateVO state = new StateVO();
				Item item = (Item) il.get(i);
				state.setState(item.toString());
				Field targetField = item.getField("targetstates");
				ItemList targetstates = (ItemList) targetField.getList();
				List<String> target = new ArrayList<String>();
				List<Map<String, List<String>>> list = new ArrayList<Map<String, List<String>>>();
				for (int j = 0; j < targetstates.size(); j++) {
					Item stateItem = (Item) targetstates.get(j);
					String targetState = stateItem.toString();
					target.add(targetState);
					Map<String, List<String>> map = new HashMap<String, List<String>>();
					Field groupField = stateItem.getField("permittedgroups");
					ItemList values = (ItemList) groupField.getList();
					List<String> groups = new ArrayList<String>(); 
					for (int k = 0; k < values.size(); k++) {
						Item group = (Item) values.get(k);
						groups.add(group.toString());
					}
					map.put(targetState, groups);
					list.add(map);
				}
				state.setTargetStates(target);
				state.setGroups(list);
				states.add(state);
			}
		}
		return states;
	}

	private void setSpecialFields(List<String> visibleFieldList) throws APIException {
		Command cmd = new Command("im", "fields");
		cmd.addOption(new Option("noAsAdmin"));
		cmd.addOption(new Option("fields", "name,type,showDateTime,richContent,backedBy"));
		for (String visibleField : visibleFieldList) {
			cmd.addSelection(visibleField);
		}
		Response res = conn.execute(cmd);
		WorkItemIterator wit = res.getWorkItems();
		while (wit.hasNext()) {
			WorkItem wi = wit.next();
			String fieldName = wi.getField("name").getValueAsString();
			Field fieldType = wi.getField("type");
			Field richContent = wi.getField("richContent");
			if (fieldType.getValueAsString().equalsIgnoreCase("date")) {
				Caches.dateFields.put(fieldName, wi.getField("showDateTime").getValueAsString());
			}
			if (fieldType.getValueAsString().equalsIgnoreCase("attachment")) {
				Caches.attachmentFields.add(fieldName);
			}
			if (fieldType.getValueAsString().equalsIgnoreCase("fva")) {
				Field backedBy = wi.getField("backedBy");
				Item backedItem = backedBy.getItem();
				String backedID = backedItem.getId();
				String backedField = backedID.substring(backedID.indexOf(".") + 1);
				if (Caches.attachmentFields.contains(backedField)) {
					Caches.attachmentFields.add(fieldName);
				}
				if (Caches.richContentFields.contains(backedField)) {
					Caches.richContentFields.add(fieldName);
				}
			}
			if (fieldType.getValueAsString().equalsIgnoreCase("user")) {
				Caches.userFields.add(fieldName);
			}
			if (fieldType.getValueAsString().equalsIgnoreCase("relationship")) {
				Caches.relationshipFields.add(fieldName);
			}
			if (fieldType.getValueAsString().equalsIgnoreCase("shorttext")) {
				Caches.textFields.add(fieldName);
			}
			if (fieldType.getValueAsString().equalsIgnoreCase("longtext")) {
				Caches.textFields.add(fieldName);
			}
			if (fieldType.getValueAsString().equalsIgnoreCase("pick")) {
				Caches.pickFields.add(fieldName);
			}
			if (richContent != null && richContent.getBoolean() != null && richContent.getBoolean().booleanValue()) {
				Caches.richContentFields.add(fieldName);
			}
		}
	}

}
