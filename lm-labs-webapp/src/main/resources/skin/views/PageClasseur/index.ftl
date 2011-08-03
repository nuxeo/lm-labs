<@extends src="/views/labs-base.ftl">
	
	<@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
     
	<@block name="scripts">
	  <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
        <script type="text/javascript" src="${skinPath}/js/PageClasseur.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.form.js"></script> 
        <#--
        <script type="text/javascript" src="${skinPath}/js/jquery/ui.core.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/ui.dialog.js"></script>
        -->
	</@block>
	
	<@block name="css">
	  <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageClasseur.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.css"/>
	</@block>

	<@block name="content">	
<h1>${This.document.dublincore.description}</h1>
<#include "views/common/comment_area.ftl">
<div id="table">
<@displayChildren ref=This.document.ref />
</div>
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

<#macro displayChildren ref recurse=false>
  <#if recurse>
    <#assign children = Session.getChildren(ref)>
  <#else>
    <#assign children = Session.getChildren(ref, "Folder")>
  </#if>
  <#if children?size &gt; 0>
  <#list children as child>
  <p><div class="row ${child.type}" id="${child.id}">
    <#if child.facets?seq_contains("Folderish") == false >
      <span class="colCheckBox"><input type="checkbox" name="checkoptions" value="${child.id}"/></span>
    </#if>
    <span class="colIcon"><img title="${child.type}" alt="${child.type}/" src="/nuxeo/${child.common.icon}" /></span>
	  <#if child.facets?seq_contains("Folderish") == false >
	    <#assign modifDate = child.dublincore.modified?datetime >
	    <#assign filename = This.getBlobHolder(child).blob.filename >
	    <span class="colFileName">${filename}</span>
	    <span class="colDescription">${child.dublincore.description}</span>
	    <span class="colFilesize">${(This.getBlobHolder(child).blob.length/1024)?ceiling} Ko</span>
	    <span class="colVersion">${child.versionLabel}</span>
	    <span class="colModified">${modifDate?string("EEEE dd MMMM yyyy HH:mm")}</span>
	    <span class="colCreator">${child.dublincore.creator}</span>
  </div></p>
	  <#else>
        <span class="colFolderTitle">${child.dublincore.title} <a class="addfile" href="${This.path}/${child.id}" >${Context.getMessage("command.PageClasseur.addFile")}</a></span>
  </div></p>
        <@displayChildren ref=child.ref recurse=true/>
	  </#if>
  </#list>
  </#if>
</#macro>

	</@block>
</@extends>	