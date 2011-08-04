<@extends src="/views/labs-base.ftl">
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
     
	<@block name="scripts">
	  <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
        <script type="text/javascript" src="${skinPath}/js/PageClasseur.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script> 
        <script type="text/javascript" src="${skinPath}/js/ckeditor/ckeditor.js"></script>
        <script type="text/javascript" src="${skinPath}/js/ckeditor/init.js"></script>
        <#--
        <script type="text/javascript" src="${skinPath}/js/jquery/ui.core.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/ui.dialog.js"></script>
        -->
	</@block>
	
	<@block name="css">
	  <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageClasseur.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
        <link rel="stylesheet" type="text/css" href="${skinPath}/css/ckeditor.css"/>
	</@block>

	<@block name="content">	
<h1>${This.document.dublincore.description}</h1>

<#assign area_height=2 />
<#include "views/common/comment_area.ftl">

<div class="classeur" id="${This.path}">

<#assign canWrite = Session.hasPermission(Document.ref, 'Write')>
<#assign children = Session.getChildren(Document.ref, "Folder")>
<#if children?size &gt; 0>
<#list children as child>
<div class="${child.type} DropZone" id="${child.id}">
<p>
<div class="row ${child.type}">
<span class="colIcon"><img title="${child.type}" alt="${child.type}/" src="/nuxeo/${child.common.icon}" /></span>
<span class="colFolderTitle">${child.dublincore.title}</span>
<#if canWrite>
<span class="colDelete">
	<a class="addfile" href="${This.path}/${child.id}" >${Context.getMessage("command.PageClasseur.addFile")}</a>
	<form class="form-removefolder" action="${This.path}/${child.id}" ><img class="removefolder removebutton" title="${Context.getMessage('command.PageClasseur.deleteFolder')} ${filename}" alt="${Context.getMessage('command.PageClasseur.deleteFolder')}" src="${This.module.skinPathPrefix}/images/x.gif" /></form>
</span>
</#if>
</div> <!-- row -->
</p>
<@displayChildren doc=child />
</div> <!-- Folder -->
</#list>
</#if>

<#if canWrite>
<button id="addFolder">${Context.getMessage('command.PageClasseur.addFolder')}</button> 
<button id="deleteSelection">${Context.getMessage('command.PageClasseur.deleteSelection')}</button> 
</#if>

</div><!-- table -->

  <#--
<div id="mainContentBox">
  Attachment: <a href="${This.path}/@file?property=file:content">${file.filename}</a>
</div>
  -->

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
        <span><input type="file" size="35" id="fileId" name="simplefile"/></span>
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
  <form id="form-addfolder" action="${This.path}/addFolder" method="post">
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

<#macro displayChildren doc recurse=false>
  <#assign children = This.getChildren(doc)>
  <#if children?size &gt; 0>
  <#list children as child>
  <p><div class="row ${child.type}" id="${child.id}">
    <#if child.facets?seq_contains("Folderish") == false >
      <span class="colCheckBox"><input type="checkbox" name="checkoptions" value="${child.id}"/></span>
    </#if>
    <span class="colIcon"><img title="${child.type}" alt="${child.type}/" src="/nuxeo${child.common.icon}" /></span>
	  <#if child.facets?seq_contains("Folderish") == false >
	    <#assign modifDate = child.dublincore.modified?datetime >
	    <#assign filename = This.getBlobHolder(child).blob.filename >
	    <span class="colFileName">${filename}</span>
	    <span class="colDescription">${child.dublincore.description}</span>
	    <span class="colFilesize">${(This.getBlobHolder(child).blob.length/1024)?ceiling} Ko</span>
	    <span class="colVersion">${child.versionLabel}</span>
	    <span class="colModified">${modifDate?string("EEEE dd MMMM yyyy HH:mm")}</span>
	    <span class="colCreator">${child.dublincore.creator}</span>
	    <#if canWrite>
	    <span class="colDelete"><form action="${This.path}/${child.id}" ><img class="removefile removebutton" title="${Context.getMessage('command.PageClasseur.deleteFile')} ${filename}" alt="${Context.getMessage('command.PageClasseur.deleteFile')}" src="${This.module.skinPathPrefix}/images/x.gif" /></form></span>
	    </#if>
	  </#if>
  </div><!-- row --></p>
  </#list>
  </#if>
</#macro>
	</@block>
</@extends>	