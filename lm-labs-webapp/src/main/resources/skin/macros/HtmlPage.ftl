<#macro displayContentHtmlWidget widget widgetMode="view" sectionIdx=0 rowIdx=0 columnIdx=0 >
	<#assign section_index = sectionIdx /> <#-- this is NOT useless code -->
	<#assign row_index = rowIdx /> <#-- this is NOT useless code -->
	<#assign content_index = columnIdx /> <#-- this is NOT useless code -->
    <#if availableHtmlWidgets?seq_contains(widget.name) >
        <#include "widgets/${widget.name}.ftl" />
    <#else>
    	Widget pas disponible.
    </#if>
</#macro>
