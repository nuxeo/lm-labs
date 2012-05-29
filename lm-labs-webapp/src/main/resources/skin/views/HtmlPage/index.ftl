<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
  <#assign nbrOsGadgets = 0 />
  <#assign mySite=Common.siteDoc(Document).site />
  <#assign availableHtmlWidgets = ["children", "lastuploads", "siteRssFeed-lastNews"] />
  <@block name="title">${mySite.title}-${This.document.title}</@block>

  <@block name="css">
    <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/js/assets/prettify/prettify.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.fancybox-1.3.4.css" />
        <link rel="stylesheet" href="${contextPath}/css/opensocial/light-container-gadgets.css">
  </@block>

  <@block name="scripts">
    <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.hotkeys.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
        <script type="text/javascript" src="${skinPath}/js/assets/prettify/prettify.js"></script>
        <script type="text/javascript" src="${skinPath}/js/PageHtml.js"></script>
        <script type="text/javascript" src="${skinPath}/js/manageDisplayHtmlLine.js"></script>

  </@block>

  <@block name="content">
  <#--
  <@cache name="${This.cacheName}" key="${This.cacheKey}" >
  -->
  <@tableOfContents>
   	<div class="container-fluid">

   	<#include "views/common/page_header.ftl">

  <#assign isContributor = This.page?? && This.page.isContributor(Context.principal.name) />
  <#if isContributor >
    <#assign layouts = This.columnLayoutsSelect />
  </#if>

  <#list page.sections as section>
    <section id="section_${section_index}">
        <div class="page-header">
            <a name="section_${section_index}"></a>
            <h1 style="display:inline;">${section.title}</h1><h2 style="display:inline;"> <small>${section.description}</small></h2>
	        <#if isContributor >
		        <div class=" editblock btn-group pull-right" style="float: right;margin-top: 7px;">
			      	<a class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><i class="icon-cog"></i>Section <span class="caret"></span></a>
					<ul class="dropdown-menu" style="left: auto;right: 0px;">
						<li>
							<a id="addsectionlink" href="#" rel="addsection" class="open-dialog" ><i class="icon-plus"></i>Ajouter une section</a>
							<a href="#" rel="editsection_${section_index}" class="open-dialog" ><i class="icon-edit"></i>Modifier la section</a>
							<a href="#section_${section_index}" id="actionAddLineOnSection_${section_index}" onClick="javascript:actionAddLine('${section_index}');" ><i class="icon-eye-open"></i>Ajouter une ligne</a>
							<a href="#" onclick="if(confirm('Voulez vous vraiment supprimer cette section ?')) { $('#frm_section_${section_index}_delete').submit();} ;return false;"><i class="icon-remove"></i>Supprimer la section</a>
						</li>
					</ul>
			    </div>
			</#if>
        </div>

        <#if isContributor >
        	<div class="editblock">
		        <div id="divAddRow_${section_index}" class="well" style="padding: 5px;display: none;">
		          <div style="float: right;">
		          	<a href="#section_${section_index}" onClick="javascript:actionAddLine('${section_index}');" ><i class="icon-remove"></i></a>
		          </div>
		          <form class="form-horizontal" id="addrow_${section_index}" action="${This.path}/s/${section_index}" method="post" >
		          	  <input type="hidden" name="action" value="addrow"/>
		              <fieldset>
		                <legend>Ajouter une ligne</legend>
		                <div class="control-group">
		                  <label class="control-label" for="title">Type de ligne</label>
		                  <div class="controls">
		                    <select name="rowTemplate">
		                    <#list layouts?keys as layoutCode >
		                      <option value="${layoutCode}">${Context.getMessage(layouts[layoutCode])}</option>
		                    </#list>
			                </select>
			                <div id="displayCssClass_${section_index}" style="display: none;float: right;">
			                	Classe CSS : <input class="input-medium" name="cssClass" />
			                </div>
			                <div id="herfDisplayCssClass_${section_index}" style="float: right;cursor: pointer;" onClick="javascript:displayCssClass('${section_index}');">
			                	<br>Ajouter un style à la ligne
			                </div>
			                <button type="submit" class="btn btn-small btn-primary">Ajouter</button>
			                <p class="help-block">
			                    Sélectionnez le type de ligne à ajouter. Plusieurs modèles sont disponibles, les chiffres entre
			                    parenthèses représentent des pourcentages de taille de colonne.
			                </p>
		                  </div>

		                </div>
		              </fieldset>
		          </form>
		        </div>
		     </div>

          <div id="editsection_${section_index}" >
          	  <h1>Modifier la section</h1>
		      <form class="form-horizontal " name="formEditsection_${section_index}" action="${This.path}/s/${section_index}/@put" method="post">
			      <fieldset>
		            <div class="control-group">
		              <label class="control-label" for="title">Titre</label>
		              <div class="controls">
		                <input class="input-large" id="sectionTitle" name="title" size="30" type="text" value="${section.title}"/>
		              </div>
		            </div>

		            <div class="control-group">
		              <label class="control-label" for="description">Sous-titre</label>
		              <div class="controls">
		                <input class="input-large" id="sectionDescription" name="description" size="30" type="text" value="${section.description}"/>
		                <p class="help-block">
		                  Texte ajouté en petit à côté du titre
		                </p>
		              </div>
		            </div>
			      </fieldset>
		          <div class="actions">
		            <button onclick="javascript:jQuery('#waitingPopup').dialog2('open');document.formEditsection_${section_index}.submit();return true;" class="btn btn-primary">Modifier</button>&nbsp;
		          </div>
		      </form>
          </div>
       	  <!-- Hidden form to handle delete action -->
          <form action="${This.path}/s/${section_index}/@delete" method="get" id="frm_section_${section_index}_delete">
          </form>

		</#if>

        <#list section.rows as row>
        	<#if isContributor >
	          <div class="row-fluid<#if row.cssClass??> ${row.cssClass}</#if>" id="row_s${section_index}_r${row_index}">
	              <#list row.contents as content>
		              <div class="span${content.colNumber}">
                      <#assign isWidgetCol = false />
                      <#assign isOsGadgetCol = false />
                      <#assign widgets = [] />
                      <@determineWidgetType content=content />
                      <#if isOsGadgetCol >
                        <div id="gadgetCol-s_${section_index}_r_${row_index}_c_${content_index}" class="columns viewblock" >
                        <@openSocialGadgetJavascript selector="#gadgetCol-s_${section_index}_r_${row_index}_c_${content_index} > div.opensocialGadgets" widget=widgets[0] />
                        <div id="${widgets[0].doc.id}" class="opensocialGadgets gadget-${widgets[0].name} bloc">
                        </div>
                        </div>
                        <div class="columns editblock bloc" style="text-align: center;" >
                            <input type="hidden" class="section-index-value" value="${section_index}" />
                            <input type="hidden" class="row-index-value" value="${row_index}" />
                            <input type="hidden" class="content-index-value" value="${content_index}" />
                            <input type="hidden" class="widget-type" value="${widgets[0].type.type()}" />
                            <input type="hidden" class="widget-name" value="${widgets[0].name}" />
                            <#assign widgetTitle = Context.getMessage('label.HtmlPage.widget.' + widgets[0].type.type() + '.' + widgets[0].name) />
                            <#if widgetTitle?starts_with('!') >
                                <#assign widgetTitle = widgets[0].name />
                            </#if>
                            <a class="btn open-dialog" rel="divConfigGadget" ><i class="icon-edit"></i>${Context.getMessage('command.HtmlPage.widget.config.button')} ${widgetTitle}</a>
                        </div>
                      <#elseif isWidgetCol >
                        <div class="columns" >
                        <#if availableHtmlWidgets?seq_contains(widgets[0].name) >
                            <#include "widgets/${widgets[0].name}.ftl" />
                        </#if>
                        </div>
                      <#else>
	                    <div class="columns viewblock">
	                       <@displayContentHtml content=content />
	                    </div>
			            <div class="row-ckeditor columns editblock toc-noreplace">
			                <div id="s_${section_index}_r_${row_index}_c_${content_index}" class="ckeditorBorder" style="cursor: pointer" >${content.html}</div>
			                <script type="text/javascript">
			                  $('#s_${section_index}_r_${row_index}_c_${content_index}').ckeip({
			                    e_url: '${This.path}/s/${section_index}/r/${row_index}/c/${content_index}',
			                    ckeditor_config: ckeditorconfig,
			                    emptyedit_message: "<div style='font-weight: bold;font-size: 18px;padding: 5px;text-decoration: underline;cursor: pointer'>${Context.getMessage('label.ckeditor.double_click_to_edit_content')}</div>",
			                    view_style: "span${content.colNumber} columns cke_hidden "
			                    }, reloadPageForTocIfNeeded);
			                </script>
			                <noscript>
			                  <a  class="btn" href="${This.path}/s/${section_index}/r/${row_index}/c/${content_index}/@views/edit">Modifier</a>
			                </noscript>
			                &nbsp; <!-- Needed to give an empty cell a content -->
			            </div>
                      </#if>
			          </div>
	              </#list>

	          </div>
			  <div class=" editblock btn-group" style="float: right;">
			      	<a class="btn dropdown-toggle" data-toggle="dropdown"><i class="icon-cog"> </i>Ligne <span class="caret"></span></a>
					<ul class="dropdown-menu" style="left: auto;right: 0px;">
						<li>
						    <input type="hidden" class="section-index-value" value="${section_index}" />
                            <input type="hidden" class="row-index-value" value="${row_index}" />
                            <input type="hidden" class="content-index-value" value="${content_index}" />
							<a href="#" onClick="javascript:openModifiyCSSLine('${This.path}/s/${section_index}/r/${row_index}', '${row.cssClass}');" rel="modifyCSSLine" style="float: left;"><i class="icon-adjust"></i>Modifier la classe CSS</a>
							<a href="#" onclick="$('#rowdelete_s${section_index}_r${row_index}').submit();return false;"><i class="icon-remove"></i>Supprimer la ligne</a>
							<a href="#" class="open-dialog" rel="divConfigRowGadgets" ><i class="icon-gift"></i>${Context.getMessage('command.HtmlPage.row.widgets.config.button')}</a>
						</li>
					</ul>
			  </div>
			  <form id="rowdelete_s${section_index}_r${row_index}" style="margin: 0px 0px 0px" action="${This.path}/s/${section_index}/r/${row_index}/@delete" method="get" onsubmit="return confirm('Voulez vous vraiment supprimer la ligne ?');" >
			  </form>
			  <br />
	          <hr class="editblock"/>
	        <#else>
	           <div class="row-fluid<#if row.cssClass??> ${row.cssClass}</#if>" id="row_s${section_index}_r${row_index}">
	              <#list row.contents as content>
                      <#assign isWidgetCol = false />
                      <#assign isOsGadgetCol = false />
                      <#assign widgets = [] />
                      <@determineWidgetType content=content />
                      <#if isOsGadgetCol >
                        <div id="gadgetCol-s_${section_index}_r_${row_index}_c_${content_index}" class="span${content.colNumber} columns" >
                        <@openSocialGadgetJavascript selector="#gadgetCol-s_${section_index}_r_${row_index}_c_${content_index} > div.opensocialGadgets" widget=widgets[0] />
                        <div id="${widgets[0].doc.id}" class="opensocialGadgets gadget-${widgets[0].name} bloc">
                        </div>
                        </div>
                      <#elseif isWidgetCol >
                        <div class="span${content.colNumber} columns" >
                        <#if availableHtmlWidgets?seq_contains(widgets[0].name) >
                            <#include "widgets/${widgets[0].name}.ftl" />
                        </#if>
                        </div>
                      <#else>
    	                <div class="span${content.colNumber} columns">
                           <@displayContentHtml content=content />
    	                </div>
                      </#if>
	              </#list>
	           </div>
	        </#if>
        </#list>

    </section>

  </#list>

		<#if isContributor >
			<#if (page.sections?size == 0)>
				<div class="editblock">
					<br />
			    	<a id="addsectionlink" href="#" rel="addsection" class="btn btn-small open-dialog" ><i class="icon-plus"></i>Ajouter une section</a>
			    </div>
			</#if>
		    <#-- Add Section -->
		    <div id="addsection">
		    	<h1>Ajouter une section</h1>
		    	<form class="form-horizontal" id="addsectionfrm" action="${This.path}" method="post" submit="javascript:jQuery('#waitingPopup').dialog2('open');this.submit();return true;">
		      		<input type="hidden" name="action" value="addsection"/>
		      		<fieldset>
			            <div class="control-group">
			              <label class="control-label" for="title">Titre</label>
			              <div class="controls">
			                <input class="input-large" id="sectionTitle" name="title" size="30" type="text" />
			              </div>
			            </div>


			            <div class="control-group">
			              <label class="control-label" for="description">Sous-titre</label>
			              <div class="controls">
			                <input class="input-large" id="sectionDescription" name="description" size="30" type="text" />
			                <p class="help-block">
			                  Texte ajouté en petit à côté du titre
			                </p>
			              </div>
			            </div>

			            <div class="control-group">
			              	  <label class="control-label" for="index">Position</label>
				              <div class="controls">
				              	<select name="index">
				              		<#assign index=0 />
				              		<#list page.sections as section>
					              		<option class="large" id="sectionIndex" value="${index}">${index+1}</option>
				              			<#assign index=index+1/>
				              		</#list>
				              		<option class="large" id="sectionIndex" value="${page.sections?size}" selected="selected">Dernière</option>
				              	</select>
				              	<p class="help-block">
				                  Position de la section
				                </p>
				              </div>
			            </div>

			            <div class="actions">
			              <button type="submit" class="btn btn-small btn-primary">Ajouter</button>&nbsp;
			            </div>
	           		</fieldset>
	        	</form>
	    	</div>

	    	<div id="div-modifyCSSLine">
		    	<h1>Modifier la ligne</h1>
		    	<form class="form-horizontal" action="${This.path}" id="form-modifyCSSLine" method="post">
		      		<input type="hidden" name="section" value=""/>
		      		<input type="hidden" name="row" value=""/>
		      		<fieldset>
			            <div class="control-group">
			              <label class="control-label" for="cssName">Classe CSS</label>
			              <div class="controls">
			                <input class="input-large" id="cssName" name="cssName" type="text" />
			              </div>
			            </div>

			            <div class="actions">
			              <button type="submit" class="btn btn-small btn-primary">Modifier</button>&nbsp;
			            </div>
	           		</fieldset>
	        	</form>
	    	</div>
	    	<div id="divConfigRowGadgets" class="dialog2" >
	    	  <h1>${Context.getMessage('label.HtmlPage.row.widgets.config.title')}</h1>
              <input type="hidden" name="section" value="" class="span1" />
              <input type="hidden" name="row" value="" class="span1"/>
              <form id="config-row-gadgets-form" method="post" class="form form-horizontal" action="" >
                <fieldset>
                  <div id="divConfigRowGadgets-content" >
                    <img src="${skinPath}/images/loading.gif" />
                  </div>
                </fieldset>
                <div class="actions">
                    <button id="config-row-gadgets-form-btn" class="btn btn-primary" form-id="config-row-gadgets-form" title="${Context.getMessage('help.HtmlPage.row.widgets.config.save')}" >${Context.getMessage('command.HtmlPage.row.widgets.config.save')}
                    </button>
                    <a class="btn close-dialog" href="#" title="${Context.getMessage('help.HtmlPage.row.widgets.config.cancel')}" >${Context.getMessage('command.HtmlPage.row.widgets.config.cancel')}</a>
                </div>
              </form>
	    	</div>
            <div id="divConfigGadget" class="dialog2" >
              <h1>${Context.getMessage('label.HtmlPage.widget.config.title')}</h1>
              <input type="hidden" name="section" value="" class="span1" />
              <input type="hidden" name="row" value="" class="span1"/>
              <input type="hidden" name="content" value="" class="span1"/>
              <input type="hidden" name="widget-type" value="" class="span1"/>
              <input type="hidden" name="widget-name" value="" class="span1"/>
              <form id="config-gadget-form" method="post" class="form-horizontal" action="" >
                <fieldset>
                  <div id="divConfigGadget-content" >
                    <img src="${skinPath}/images/loading.gif" />
                  </div>
                </fieldset>
                <div class="actions">
                    <button id="config-gadget-form-btn" class="btn btn-primary" form-id="config-gadget-form" title="${Context.getMessage('help.HtmlPage.widget.config.save')}" >${Context.getMessage('command.HtmlPage.widget.config.save')}
                    </button>
                    <a class="btn close-dialog" href="#" title="${Context.getMessage('help.HtmlPage.widget.config.cancel')}" >${Context.getMessage('command.HtmlPage.widget.config.cancel')}</a>
                </div>
              </form>
            </div>
		</#if>

	</div>

  </@tableOfContents>
  <#--/@cache-->
