package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;

public enum RowTemplate {
	R1COL("1COL"), R2COL_2575("2COL_2575"), R2COL_7525("2COL_7525"), R3COL(
			"3COL"), R4COL("4COL");

	private final String value;

	private RowTemplate(String value) {
		this.value = value;

	}

	private static final Map<String, RowTemplate> stringToEnum = new HashMap<String, RowTemplate>();
	static { // Initialize map from constant name to enum constant
		for (RowTemplate template : values())
			stringToEnum.put(template.value(), template);
	}

	public static RowTemplate fromString(String rowTemplate) {
		return stringToEnum.get(rowTemplate);
	}

	private String value() {
		return value;
	}

	public static void initRow(HtmlRow row, RowTemplate template) throws ClientException {
		for (HtmlContent content : row.getContents()) {
			content.remove();
		}

		String dummy = "DummyContent";
		switch (template) {

		case R2COL_2575:
			row.addContent(4, dummy);
			row.addContent(12, dummy);
			break;
		case R2COL_7525:
			row.addContent(12, dummy);
			row.addContent(4, dummy);
			break;
		case R3COL:
			row.addContent(5, dummy);
			row.addContent(5, dummy);
			row.addContent(6, dummy);
			break;
		case R4COL:
			row.addContent(4, dummy);
			row.addContent(4, dummy);
			row.addContent(4, dummy);
			row.addContent(4, dummy);
			break;
		default:
			row.addContent(16, dummy);
			break;
		}

	}

}
