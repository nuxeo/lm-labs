<#if permissionsAdmin?size == 1>
	${Context.getMessage("label.footer.contact.oneAdmin")}
<#elseif permissionsAdmin?size &gt; 1>
	${Context.getMessage("label.footer.contact.manyAdmin")}
</#if>

<#assign i=0 />
<#list permissionsAdmin as perm>
	<#if i &gt; 0>, </#if><a href="mailto:${usernameAndEmail[perm.name]}">${perm.name}</a>
	<#assign i=i+1 />
</#list>
