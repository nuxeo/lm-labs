package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.platform.picture.api.ImageInfo;
import org.nuxeo.ecm.platform.picture.api.ImagingService;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.NullException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.bean.ThemeProperty;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.PropertyType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class SiteThemeAdapter implements SiteTheme {

    private static final String CACHE_NAME_PREFIX = "cache.";

    private static final String DC_MODIFIED = "dc:modified";

    public static final Map<String, ThemeProperty> EMPTY_PROPERTIES = new HashMap<String, ThemeProperty>();

    private static final Log LOG = LogFactory.getLog(SiteThemeAdapter.class);

    private static final String PROPERTY_NAME = "dc:title";

    private static final String PROPERTY_BANNER_BLOB = Schemas.SITETHEME.prefix()
            + ":banner";

    private static final String PROPERTY_LOGO_BLOB = Schemas.SITETHEME.prefix()
            + ":logo";

    private static final String PROPERTY_LOGO_AREA_HEIGHT = Schemas.SITETHEME.prefix()
            + ":logoAreaHeight";

    private static final String PROPERTY_LOGO_POSX = Schemas.SITETHEME.prefix()
            + ":logo_posx";

    private static final String PROPERTY_LOGO_POSY = Schemas.SITETHEME.prefix()
            + ":logo_posy";

    private static final String PROPERTY_LOGO_RESIZE_RATIO = Schemas.SITETHEME.prefix()
            + ":logo_resize_ratio";

    private static final String PROPERTY_STYLECSS = Schemas.SITETHEME.prefix()
            + ":style";

    private static final String PROPERTY_CACHEDSTYLECSS = Schemas.SITETHEME.prefix()
            + ":cachedstyle";

    private static final String PROPERTIES = Schemas.SITETHEME.prefix()
            + ":properties";

    private static final String LAST_READ = Schemas.SITETHEME.prefix()
            + ":lastRead";

    private final DocumentModel doc;

    public SiteThemeAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    public DocumentModel getDocument() {
        return doc;
    }

    @Override
    public String getName() throws ClientException {
        String name = (String) doc.getTitle();
        return name;
    }

    @Override
    public void setName(String name) throws ClientException {
        doc.setPropertyValue(PROPERTY_NAME, name);
    }

    @Override
    public Blob getBanner() throws ClientException {
        return (Blob) doc.getPropertyValue(PROPERTY_BANNER_BLOB);
    }

    @Override
    public void setBanner(Blob blob) throws ClientException {
        doc.setPropertyValue(PROPERTY_BANNER_BLOB, (Serializable) blob);
    }

    @Override
    public Blob getLogo() throws ClientException {
        Blob blob = (Blob) doc.getPropertyValue(PROPERTY_LOGO_BLOB);
        return blob;
    }

    @Override
    public void setLogo(Blob blob) throws ClientException {
        doc.setPropertyValue(PROPERTY_LOGO_BLOB, (Serializable) blob);
    }

    @Override
    public int getLogoPosX() throws ClientException {
        return ((Long) doc.getPropertyValue(PROPERTY_LOGO_POSX)).intValue();
    }

    @Override
    public void setLogoPosX(int pos) throws ClientException {
        doc.setPropertyValue(PROPERTY_LOGO_POSX, new Long(pos));

    }

    @Override
    public int getLogoPosY() throws ClientException {
        return ((Long) doc.getPropertyValue(PROPERTY_LOGO_POSY)).intValue();
    }

    @Override
    public void setLogoPosY(int pos) throws ClientException {
        doc.setPropertyValue(PROPERTY_LOGO_POSY, new Long(pos));
    }

    @Override
    public int getLogoResizeRatio() throws ClientException {
        return ((Long) doc.getPropertyValue(PROPERTY_LOGO_RESIZE_RATIO)).intValue();
    }

    @Override
    public void setLogoResizeRatio(int pos) throws ClientException {
        doc.setPropertyValue(PROPERTY_LOGO_RESIZE_RATIO, new Long(pos));
    }

    @Override
    public int getLogoWidth() throws ClientException {
        Blob blob = (Blob) doc.getPropertyValue(PROPERTY_LOGO_BLOB);
        if (blob == null) {
            return 0;
        }
        int logoResizeRatio = getLogoResizeRatio();
        ImagingService imagingService = null;
        try {
            imagingService = Framework.getService(ImagingService.class);
            if (imagingService == null) {
                return 0;
            }
            final ImageInfo imageInfo = imagingService.getImageInfo(blob);
            if (imageInfo == null) {
                LOG.error("image infos are null");
                return 0;
            }
            return (imageInfo.getWidth() * logoResizeRatio / 100);
        } catch (Exception e1) {
            LOG.error("Unable to get Imaging Service: " + e1.getMessage());
        }
        return 0;
    }

    @Override
    public String getStyle() throws ClientException {
        return (String) doc.getPropertyValue(PROPERTY_STYLECSS);
    }

    @Override
    public void setStyle(String style) throws ClientException {
        doc.setPropertyValue(PROPERTY_STYLECSS, style);
    }

    @Override
    public Map<String, ThemeProperty> getProperties() throws ClientException {
        Map<String, ThemeProperty> properties = new HashMap<String, ThemeProperty>();
        Serializable objPropertiesList = doc.getProperty(PROPERTIES);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> propertiesList = (List<Map<String, Object>>) objPropertiesList;
        ThemeProperty prop = null;
        for (Map<String, Object> list : propertiesList) {
            try {
                String typeStr = Tools.getString(list.get("type"));
                PropertyType type = PropertyType.fromString(typeStr);
                if (type == null) {
                    LOG.warn("Unable to get property: " + typeStr);
                    continue;
                }
                prop = new ThemeProperty(
                        Tools.getString(list.get("key")),
                        Tools.getString(list.get("value")),
                        Tools.getString(list.get("label")),
                        Tools.getString(list.get("description")),
                        type,
                        Tools.getInt(list.get("orderNumber")));
            } catch (NullException e) {
                LOG.error("Unable to get property: " + e.getMessage());
            }
            properties.put(prop.getKey(), prop);
        }
        return properties;
    }

    @Override
    public void setProperties(Map<String, ThemeProperty> properties)
            throws ClientException {
        List<Map<String, Object>> listProperties = new ArrayList<Map<String, Object>>();
        if (properties != null) {
            for (Map.Entry<String, ThemeProperty> property : properties.entrySet()) {
                listProperties.add(getPropertyMap(property));
            }
            doc.getProperty(PROPERTIES)
                    .setValue(listProperties);
        } else {
            doc.getProperty(PROPERTIES)
                    .setValue(null);
        }
    }

    private Map<String, Object> getPropertyMap(
            Entry<String, ThemeProperty> pProperty) {
        Map<String, Object> property = new HashMap<String, Object>();
        property.put("key", pProperty.getKey());
        property.put("value", pProperty.getValue()
                .getValue());
        property.put("label", pProperty.getValue()
                .getLabel());
        property.put("description", pProperty.getValue()
                .getDescription());

        if (pProperty.getValue()
                .getType() != null) {
            property.put("type", pProperty.getValue()
                    .getType().toString());
        } else {
            property.put("type", null);
        }
        property.put("orderNumber", pProperty.getValue()
                .getOrderNumber());
        return property;
    }

    @Override
    public long getLastRead() throws ClientException {
        Long lastRead = (Long) doc.getPropertyValue(LAST_READ);
        if (lastRead != null) {
            return lastRead.longValue();
        }
        return 0;
    }

    @Override
    public void setLastRead(long lastRead) throws ClientException {
        doc.setPropertyValue(LAST_READ, lastRead);
    }

    @Override
    public int getLogoAreaHeight() throws ClientException {
        return ((Long) doc.getPropertyValue(PROPERTY_LOGO_AREA_HEIGHT)).intValue();
    }

    @Override
    public void setLogoAreaHeight(int height) throws ClientException {
        doc.setPropertyValue(PROPERTY_LOGO_AREA_HEIGHT, new Long(height));
    }

    @Override
    public String getCssValue() throws ClientException {
        DocumentModel cache = getSibblingDocument();
        if (cache != null) {
            Calendar cacheModified = (Calendar) cache.getPropertyValue(DC_MODIFIED);
            Calendar modified = (Calendar) doc.getPropertyValue(DC_MODIFIED);
            if (modified.before(cacheModified)) {
                return (String) cache.getPropertyValue(PROPERTY_CACHEDSTYLECSS);
            }
        }
        return null;
    }

    private DocumentModel getSibblingDocument() throws ClientException {
        CoreSession session = doc.getCoreSession();
        String sibblingName = CACHE_NAME_PREFIX + doc.getName();
        DocumentModel parent = session.getDocument(doc.getParentRef());
        DocumentRef cacheRef = new PathRef(parent.getPathAsString() + "/"
                + sibblingName);
        if (session.exists(cacheRef)) {
            return session.getDocument(cacheRef);
        }
        return null;
    }

    @Override
    public void setCssValue(final String css) throws ClientException {
        DocumentModel cache = getSibblingDocument();
        if (cache == null) {
            cache = createSibbling();
        }
        final DocumentRef cacheRef = cache.getRef();
        UnrestrictedSessionRunner runner = new UnrestrictedSessionRunner(doc.getCoreSession()){
            @Override
            public void run() throws ClientException {
                DocumentModel cache = session.getDocument(cacheRef);
                cache.setPropertyValue(PROPERTY_CACHEDSTYLECSS, css);
                session.saveDocument(cache);
                session.save();
            }
        };
        runner.runUnrestricted();
    }

    private DocumentModel createSibbling() throws ClientException {
        CoreSession session = doc.getCoreSession();
        final String sibblingName = CACHE_NAME_PREFIX + doc.getName();
        final DocumentModel parent = session.getDocument(doc.getParentRef());
        
        UnrestrictedSessionRunner runner = new UnrestrictedSessionRunner(session){
            @Override
            public void run() throws ClientException {
            	DocumentModel cache = session.createDocumentModel(
                        parent.getPathAsString(), sibblingName,
                        LabsSiteConstants.Docs.SITETHEME.type());
                cache = session.createDocument(cache);
                session.save();
            }

        };
        runner.runUnrestricted();
        DocumentModel cache = session.getChild(parent.getRef(), sibblingName);
        return cache;
    }

}