<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
  <#assign mySite=Common.siteDoc(Document).site />
  <@block name="title">${mySite.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.hotkeys.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
        <script type="text/javascript" src="${skinPath}/js/assets/prettify/prettify.js"></script>
        <script type="text/javascript" src="${skinPath}/js/PageHtml.js"></script>
        <script type="text/javascript" src="${skinPath}/js/manageDisplayHtmlLine.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/js/assets/prettify/prettify.css"/>
  </@block>

  <@block name="content">
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
		        <div class=" editblock btn-group" style="float: right;margin-top: 7px;">
			      	<a class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><i class="icon-cog"></i>Section</a>
			      	<button class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
					    <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<a id="addsectionlink" href="#" rel="addsection" class="open-dialog" ><i class="icon-plus"></i>Ajouter une section</a>
							<a href="#" rel="editsection_${section_index}" class="open-dialog" ><i class="icon-edit"></i>Modifier la section</a>
							<a href="#" id="actionAddLineOnSection_${section_index}" onClick="javascript:actionAddLine('${section_index}');" ><i class="icon-eye-open"></i>Ajouter une ligne</a>
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
		          	<a href="#" onClick="javascript:actionAddLine('${section_index}');" ><i class="icon-remove"></i></a>
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
	        <script type="text/javascript">
	        	function displayCssClass(section){
	        		jQuery("#displayCssClass_" + section).show();
	        		jQuery("#herfDisplayCssClass_" + section).hide();
	        	}
	        </script>
	        
          <div id="editsection_${section_index}" >
          	  <h1>Modifier la section</h1>
		      <form class="form-horizontal" action="${This.path}/s/${section_index}" method="post">
			      <input type="hidden" name="action" value="editsection"/>
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
		            <button type="submit" class="btn btn-primary">Modifier</button>&nbsp;
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
	                    <div class="columns viewblock">
		                    <#if content.html == "">
		                        &nbsp;
		                    <#else>
		                      ${content.html}
		                    </#if>
	                    </div>
			            <div class="row-ckeditor columns editblock toc-noreplace ">
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
							<a href="#" onClick="javascript:openModifiyCSSLine('${This.path}/s/${section_index}/r/${row_index}', '${row.cssClass}');" rel="modifyCSSLine" style="float: left;"><i class="icon-adjust"></i>Modifier la classe CSS</a>
							<a href="#" onclick="$('#rowdelete_s${section_index}_r${row_index}').submit();return false;"><i class="icon-remove"></i>Supprimer la ligne</a>
						</li>
					</ul>
			  </div>
			  <form id="rowdelete_s${section_index}_r${row_index}" action="${This.path}/s/${section_index}/r/${row_index}/@delete" method="get" onsubmit="return confirm('Voulez vous vraiment supprimer la ligne ?');" >
			  </form>
			  <br />
	          <hr class="editblock"/>
	        <#else>
	           <div class="row-fluid<#if row.cssClass??> ${row.cssClass}</#if>" id="row_s${section_index}_r${row_index}">
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
		    	<form class="form-horizontal" id="addsectionfrm" action="${This.path}" method="post">
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
		</#if>
		
	</div>

  </@tableOfContents>
  </@block>
</@extends>