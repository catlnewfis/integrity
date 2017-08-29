package com.catlbattery.alm.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.catlbattery.alm.caches.Caches;
import com.catlbattery.alm.caches.Constants;
import com.catlbattery.alm.connect.Connection;
import com.mks.api.Command;
import com.mks.api.MultiValue;
import com.mks.api.Option;
import com.mks.api.SelectionList;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.ItemList;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;

public class IntegrityUtil {

	private static Log log = LogFactory.getLog(Connection.class);

	private static IntegrityUtil instance;

	private static Connection conn = (Connection) Caches.objects.get(Constants.CONECTION_KEY);

	private IntegrityUtil() {
	}

	public synchronized static IntegrityUtil getInstance() {
		if (instance == null) {
			instance = new IntegrityUtil();
		}
		return instance;
	}

	public static String getMsg(APIException e) {
		String msg = e.getMessage();
		Response res = e.getResponse();
		if(res != null) {
			WorkItemIterator wit = res.getWorkItems();
			try {
				while(wit.hasNext()) {
					wit.next();
				}
			} catch (APIException aenested) {
				String message = aenested.getMessage();
				if(message != null) {
					msg = message;
				}
			}
		}
		return msg;
	}

	public static String getResult(Response res) {
		try {
			if(res.getResult() != null) {
				return res.getResult().getMessage();
			}
			WorkItemIterator wit = res.getWorkItems();
			while(wit.hasNext()) {
				WorkItem wi = wit.next();
				if(wi.getResult() != null) {
					return wi.getResult().getMessage();
				}
			}
			return null;
		} catch (Exception e) {
			log.warn(e.getMessage());
			return "";
		}
		
	}

	public String createItem(String type, Hashtable<String, String> itemData) throws APIException {
		String id = null;
		Command cmd = new Command("im", "createissue");
		cmd.addOption(new Option("displayIdOnly"));
		cmd.addOption(new Option("type", type));
		for (String fieldName : itemData.keySet()) {
			if (fieldName.equalsIgnoreCase("ID")) {
				log.warn("Cannot modify 'ID' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Create Date")) {
				log.warn("Cannot modify 'Create Date' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Create By")) {
				log.warn("Cannot modify 'Create By' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Type")) {
				log.warn("Cannot modify 'Type' field for the create issue command! Ignoring mapping!");
			} else {
				String fieldValue = itemData.get(fieldName);
				if (Caches.attachmentFields.contains(fieldName)) {
					List<Option> attachmentOptions = getItemAttachments(fieldValue, fieldName);
					for (Option opt : attachmentOptions) {
						cmd.addOption(opt);
					}
				} else {
					MultiValue mv = new MultiValue("=");
					mv.add(fieldName);
					mv.add(fieldValue);
					if (Caches.richContentFields.contains(fieldName)) {
						cmd.addOption(new Option("richContentField", mv));
					} else {
						cmd.addOption(new Option("field", mv));
					}
				}
			}
		}
		Response res = conn.execute(cmd);
		if (res != null) {
			Result result = res.getResult();
			if (result != null) {
				Item item = result.getPrimaryValue();
				if (item != null) {
					id = item.getId();
					log.debug("create issue: " + result.getMessage());
				}
			}
		} else {
			log.error("Could not get Result from command.");
		}
		return id;
	}

	public String createSegment(String type, Map<String, String> itemData) throws APIException {
		Hashtable<String, String> hashData = new Hashtable<>();
		for (String fieldName : itemData.keySet()) {
			String fieldValue = itemData.get(fieldName);
			hashData.put(fieldName, fieldValue);
		}
		return createSegment(type, hashData);
	}