<#macro openSocialGadgetJavascript selector widget >
    <#assign nbrOsGadgets = nbrOsGadgets + 1 />
<script type="text/javascript">
jQuery(document).ready(function() {
    var userPrefs = {};
    <#assign userPrefs = widget.userPrefs />
    <#list userPrefs as userPref >
    userPrefs["${userPref.name}"] = {name: '${userPref.name}', value:'${userPref.actualValue}', default: '${userPref.defaultValue}'};
    </#list>
    jQuery('${selector}').openSocialGadget({
    <#--
        baseURL: '${Context.baseURL}${contextPath}' + '/',
    -->
        baseURL: '${contextPath}' + '/',
        language: '${Context.locale.language}',
        gadgetDefs: [
            {
            <#--
            specUrl: '${Context.baseURL}${contextPath}/site/gadgets/${widget.name}/${widget.name}.xml',
            -->
            specUrl: 'http://localhost:8080/nuxeo/site/gadgets/${widget.name}/${widget.name}.xml',
            <#--
            -->
            <#if 0 < userPrefs?size >
            userPrefs: userPrefs,
            </#if>
            displayTitleBar: false,
            width: '90%',
            <#--
            displayButtons: true,
            displaySettingsButton: true,
            displayToggleButton: false,
            -->
<#-- TODO
            <#if isContributor >
            permission: '[SpaceContributeur]',
            </#if>
-->
            title: '${widget.name}' }
        ]
    });
})
</script>
</#macro>

