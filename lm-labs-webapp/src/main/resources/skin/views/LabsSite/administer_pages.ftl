<#if site?? && Session.hasPermission(site.document.ref, "Everything")>
<#assign adminTreeviewType="Pages" />
<#include "views/common/administer_treeview.ftl" />
<#else>
	<#include "error/error_404.ftl" >
</#if>
