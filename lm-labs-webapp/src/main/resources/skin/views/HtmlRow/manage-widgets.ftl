<#assign contents = This.row.contents />
<#--
<ul>
<#list contents as content >
    <li> ${content.type}-${content.html}</li>
</#list>
</ul>
-->
<#include "macros/widgets.ftl" />
<#assign uncategorizedWidgets = Common.getUncategorizedWidgets(Document.type) />
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
            
			<#list Common.getPageWidgetGroups(Document.type) as widgetGroup>
			            <optgroup label="${widgetGroup}">
				<#assign widgetDocs = Common.getPageWidgets(Document.type, widgetGroup) />
				<#if (widgetDocs?size > 0) >
					<#list widgetDocs as widgetDoc >
			            <#assign gadgetType = widgetDoc['labshtmlpagewidgets:type'] gadgetName = widgetDoc['labshtmlpagewidgets:wname'] />
			            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
					</#list>
				</#if>
			</#list>
			<#if 0 < uncategorizedWidgets?size >
			            <optgroup label="Autres">
				<#list uncategorizedWidgets as gadgetName >
			            <@gadgetOption type="html" name=gadgetName selected=isOptionSelected("html", gadgetName, widgets) />
				</#list>
			</#if>
            <#-- gadget opensocial désactivé
            <#assign gadgetType = "opensocial" gadgetName = "lastuploads" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            -->
            <#-- TODO
            <#assign gadgetType = "opensocial" gadgetName = "bookmarks" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "navigation" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            -->
            <#-- Pas compatible : la config du gadget est faite par le gadget lui-meme (sur base des permissions, 
            	qui ne sont pas supportees par le light-container) et ne se base pas sur les user prefs
            <#assign gadgetType = "opensocial" gadgetName = "rssreader" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            -->
            <#-- Ne marchent pas
            <#assign gadgetType = "opensocial" gadgetName = "meteo" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            -->
            <#-- Gadgets opensocial externes
            -->
            <#-- Gadgets opensocial externes qui ne marchent pas
            <#assign gadgetType = "opensocial" gadgetName = "googleweather" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "Le Monde" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "Weather" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            -->
        </select>
    </div>
</div>
</#list>

