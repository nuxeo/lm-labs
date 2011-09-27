package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;

public class HtmlPageImpl extends AbstractPage implements HtmlPage, ChangeListener {

    public static final String DOCTYPE = "HtmlPage";

    private List<HtmlSection> sections;

    public HtmlPageImpl(DocumentModel doc) {
        this.doc = doc;
    }


    @Override
    public List<HtmlSection> getSections() throws ClientException {
        if (sections == null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Serializable>> sectionsMap = (List<Map<String, Serializable>>) doc
                    .getPropertyValue("html:sections");
            sections = new ArrayList<HtmlSection>(sectionsMap.size());
            for (Map<String, Serializable> map : sectionsMap) {
                sections.add(new HtmlSectionImpl(this, map));
            }

        }
        return sections;

    }

    @Override
    public HtmlSection addSection() throws ClientException {
        List<HtmlSection> sections = getSections();
        HtmlSection returnedSection = new HtmlSectionImpl(this);
        sections.add(returnedSection);
        update();

        return returnedSection;
    }

    void update() throws ClientException {
        List<Map<String, Serializable>> sectionsMap = new ArrayList<Map<String, Serializable>>();

        for (HtmlSection section : sections) {
            HtmlSectionImpl si = (HtmlSectionImpl) section;
            sectionsMap.add(si.toMap());
        }

        doc.setPropertyValue("html:sections", (Serializable) sectionsMap);

    }

    @Override
    public HtmlSection section(int index) throws ClientException {
        return getSections().get(index);
    }

    public HtmlSection addSectionBefore(HtmlSection htmlSection) throws ClientException {
        List<HtmlSection> sections = getSections();
        HtmlSection section = new HtmlSectionImpl(this);
        sections.add(sections.indexOf(htmlSection), section);
        update();
        return section;


    }

    public void removeSection(HtmlSection htmlSection) throws ClientException {
        getSections().remove(htmlSection);
        update();
    }


    @Override
    public void onChange(Object obj) throws ClientException {
        update();
    }


}
