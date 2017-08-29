package cli;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.mks.api.Command;
import com.mks.api.MultiValue;
import com.mks.api.Option;
import com.mks.api.Session;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;

public class Util {
	public static final FileFilter filesOnly = new FileFilter() {
		
		@Override
		public boolean accept(File arg0) {
			return arg0.isFile();
		}
	};
//	private List<String> projects;
//	private List<String> types;
	private Session session;
	private Hashtable<String, String> dateFields = new Hashtable<>();
	private Hashtable<String, String> typeClass = new Hashtable<>();
	private List<String> attachmentFields = new ArrayList<>();
	private List<String> relationshipFields = new ArrayList<>();
	private List<String> textFields = new ArrayList<>();
	private List<String> pickFields = new ArrayList<>();
	private List<String> richContentFields = new ArrayList<>();
	Connection conn;
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
		Response res = conn.execute(null, cmd);
		if(res == null) {
			System.out.println("was null");
		} else {
			WorkItemIterator wit = res.getWorkItems();
			while(wit.hasNext()) {
				WorkItem wi = wit.next();
				String fieldName = wi.getField("").getValueAsString();
				if(command.equals("types")) {
					Field typeclass = wi.getField("typeClass");
					typeClass.put(fieldName, typeclass.getValueAsString());
				}
				list.add(fieldName);
			}
		}
		return list;
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
			} catch (APIException ae) {
				String message = ae.getMessage();
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
			return "";
		}
		
	}
	public String createItem(String type, Hashtable<String, String> item) throws APIException {
		String id = null;
		Command command = new Command("im", "createissue");
		command.addOption(new  Option("displayIdOnly"));
		command.addOption(new Option("type", type));
		for(String fieldName : item.keySet()) {
			if(fieldName.equalsIgnoreCase("ID")) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Created By") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Created Date") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Type") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else {
				String value = item.get(fieldName);
				if(this.attachmentFields.contains(fieldName)) {
					getAttachments(value, fieldName);
				} else {
					MultiValue mv = new MultiValue("=");
					mv.add(fieldName);
					mv.add(value);
					if(richContentFields.contains(fieldName)) {
						command.addOption(new Option("richContentField", mv));
					} else {
						command.addOption(new Option("field", mv));
					}
				}
			}
		}
		Response res = conn.execute(session, command);
		Result result = res.getResult();
		if(result != null) {
			Item issue = result.getPrimaryValue();
			if(issue != null) {
				id = issue.getId();
			}
		}
		return id;
	}
	public String editItem(String id, Hashtable<String, String> item) throws APIException {
		Command command = new Command("im", "editissue");
		command.addOption(new  Option("displayIdOnly"));
		for(String fieldName : item.keySet()) {
			if(fieldName.equalsIgnoreCase("ID")) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Created By") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Created Date") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Type") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else {
				String value = item.get(fieldName);
				if(this.attachmentFields.contains(fieldName)) {
				} else if(this.relationshipFields.contains(fieldName)) {
					Option relationship = new Option("addRelationships");
					MultiValue mv = new MultiValue("=");
					mv.add(fieldName + ":" + value);
					relationship.setSeparator(",");
					relationship.add(mv);
					command.addOption(relationship);
				} else {
					MultiValue mv = new MultiValue("=");
					mv.add(fieldName);
					mv.add(value);
					if(richContentFields.contains(fieldName)) {
						command.addOption(new Option("richContentField", mv));
					} else {
						command.addOption(new Option("field", mv));
					}
				}
			}
			command.addSelection(id);
			Response res = conn.execute(session, command);
			res.getExitCode();
			res.getWorkItem(id).getResult().getMessage();
		}
		return id;
	}
	private List<Option> getAttachments(String value, String field){
		List<Option> list = new ArrayList<>();
		if(value.indexOf(";") > 0) {
			String[] token = value.split(";");
			for(String file : token) {
				File attach = new File(file);
				if(attach.isDirectory()) {
					File[] files = attach.listFiles();
					for(File attachment :files) {
						list.add(getSingleAttachment(attachment, field));
					}
					
				} else if(attach.canRead() ) {
					list.add(getSingleAttachment(attach, field));
				}
			}
		} else {
			File attach = new File(value);
			if(attach.isDirectory()) {
				File[] files = attach.listFiles();
				for(File attachment :files) {
					list.add(getSingleAttachment(attachment, field));
				}
				
			} else if(attach.canRead() ) {
				list.add(getSingleAttachment(attach, field));
			}
			
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<String> getVisibleFields(String type) throws APIException {
		List<String> fields = new ArrayList<>();
		fields.add("ID");
		fields.add("Created Date");
		fields.add("Created By");
		Command cmd = new Command("im", "types");
		cmd.addOption(new Option("noAsAdmin"));
		cmd.addOption(new Option("fields", "visibleFieldsForme"));
		cmd.addSelection(type);
		Response res = conn.execute(session, cmd);
		WorkItem wi = res.getWorkItem(type);
		Field field = wi.getField("visibleFieldsForMe");
		List<Item> items = field.getList();
		for(Iterator<Item> it = items.iterator(); it.hasNext() ;) {
			String filedName = it.next().getId();
			if(!fields.contains(filedName)) {
				fields.add(filedName);
			}
		}
		setFields(fields);
		Collections.sort(fields);
		return fields;
	}
	private void setFields(List<String> fields) throws APIException {
		dateFields = new Hashtable<>();
		attachmentFields = new ArrayList<>();
		relationshipFields = new ArrayList<>();
		textFields = new ArrayList<>();
		Command cmd = new Command("im", "fields");
		cmd.addOption(new Option("noAsAdmin"));
		cmd.addOption(new Option("fields", "name,type,showDateTime,richContent"));
		for (String string : fields) {
			cmd.addSelection(string);
		}
		Response res = conn.execute(session, cmd);
		WorkItemIterator wit = res.getWorkItems();
		while(wit.hasNext()) {
			WorkItem wi = wit.next();
			String fieldName = wi.getField("name").getValueAsString();
			Field fieldType = wi.getField("type");
			Field isRich = wi.getField("richContent");
			if(fieldType.getValueAsString().compareToIgnoreCase("date") == 0) {
				dateFields.put(fieldName, wi.getField("showDateTime").getValueAsString());
			}
			if(fieldType.getValueAsString().compareToIgnoreCase("attachment") == 0) {
				attachmentFields.add(fieldName);
			}
			if(fieldType.getValueAsString().compareToIgnoreCase("relationship") == 0) {
				relationshipFields.add(fieldName);
			}
			if(fieldType.getValueAsString().compareToIgnoreCase("shottext") == 0) {
				textFields.add(fieldName);
			}
			if(fieldType.getValueAsString().compareToIgnoreCase("longtext") == 0) {
				textFields.add(fieldName);
			}
			if(fieldType.getValueAsString().compareToIgnoreCase("pick") == 0) {
				pickFields.add(fieldName);
			}
			if(isRich != null&&isRich.getBoolean() != null && isRich.getBoolean().booleanValue()) {
				richContentFields.add(fieldName);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public List<String> getPickValues(String field) throws APIException {
		List<String> picks = new ArrayList<>();
		Command cmd = new Command("im", "fields");
		cmd.addOption(new Option("noAsAdmin"));
		cmd.addOption(new Option("fields", "picks"));
		cmd.addSelection(field);
		Response res = conn.execute(session, cmd);
		WorkItem wi = res.getWorkItem(field);
		List<Item> itemList = wi.getField("picks").getList();
		for(Item item :itemList) {
			if(picks.contains(item.getId())) {
				picks.add(item.getId());
			}
		}
		return picks;
	}
	@SuppressWarnings("unchecked")
	public List<String> getActivePicks(String field) throws APIException{
		List<String> picks = new ArrayList<>();
		Command cmd = new Command("im", "viewfield");
		cmd.addSelection(field);
		Response res = conn.execute(session, cmd);
		WorkItem wi = res.getWorkItem(field);
		List<Item> itemList = wi.getField("picks").getList();
		for(Item item :itemList) {
			if(item.getField("active").getBoolean().booleanValue()) {
				if(picks.contains(item.getId())) {
					picks.add(item.getId());
				}
			}
		}
		return picks;
		
	}
	public Item getItem(String id) throws APIException {
		Command command = new Command("im", "viewissue");
		command.addOption(new Option("substituteParams"));
		command.addSelection(id);
		Response res = conn.execute(session, command);
		Item item = res.getWorkItem(id);
		return item;
	}
	public static Option getSingleRelationship(String field, String id) {
		Option relationship = new Option("addRelationships");
		MultiValue mv = new MultiValue("=");
		mv.add(field + ":" + id);
		relationship.setSeparator(",");
		relationship.add(mv);
		return relationship;
	}
	private Option getSingleAttachment(File attach, String field) {
		if(attach.getName().indexOf(",") > -1) {
			File file = new File(attach.getAbsolutePath().replace(",", "_"));
			attach.renameTo(file);
			attach = file;
		}
		Option option = new Option("addAttachment");
		MultiValue mvField = new MultiValue("=");
		MultiValue mvPath = new MultiValue("=");
		MultiValue mvName = new MultiValue("=");
		MultiValue mvSummary = new MultiValue("=");
		mvField.add("field");
		mvField.add(field);
		mvPath.add("path");
		mvPath.add("remote://" + attach.getAbsolutePath());
		mvName.add("name");
		mvName.add(attach.getName());
		mvSummary.add("summary");
		mvSummary.add(attach.getName());
		option.setSeparator(",");
		option.add(mvField);
		option.add(mvField);
		option.add(mvField);
		option.add(mvField);
		return option;
	}
	public String createContent(String type, String parentID, String insertLocation, Hashtable<String, String> item) throws APIException {
		String id = null;
		Command command = new Command("im", "createissue");
		command.addOption(new  Option("parentID", parentID));
		command.addOption(new  Option("insertLocation",insertLocation));
		command.addOption(new Option("type", type));
		for(String fieldName : item.keySet()) {
			if(fieldName.equalsIgnoreCase("ID")) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Created By") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Created Date") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Type") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else {
				String value = item.get(fieldName);
				if(this.attachmentFields.contains(fieldName)) {
					
				} else {
					MultiValue mv = new MultiValue("=");
					mv.add(fieldName);
					mv.add(value);
					if(richContentFields.contains(fieldName)) {
						command.addOption(new Option("richContentField", mv));
					} else {
						command.addOption(new Option("field", mv));
					}
				}
			}
		}
		Response res = conn.execute(session, command);
		Result result = res.getResult();
		if(result != null) {
			Item issue = result.getPrimaryValue();
			if(issue != null) {
				id = issue.getId();
			}
		}
		return id;
	}
	public String createSegment(String type, Hashtable<String, String> item) throws APIException {
		String id = null;
		Command command = new Command("im", "createsegment");
		command.addOption(new Option("type", type));
		for(String fieldName : item.keySet()) {
			if(fieldName.equalsIgnoreCase("ID")) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Created By") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Created Date") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else if(fieldName.equalsIgnoreCase("Type") ) {
				System.out.println("Cannot modify id for the create issue command. Ignoring mapping.");
			} else {
				String value = item.get(fieldName);
					MultiValue mv = new MultiValue("=");
					mv.add(fieldName);
					mv.add(value);
					command.addOption(new Option("field", mv));
			}
		}
		Response res = conn.execute(session, command);
		Result result = res.getResult();
		if(result != null) {
			Item issue = result.getPrimaryValue();
			if(issue != null) {
				id = issue.getId();
			}
		}
		return id;
	}
}
