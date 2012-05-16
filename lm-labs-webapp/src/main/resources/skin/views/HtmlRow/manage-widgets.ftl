<#assign contents = This.row.contents />
<#--
<ul>
<#list contents as content >
    <li> ${content.type}-${content.html}</li>
</#list>
</ul>
-->

<#list contents as content >
    <#assign widgets = [] />
    <#if content.type == "widgetcontainer">
        <#assign widgets = content.getGadgets(Session) />
    </#if>
<div class="control-group">
    <label class="control-label" for="column${content_index}">Colonne ${content_index + 1}</label>
    <div class="controls" >
        <select name="column${content_index}" >
            <#assign widgetTitle = Context.getMessage('label.HtmlPage.widget.html.editor') />
            <#if widgetTitle?starts_with('!') >
                <#assign widgetTitle = "Aucun" />
            </#if>
            <option value="html/editor"<#if content.type == "html"> selected</#if> >${widgetTitle}</option>
            <#assign gadgetType = "html" gadgetName = "lastuploads" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "html" gadgetName = "children" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "html" gadgetName = "siteRssFeed-lastNews" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "lmactu" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "Contact" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "calculette" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#-- TODO
            <#assign gadgetType = "opensocial" gadgetName = "rss" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "bookmarks" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            -->
            <#-- Pas compatible
            <#assign gadgetType = "opensocial" gadgetName = "rssreader" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            -->
            <#-- Ne marchent pas
            <#assign gadgetType = "opensocial" gadgetName = "meteo" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "lastuploads" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            -->
        </select>
    </div>
</div>
</#list>

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

