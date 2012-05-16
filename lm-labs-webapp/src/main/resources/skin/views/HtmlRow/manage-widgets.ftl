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
            <option value="html/editor"<#if content.type == "html"> selected</#if> >Aucun</option>
            <#assign gadgetType = "html" gadgetName = "lastuploads" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "html" gadgetName = "children" />
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
    <option value="${type}/${name}"<#if selected > selected</#if> ><#if type == "html">Widget<#else>Gadget</#if> ${name}</option>
</#macro>
<#function isOptionSelected type name widgets widgetNbr=0 >
    <#if 0 < widgets?size && widgets[widgetNbr].type.type() == type && widgets[widgetNbr].name == name >
        <#return true />
    </#if>
    <#return false />
</#function>

