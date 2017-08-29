package com.catlbattery.alm.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;

@Component
public class VerdictUtil {

	public String buildFile(String sessionid, String caseid, String result, String file) {
		try {
			// DocumentHelper提供了创建Document对象的方法
			Document document = DocumentHelper.createDocument();
			document.addDocType("ActionList", null, "ActionList.dtd");
			// 添加节点信息
			Element session = document.addElement("ActionList");
			session.addAttribute("sessionID", sessionid);
			Element setResult = session.addElement("SetResult");
			setResult.addAttribute("caseID", caseid);
			Element verdict = setResult.addElement("Verdict");
			verdict.setText(result);
			Writer fileWriter = new FileWriter(file);
			OutputFormat format = new OutputFormat();
			format.setIndent(true);
			format.setNewlines(true);
			// dom4j提供了专门写入文件的对象XMLWriter
			XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
			xmlWriter.write(document);
			xmlWriter.flush();
			xmlWriter.close();
			return file;
		} catch (IOException e) {
			return e.getMessage();
		}
	}
}
