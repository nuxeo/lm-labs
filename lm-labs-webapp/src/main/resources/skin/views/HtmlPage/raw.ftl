<#include "macros/HtmlPage.ftl" />
<#list page.sections as section >
	<@displayRawSection section=section />
</#list>