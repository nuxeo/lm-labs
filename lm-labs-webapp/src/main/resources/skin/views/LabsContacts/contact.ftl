<#if contactsAdmin?size &gt; 0>
	${Context.getMessage("label.footer.contact.other")}
	<#assign i=0 />
	<#list contactsAdmin as contact>
		<#if i &gt; 0>, </#if><a href="mailto:${contact.email}">${contact.displayName} (${contact.ldap})</a>
		<#assign i=i+1 />
	</#list>
</#if>