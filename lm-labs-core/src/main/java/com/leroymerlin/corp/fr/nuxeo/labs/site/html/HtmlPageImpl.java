package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class HtmlPageImpl extends AbstractPage implements HtmlPage,
        ChangeListener {

    public HtmlPageImpl(DocumentModel document) {
		super(document);
	}

	public static final String DOCTYPE = Docs.HTMLPAGE.type();

    private List<HtmlSection> sections;

    @Override
    public List<HtmlSection> getSections() throws ClientException {
        if (sections == null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Serializable>> sectionsMap = (List<Map<String, Serializable>>) doc.getPropertyValue("html:sections");
            sections = new ArrayList<HtmlSection>(sectionsMap.size());
            for (Map<String, Serializable> map : sectionsMap) {
                sections.add(new HtmlSectionImpl(this, map));
            }

        }
        return sections;

    }

    @Override
    public HtmlSection addSection() throws ClientException {
        return addSection(getSections().size());
    }

    @Override
    public HtmlSection addSection(int index) throws ClientException {
        if (index < 0 || index > getSections().size()) {
            return null;
        }

        List<HtmlSection> sections = getSections();
        HtmlSection returnedSection = new HtmlSectionImpl(this);
        sections.add(index, returnedSection);
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

    public HtmlSection addSectionBefore(HtmlSection htmlSection)
            throws ClientException {
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

	@Override
	public void moveUp(int index) throws ClientException {
		if (index - 1 < 0){
			return;
		}
		Tools.changePositionWith(index, index - 1, sections);
		update();
	}

	@Override
	public void moveDown(int index) throws ClientException  {
		if (index + 1 > sections.size()){
			return;
		}
		Tools.changePositionWith(index, index + 1, sections);
		update();
	}
}
