<#assign userPreferences = This.userPreferences />
<#assign userPreferenceValues = This.userPreferenceValues />
<#if 0 < userPreferences?size >
    <#list userPreferences?keys as prefName >
        <#assign userPref = This.getUserPrefByName(prefName) />
<div class="control-group">
        <#if userPreferences[prefName] == "string" || userPreferences[prefName] == "enum" >
<label class="control-label" for="${prefName}" >${userPref.displayName}</label>
        <#else>
        </#if>
        <#if userPreferences[prefName] == "string" >
<div class="controls" >
    <input type="text" class="input" name="${prefName}" value="${userPreferenceValues[prefName]}" />
</div>
        <#elseif userPreferences[prefName] == "enum" >
<div class="controls" >
    <select name="${prefName}" >
            <#assign enumValues = userPref.enumValues />
            <#list enumValues?keys as enumValue >
        <option value="${enumValues[enumValue]}" <#if enumValues[enumValue] == userPref.actualValue >selected=""</#if> >${enumValue}</option>
            </#list>
    </select>
</div>
        <#else>
${Context.getMessage('label.HtmlPage.widget.config.preference.not-supported', prefName)}
        </#if>
</div>
    </#list>
<#else>
    Pas de préférence configurable
</#if>
