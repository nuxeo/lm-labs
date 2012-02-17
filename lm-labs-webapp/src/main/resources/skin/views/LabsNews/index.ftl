<@extends src="/views/TemplatesBase/" + site.template.templateName + "/template.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${site.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
      <script type="text/javascript" src="${skinPath}/js/LabsNews.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
  </@block>

  <@block name="docactionsaddpage"></@block>
  <@block name="docactionsonpage"></@block>
  
  <#assign isContributor = site?? && site.isContributor(Context.principal.name) />
  <#if isContributor >
    <#assign layouts = This.columnLayoutsSelect />
  </#if>

  <@block name="content">
    <#if news??>
      <div id="content" class="container">
          <section>
          	  <#if news != null>
	              <div class="page-header">
	                <h1>${news.title} <small>${Context.getMessage('label.labsNews.display.by')} ${news.lastContributorFullName}</small></h1>
	                <small>${Context.getMessage('label.labsNews.display.publish')} ${news.startPublication.time?string('dd MMMMM yyyy')}</small>
	              </div>
	          <#else>
	              <div class="page-header">
	                <h1>${Context.getMessage('label.labsNews.edit.title')} <small>${Context.getMessage('label.labsNews.display.by')}...</small></h1>
	                <small>${Context.getMessage('label.labsNews.display.publish')}...</small>
	              </div>
			  </#if>
			  <#if isContributor >
			  	<#assign news=news/>
			  	<#include "views/LabsNews/editProps.ftl" />
			  </#if>
				
		  	<#assign section_index=0/>
		  	<#list news.rows as row>
		  		<#if isContributor >
		          <div class="row" id="row_s${section_index}_r${row_index}">
		              <#list row.contents as content>
			              <div class="span${content.colNumber} columns editblock">
			                <div id="s_${section_index}_r_${row_index}_c_${content_index}">${content.html}</div>
			                <script type="text/javascript">
			                  $('#s_${section_index}_r_${row_index}_c_${content_index}').ckeip({
			                    e_url: '${This.path}/s/${section_index}/r/${row_index}/c/${content_index}',
			                    ckeditor_config: ckeditorconfig,
			                    emptyedit_message: "${Context.getMessage('label.PageHtml.double_click_to_edit_content')}",
			                    view_style: "span${content.colNumber} columns "
			                    });
			                </script>
			                <noscript>
			                  <a  class="btn editblock" href="${This.path}/s/${section_index}/r/${row_index}/c/${content_index}/@views/edit">Modifier</a>
			                </noscript>
			                &nbsp; <!-- Needed to give an empty cell a content -->
			              </div>
		              </#list>
		              <div style="margin-left:20px;clear:both;" class="editblock">
			              <form id="rowdelete_s${section_index}_r${row_index}" action="${This.path}/s/${section_index}/r/${row_index}/@delete" method="get" onsubmit="return confirm('Voulez vous vraiment supprimer la ligne ?');">
			              	<button type="submit" class="btn small danger">Supprimer la ligne</button>
			              </form>
		              </div>
		          </div>
		        <#else>
		        	<div class="row" id="row_s${section_index}_r${row_index}">
	                  <#list row.contents as content>
	                    <div class="span${content.colNumber} columns">
	                      <#if content.html == "">
	                        &nbsp;
	                      <#else>
	                        ${content.html}
	                      </#if>
	                    </div>
	                  </#list>
	                </div>
		        </#if>
		    </#list>
			
			<#if isContributor >
	          <div class="well editblock">
	            <form id="addrow" action="${This.path}/s/0" method="post" >
	              <input type="hidden" name="action" value="addrow"/>
	              <fieldset>
	                <legend>Ajouter une ligne</legend>
	                <div class="clearfix">
	                  <label for="title">Type de ligne</label>
	                  <div class="input">
	                    <select name="rowTemplate">
	                    <#list layouts?keys as layoutCode >
	                      <option value="${layoutCode}">${Context.getMessage(layouts[layoutCode])}</option>
	                    </#list>
		                </select>
		                <button type="submit" class="btn small primary">Ajouter</button>
		                <span class="help-block">
		                    Sélectionnez le type de ligne à ajouter. Plusieurs modèles sont disponibles, les chiffres entre
		                    parenthèses représentent des pourcentages de taille de colonne.
		                 </span>
	                  </div>
	
	                </div><!-- /clearfix -->
	              </fieldset>
	            </form>
	          </div>
	        </#if>

		  </section>
      </div>
    </#if>
  </@block>
</@extends>