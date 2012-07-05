<#assign widgetType = This.widget.type.type() widgetName = This.widget.name />
<#assign opensocialWidgetsCustomConfig = ["video", "flash", "picturebook", "pageclasseurfolder"] />

<#if widgetType == "opensocial" >
    <#if opensocialWidgetsCustomConfig?seq_contains(widgetName) >
        <#include "views/HtmlWidget/${widgetType}-${widgetName}-config.ftl" />
    <#else>
        <#include "views/HtmlWidget/${widgetType}-config.ftl" />
    </#if>
<#elseif widgetType == "html" && widgetName == "externalContent" >
    <#include "views/HtmlWidget/${widgetType}-${widgetName}-config.ftl" />
</#if>
