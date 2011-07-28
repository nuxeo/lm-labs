<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
    "http://www.w3.org/TR/html4/strict.dtd">
<html lang="fr">
    <head>
          <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
          <meta name="gwt:property" content="locale=fr">

        <title>
              ${Context.module.name} - ${This.document.type} ${This.document.title}
        </title>
     
          <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageClasseur.css"/>
    </head>
    <body>

<h1>${This.document.dublincore.description}</h1>

  <div id="table">
<@displayChildren ref=This.document.ref />
  </div>
  <#--
<div id="mainContentBox">
  Attachment: <a href="${This.path}/@file?property=file:content">${file.filename}</a>
</div>
  -->

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
    <span class="colIcon"><img title="${child.type}" alt="${child.type}" src="/nuxeo/${child.common.icon}" /></span>
	  <#if child.facets?seq_contains("Folderish") == false >
	    <#assign modifDate = child.dublincore.modified?datetime >
	    <#assign file = child["file:content"]/>
	    <span class="colFileName"><#if file.filename>${file.filename}<#else>${child.dublincore.title}</#if></span>
	    <#--
	    -->
	    <#if file.filename>
	      <span class="colDescription">${child.dublincore.description}</span>
	      <span class="colFilesize">${(file.length/1024)?ceiling} Ko</span>
	    <#else>
	      <span class="colDescription">${child.dublincore.description}</span>
	      <span class="colFilesize">000</span>
	    </#if>
	    <span class="colVersion">${child.versionLabel}</span>
	    <span class="colModified">${modifDate?string("EEEE dd MMMM yyyy HH:mm")}</span>
	    <span class="colCreator">${child.dublincore.creator}</span>
	    
        </div></p>
	  <#else>
        <span class="colFolderTitle">${child.dublincore.title}</span>
        </div></p>
        <@displayChildren ref=child.ref recurse=true/>
	  </#if>
  </#list>
  </#if>
</#macro>

    </body>
</html>
