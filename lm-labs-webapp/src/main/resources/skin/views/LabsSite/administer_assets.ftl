<#if site?? && (Session.hasPermission(site.document.ref, "Everything") || Session.hasPermission(Document.ref, 'ReadWrite'))>
<#assign adminTreeviewType="Assets" />
<#include "views/common/administer_treeview.ftl" />
<#else>
	<#include "error/error_404.ftl" >
</#if>