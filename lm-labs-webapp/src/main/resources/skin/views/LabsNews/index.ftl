<#assign mySite=Common.siteDoc(Document).site />
<@extends src="/views/TemplatesBase/" + mySite.template.templateName + "/template.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${mySite.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.ui.datepicker-fr.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.18.datepicker.min.js"></script>
      <script type="text/javascript" src="${skinPath}/js/LabsNews.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jcrop/jquery.Jcrop.min.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
      <#include "views/common/datepicker_css.ftl">
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jcrop/jquery.Jcrop.css"/>
  </@block>

  <@block name="docactionsaddpage"></@block>
  <@block name="docactionsonpage"></@block>
  
  <#assign isContributor = mySite?? && mySite.isContributor(Context.principal.name) />
  <#if isContributor >
    <#assign layouts = This.columnLayoutsSelect />
  </#if>

  <@block name="content">
  	<#include "views/LabsNews/macroNews.ftl">
    <#if news??>
      <div id="content" class="">
          <section>
          	  <#if news != null>
	              <div class="page-header">
	                <h1>${news.title} <small>${Context.getMessage('label.labsNews.display.by')} ${news.lastContributorFullName}</small></h1>
	                <small>${Context.getMessage('label.labsNews.display.publish')} ${news.startPublication.time?string('dd MMMMM yyyy')}</small>
	              	<#if isContributor >
		              	<div class="editblock" style="margin-top: -15px;width: 100%;text-align: right;float: right">
							<a id="btnModifyPropsNews" class="btn" style="cursor: pointer;margin-right: 5px;" onclick="javascript:actionPropsNews();"><i class="icon-eye-open"></i>Modifier les propriétés</a>
					  	</div>
				  	</#if>
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
		          <div class="row-fluid" id="row_s${section_index}_r${row_index}">
		  		<#if isContributor >
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
			              	<button type="submit" class="btn btn-small btn-danger">Supprimer la ligne</button>
			              </form>
		              </div>
		        <#else>
	                  <#list row.contents as content>
	                    <div class="span${content.colNumber} columns">
	                      <#if content.html == "">
	                        &nbsp;
	                      <#else>
	                        ${content.html}
	                      </#if>
	                    </div>
	                  </#list>
		        </#if>
	                </div>
		    </#list>
			
			<#if isContributor >
	          <div class="well editblock">
	            <form class="form-horizontal" id="addrow" action="${This.path}/s/0" method="post" >
	              <input type="hidden" name="action" value="addrow"/>
	              <fieldset>
	                <legend>Ajouter une ligne</legend>
	                <div class="control-group">
	                  <label class="control-label" for="rowTemplate">Type de ligne</label>
	                  <div class="controls">
	                    <select name="rowTemplate">
	                    <#list layouts?keys as layoutCode >
	                      <option value="${layoutCode}">${Context.getMessage(layouts[layoutCode])}</option>
	                    </#list>
		                </select>
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
	        </#if>

		  </section>
      </div>
    </#if>
  </@block>
</@extends>