	public String createSegment(String type, Hashtable<String, String> itemData) throws APIException {
		String id = null;
		Command cmd = new Command("im", "createsegment");
		cmd.addOption(new Option("type", type));
		for (String fieldName : itemData.keySet()) {
			if (fieldName.equalsIgnoreCase("ID")) {
				log.warn("Cannot modify 'ID' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Create Date")) {
				log.warn("Cannot modify 'Create Date' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Create By")) {
				log.warn("Cannot modify 'Create By' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Type")) {
				log.warn("Cannot modify 'Type' field for the create issue command! Ignoring mapping!");
			} else {
				String fieldValue = itemData.get(fieldName);
				MultiValue mv = new MultiValue("=");
				mv.add(fieldName);
				mv.add(fieldValue);
				cmd.addOption(new Option("field", mv));
			}
		}
		Response res = conn.execute(cmd);
		if (res != null) {
			Result result = res.getResult();
			if (result != null) {
				Item item = result.getPrimaryValue();
				if (item != null) {
					id = item.getId();
					log.debug("create issue: " + result.getMessage());
				}
			}
		} else {
			log.error("Could not get Result from command.");
		}
		return id;
	}

	public String createContent(String type, String parentID, String insertLocation, Map<String, String> itemData)
			throws APIException {
		Hashtable<String, String> hashData = new Hashtable<>();
		for (String fieldName : itemData.keySet()) {
			String fieldValue = itemData.get(fieldName);
			hashData.put(fieldName, fieldValue);
		}
		return createContent(type, parentID, insertLocation, hashData);
	}

	public String createContent(String type, String parentID, String insertLocation, Hashtable<String, String> itemData)
			throws APIException {
		String id = null;
		if (StringUtil.isEmpty(parentID)) {
			throw new APIException("create content API Exception: parentID is [" + parentID + "].");
		}
		Command cmd = new Command("im", "createcontent");
		cmd.addOption(new Option("type", type));
		cmd.addOption(new Option("parentID", parentID));
		if (StringUtil.isNotEmpty(insertLocation)) {
			cmd.addOption(new Option("insertLocation", insertLocation));
		}
		for (String fieldName : itemData.keySet()) {
			if (fieldName.equalsIgnoreCase("ID")) {
				log.warn("Cannot modify 'ID' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Create Date")) {
				log.warn("Cannot modify 'Create Date' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Create By")) {
				log.warn("Cannot modify 'Create By' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Type")) {
				log.warn("Cannot modify 'Type' field for the create issue command! Ignoring mapping!");
			} else {
				String fieldValue = itemData.get(fieldName);
				if (Caches.attachmentFields.contains(fieldName)) {
					List<Option> attachmentOptions = getItemAttachments(fieldValue, fieldName);
					for (Option opt : attachmentOptions) {
						cmd.addOption(opt);
					}
				} else {
					MultiValue mv = new MultiValue("=");
					mv.add(fieldName);
					mv.add(fieldValue);
					if (Caches.richContentFields.contains(fieldName)) {
						cmd.addOption(new Option("richContentField", mv));
					} else {
						cmd.addOption(new Option("field", mv));
					}
				}
			}
		}
		Response res = conn.execute(cmd);
		if (res != null) {
			Result result = res.getResult();
			if (result != null) {
				Item item = result.getPrimaryValue();
				if (item != null) {
					id = item.getId();
					log.debug("create issue: " + result.getMessage());
				}
			}
		} else {
			log.error("Could not get Result from command.");
		}
		return id;
	}

	public boolean editItem(String itemID, Map<String, String> itemData) throws APIException {
		Hashtable<String, String> hashData = new Hashtable<>();
		for (String fieldName : itemData.keySet()) {
			String fieldValue = itemData.get(fieldName);
			hashData.put(fieldName, fieldValue);
		}
		return editItem(itemID, hashData);
	}

