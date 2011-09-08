<@extends src="/views/labs-base.ftl">
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
     
	<@block name="scripts">
	  <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.filedrop.js"></script>
        <script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
        <script type="text/javascript" src="${skinPath}/js/PageClasseur.js"></script>
	</@block>
	
	<@block name="css">
	  <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageClasseur.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
        <link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
	</@block>

	<@block name="content">	
	<div class="container">

	<#include "views/common/description_area.ftl">	

	<#assign area_height=2 />
    <#include "views/common/comment_area.ftl">

    <input type="hidden" id="folderPath" value="" />

    <div class="classeur" id="${This.path}">

	<#assign canWrite = Session.hasPermission(Document.ref, 'Write')>
	<#assign children = Session.getChildren(Document.ref, "Folder")>
	
	<#if classeur.folders?size &gt; 0>
	<#list classeur.folders as folder>
		<section class="${folder.document.type}" id="${folder.document.id}" >
			<div class="page-header">
				<h1>${folder.document.dublincore.title}</h1>
			</div>

			
				<#if canWrite>
				<div class="row">
				  
				  <div class="span16 columns">
				    <div class="well">
				    <form action="${This.path}/${folder.document.name}" method="post" enctype="multipart/form-data">
				    	<fieldset>
				    		<legend>${Context.getMessage('command.PageClasseur.addFile')}</legend>
				    		
				    		<div class="clearfix">
			                <label for="title">Choisir le fichier</label>
			                  <div class="input">
			                    <input type="file" name="file"/>
			                  </div>
			            
			                </div><!-- /clearfix -->
			                
			                <div class="clearfix">
			                <label for="description">Description</label>
			                  <div class="input">
			                    <textarea name="description"></textarea>
			                  </div>
			            
			                </div><!-- /clearfix -->
			                
			                <div class="actions">
			                	<button type="submit" class="btn primary">Envoyer</button>&nbsp;
					            <!-- This button submits the hidden delete form -->
					            <button type="submit" class="btn danger" onclick="$('#delete_${folder.document.id}').submit();return false;">${Context.getMessage('command.PageClasseur.deleteFolder')}</button>
				    	</fieldset>
			           
				    </form>
				    
				    <form id="delete_${folder.document.id}" onsubmit="return confirm('Voulez vous vraiment supprimer ce répertoire ?');" action="${This.path}/${folder.document.name}/@delete" method="get" enctype="multipart/form-data">
				    </form>
  					</div>
				  </div>
				  
				</div>
				</#if>
		
			
			
			
		
			<@displayChildren folder=folder />
		</section> <!-- Folder -->
	</#list>
	</#if>

	<div id="classeurBottomActions">
		<#list This.getLinks("BOTTOM_ACTIONS") as link>
			<button class="btn" id="${link.id}">${Context.getMessage('command.' + Document.type + '.' + link.id)}</button> 
		</#list>
	</div>


	</div><!-- table -->

  

	<div id="div-addfile" style="display: none;" title="${Context.getMessage('label.PageClasseur.form.title')}" >
  <form enctype="multipart/form-data" id="form-addfile" action="" method="post">
    <fieldset>
      <p>
        <label for="fileId" id="label_fichier">${Context.getMessage('label.PageClasseur.form.filename')}</label>
      </p>
      <#-- TODO
      <p>
        <span><input type="radio" name="radioFile" value="desktop" checked="checked">${Context.getMessage('label.PageClasseur.form.radio.desktop')}</input></span>
      </p>
      -->
      <p>
        <span><input type="file" size="33" id="fileId" name="simplefile"/></span>
      </p>
      <#-- TODO
      <p>
        <span><input type="radio" name="radioFile" value="web">${Context.getMessage('label.PageClasseur.form.radio.web')}</input></span>
      </p>
      <p>
        <span><input type="text" size="35" id="fileUrl"/><input type="button" id="downloadFile" value="${Context.getMessage('command.PageClasseur.form.download')}"/></span>
      </p>
      <p>
        <span>${Context.getMessage('label.PageClasseur.form.radio.web.displaytext')} <input type="text" size="35" id="displayText"/></span>
      </p>
      -->
      <p>
        <label for="description" id="label_description">${Context.getMessage('label.PageClasseur.form.description')}</label>
      </p>
      <p>
        <textarea name="description" id="description" rows="4" cols="40" ></textarea>
      </p>
    </fieldset>
  </form>
</div>

<div id="div-addfolder" style="display: none;" title="${Context.getMessage('label.PageClasseur.form.folder.title')}" >

  <form id="form-addfolder" action="${This.path}/addFolder" method="post" onkeypress="return event.keyCode != 13;">
    <fieldset>
      <p>
        <label for="folderName" id="label_folderName">${Context.getMessage('label.PageClasseur.form.folder.name')}</label>
      </p>
      <p>
        <span><input type="text" size="35" name="folderName" id="folderName" /></span>
      </p>
    </fieldset>
  </form>
</div>

<#include "views/common/loading.ftl">

<#macro displayChildren folder recurse=false>
  
  <#if folder.files?size &gt; 0>
  <table class="zebra-striped classeurFiles" >
  <thead>
    <tr>
      <th>
      	<input type="checkbox" name="checkoptionsFolder" value="${folder.document.id}" title="${Context.getMessage('label.PageClasseur.folder.checkbox')}"/>
      </th>
      <th>&nbsp;</th>
      <th>Filename</th>
      <th>Size</th>
      <th>Version</th>
      <th>Modified</th>
      <th>Creator</th>
      <th>&nbsp;</th>
      </tr>
  </thead>
  <tbody>
  <#list folder.files as child>
  <tr class="main ${child.id}">
  	<td>
    <#if child.facets?seq_contains("Folderish") == false >
      <#if canWrite>
      	<input type="checkbox" name="checkoptions" value="${child.id}" />
      </#if>
    </#if>
    </td>
    <td>
    	<img title="${child.type}" alt="${child.type}/" src="/nuxeo${child.common.icon}" />
    </td>
	  <#if child.facets?seq_contains("Folderish") == false >
	    <#assign modifDate = child.dublincore.modified?datetime >
	    <#assign modifDateStr = modifDate?string("EEEE dd MMMM yyyy HH:mm") >
	    <#assign filename = This.getBlobHolder(child).blob.filename >
	    <td><span title="${child.dublincore.description}">${filename}</span></td>
	    <td>${bytesFormat(This.getBlobHolder(child).blob.length, "K", "fr_FR")}</span></td>
	    <td>${child.versionLabel}</span></td>
	    <td>${modifDateStr}</td>
	    <td>${userFullName(child.dublincore.creator)}</td>
	    <td>
			<#if canWrite>
	        	<button class="btn danger" onclick="$('#docdelete_${child.id}').submit()">${ Context.getMessage('command.PageClasseur.deleteFile')}</button>
			</#if>      
		    <a class="btn small" href="${This.path}/${folder.document.name}/${child.name}/@blob/preview" target="_blank">${Context.getMessage('command.PageClasseur.display')}</a>
	    	<a class="btn small" href="${This.path}/${folder.document.name}/${child.name}/@blob/">${Context.getMessage('command.PageClasseur.download')}</a>
		    </td>
	    
        	<form id="docdelete_${child.id}" action="${This.path}/${folder.document.name}/${child.name}/@delete" onsubmit="return confirm('Voulez vous vraiment supprimer le document ?')">
        	</form>
	    
	  </#if>
  </tr>
  </#list>
  </tbody>
  </table>
  </#if>
</#macro>
	</div>
	</@block>
</@extends>	