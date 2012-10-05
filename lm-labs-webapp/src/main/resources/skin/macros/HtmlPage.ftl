<#macro displayContentHtmlWidget widget widgetMode="view" sectionIdx=0 rowIdx=0 columnIdx=0 content="" >
	<#assign section_index = sectionIdx /> <#-- this is NOT useless code -->
	<#assign row_index = rowIdx /> <#-- this is NOT useless code -->
	<#assign content_index = columnIdx /> <#-- this is NOT useless code -->
    <#if availableHtmlWidgets?seq_contains(widget.name) >
        <#include "widgets/${widget.name}.ftl" />
    <#else>
    	Widget pas disponible.
    </#if>
</#macro>

<#macro generateCssClass row >
	<#if row??>
		<#assign listUserClass=row.userClass>
<#if row.cssClass??> ${row.cssClass}</#if><#if (listUserClass?size > 0)><#list listUserClass as userClass> ${userClass}</#list></#if>
	</#if>
</#macro>

<#macro generateInputCssClass row >
	<#assign listUserClass=row.userClass>
<#if (listUserClass?size > 0)><#list listUserClass as userClass>"${userClass}"<#if (listUserClass?last != userClass)>,</#if></#list></#if></#macro>

<#macro generateAvailableUserClass mapUserClass >
	<#assign keys = mapUserClass?keys>
<#if (keys?size > 0)><#list keys as key>{id:'${mapUserClass[key]}',text:'${key?js_string}'}<#if (keys?last != key)>,</#if></#list></#if></#macro>