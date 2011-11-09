package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.MailNotification;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.ChangeListener;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlSection;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlSectionImpl;

public class LabsNewsAdapter extends AbstractPage implements LabsNews,
        ChangeListener {

    static final String CREATOR = "dublincore:creator";
    static final String LAST_CONTRIBUTOR = "dublincore:lastContributor";

    static final String START_PUBLICATION = "ln:startPublication";
    static final String END_PUBLICATION = "ln:endPublication";
    static final String ACCROCHE = "ln:accroche";
    static final String CONTENT = "ln:content";
    static final String NEWS_TEMPLATE = "ln:template";

    private String lastContributorFullName = null;
    private HtmlSection section;

    public LabsNewsAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public Calendar getStartPublication() throws ClientException {
        return (Calendar) doc.getPropertyValue(START_PUBLICATION);
    }

    @Override
    public String getCreator() throws ClientException {
        return (String) doc.getPropertyValue(CREATOR);
    }

    @Override
    public String getAccroche() throws ClientException {
        return (String) doc.getPropertyValue(ACCROCHE);
    }

    @Override
    public String getContent() throws ClientException {
        return (String) doc.getPropertyValue(CONTENT);
    }

    @Override
    public void setAccroche(String pAccroche) throws ClientException {
        doc.setPropertyValue(ACCROCHE, pAccroche);
    }

    @Override
    public void setContent(String pContent) throws ClientException {
        doc.setPropertyValue(CONTENT, pContent);
    }

    @Override
    public String getNewsTemplate() throws ClientException {
        return (String) doc.getPropertyValue(NEWS_TEMPLATE);
    }

    @Override
    public void setNewsTemplate(String pNewsTemplate) throws ClientException {
        doc.setPropertyValue(NEWS_TEMPLATE, pNewsTemplate);
    }

    @Override
    public void setStartPublication(Calendar pStartPublication)
            throws ClientException {
        pStartPublication.set(Calendar.HOUR_OF_DAY, 0);
        pStartPublication.set(Calendar.MINUTE, 0);
        pStartPublication.set(Calendar.SECOND, 0);
        pStartPublication.set(Calendar.MILLISECOND, 0);
        doc.setPropertyValue(START_PUBLICATION, pStartPublication);

    }

    @Override
    public Calendar getEndPublication() throws ClientException {
        return (Calendar) doc.getPropertyValue(END_PUBLICATION);
    }

    @Override
    public void setEndPublication(Calendar pEndPublication)
            throws ClientException {
        pEndPublication.add(Calendar.DATE, 1);
        pEndPublication.set(Calendar.HOUR_OF_DAY, 0);
        pEndPublication.set(Calendar.MINUTE, 0);
        pEndPublication.set(Calendar.SECOND, 0);
        pEndPublication.set(Calendar.MILLISECOND, 0);
        pEndPublication.add(Calendar.MILLISECOND, -1);
        doc.setPropertyValue(END_PUBLICATION, pEndPublication);

    }

    @Override
    public String getLastContributor() throws ClientException {
        String lastContributor = (String) doc.getPropertyValue(LAST_CONTRIBUTOR);
        if (StringUtils.isEmpty(lastContributor)) {
            lastContributor = getCreator();
        }
        return lastContributor;
    }

    @Override
    public DocumentModel getDocumentModel() {
        return doc;
    }

    public String getLastContributorFullName() throws Exception {
        if (StringUtils.isEmpty(lastContributorFullName)) {
            UserManager userManager = Framework.getService(UserManager.class);
            NuxeoPrincipal user = userManager.getPrincipal(getLastContributor());
            lastContributorFullName = user.getFirstName() + " "
                    + user.getLastName();
        }
        return lastContributorFullName;
    }

    @Override
    public HtmlRow addRow() throws ClientException {
        return getSection().addRow();
    }

    @Override
    public List<HtmlRow> getRows() {
        try {
            return getSection().getRows();
        } catch (Exception e) {
            return new ArrayList<HtmlRow>();
        }
    }

    private HtmlSection getSection() throws ClientException {
        if (section == null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Serializable>> sectionsMap = (List<Map<String, Serializable>>) doc.getPropertyValue("html:sections");
            List<HtmlSection> sections = new ArrayList<HtmlSection>(
                    sectionsMap.size());
            for (Map<String, Serializable> map : sectionsMap) {
                sections.add(new HtmlSectionImpl(this, map));
            }
            if (sections.size() > 0) {
                section = sections.get(0);
            } else {
                section = new HtmlSectionImpl(this);
                section.setTitle(getTitle());
            }

        }
        return section;

    }

    private void update() throws ClientException {
        List<Map<String, Serializable>> sectionsMap = new ArrayList<Map<String, Serializable>>();
        HtmlSectionImpl si = (HtmlSectionImpl) getSection();
        sectionsMap.add(si.toMap());
        doc.setPropertyValue("html:sections", (Serializable) sectionsMap);
    }

    @Override
    public HtmlRow row(int index) {
        try {
            return getSection().row(index);
        } catch (ClientException e) {
            return null;
        }
    }

    @Override
    public HtmlSection insertBefore() throws ClientException {
        return this;
    }

    @Override
    public void remove() throws ClientException {
        // Do nothing
    }

    @Override
    public HtmlRow insertBefore(HtmlRow htmlRow) throws ClientException {
        return getSection().insertBefore(htmlRow);
    }

    @Override
    public void remove(HtmlRow row) throws ClientException {
        getSection().remove(row);

    }

    @Override
    public void onChange(Object obj) throws ClientException {
        this.doc.getAdapter(MailNotification.class).reset();
        update();
        if (isParentPageNewsPublished()) {
            Calendar now = Calendar.getInstance();
            Calendar startPublication = getStartPublication();
            if (startPublication != null && startPublication.before(now) && now.before(getEndPublication())) {
                setStartPublication(now);
            }
        }

    }

    private boolean isParentPageNewsPublished() {
        DocumentModel parentDocument;
        try {
            parentDocument = doc.getCoreSession().getParentDocument(doc.getRef());
            PageNews pageNews = parentDocument.getAdapter(PageNews.class);
            if (pageNews != null) {
                if (pageNews.isVisible()) {
                    return true;
                }
            }
        } catch (ClientException e) {
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return this.doc.getId().equals(((LabsNews) obj).getDocumentModel().getId());
    }

}
