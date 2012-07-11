package com.leroymerlin.corp.fr.nuxeo.labs.site.page;

import java.util.HashMap;
import java.util.Map;

public enum CollapseTypes {
	
	EXPAND_ALL("expand_all"),
	COLLAPSE_ALL("collapse_all");

	private String type;
	

    private static final Map<String, CollapseTypes> stringToEnum = new HashMap<String, CollapseTypes>();
    static { // Initialize map from constant name to enum constant
        for (CollapseTypes op : values())
        	stringToEnum.put(op.type(), op);
    }
	private CollapseTypes(String type) {
		this.type = type;
	}
	
    public String type() {
        return type;
    }
    
}
