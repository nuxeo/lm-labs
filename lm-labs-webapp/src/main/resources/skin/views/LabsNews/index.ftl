<@extends src="/views/TemplatesBase/" + site.template.templateName + "/template.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${site.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
      <script type="text/javascript" src="${skinPath}/js/LabsNews.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jcrop/jquery.Jcrop.min.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jcrop/jquery.Jcrop.css"/>
  </@block>

  <@block name="docactionsaddpage"></@block>
  <@block name="docactionsonpage"></@block>
  
  <#assign isContributor = site?? && site.isContributor(Context.principal.name) />
  <#if isContributor >
    <#assign layouts = This.columnLayoutsSelect />
  </#if>

  <@block name="content">
    <#if news??>
      <div id="content" class="">
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
			  <section class="labsnews editblock">
			  	<div class="row" id="summaryNews${news.documentModel.ref}">
          			<#if news.hasSummaryPicture()>
	          			<div class="span15">
			          		<#-- Image -->
			          		<div class="span3">
			          			<@generateSummaryPictureNews news=news />
			          		</div>
			          		<#-- Central -->
			          		<div class="span12">
			          			<h2 style="line-height: 24px;">${news.title}</a></h2>
		          				<p class="labsNewsDate"><small>${Context.getMessage('label.labsNews.display.publish')} <#if news.startPublication != null >${news.startPublication.time?string('dd MMMMM yyyy')}</#if></small></p>
			          			<div class="ellipsisText" id="ellipsisTextNews" ellipsisTextOptions="{ max_rows:2, alt_text_e:true, alt_text_t:true }">
			          				<@generateContentHtmlNews news=news />
			          			</div>
			          		</div>
			          	</div>
		          	<#else>
		          		<#-- Central -->
		          		<div class="span15">
		          			<h2 style="line-height: 24px;">${news.title}</h2>
			          		<p class="labsNewsDate"><small>${Context.getMessage('label.labsNews.display.publish')} <#if news.startPublication != null >${news.startPublication.time?string('dd MMMMM yyyy')}</#if></small></p>
			          		<div class="ellipsisText" id="ellipsisTextNews" ellipsisTextOptions="{ max_rows:2, alt_text_e:true, alt_text_t:true }">
		          				<@generateContentHtmlNews news=news />
					        </div>
		          		</div>
		          	</#if>
	          		<#-- Collapse -->
	          		<div class="span1" style="margin-left: 15px;">
	          			<img src="${skinPath}/images/newsOpen.png" style="margin-top:5px;"/>
	          		</div>
	          	</div>
	          </section>
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

<#macro generateContentHtmlNews news>
	<#list news.rows as row>
	    <div class="row" id="row_s${news_index}_r${row_index}">
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
	  </#list>
</#macro>


<#macro generateSummaryPictureNews news>
	<img src="${This.path}/summaryPictureTruncated" style="margin-top: 5px;"/>
</#macro>