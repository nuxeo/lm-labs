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
            <optgroup label="Usine à Sites - Contenu">
            <#assign gadgetType = "html" gadgetName = "externalContent" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            </optgroup>
            <optgroup label="Usine à Sites - Navigation">
            <#-- gadget opensocial désactivé
            <#assign gadgetType = "opensocial" gadgetName = "lastuploads" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            -->
            <#assign gadgetType = "html" gadgetName = "lastuploads" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "html" gadgetName = "children" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "pageclasseurfolder" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "html" gadgetName = "myPages" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "html" gadgetName = "pagesSameAuthor" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "html" gadgetName = "myDraftPages" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "html" gadgetName = "draftPagesSameAuthor" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            </optgroup>
            <optgroup label="Usine à Sites - Actualités">
            <#assign gadgetType = "html" gadgetName = "siteRssFeed-lastNews" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "html" gadgetName = "myPublishedNews" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "html" gadgetName = "publishedNewsSameAuthor" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            </optgroup>
            <optgroup label="Actualités">
            <#assign gadgetType = "opensocial" gadgetName = "rss" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "lmactu" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            </optgroup>
            <optgroup label="Média">
            <#assign gadgetType = "opensocial" gadgetName = "picturebook" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "video" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "flash" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            </optgroup>
            <optgroup label="Intranet">
            <#assign gadgetType = "opensocial" gadgetName = "IDENOV" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "Contact" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            <#assign gadgetType = "opensocial" gadgetName = "hi42" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            </optgroup>
            <optgroup label="Divers">
            <#assign gadgetType = "opensocial" gadgetName = "calculette" />
            <@gadgetOption type=gadgetType name=gadgetName selected=isOptionSelected(gadgetType, gadgetName, widgets) />
            </optgroup>
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

