<@extends src="/views/labs-base.ftl">
	
	<@block name="title">${This.document.title}</@block>
     
	<@block name="scripts">
	  <@superBlock/>
	  
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.hotkeys.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.controls.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.dialog2.js"></script> 
        <script type="text/javascript" src="${skinPath}/js/assets/prettify/prettify.js"></script>
        <script type="text/javascript" src="${skinPath}/js/PageHtml.js"></script>
	</@block>
	
	<@block name="css">
	  <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/js/assets/prettify/prettify.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.dialog2.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageHtml.css"/>

	</@block>
	
	
	<@block name="docactions">
	    <@superBlock/>
		<li><a id="page_view" href="${This.path}">Voir la page</a></li>
	</@block>

	<@block name="content">	
	
	<script type="text/javascript">
	var ckeditorconfig = {
		filebrowserBrowseUrl : '${This.path}/displayBrowseTree',
		filebrowserImageBrowseUrl : '${This.path}/displayBrowseTree',
		filebrowserFlashBrowseUrl : '${This.path}/displayBrowseTree',
		filebrowserUploadUrl : '${This.path}/displayBrowseTree',
		filebrowserImageUploadUrl : '${This.path}/displayBrowseTree',
		filebrowserFlashUploadUrl : '${This.path}/displayBrowseTree',
		toolbar:
		[
		['Source','-','Preview','-'],
		['Cut','Copy','Paste','PasteText','PasteFromWord'],
		
		['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
		'/',
		[ 'Bold','Italic','Strike','-','SelectAll','RemoveFormat'],
		[ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar' ] ,'/',
		['Format','Font','FontSize'],
		['TextColor','BGColor'],
		['Maximize', 'ShowBlocks','-']
		]
		};
	</script>

  
       <div class="container">

<#include "views/common/description_area.ftl">	
	
	
	<#assign area_height=2 />
    <#include "views/common/comment_area.ftl">
	    
<div style="clear:both;"></div>    
    
		
		<#list page.sections as section>
		<section id="section_${section_index}">
  		  <div class="page-header">
            <h1>${section.title} <small>${section.description}</small></h1>
  		  </div>
  	
  	
  		  
  		  <div class="well editblock">
  		    <a href="#" dialog="#editsection_${section_index}" class="btn open-dialog" >Modifier la section</a>
  		    <!-- This button submits the hidden delete form -->
	        <button type="submit" class="btn danger" onclick="if(confirm('Voulez vous vraiment supprimer cette section ?')) { $('#frm_section_${section_index}_delete').submit();} ;return false;">Supprimer la section</button>
	        
	        
  		    <div class="dialog2" id="editsection_${section_index}" >
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
	          </legend>
	         </fieldset>
	       </form>
	       </div>
	       
		   <!-- Hidden form to handle delete action -->	       
	       <form action="${This.path}/s/${section_index}/@delete" method="get" id="frm_section_${section_index}_delete">
	       </form>

  		  
	            
          </div>
            
  		  
  		  <#list section.rows as row>
  		    <div class="row" id="row_s${section_index}_r${row_index}">
              <#list row.contents as content>
              <div class="span${content.colNumber} columns">
                <div id="s_${section_index}_r_${row_index}_c_${content_index}">${content.html}</div>
                <script type="text/javascript">
                	$('#s_${section_index}_r_${row_index}_c_${content_index}').ckeip({
                		e_url: '${This.path}/s/${section_index}/r/${row_index}/c/${content_index}',
                		ckeditor_config: ckeditorconfig
                		});
                </script>
                <noscript>
                	<a  class="btn editblock" href="${This.path}/s/${section_index}/r/${row_index}/c/${content_index}/@views/edit">Modifier</a>
                </noscript>
                &nbsp; <!-- Needed to give an empty cell a content -->
              </div>
              </#list>
              
               <div style="margin-left:20px" class="editblock">
	        		<form id="rowdelete_s${section_index}_r${row_index}" action="${This.path}/s/${section_index}/r/${row_index}/@delete" method="get" onsubmit="return confirm('Voulez vous vraiment supprimer la ligne ?');">
	                  <button type="submit" class="btn small danger">Supprimer la ligne</button>
	                </form>
	        	</div>
            </div>
            
            

          </#list>
          
          
          <div class="well editblock">
          <form id="addrow_${section_index}" action="${This.path}/s/${section_index}" method="post" >
          <input type="hidden" name="action" value="addrow"/>
              <fieldset>
                <legend>Ajouter une ligne</legend>
                <div class="clearfix">
                <label for="title">Type de ligne</label>
                  <div class="input">
                    <select name="rowTemplate">
                  		<option value="1COL">1 colonne</option>
		                <option value="2COL_2575">2 colonnes (25/75)</option>
		                <option value="2COL_7525">2 colonnes (75/25)</option>
		                <option value="3COL">3 colonnes (33/33/33)</option>
		                <option value="4COL">4 colonnes (25/25/25/25)</option>
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
          
		</section>
		
		</#list>
		
		
		
		
		
		<#-- Add Section -->
		<a id="addsectionlink" href="#" dialog="#addsection" class="btn open-dialog editblock" >Ajouter une section</a>
		
		<div id="addsection" class="dialog2">
		<h1>Ajouter une section</h1>
		<form  id="addsectionfrm" action="${This.path}" method="post">
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
	          <div class="actions">
	            <button type="submit" class="btn small primary">Ajouter</button>&nbsp;
	          </div>
	          </legend>
	         </fieldset>
         </form>
         </div>
      </div>
      
      <script type="text/javascript">
      	$(document).bind('keyup', 'e', function() {
      		$(".editblock").toggle();
      	});
      </script>
	</@block>
</@extends>	