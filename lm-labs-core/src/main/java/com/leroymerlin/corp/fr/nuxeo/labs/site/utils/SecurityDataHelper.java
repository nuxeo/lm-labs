package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.UserEntry;
import org.nuxeo.ecm.core.api.security.impl.ACPImpl;

import com.leroymerlin.common.core.security.SecurityData;
import com.leroymerlin.common.core.security.SecurityDataConverter;


//TODO move to lm-nuxeo-common and delete from lm-portal-common
/**
 * @author vdutat
 * @deprecated use {@link com.leroymerlin.common.core.security.SecurityDataHelper} instead
 *
 */
@Deprecated
public class SecurityDataHelper {

    // Static class
    private SecurityDataHelper() {
    };

    /**
     * Returns a security data class given a document
     *
     * @param currentDocument
     * @return
     * @throws ClientException
     */
    public static SecurityData buildSecurityData(DocumentModel currentDocument)
            throws ClientException {
        try {
            SecurityData securityData = new SecurityData();
            securityData.setDocumentType(currentDocument.getType());

            ACP acp = currentDocument.getACP();

            if (null != acp) {
                SecurityDataConverter.convertToSecurityData(acp, securityData);
            } else {
                securityData.clear();
            }
            return securityData;
        } catch (Throwable t) {
            throw ClientException.wrap(t);
        }

    }

    /**
     * Update the security on a document given a security data class The session
     * must be saved after invocation
     *
     * @param currentDocument
     * @param securityData
     * @throws ClientException
     */
    public static void updateSecurityOnDocument(DocumentModel currentDocument,
            SecurityData securityData) throws ClientException {
        try {
            List<UserEntry> modifiableEntries = SecurityDataConverter.convertToUserEntries(securityData);
            ACP acp = currentDocument.getACP();

            if (null == acp) {
                acp = new ACPImpl();
            }

            acp.setRules(modifiableEntries.toArray(new UserEntry[0]));

            currentDocument.setACP(acp, true);

        } catch (Throwable t) {
            throw ClientException.wrap(t);
        }
    }

    /**
     * Copy the security from the source {@link DocumentModel} to the
     * destination {@link DocumentModel} /!\ The session must be saved after
     * invocation
     *
     * @param src source {@link DocumentModel}
     * @param dest destination {@link DocumentModel}
     * @throws ClientException
     */
    public static void copySecurity(final DocumentModel src,
            DocumentModel dest, final Boolean override) throws ClientException {
        ACP srcACP = src.getACP();
        dest.setACP(srcACP, override);
    }

    /**
     * Block inheritance in security
     *
     * @param doc the {@link DocumentModel}
     * @throws ClientException
     */
    public static void blockInheritedSecurity(DocumentModel doc)
            throws ClientException {
        SecurityData buildSecurityData = SecurityDataHelper.buildSecurityData(doc);
        buildSecurityData.setBlockRightInheritance(true, null);
        SecurityDataHelper.updateSecurityOnDocument(doc, buildSecurityData);
    }

    /**
     * Returns a security data class given a document
     *
     * @param doc
     * @return
     * @throws ClientException
     */
    public static Map<String, List<String>> getGrantAuthorizationFromDocument(
            final DocumentModel doc) throws ClientException {
        SecurityData sd = SecurityDataHelper.buildSecurityData(doc);
        return sd.getCurrentDocGrant();
    }

}
