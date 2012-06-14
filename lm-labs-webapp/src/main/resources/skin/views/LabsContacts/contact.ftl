<@cache name="LabsSites-footer" key="LabsSites-footer-contacts-${Common.siteDoc(Document).getSite().URL}" >
<#if contactsAdmin?size &gt; 0>
	<#assign mySite=Common.siteDoc(Document).getSite() />
	${Context.getMessage("label.footer.contact.other")}
	<#list contactsAdmin as contact>
		<a href="mailto:${contact.email}">${contact.displayName} (${contact.ldap})</a><#if contact != contactsAdmin?last>, </#if>
	</#list>
	<#if mySite.isAdministrator(Context.principal.name)> 
        <a class="editblock" href="${Context.modulePath}/${mySite.URL}/@views/edit_contacts" >
            <img style="vertical-align: middle;" title="${Context.getMessage("label.footer.contact.goToAdminContact")}" alt="${Context.getMessage("label.footer.contact.goToAdminContact")}" src="${skinPath}/images/edit.gif" />
        </a>
	</#if>
</#if>
</@cache>
