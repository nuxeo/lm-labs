<#assign mySite=Common.siteDoc(Document).site />
<@extends src="/views/TemplatesBase/" + mySite.template.templateName + "/template.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${mySite.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.ui.datepicker-fr.js"></script>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.18.datepicker.min.js"></script>
      <script type="text/javascript" src="${skinPath}/js/LabsNews.js"></script>
      <script type="text/javascript" src="${skinPath}/js/manageDisplayHtmlLine.js"></script>
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
  
  <#assign isContributor = This.previous.page?? && This.previous.page.isContributor(Context.principal.name) />
  <#if isContributor >
    <#assign layouts = This.columnLayoutsSelect />
  </#if>

  <@block name="content">
  	<#include "views/LabsNews/macroNews.ftl">
    <#if news??>
      <div class="container-fluid">
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
		  		<#if isContributor >
			        <div class="row-fluid" id="row_s${section_index}_r${row_index}">
			              <#list row.contents as content>
			              	  <div class="span${content.colNumber}">
				                    <div class="columns viewblock">
					                    <#if content.html == "">
					                        &nbsp;
					                    <#else>
					                      ${content.html}
					                    </#if>
				                    </div>
	                    
						              <div class="row-ckeditor columns editblock">
						                <div id="s_${section_index}_r_${row_index}_c_${content_index}" class="ckeditorBorder" style="cursor: pointer" >${content.html}</div>
						                <script type="text/javascript">
						                  $('#s_${section_index}_r_${row_index}_c_${content_index}').ckeip({
						                    e_url: '${This.path}/s/${section_index}/r/${row_index}/c/${content_index}',
						                    ckeditor_config: ckeditorconfig,
						                    emptyedit_message: "<div style='font-weight: bold;font-size: 18px;padding: 5px;text-decoration: underline;cursor: pointer'>${Context.getMessage('label.PageHtml.double_click_to_edit_content')}</div>",
						                    view_style: "span${content.colNumber} columns cke_hidden "
											}, reloadPageLabsNews);
											
											function reloadPageLabsNews(response, ckeObj, ckeip_html) {
												jQuery(ckeObj).closest('div.row-ckeditor').siblings('.viewblock').html(ckeip_html);
												scrollToRowAfterCkeip(response, ckeObj, ckeip_html);
											}
						                </script>
						                <noscript>
						                  <a  class="btn editblock" href="${This.path}/s/${section_index}/r/${row_index}/c/${content_index}/@views/edit">Modifier</a>
						                </noscript>
						                &nbsp; <!-- Needed to give an empty cell a content -->
						              </div>
						         </div>
			              </#list>
			         </div>
			         <div class=" editblock btn-group" style="float: right;">
					      	<a class="btn dropdown-toggle" data-toggle="dropdown"><i class="icon-cog"></i>Ligne</a>
					      	<button class="btn dropdown-toggle" data-toggle="dropdown">
							    <span class="caret"></span>
							</button>
							<ul class="dropdown-menu">
								<li>
									<a href="#" onclick="$('#rowdelete_s${section_index}_r${row_index}').submit();return false;"><i class="icon-remove"></i>Supprimer la ligne</a>
								</li>
							</ul>
					  </div>
					  <form id="rowdelete_s${section_index}_r${row_index}" style="margin: 0px 0px 0px;" action="${This.path}/s/${section_index}/r/${row_index}/@delete" method="get" onsubmit="return confirm('Voulez vous vraiment supprimer la ligne ?');" >
					  </form>
					  <br />
			          <hr class="editblock"/>
			    <#else>
			    	<div class="row-fluid" id="row_s${section_index}_r${row_index}">
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
	          <div class="editblock">
			    <a href="#" class="btn btn-primary btn-small" style="margin-bottom: 5px" id="actionAddLineOnSection_${section_index}" onClick="javascript:actionAddLine('${section_index}');" ><i class="icon-plus"></i>Ajouter une ligne</a>
		        <div id="divAddRow_${section_index}" class="well" style="padding: 5px;display: none;">
		            <div style="float: right;">
		          		<a href="#" onClick="javascript:actionAddLine('${section_index}');" ><i class="icon-remove"></i></a>
		            </div>
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
	          </div>
	        </#if>	
			
		  </section>
      </div>
    </#if>
  </@block>
</@extends>