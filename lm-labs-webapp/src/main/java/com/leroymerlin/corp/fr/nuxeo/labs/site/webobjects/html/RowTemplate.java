package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;

public enum RowTemplate {
	R1COL("1COL"), R2COL_5050("2COL_5050"), R2COL_2575("2COL_2575"), R2COL_7525("2COL_7525"), R3COL(
			"3COL"), R4COL("4COL");

	private final String value;
	
	private static final String NO_CONTENT = "";

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

		
		switch (template) {

		case R2COL_5050:
            row.addContent(8, NO_CONTENT);
            row.addContent(8, NO_CONTENT);
            break;
		case R2COL_2575:
			row.addContent(4, NO_CONTENT);
			row.addContent(12, NO_CONTENT);
			break;
		case R2COL_7525:
			row.addContent(12, NO_CONTENT);
			row.addContent(4, NO_CONTENT);
			break;
		case R3COL:
			row.addContent(333, NO_CONTENT);
			row.addContent(333, NO_CONTENT);
			row.addContent(333, NO_CONTENT);
			break;
		case R4COL:
			row.addContent(4, NO_CONTENT);
			row.addContent(4, NO_CONTENT);
			row.addContent(4, NO_CONTENT);
			row.addContent(4, NO_CONTENT);
			break;
		default:
			row.addContent(16, NO_CONTENT);
			break;
		}

	}

}