<#macro determineWidgetType content >
    <#if content.type == "widgetcontainer">
        <#assign widgets = content.getGadgets(Session) />
    </#if>
    <#if 0 < widgets?size >
        <#if widgets[0].type.type() == "opensocial" >
            <#assign isOsGadgetCol = true />
        <#else>
            <#assign isWidgetCol = true />
        </#if>
    </#if>
</#macro>

<#macro displayContentHtml content >
    <#if content.html == "" >
    &nbsp;
    <#else>
        ${content.html}
    </#if>
</#macro>

<#macro displayContentHtmlWidget widget >
    <#if availableHtmlWidgets?seq_contains(widget.name) >
        <#include "widgets/${widget.name}.ftl" />
    </#if>
</#macro>

  </@block>

  <@block name="bottom-page-js" >
    <@superBlock />
    <#if 0 < nbrOsGadgets >
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.fancybox-1.3.4.pack.js"></script>
    <script type="text/javascript" src="${contextPath}/opensocial/gadgets/js/rpc.js?c=1"></script>
    <script type="text/javascript" src="${contextPath}/js/?scripts=opensocial/cookies.js|opensocial/util.js|opensocial/gadgets.js|opensocial/cookiebaseduserprefstore.js|opensocial/jquery.opensocial.gadget.js"></script>
    </#if>
    <#include "views/HtmlPage/bottom-js.ftl" />
  </@block>
</@extends>