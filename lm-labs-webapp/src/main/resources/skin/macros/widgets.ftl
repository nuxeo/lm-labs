<#macro gadgetOption type name selected=false >
    <#assign widgetTitle = Context.getMessage('label.HtmlPage.widget.' + type + '.' + name) />
    <#if widgetTitle?starts_with('!') >
        <#assign widgetTitle = name />
    </#if>
    <option value="${type}/${name}"<#if selected > selected</#if> >Widget ${widgetTitle}</option>
</#macro>

<#function isOptionSelected type name widgets widgetNbr=0 >
    <#if 0 < widgets?size && widgets[widgetNbr].type.type() == type && widgets[widgetNbr].name == name >
        <#return true />
    </#if>
    <#return false />
</#function>

<#macro determineWidgetType content >
    <#if content.type == "widgetcontainer">
        <#assign widgets = content.getGadgets(Session) />
    </#if>
    <#if 0 < widgets?size >
        <#if widgets[0].type.type() == "opensocial" >
            <#assign isOsGadgetCol = true />
        <#else>
            <#assign isWidgetCol = true />
        </#if>
    </#if>
</#macro>

<#macro displayContentHtmlWidget widget widgetMode="view" sectionIdx=0 rowIdx=0 columnIdx=0 content="" >
	<#if Common.declaredHtmlWidgets?seq_contains(widget.name) >
		<#assign section_index = sectionIdx /> <#-- this is NOT useless code -->
		<#assign row_index = rowIdx /> <#-- this is NOT useless code -->
		<#assign content_index = columnIdx /> <#-- this is NOT useless code -->
	    <#include "widgets/${widget.name}.ftl" />
	<#else>
	    <#assign widgetTitle = Context.getMessage('label.HtmlPage.widget.' + widget.type + '.' + widget.name) />
	    <#if widgetTitle?starts_with('!') >
	        <#assign widgetTitle = widget.name />
	    </#if>
		<span>Le widget '${widgetTitle}' n'est pas disponible</span>
	</#if>
</#macro>

<#macro displayContentHtml content >
    <#if content.html == "" >
    &nbsp;
    <#else>
        ${content.html}
    </#if>
</#macro>