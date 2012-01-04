<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">

  <@block name="title">${site.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.filedrop.js"></script>
        <script type="text/javascript" src="${skinPath}/js/PageClasseur.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>

  </@block>

  <@block name="css">
    <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageClasseur.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tablesorter.css"/>
  </@block>
  
  <@block name="docactions">
		<@superBlock/>
		<#include "views/common/page_actions.ftl" />
	</@block>

  <@block name="content">

	<style>
	.arrowOpacity {
		opacity:0.4;
		cursor:default;
	}
	</style>

  <div class="container">
	<#if page.isDisplayable(This.DC_TITLE)>
    	<h1>${page.title}</h1>
    </#if>
    <#if page.isDisplayable(This.DC_DESCRIPTION)>
    	<#if Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(Document.ref, 'ReadWrite')>
    		<#include "views/common/description_area.ftl">
    	<#else>
    		${page.description}
    	</#if>
    </#if>


  <#assign area_height=2 />

    <input type="hidden" id="folderPath" value="" />

    <div class="classeur" id="${This.path}">

  <#assign canWrite = Session.hasPermission(Document.ref, 'Write')>
  <#assign children = Session.getChildren(Document.ref, "Folder")>

  <#if classeur.folders?size &gt; 0>
  <#assign i=0 />
  <#list classeur.folders as folder>
    <section class="${folder.document.type}" id="${folder.document.id}" >
      <div class="page-header">
        <h1><span id="spanFolderTitle${folder.document.id}" title="${folder.document.dublincore.description}" >${folder.document.dublincore.title}</span></h1>
	    
	    <#if Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(Document.ref, 'ReadWrite')> 
	    <div id="arrowOrder" class="editblock">
	        <#if i &gt; 0>
	        	<a href="" onclick="moveFolder('${This.path}', '${Document.ref}', '${folder.document.id}', $('#${folder.document.id}').prev('section').attr('id'));return false">
	        		<img src="/nuxeo/icons/move_up.png"/>
	        	</a>
	        <#else>
	        	<a href="" onclick="return false">
	        		<img src="/nuxeo/icons/move_up.png" class="arrowOpacity"/>
	        	</a>
	        </#if>
	        <#if (i+1) &lt; classeur.folders?size>
	        	<a href="" onclick="moveFolder('${This.path}', '${Document.ref}', $('#${folder.document.id}').next('section').attr('id'), '${folder.document.id}');return false">
	        		<img src="/nuxeo/icons/move_down.png"/>
	        	</a>
	        <#else>
	        	<a href="" onclick="return false">
	        		<img src="/nuxeo/icons/move_down.png" class="arrowOpacity"/>
	        	</a>
	        </#if>
	    </div>
	    </#if>
	    
      </div>

        <#if canWrite>
        <div class="row editblock">

          <div class="span16 columns">
            <a href="#" rel="addfile_${folder.document.id}_modal" class="btn open-dialog" >Ajouter un fichier</a>


          <!-- This button submits the hidden delete form -->
          <button type="submit" class="btn danger" onclick="$('#delete_${folder.document.id}').submit();return false;">${Context.getMessage('command.PageClasseur.deleteFolder')}</button>

		  <!-- rename   -->
		  <button class='btn' onClick='javascript:renameFolder("${This.path}/@rename/${folder.document.id}", "${folder.document.id}");'>${Context.getMessage("command." + Document.type + ".renameFolder" )}</button>

          <div id="addfile_${folder.document.id}_modal" >
              <h1>${Context.getMessage('command.PageClasseur.addFile')}</h1>
              <form id="form-upload-file-${folder.document.id}" action="${This.path}/${folder.document.name}" method="post" enctype="multipart/form-data">
                <fieldset>
                  <div class="clearfix">
                        <label for="title">Choisir le fichier</label>
                          <div class="input">
                            <input type="file" name="file" class="required"/>
                          </div>
                        </div><!-- /clearfix -->

                        <div class="clearfix">
                        <label for="description">Description</label>
                          <div class="input">
                            <textarea name="description"></textarea>
                          </div>
                        </div><!-- /clearfix -->
                  </fieldset>



              <div class="actions">
                <button class="btn primary required-fields" form-id="form-upload-file-${folder.document.id}">Envoyer</button>
              </div>
              </form>

            </div> <!-- /modal -->

              <form id="delete_${folder.document.id}" onsubmit="return confirm('Voulez vous vraiment supprimer ce répertoire ?');" action="${This.path}/${folder.document.name}/@delete" method="get" enctype="multipart/form-data">
              </form>

          </div>

        </div>
        </#if>

      <@displayChildren folder=folder />
    </section> <!-- Folder -->
    <#assign i=i+1 />
  </#list>
  </#if>

  <div id="classeurBottomActions" class="editblock">
    <#list This.getLinks("BOTTOM_ACTIONS") as link>
      <button class="btn" id="${link.id}">${Context.getMessage('command.' + Document.type + '.' + link.id)}</button>
    </#list>
  </div>


  </div><!-- table -->




<div id="div-addfolder" style="display: none;" >
    <h1>${Context.getMessage('label.PageClasseur.form.folder.title')}</h1>

  <form class="ajax" id="form-folder" action="${This.path}" method="post" enctype="multipart/form-data">
      <fieldset>
      <div class="clearfix">
          <label for="folderName">${Context.getMessage('label.PageClasseur.form.folder.name')}</label>
            <div class="input">
              <input id="folderName" name="folderName" class="xlarge required"/>
            </div>
          </div><!-- /clearfix -->
      </fieldset>



  <div class="actions">
    <button class="btn primary required-fields" form-id="form-folder">Envoyer</button>
  </div>
  </form>
  



</div>

<#include "views/common/loading.ftl">

<#macro displayChildren folder recurse=false>

  <#if folder.files?size &gt; 0>
  <table class="zebra-striped classeurFiles bs" >
  <thead>
    <tr>
      <th class="header">&nbsp;</th>
      <th class="header editblock" style="min-width: 25px">
      <#if canWrite>
        <input type="checkbox" name="checkoptionsFolder" value="${folder.document.id}" title="${Context.getMessage('label.PageClasseur.folder.checkbox')}"/>
      </#if>
      </th>
      <th class="header">${Context.getMessage('label.PageClasseur.tableheader.filename')}</th>
      <th class="header" style="min-width: 42px">${Context.getMessage('label.PageClasseur.tableheader.size')}</th>
      <#-- <th>${Context.getMessage('label.PageClasseur.tableheader.version')}</th> -->
      <th class="header">${Context.getMessage('label.PageClasseur.tableheader.modified')}</th>
      <th class="header">${Context.getMessage('label.PageClasseur.tableheader.creator')}</th>
      <th class="header" style="width: 55px;">&nbsp;</th>
    </tr>
  </thead>
  <tbody>
  <#list folder.files as child>
  <tr class="main ${child.id}">
    <td>
      <img title="${child.type}" alt="${child.type}/" src="/nuxeo${child.common.icon}" />
    </td>
    <td class="editblock">
    <#if child.facets?seq_contains("Folderish") == false >
      <#if canWrite>
        <input type="checkbox" name="checkoptions" value="${child.id}" />
      </#if>
    </#if>
    </td>
    <#if child.facets?seq_contains("Folderish") == false >
      <#assign modifDate = child.dublincore.modified?datetime >
      <#assign modifDateStr = modifDate?string("EEEE dd MMMM yyyy HH:mm") >
      <#assign filename = child.dublincore.title >
      <#assign words = filename?word_list>
      <#assign isModifiedFilename = false>
      <#assign max_len_word = 50>
      <#list words as word>
      	<#if (word?length > max_len_word)>
      		<#assign isModifiedFilename = true>
      		<#break>
      	</#if>
      </#list>
      <#assign blob = This.getBlobHolder(child).blob >
      <#assign blobLenght = blob.length >
      <#assign max_lenght = This.getProperty("labs.max.size.file.readr", "10")?number * 1048576 />
      <td>
      	<#if (isModifiedFilename)>
      		<span title="${filename}-${child.dublincore.description}">${filename?substring(0, max_len_word)}...</span>
      	<#else>
      		<span title="${child.dublincore.description}">${filename}</span>
      	</#if>
      </td>
      <td>${bytesFormat(blobLenght, "K", "fr_FR")}<span class="sortValue">${blobLenght?string.computer}</span></td>
      <#-- <td>${child.versionLabel}</span></td> -->
      <td><span title="${modifDateStr}" >${Context.getMessage('label.PageClasseur.table.dateInWordsFormat',[dateInWords(modifDate)])}</span><span class="sortValue">${modifDate?string("yyyyMMddHHmmss")}</span></td>
      <td><span title="${child.dublincore.creator}" >${userFullName(child.dublincore.creator)}</span></td>
      <td>
      <#if canWrite>
            <button class="btn danger editblock" onclick="$('#docdelete_${child.id}').submit()">${ Context.getMessage('command.PageClasseur.deleteFile')}</button>
            <span class="editblock"><br /></span>
      </#if>
        <a class="btn small classeurDownload" href="${This.path}/${folder.document.name}/${Common.quoteURIPathComponent(child.name)}/@blob/">${Context.getMessage('command.PageClasseur.download')}</a>
      <#if (max_lenght > blobLenght) && This.hasConvertersForHtml(blob.mimeType)>
       	<a class="btn small classeurDisplay" href="${This.path}/${folder.document.name}/${Common.quoteURIPathComponent(child.name)}/@blob/preview" target="_blank">${Context.getMessage('command.PageClasseur.display')}</a>
      </#if>
        <form id="docdelete_${child.id}" action="${This.path}/${folder.document.name}/${Common.quoteURIPathComponent(child.name)}/@delete" onsubmit="return confirm('Voulez vous vraiment supprimer le document ?')">
        </form>
       	</td>


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