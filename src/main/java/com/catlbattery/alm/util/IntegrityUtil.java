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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.catlbattery.alm.caches.Caches;
import com.catlbattery.alm.caches.Constants;
import com.catlbattery.alm.connect.Connection;
import com.catlbattery.alm.vo.PickVO;
import com.catlbattery.alm.vo.ViewIssueOption;
import com.mks.api.Command;
import com.mks.api.FileOption;
import com.mks.api.MultiValue;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.SelectionList;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.ItemList;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import com.mks.api.response.impl.ItemImpl;

@Repository
public class IntegrityUtil {

	private static Log log = LogFactory.getLog(Connection.class);

	@Autowired
	private Connection conn;

	private IntegrityUtil() {
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
	public String createItem(String type, Map<String, String> itemData) throws APIException {
		Hashtable<String, String> hashData = new Hashtable<>();
		for (String fieldName : itemData.keySet()) {
			String fieldValue = itemData.get(fieldName);
			hashData.put(fieldName, fieldValue);
		}
		return createItem(type, hashData);
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
			} else if (fieldName.equalsIgnoreCase("removeAttachment")) {
				String removeFieldValues = itemData.get("removeAttachment");
				if (StringUtil.isNotEmpty(removeFieldValues)) {
					cmd.addOption(new Option("removeAttachment", removeFieldValues));
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

	public List<Map<String, String>> findIssuesByQueryDef(List<String> fields, String query, boolean sortAscending, String sortField) throws APIException {
		return findIssuesByQueryDef(fields, query, null, sortAscending, sortField);
	}

	public List<Map<String, String>> findIssuesByQueryDef(List<String> fields, String query, String asOf) throws APIException {
		return findIssuesByQueryDef(fields, query, asOf, false, null);
	}

	public List<Map<String, String>> findIssuesByQueryDef(List<String> fields, String query, String asOf, boolean sortAscending, String sortField) throws APIException {
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
		if(StringUtil.isNotEmpty(asOf)) {
			command.addOption(new Option("asOf", asOf));
		}
		if(StringUtil.isNotEmpty(sortField)) {
			command.addOption(new Option("sortField", sortField));
		}
		if(!sortAscending) {
			command.addOption(new Option("noSortAscending"));
		}
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
		return findissueById(id, null, null);
	}

	public Map<String, String> findissueById(String id, String asOf) throws APIException {
		return findissueById(id, null, asOf);
	}

	public Map<String, String> findissueById(String id, ViewIssueOption vio) throws APIException {
		return findissueById(id, vio, null);
	}

	public Map<String, String> findissueById(String id, ViewIssueOption vio, String asOf) throws APIException {
		if (conn == null) {
			throw new APIException("invoke findissueById() ----- connection is null.");
		}
		if (id == null || id.isEmpty()) {
			throw new APIException("invoke findissueById() ----- id is null or empty.");
		}
		Command command = new Command(Command.IM, Constants.VIEW_ISSUE);
		command.addOption(new Option("substituteParams"));
		if(StringUtil.isNotEmpty(asOf)) {
			command.addOption(new Option("asOf", asOf));
		}
		if(vio != null) {
			if(vio.isShowBranches()) {
				command.addOption(new Option("showBranches"));
			}
			if(vio.isShowHistory()) {
				command.addOption(new Option("showHistory"));
			}
			if(vio.isShowLabels()) {
				command.addOption(new Option("showLabels"));
			}
			if(vio.isShowRichContent()) {
				command.addOption(new Option("showRichContent"));
			}
			if(vio.isShowTestResults()) {
				command.addOption(new Option("showTestResults"));
			}
			if(vio.isShowAttachmentDetails()) {
				command.addOption(new Option("showAttachmentDetails"));
			}
		}
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
		return findIssuesByIds(fields, ids, null);
	}

	public List<Map<String, String>> findIssuesByIds(List<String> fields, List<String> ids, String asOf) throws APIException {
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
		if(StringUtil.isNotEmpty(asOf)) {
			command.addOption(new Option("asOf", asOf));
		}
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

	public void batchEdit(List<String> ids, Map<String, String> fieldvalues) throws APIException {
		String commandName = "editissue";
		OptionList ol = new OptionList();
		Option option= new Option("batchEdit");
		ol.add(option);
		for (String key : fieldvalues.keySet()) {
			MultiValue mv = new MultiValue("=");
			mv.add(key);
			mv.add(fieldvalues.get(key));
			ol.add(new Option("field", mv));
		}
		SelectionList sl = new SelectionList();
		for (String id : ids) {
			sl.add(id);
		}
		Command cmd = new Command(Command.IM, commandName);
		cmd.setOptionList(ol);
		cmd.setSelectionList(sl);
		conn.execute(cmd);
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

	public String deleteTestResult(String sessionID, String caseid) throws APIException {
		Command cmd = new Command("deleteresult");
		cmd.addOption(new Option("sessionID", sessionID));
		cmd.addSelection(caseid);
		Response res = conn.execute(cmd);
		Result result = res.getResult();
		String msg = null;
		if(result != null) {
			msg = result.getMessage();
		}
		return msg;
	}

	public String deleteTestResults(String sessionID, List<String> ids) throws APIException {
		Command cmd = new Command("deleteresult");
		cmd.addOption(new Option("sessionID", sessionID));
		if(ids != null) {
			for (String id : ids) {
				cmd.addSelection(id);
			}
		}
		Response res = conn.execute(cmd);
		Result result = res.getResult();
		String msg = null;
		if(result != null) {
			msg = result.getMessage();
		}
		return msg;
	}

	public String purgeResults(String before, String sessionID) throws APIException {
		Command cmd = new Command("purgeresults");
		cmd.addOption(new Option("before", before));
		cmd.addSelection(sessionID);
		Response res = conn.execute(cmd);
		Result result = res.getResult();
		String msg = null;
		if(result != null) {
			msg = result.getMessage();
		}
		return msg;
	}

	public List<PickVO> getVerdicts() throws APIException {
		Command cmd =new Command(Command.TM, "verdicts");
		Response res = conn.execute(cmd);
		WorkItemIterator wis = res.getWorkItems();
		List<PickVO> picks = new ArrayList<PickVO>();
		while(wis.hasNext()) {
			WorkItem wi = wis.next();
			Boolean isActive = wi.getField("isactive").getBoolean();
			if(isActive) {
				PickVO pick = new PickVO();
				pick.setValue(wi.getId());
				pick.setName(wi.getDisplayId());
				picks.add(pick);
			}
		}
		return picks;
	}


	public List<PickVO> findPicksByIbplField(String field, String query) throws APIException {
		long start = System.currentTimeMillis();
		Command command =new Command(Command.IM, "issues");
		command.addOption(new Option("fields", field));
		Option option = new Option("queryDefinition", "(" + query + ")");
		command.addOption(option);
		Response res = conn.execute(command);
		WorkItemIterator workItems = res.getWorkItems();
		List<PickVO> picks = new ArrayList<PickVO>();
		while(workItems.hasNext()) {
			WorkItem wi = workItems.next();
			String name = wi.getField(field).getValueAsString();
			if(name == null) {
				name = wi.getId();
			}
			String fullName = null;
			if(field.equals("Assigned User")){
				ItemImpl itemImpl  = (ItemImpl) wi.getField(field).getValue();				
				Object o = null;
				try {
					o = itemImpl.getField("fullname");
				} catch (Exception e) {
					log.debug(e.getMessage());
				}
				if(o!=null){
					fullName = itemImpl.getField("fullname").getValueAsString();
				}
			}
			PickVO pick = new PickVO();
			if(query.contains("field[Type]=Branch") || query.contains("field[Type]=Team")){
				pick.setValue(name);
			}else{
				pick.setValue(wi.getId());
			}
			if(field.equals("Assigned User") && fullName!=null && !fullName.equals("")){
				pick.setName(fullName+" ("+name+")");
			}else{
				pick.setName(name);
			}
			picks.add(pick);
		}
		log.info("findPicksByIpblField: " + (System.currentTimeMillis() - start));
		return picks;
	}

	public List<PickVO> viewPicks(String fieldName) throws APIException {
		Command cmd = new Command(Command.IM, "viewfield");
		cmd.addSelection(fieldName);
		Response res =  conn.execute(cmd);
		WorkItem wi = res.getWorkItem(fieldName);
		Field picks = wi.getField("picks");
		ItemList list = (ItemList) picks.getList();
		List<PickVO> picklist = new ArrayList<PickVO>();
		for (int i = 0; i < list.size(); i++) {
			Item item = (Item) list.get(i);
			Boolean isActive = item.getField("active").getBoolean();
			if(isActive) {
				PickVO pick = new PickVO();
				String name = item.getField("label").getString();
				String value = item.getField("value").getValueAsString();
				if(name == null) {
					name = value;
				}
				pick.setName(name);
				pick.setValue(value);
				
				picklist.add(pick);
			}
		}
		return picklist;
	}

	public List<PickVO> viewIbplByFields(String field, Map<String, String> relatedFields) throws APIException {
		long start = System.currentTimeMillis();
		Command cmd = new Command(Command.IM, "viewfield");
		cmd.addSelection(field);
		Response res = conn.execute(cmd);
		WorkItem wi = res.getWorkItem(field);
		Field type = wi.getField("backingtype");
		Field backtext = wi.getField("backingtextformat");
		String text = backtext.getString();
		text = text.substring(1, text.length() - 1);
		Field backstate = wi.getField("backingstates");
		StringBuilder sb = new StringBuilder();
		sb.append("(field[Type]=").append(type.getString()).append(")");
		if(backstate.getValue() != null) {
			sb.append(" and (field[State]=").append(backstate.getString().replace(", ", ",")).append(")");
		}
		if(relatedFields != null) {
			for(String key : relatedFields.keySet()) {
				sb.append(" and (field[").append(key).append("]=").append(relatedFields.get(key)).append(")");
			}
		} else {
			sb.append(" and (field[ID]=0)");
		}
		log.info("viewIbplFields: " + (System.currentTimeMillis() - start));
		List<PickVO> picks = findPicksByIbplField(text, sb.toString());
		return picks;
	}

	public List<PickVO> viewIbplByField(String field, String relatedField, String relatedValue) throws APIException {
		Command cmd = new Command(Command.IM, "viewfield");
		cmd.addSelection(field);
		Response res = conn.execute(cmd);
		WorkItem wi = res.getWorkItem(field);
		Field type = wi.getField("backingtype");
		Field backtext = wi.getField("backingtextformat");
		String text = backtext.getString();
		text = text.substring(1, text.length() - 1);
		Field backstate = wi.getField("backingstates");
		StringBuilder sb = new StringBuilder();
		sb.append("(field[Type]=").append(type.getString()).append(")");
		if(backstate.getValue() != null) {
			sb.append(" and (field[State]=").append(backstate.getString().replace(", ", ",")).append(")");
		}
		sb.append(" and (field[").append(relatedField).append("]=").append(relatedValue).append(")");
		List<PickVO> picks = findPicksByIbplField(text, sb.toString());
		return picks;
	}

	public List<PickVO> viewIbpl(String fieldName) throws APIException {
		return viewIbpl(fieldName, null);
	}

	public List<PickVO> viewIbpl(String fieldName, String condition) throws APIException {
		Command cmd = new Command(Command.IM, "viewfield");
		cmd.addSelection(fieldName);
		Response res = conn.execute(cmd);
		WorkItem wi = res.getWorkItem(fieldName);
		Field type = wi.getField("backingtype");
		Field backtext = wi.getField("backingtextformat");
		String text = backtext.getString();
		text = text.substring(1, text.length() - 1);
		Field backstate = wi.getField("backingstates");
		StringBuilder sb = new StringBuilder();
		sb.append("(field[Type]=").append(type.getString()).append(")");
		if(backstate.getValue() != null) {
			sb.append(" and (field[State]=").append(backstate.getString().replace(", ", ",")).append(")");
		}
		if(condition != null && !condition.trim().isEmpty()) {
			sb.append(" and (field[").append(text).append("]=").append(condition).append(")");
		}
		List<PickVO> picks = findPicksByIbplField(text, sb.toString());
		return picks;
	}

	public List<PickVO> getAllowedTypes(String fieldName, String currentType) throws APIException {
		Command command = new Command(Command.IM, "viewfield");
		command.addSelection(fieldName);
		Response res = conn.execute(command);
		WorkItem wi = res.getWorkItem(fieldName);
		String type = wi.getField("type").getValueAsString();
		List<PickVO> list = new ArrayList<PickVO>();
		if("relationship".equalsIgnoreCase(type)) {
			ItemList il = (ItemList) wi.getField("allowedtypes").getList();
			Item item = il.getItem(currentType);
			if(item != null) {
				ItemList itemlist = (ItemList) item.getField("to").getList();
				for (int i = 0; i < itemlist.size(); i++) {
					PickVO pick = new PickVO();
					Item to = (Item) itemlist.get(i);
					pick.setName(to.getId());
					pick.setValue(to.getId());
					list.add(pick);
				}
			}
		}
		return list;
	}

	public void extractIssueAttachs(String issueId, String field, String fileName, String outputFile, String asOf) throws APIException {
		if(fileName == null || fileName.isEmpty()) {
			log.error("FileName is empty:" + issueId + " Filename:" + fileName);
			throw new APIException("FileName is empty:" + issueId + " Filename:" + fileName);
		}
		String commandName = "extractattachments";
		Command cmd = new Command(Command.IM, commandName);
		cmd.addOption(new Option("issue", issueId));
		cmd.addOption(new Option("overwriteExisting"));
		cmd.addOption(new FileOption("outputFile", outputFile));
		if(field != null && !field.isEmpty()) {
			cmd.addOption(new FileOption("field", field));
		}
		if(asOf != null) {
			cmd.addOption(new FileOption("asOf", asOf));
		}
		cmd.addSelection(fileName);
		conn.execute(cmd);
	}

	public void tmExtractResultAttachs(String resultId, String fileName, String outputFile) throws APIException {
		if(fileName == null || fileName.isEmpty()) {
			log.error("FileName is empty:" + resultId + " Filename:" + fileName);
			throw new APIException("FileName is empty:" + resultId + " Filename:" + fileName);
		}
		String commandName = "extractattachments";
		Command cmd = new Command(Command.IM, commandName);
		cmd.addOption(new Option("resultID", resultId));
		cmd.addOption(new Option("overwriteExisting"));
		cmd.addOption(new FileOption("outputFile", outputFile));
		cmd.addSelection(fileName);
		conn.execute(cmd);
	}

}
