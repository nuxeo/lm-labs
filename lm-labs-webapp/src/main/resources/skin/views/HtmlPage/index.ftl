<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">

  <@block name="title">${site.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.hotkeys.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
        <script type="text/javascript" src="${skinPath}/js/assets/prettify/prettify.js"></script>
        <script type="text/javascript" src="${skinPath}/js/PageHtml.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/js/assets/prettify/prettify.css"/>
  </@block>

  <@block name="content">

   	<div class="container">
   
   	<#include "views/common/page_header.ftl">
  	
  <#assign isContributor = site?? && site.isContributor(Context.principal.name) />

  <#list page.sections as section>
    <section id="section_${section_index}">
        <div class="page-header">
            <h1>${section.title} <small>${section.description}</small></h1>
        </div>

		<#if isContributor >
	        <div class="well editblock">
	          <a href="#" rel="editsection_${section_index}" class="btn small open-dialog" >Modifier la section</a>
	          <!-- This button submits the hidden delete form -->
	          <button type="submit" class="btn small danger" onclick="if(confirm('Voulez vous vraiment supprimer cette section ?')) { $('#frm_section_${section_index}_delete').submit();} ;return false;">Supprimer la section</button>
	
	          <div id="editsection_${section_index}" >
	          	  <h1>Modifier la section</h1>
			      <form action="${This.path}/s/${section_index}" method="post">
				      <input type="hidden" name="action" value="editsection"/>
				      <fieldset>
			            <legend>Modifier la section</legend>
			            <div class="clearfix">
			              <label for="title">Titre</label>
			              <div class="input">
			                <input class="large" id="sectionTitle" name="title" size="30" type="text" value="${section.title}"/>
			              </div>
			            </div><!-- /clearfix -->
			
			            <div class="clearfix">
			              <label for="description">Sous-titre</label>
			              <div class="input">
			                <input class="large" id="sectionDescription" name="description" size="30" type="text" value="${section.description}"/>
			                <span class="help-block">
			                  Texte ajouté en petit à côté du titre
			                </span>
			              </div>
			            </div><!-- /clearfix -->
			            <div class="actions">
			              <button type="submit" class="btn primary">Modifier</button>&nbsp;
			            </div>
				      </fieldset>
			      </form>
	          </div>
	       	  <!-- Hidden form to handle delete action -->
	          <form action="${This.path}/s/${section_index}/@delete" method="get" id="frm_section_${section_index}_delete">
	          </form>
	
	        </div>
		</#if>

        <#list section.rows as row>
        	<#if isContributor >
	          <div class="row<#if row.cssClass??> ${row.cssClass}</#if>" id="row_s${section_index}_r${row_index}">
	              <#list row.contents as content>
		              <div class="span${content.colNumber} columns editblock">
		                <div id="s_${section_index}_r_${row_index}_c_${content_index}">${content.html}</div>
		                <script type="text/javascript">
		                  $('#s_${section_index}_r_${row_index}_c_${content_index}').ckeip({
		                    e_url: '${This.path}/s/${section_index}/r/${row_index}/c/${content_index}',
		                    ckeditor_config: ckeditorconfig,
		                    emptyedit_message: "${Context.getMessage('label.ckeditor.double_click_to_edit_content')}",
		                    view_style: "span${content.colNumber} columns "
		                    });
		                </script>
		                <noscript>
		                  <a  class="btn" href="${This.path}/s/${section_index}/r/${row_index}/c/${content_index}/@views/edit">Modifier</a>
		                </noscript>
		                &nbsp; <!-- Needed to give an empty cell a content -->
		              </div>
	              </#list>
	
	               <div style="margin-left:20px;clear:both;" class="editblock">
	               	  <a href="#" class="btn small" onClick="javascript:openModifiyCSSLine('${This.path}/s/${section_index}/r/${row_index}', '${row.cssClass}');" rel="modifyCSSLine" style="float: left;">Modifier la classe CSS</a>
		              <form id="rowdelete_s${section_index}_r${row_index}" action="${This.path}/s/${section_index}/r/${row_index}/@delete" method="get" onsubmit="return confirm('Voulez vous vraiment supprimer la ligne ?');">
		                    <button type="submit" class="btn small danger">Supprimer la ligne</button>
		              </form>
	               </div>
	          </div>
	        <#else>
	           <div class="row<#if row.cssClass??> ${row.cssClass}</#if>" id="row_s${section_index}_r${row_index}">
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
	          <form id="addrow_${section_index}" action="${This.path}/s/${section_index}" method="post" >
	          	  <input type="hidden" name="action" value="addrow"/>
	              <fieldset>
	                <legend>Ajouter une ligne</legend>
	                <div class="clearfix">
	                  <label for="title">Type de ligne</label>
	                  <div class="input">
	                    <select name="rowTemplate">
	                    <#assign layouts = This.columnLayoutsSelect />
	                    <#list layouts?keys as layoutCode >
	                      <option value="${layoutCode}">${Context.getMessage(layouts[layoutCode])}</option>
	                    </#list>
		                </select>
		                <div id="displayCssClass_${section_index}" style="display: none;float: right;">
		                	Classe CSS : <input class="medium" name="cssClass" />
		                </div>
		                <div id="herfDisplayCssClass_${section_index}" style="float: right;cursor: pointer;" onClick="javascript:displayCssClass('${section_index}');">
		                	<br>Ajouter un style à la ligne
		                </div>
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
	        <script type="text/javascript">
	        	function displayCssClass(section){
	        		jQuery("#displayCssClass_" + section).show();
	        		jQuery("#herfDisplayCssClass_" + section).hide();
	        	}
	        </script>
	    </#if>
    </section>

  </#list>

		<#if isContributor >
		    <#-- Add Section -->
		    <a id="addsectionlink" href="#" rel="addsection" class="btn small open-dialog editblock" >Ajouter une section</a>
	
		    <div id="addsection">
		    	<h1>Ajouter une section</h1>
		    	<form  id="addsectionfrm" action="${This.path}" method="post">
		      		<input type="hidden" name="action" value="addsection"/>
		      		<fieldset>
			            <div class="clearfix">
			              <label for="title">Titre</label>
			              <div class="input">
			                <input class="large" id="sectionTitle" name="title" size="30" type="text" />
			              </div>
			            </div><!-- /clearfix -->
			
			
			            <div class="clearfix">
			              <label for="description">Sous-titre</label>
			              <div class="input">
			                <input class="large" id="sectionDescription" name="description" size="30" type="text" />
			                <span class="help-block">
			                  Texte ajouté en petit à côté du titre
			                </span>
			              </div>
			            </div><!-- /clearfix -->
			            
			            <div class="clearfix">
			              	  <label for="index">Position</label>
				              <div class="input">
				              	<select name="index">
				              		<#assign index=0 />
				              		<#list page.sections as section>
					              		<option class="large" id="sectionIndex" value="${index}">${index+1}</option>
				              			<#assign index=index+1/>
				              		</#list>
				              		<option class="large" id="sectionIndex" value="${page.sections?size}" selected="selected">Dernière</option>
				              	</select>
				              	<span class="help-block">
				                  Position de la section
				                </span>
				              </div>
			            </div><!-- /clearfix -->
			              
			            <div class="actions">
			              <button type="submit" class="btn small primary">Ajouter</button>&nbsp;
			            </div>
	           		</fieldset>
	        	</form>
	    	</div>
	    	
	    	<div id="div-modifyCSSLine">
		    	<h1>Modifier la ligne</h1>
		    	<form action="${This.path}" id="form-modifyCSSLine" method="post">
		      		<input type="hidden" name="section" value=""/>
		      		<input type="hidden" name="row" value=""/>
		      		<fieldset>
			            <div class="clearfix">
			              <label for="cssName">Classe CSS</label>
			              <div class="input">
			                <input class="large" id="cssName" name="cssName" type="text" />
			              </div>
			            </div><!-- /clearfix -->
			              
			            <div class="actions">
			              <button type="submit" class="btn small primary">Modifier</button>&nbsp;
			            </div>
	           		</fieldset>
	        	</form>
	    	</div>
		</#if>
		
	</div>
 
  </@block>
</@extends>