	public boolean editItem(String itemID, Hashtable<String, String> itemData) throws APIException {
		Command cmd = new Command("im", "editissue");
		for (String fieldName : itemData.keySet()) {
			if (fieldName.equalsIgnoreCase("ID")) {
				log.warn("Cannot modify 'ID' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Create Date")) {
				log.warn("Cannot modify 'Create Date' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Create By")) {
				log.warn("Cannot modify 'Create By' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("Type")) {
				log.warn("Cannot modify 'Type' field for the create issue command! Ignoring mapping!");
			} else if (fieldName.equalsIgnoreCase("removeFieldValues")) {
				String removeFieldValues = itemData.get("removeFieldValues");
				if (StringUtil.isNotEmpty(removeFieldValues)) {
					cmd.addOption(new Option("removeFieldValues", removeFieldValues));
				}
			} else {
				String fieldValue = itemData.get(fieldName);
				if (Caches.attachmentFields.contains(fieldName)) {
					List<Option> attachmentOptions = getItemAttachments(fieldValue, fieldName);
					for (Option opt : attachmentOptions) {
						cmd.addOption(opt);
					}
				} else if (Caches.relationshipFields.contains(fieldName)) {
					Option relationshipOption = new Option("addFieldValues");
					MultiValue mv = new MultiValue("=");
					mv.add(fieldName);
					mv.add(fieldValue);
					relationshipOption.add(mv);
					cmd.addOption(relationshipOption);
				} else {
					MultiValue mv = new MultiValue("=");
					mv.add(fieldName);
					mv.add(fieldValue);
					if (Caches.richContentFields.contains(fieldName)) {
						cmd.addOption(new Option("richContentField", mv));
					} else {
						cmd.addOption(new Option("field", mv));
					}
				}
			}
		}
		cmd.addSelection(itemID);
		Response res = conn.execute(cmd);
		if (res != null && res.getExitCode() == 0) {
			log.debug("Response: " + res.getWorkItem(itemID).getResult().getMessage());
		}
		log.error("Failed to edit item: " + itemID);
		return false;
	}

	private List<Option> getItemAttachments(String attachmentValue, String attachmentField) {
		List<Option> attachmentList = new ArrayList<>();
		if (attachmentValue.indexOf(";") > 0) {
			String[] attachs = attachmentValue.split(";");
			for (String attach : attachs) {
				File attachFile = new File(attach);
				if (attachFile.isDirectory()) {
					File[] fileList = attachFile.listFiles();
					for (File file : fileList) {
						attachmentList.add(getSingleAttachment(file, attachmentField));
					}
				} else if (attachFile.isFile() && attachFile.canRead()) {
					attachmentList.add(getSingleAttachment(attachFile, attachmentField));
				} else {
					log.error("Attachment directory or file '" + attach + "' does not exist!");
				}
			}
		} else {
			File itemAttach = new File(attachmentValue);
			if (itemAttach.isDirectory()) {
				File[] fileList = itemAttach.listFiles();
				for (File file : fileList) {
					attachmentList.add(getSingleAttachment(file, attachmentField));
				}
			} else if (itemAttach.isFile() && itemAttach.canRead()) {
				attachmentList.add(getSingleAttachment(itemAttach, attachmentField));
			} else {
				log.error("Attachment directory or file '" + attachmentValue + "' does not exist!");
			}
		}
		return attachmentList;
	}

	private Option getSingleAttachment(File attachment, String attachmentField) {
		if (attachment.getName().indexOf(",") > -1) {
			File renamedFile = new File(attachment.getAbsolutePath().replaceAll(",", "_"));
			attachment.renameTo(renamedFile);
			attachment = renamedFile;
		}
		Option attachOption = new Option("addAttachment");
		MultiValue mvAttachField = new MultiValue("=");
		MultiValue mvAttachPath = new MultiValue("=");
		MultiValue mvAttachName = new MultiValue("=");
		MultiValue mvAttachSummary = new MultiValue("=");
		mvAttachField.add("field");
		mvAttachField.add(attachmentField);
		mvAttachPath.add("path");
		mvAttachPath.add("remote://" + attachment.getAbsolutePath());
		mvAttachName.add("name");
		mvAttachName.add(attachment.getName());
		mvAttachSummary.add("summary");
		mvAttachSummary.add(attachment.getName());
		attachOption.setSeparator(",");
		attachOption.add(mvAttachField);
		attachOption.add(mvAttachPath);
		attachOption.add(mvAttachName);
		attachOption.add(mvAttachSummary);
		return attachOption;
	}

	public static Option getSingleRelationship(String relationshipField, String id) {
		Option relationshipOption = new Option("addRelationships");
		MultiValue mv = new MultiValue("=");
		mv.add(relationshipField + ":" + id);
		relationshipOption.setSeparator(",");
		relationshipOption.add(mv);
		return relationshipOption;
	}

	public boolean removeContents(Set<String> ids) throws APIException {
		boolean removed = false;
		if (ids == null) {
			return removed;
		}
		Command cmd = new Command("im", "removecontent");
		cmd.addOption(new Option("recurse"));
		cmd.addOption(new Option("noconfirm"));
		List<SelectionList> parallel = new ArrayList<>();
		if (ids.size() > 500) {
			SelectionList sl = new SelectionList();
			Iterator<String> it = ids.iterator();
			int i = 0;
			while (it.hasNext()) {
				if (i % 500 == 0 && sl.size() > 0) {
					parallel.add(sl);
					sl = new SelectionList();
				}
				sl.add(it.next());
				if (++i == ids.size()) {
					parallel.add(sl);
					break;
				}
			}
		} else {
			SelectionList sl = new SelectionList();
			for (String id : ids) {
				sl.add(id);
			}
			parallel.add(sl);
		}
		for (SelectionList selectionList : parallel) {
			cmd.setSelectionList(selectionList);
			Response res = conn.execute(cmd);
			String result = getResult(res);
			log.info("remove content: " + result);
		}
		return removed;
	}

	public Item getItem(String id) throws APIException {
		Command cmd = new Command("im", "viewissue");
		cmd.addOption(new Option("substituteParams"));
		cmd.addSelection(id);
		Item item = null;
		Response res = conn.execute(cmd);
		if (res == null) {
			log.error("Could not get Result from command.");
		} else {
			item = res.getWorkItem(id);
		}
		return item;
	}

	public List<Map<String, String>> findIssuesByQueryDef(List<String> fields, String query) throws APIException {
		if (conn == null) {
			throw new APIException("invoke findIssuesByQueryDef() ----- connection is null.");
		}
		if (query == null || query.isEmpty()) {
			throw new APIException("invoke findIssuesByQueryDef() ----- query is null or empty.");
		}
		if (fields == null) {
			fields = new ArrayList<>();
		}
		if (fields.size() < 1) {
			fields.add("ID");
			fields.add("Project");
			fields.add("Type");
			fields.add("State");
		}
		MultiValue mv = new MultiValue(",");
		for (String field : fields) {
			mv.add(field);
		}
		Command command = new Command(Command.IM, Constants.ISSUES);
		command.addOption(new Option(Constants.FIELDS, mv));
		command.addOption(new Option(Constants.QUERY_DEFINITION, query));
		Response res = conn.execute(command);
		WorkItemIterator it = res.getWorkItems();
		List<Map<String, String>> list = new ArrayList<>();
		while (it.hasNext()) {
			try {
				WorkItem wi = it.next();
				Iterator<?> iterator = wi.getFields();
				Map<String, String> map = new HashMap<>();
				while (iterator.hasNext()) {
					Field field = (Field) iterator.next();
					String fieldName = field.getName();
					if (Constants.ITEMLIST.equals(field.getDataType())) {
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
				}
				list.add(map);
			} catch (APIException e) {
				log.warn(e.getMessage());
				Map<String, String> map = new HashMap<>();
				map.put(Constants.API_EXCEPTION, Constants.API_EXCEPTION + ": " + e.getMessage());
				list.add(map);
			}
		}
		return list;
	}

	public Map<String, String> findissueById(String id) throws APIException {
		if (conn == null) {
			throw new APIException("invoke findissueById() ----- connection is null.");
		}
		if (id == null || id.isEmpty()) {
			throw new APIException("invoke findissueById() ----- id is null or empty.");
		}
		Command command = new Command(Command.IM, Constants.VIEW_ISSUE);
		command.addSelection(id);
		Response res = conn.execute(command);
		Map<String, String> map = new HashMap<>();
		try {
			WorkItem wi = res.getWorkItem(id);
			Iterator<?> iterator = wi.getFields();
			while (iterator.hasNext()) {
				Field field = (Field) iterator.next();
				String fieldName = field.getName();
				if (Constants.ITEMLIST.equals(field.getDataType())) {
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
			}
		} catch (Exception e) {
			log.warn(e.getMessage());
			map = new HashMap<>();
			map.put(Constants.API_EXCEPTION, Constants.API_EXCEPTION + ": " + e.getMessage());
		}
		return map;
	}

	public List<Map<String, String>> findIssuesByIds(List<String> fields, List<String> ids) throws APIException {
		if (conn == null) {
			throw new APIException("invoke findIssuesByIds() ----- connection is null.");
		}
		if (ids == null || ids.isEmpty()) {
			throw new APIException("invoke findIssuesByIds() ----- ids is null or empty.");
		}
		if (fields == null) {
			fields = new ArrayList<>();
		}
		if (fields.size() < 1) {
			fields.add("ID");
			fields.add("Project");
			fields.add("Type");
			fields.add("State");
		}
		MultiValue mv = new MultiValue(",");
		for (String field : fields) {
			mv.add(field);
		}
		Command command = new Command(Command.IM, Constants.ISSUES);
		command.addOption(new Option(Constants.FIELDS, mv));
		SelectionList sl = new SelectionList();
		for (String id : ids) {
			sl.add(id);
		}
		command.setSelectionList(sl);
		Response res = conn.execute(command);
		WorkItemIterator it = res.getWorkItems();
		List<Map<String, String>> list = new ArrayList<>();
		while (it.hasNext()) {
			try {
				WorkItem wi = it.next();
				Iterator<?> iterator = wi.getFields();
				Map<String, String> map = new HashMap<>();
				while (iterator.hasNext()) {
					Field field = (Field) iterator.next();
					String fieldName = field.getName();
					if (Constants.ITEMLIST.equals(field.getDataType())) {
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
				}
				list.add(map);
			} catch (APIException e) {
				log.warn(e.getMessage());
				Map<String, String> map = new HashMap<>();
				map.put(Constants.API_EXCEPTION, Constants.API_EXCEPTION + ": " + e.getMessage());
				list.add(map);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> results(String sessionID) throws APIException {
		List<Map<String, String>> list = new ArrayList<>();
		if (StringUtil.isEmpty(sessionID)) {
			throw new APIException("invoke results() ---- sessionID is null or empty.");
		}
		Command cmd = new Command(Command.TM, "results");
		cmd.addOption(new Option("sessionID", sessionID));
		Response res = conn.execute(cmd);
		WorkItemIterator wit = res.getWorkItems();
		while (wit.hasNext()) {
			WorkItem wi = wit.next();
			Iterator<Field> it = wi.getFields();
			Map<String, String> map = new HashMap<>();
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
			}
			list.add(map);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> viewResult(String resultID) throws APIException {
		if (StringUtil.isEmpty(resultID)) {
			throw new APIException("invoke viewresult() ----- resultID is nul or empty.");
		}
		Map<String, String> map = new HashMap<>();
		Command cmd = new Command(Command.TM, "viewresult");
		cmd.addSelection(resultID);
		Response res = conn.execute(cmd);
		WorkItem wi = res.getWorkItem(resultID);
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
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> viewResult(String sessionID, String caseID) throws APIException {
		if (StringUtil.isEmpty(sessionID)) {
			throw new APIException("invoke viewresult() ----- sessionID is nul or empty.");
		}
		if (StringUtil.isEmpty(caseID)) {
			throw new APIException("invoke viewresult() ----- caseID is nul or empty.");
		}
		Map<String, String> map = new HashMap<>();
		Command cmd = new Command(Command.TM, "viewresult");
		cmd.addOption(new Option("sessionID", sessionID));
		cmd.addSelection(caseID);
		Response res = conn.execute(cmd);
		WorkItem wi = res.getWorkItem(sessionID + ":" + caseID);
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
		}
		return map;
	}

}
