<#function getTemplatesMap theme >
    <#assign templatesMap = [] />
    <#list Common.getTemplates(theme) as template>
        <#assign trad = Context.getMessage('label.labssites.appearance.templates.' + template) />
        <#if trad?starts_with('!') >
            <#assign templateName = template />
        <#else>
            <#assign templateName = trad />
        </#if>
        <#assign templateDesc = Context.getMessage('label.labssites.appearance.templates.' + template + '.description') />
        <#if templateDesc?starts_with('!') >
            <#assign templateDesc = "" />
        </#if>
        <#assign templatesMap = templatesMap + [ {"name" : template, "title" : templateName, "description" : templateDesc} ] />
    </#list>
    <#return templatesMap />
</#function>