package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;

public class HtmlSectionImpl implements HtmlSection {

    private static final String DESCRIPTION_KEY = "subtitle";
    private static final String TITLE_KEY = "title";
    private final Map<String, Serializable> innerMap;
    private final ChangeListener parent;
    private List<HtmlRow> rows;

    public HtmlSectionImpl(ChangeListener parent, Map<String, Serializable> map) {
        this.parent = parent;
        if (map != null) {
            this.innerMap = map;
        } else {
            innerMap = new HashMap<String, Serializable>();
        }
    }

    public HtmlSectionImpl(ChangeListener parent) {
        this.parent = parent;
        innerMap = new HashMap<String, Serializable>();
    }

    public Map<String, Serializable> toMap() {
        return innerMap;
    }

    public void setTitle(String title) throws ClientException {
        innerMap.put(TITLE_KEY, title);
        parent.onChange(this);

    }

    public void setDescription(String description) throws ClientException {
        innerMap.put(DESCRIPTION_KEY, description);
        parent.onChange(this);
    }

    public String getTitle() {
        return innerMap.containsKey(TITLE_KEY) ? (String) innerMap
                .get(TITLE_KEY) : null;
    }

    public String getDescription() {
        return innerMap.containsKey(DESCRIPTION_KEY) ? (String) innerMap
                .get(DESCRIPTION_KEY) : null;
    }

    public HtmlRow addRow() throws ClientException {
        HtmlRow row = new HtmlRow(this);
        getRows().add(row);
        update();
        parent.onChange(this);
        return row;
    }

    public void update() throws ClientException {
        List<Serializable> rowsMap = new ArrayList<Serializable>();
        for (HtmlRow row : getRows()) {
            rowsMap.add((Serializable) row.toMap());
        }
        innerMap.put("rows", (Serializable) rowsMap);
        parent.onChange(this);
    }

    public List<HtmlRow> getRows() {

        if (rows == null) {
            rows = new ArrayList<HtmlRow>();
            if (innerMap.containsKey("rows")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Serializable>> rowsMap = (List<Map<String, Serializable>>) innerMap
                        .get("rows");
                for (Map<String, Serializable> rowMap : rowsMap) {
                    rows.add(new HtmlRow(this, rowMap));
                }
            }

        }

        return rows;

    }

    public HtmlRow row(int index) {
        return getRows().get(index);
    }

    public HtmlSection insertBefore() throws ClientException {
        return ((HtmlPageImpl)parent).addSectionBefore(this);
    }

    public void remove() throws ClientException {
        ((HtmlPageImpl)parent).removeSection(this);

    }

    public HtmlRow insertBefore(HtmlRow htmlRow) throws ClientException {
        List<HtmlRow> rows = getRows();
        HtmlRow row = new HtmlRow(this);
        rows.add(rows.indexOf(htmlRow), row);
        update();
        return row;

    }

    public void remove(HtmlRow row) throws ClientException {
        getRows().remove(row);
        update();
    }

    @Override
    public void onChange(Object obj) throws ClientException {
        update();

    }

}
