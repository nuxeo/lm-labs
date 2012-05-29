<#assign widgetType = This.widget.type.type() widgetName = This.widget.name />
<#if widgetType == "opensocial" >
    <#if widgetName == "video" || widgetName == "flash" >
        <#include "views/HtmlWidget/${widgetType}-${widgetName}-config.ftl" />
    <#else>
        <#include "views/HtmlWidget/${widgetType}-config.ftl" />
    </#if>
</#if>
