<#assign widgetType = This.widget.type.type() widgetName = This.widget.name />
<#if widgetType == "opensocial" >
    <#if widgetName == "rss-" >
        <#include "views/HtmlWidget/${widgetType}-${widgetName}-config.ftl" />
    <#else>
        <#assign userPreferences = This.userPreferences />
        <#assign userPreferenceValues = This.userPreferenceValues />
        <#if 0 < userPreferences?size >
            <#list userPreferences?keys as prefName >
    <div class="control-group">
                <#if userPreferences[prefName] == "string" >
        <label class="control-label" for="${prefName}" >${prefName}</label>
        <div class="controls" >
            <input type="text" class="input" name="${prefName}" value="${userPreferenceValues[prefName]}" />
        </div>
                <#else>
        ${Context.getMessage('label.HtmlPage.widget.config.preference.not-supported', prefName)}
                </#if>
    </div>
            </#list>
        <#else>
            Pas de préférence configurable
        </#if>
    </#if>
</#if>
