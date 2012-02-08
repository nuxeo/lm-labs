<#if contactsAdmin?size &gt; 0>
	${Context.getMessage("label.footer.contact.other")}
	<#list contactsAdmin as contact>
		<a href="mailto:${contact.email}">${contact.displayName} (${contact.ldap})</a><#if contact != contactsAdmin?last>, </#if>
	</#list>
	<#if Session.hasPermission(Document.ref, 'Everything') >
        <a class="editblock" href="${Context.modulePath}/${site.URL}/@views/edit_contacts">
            <img style="vertical-align: middle;" title="${Context.getMessage("label.footer.contact.goToAdminContact")}" alt="${Context.getMessage("label.footer.contact.goToAdminContact")}" src="${skinPath}/images/edit.gif" />
        </a>
	</#if>
</#if>