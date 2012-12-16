<#assign widgetType = This.widget.type.type() widgetName = This.widget.name />

<#if Common.widgetTypes?seq_contains(widgetType) >
    <#if Common.getWidgetsWithCustomConfigView(widgetType)?seq_contains(widgetName) >
        <#include "views/HtmlWidget/${widgetType}-${widgetName}-config.ftl" />
    <#else>
        <#include "views/HtmlWidget/${widgetType}-config.ftl" />
    </#if>
</#if>
