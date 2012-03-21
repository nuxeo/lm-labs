<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
  <#assign mySite=Common.siteDoc(Document).site />
  <@block name="title">${ mySite.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.filedrop.js"></script>
        <script type="text/javascript" src="${skinPath}/js/PageClasseur.js"></script>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
        
        
        <script type="text/javascript">
        jQuery(document).ready(function() {
            jQuery.each(jQuery("[id^=spanFolderTitle]"), function(i, n){
            	var encoded = jQuery(this).html();
            	jQuery(this).html(decodeURIComponent(encoded.replace(/\+/g,  " ")));
            });
            jQuery.each(jQuery("form.form-upload-file"), function(i, n){
                var encoded = jQuery(this).attr('action');
                jQuery(this).attr('action', decodeURIComponent(encoded.replace(/\+/g,  " ")));
            });
            <#list folders as folder>
            jQuery("#form-upload-file-${folder.document.id}").ajaxForm(function() {
                reloadAfterAddingFile("${folder.document.id}");
            });
            </#list>
		});
		
		function reloadAfterAddingFile(folderId) {
			//jQuery("#addfile_"+folderId+"_modal").dialog2('close');
            jQuery('#waitingPopup').dialog2('open');
			window.location.reload();
		}
		
		function updateBtLabels(bt, title, alt) {
			jQuery(bt).attr("title", title);
			jQuery(bt).attr("alt", alt);
		}
		function changeFolderBt(imgObj, newStatus) {
			if (newStatus === 'open') {
				jQuery(imgObj).attr("src", "${skinPath}/images/toggle_plus.png");
			} else {
				jQuery(imgObj).attr("src", "${skinPath}/images/toggle_minus.png");
			}
		}
		function slideFolder(imgObj, action) {
			var collapsables = jQuery(imgObj).closest(".Folder").find("div[class*='folder-collapsable']");
			if (action === '') {
				if (collapsables.is(":visible")) {
					action = 'open';
				} else {
					action = 'close';
				}
			}
			if (action === "open") {
				changeFolderBt(imgObj, 'open');
				updateBtLabels(imgObj, "${Context.getMessage('label.PageClasseur.open')}", "${Context.getMessage('command.PageClasseur.open')}");
				collapsables.slideUp("fast");
			} else {
				changeFolderBt(imgObj, 'close');
				updateBtLabels(imgObj, "${Context.getMessage('label.PageClasseur.collapse')}", "${Context.getMessage('command.PageClasseur.collapse')}");
				collapsables.slideDown("fast");
				refreshDisplayMode(collapsables);
			}
		}
		function slideAllFolders(imgObj) {
			if (jQuery(imgObj).hasClass('allFoldersOpened')) {
				jQuery(imgObj).removeClass('allFoldersOpened');
				jQuery(imgObj).addClass('allFoldersClosed');
				changeFolderBt(imgObj, 'open');
				updateBtLabels(imgObj, "${Context.getMessage('label.PageClasseur.allFolders.open')}", "${Context.getMessage('command.PageClasseur.allFolders.open')}");
				jQuery("img[class*='openCloseBt']").each(function(i, e) {
					slideFolder(jQuery(this), 'open');
				});
			} else {
				jQuery(imgObj).removeClass('allFoldersClosed');
				jQuery(imgObj).addClass('allFoldersOpened');
				changeFolderBt(imgObj, 'close');
				updateBtLabels(imgObj, "${Context.getMessage('label.PageClasseur.allFolders.collapse')}", "${Context.getMessage('command.PageClasseur.allFolders.collapse')}");
				jQuery("img[class*='openCloseBt']").each(function(i, e) {
					slideFolder(jQuery(this), 'close');
				});
			}
		}
        </script>

  </@block>

  <@block name="css">
    <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageClasseur.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tablesorter.css"/>
  </@block>

  <@block name="content">
  <div class="container-fluid">
  <@tableOfContents anchorSelector="section > div > div.header-toc">

  <#assign folders = classeur.folders />
  <div class="">
    <#if folders?size &gt; 0>
	<img class='allFoldersOpened' src="${skinPath}/images/toggle_minus.png" onclick="slideAllFolders(this);" style="float: left; margin: 5px; cursor: pointer;" title="${Context.getMessage('label.PageClasseur.allFolders.collapse')}" alt="${Context.getMessage('command.PageClasseur.allFolders.collapse')}" />
	</#if>
	<#include "views/common/page_header.ftl">

  <#assign canWrite = Session.hasPermission(Document.ref, 'Write')>
  <#if folders?size &gt; 0>
  <div id="classeurTopActions" class="editblock">
    <@mainButtons />
  </div>
  </#if>

    <input type="hidden" id="folderPath" value="" />

    <div class="classeur" id="${This.path}">

  <#assign children = Session.getChildren(Document.ref, "Folder")>

  <#if folders?size &gt; 0>
  <#assign i=0 />
  <#list folders as folder>
    <section class="${folder.document.type}" id="${folder.document.id}" >
      
      <div>
      	<#if folder.files?size &gt; 0>
	      <img class="openCloseBt" src="${skinPath}/images/toggle_minus.png" onclick="slideFolder(this, '');" style="float: left; margin: 5px; cursor: pointer;" title="${Context.getMessage('label.PageClasseur.collapse')}" alt="${Context.getMessage('command.PageClasseur.collapse')}" />
	    </#if>
	    <div class="header-toc">
          <a name="section_${folder_index}" ></a>
          <h2><span id="spanFolderTitle${folder.document.id}" title="${folder.document.dublincore.description}" >${folder.document.dublincore.title?html}</span></h2>
        </div>
        <#if canWrite>
	    <div class="folder-actions row-fluid editblock">
          <div class="btn-group">
            <a class="btn btn-small dropdown-toggle" data-toggle="dropdown"><i class="icon-cog"></i><span class="caret"></span></a>
            <ul class="dropdown-menu" >
              <li><a 
                <#if folders?first.document.id == folder.document.id>class="arrowOpacity" onclick="return false" <#else> onclick="moveFolder('${This.path}', '${Document.ref}', '${folder.document.id}', $('#${folder.document.id}').prev('section').attr('id'));return false"</#if>
                href=""><i class="icon-arrow-up"></i>${Context.getMessage("command." + Document.type + ".moveup" )}</a></li>
              <li><a 
                <#if folders?last.document.id == folder.document.id>class="arrowOpacity" onclick="return false" <#else> onclick="moveFolder('${This.path}', '${Document.ref}', $('#${folder.document.id}').next('section').attr('id'), '${folder.document.id}');return false"</#if>
                href=""><i class="icon-arrow-down"></i>${Context.getMessage("command." + Document.type + ".movedown" )}</a></li>
              <li><a href="#" rel="addfile_${folder.document.id}_modal" class="open-dialog" ><i class="icon-upload"></i>${Context.getMessage("command." + Document.type + ".addFile" )}</a></li>
              <#-- This button submits the hidden delete form -->
              <li><a href="#" onclick="$('#delete_${folder.document.id}').submit();return false;"><i class="icon-remove"></i>${Context.getMessage('command.PageClasseur.deleteFolder')}</a></li>
		      <#-- rename   -->
		      <li><a href="#" onClick='javascript:renameFolder("${This.path}/${folder.document.name}/@put", "${folder.document.id}");return false;'><i class="icon-edit"></i>${Context.getMessage("command." + Document.type + ".renameFolder" )}</a></li>
            </ul>
          </div> <#-- btn-group -->

          <div id="addfile_${folder.document.id}_modal" >
              <h1>${Context.getMessage('command.PageClasseur.addFile')}</h1>
              <form class="form-horizontal form-upload-file" id="form-upload-file-${folder.document.id}"
                action="${This.path}/${folder.document.name}" method="post" enctype="multipart/form-data">
                <fieldset>
                  <div class="control-group">
                        <label class="control-label" for="title">${Context.getMessage('label.PageClasseur.form.title')}</label>
                          <div class="controls">
                            <input type="text" name="title" class="input-large"/>
                          </div>
                        </div>
                        
                   <div class="control-group">
                        <label class="control-label" for="blob">${Context.getMessage('label.PageClasseur.choosefile')}</label>
                          <div class="controls">
                            <input type="file" name="blob" class="required focused input-file"/>
                          </div>
                        </div>

                   <div class="control-group">
                        <label class="control-label" for="description">${Context.getMessage('label.PageClasseur.form.description')}</label>
                          <div class="controls">
                            <textarea class="input" name="description"></textarea>
                          </div>
                        </div>
                  </fieldset>

              <div class="actions">
                <button class="btn btn-primary required-fields" form-id="form-upload-file-${folder.document.id}">Envoyer</button>
              </div>
              </form>

            </div> <#-- /modal -->

              <form id="delete_${folder.document.id}" onsubmit="return confirm('${Context.getMessage('label.PageClasseur.delete.folder.confirm')}');" action="${This.path}/${folder.document.name}/@delete" method="get" enctype="multipart/form-data">
              </form>

        </div> <#-- folder-actions -->
        </#if> <#-- canWrite -->
      </div> <#-- page-header -->

      <@displayChildren folder=folder />
    </section> <!-- Folder -->
    <#assign i=i+1 />
  </#list>
  </#if>

  <div id="classeurBottomActions" class="editblock">
    <@mainButtons />
  </div>

  </div><!-- table -->

<div id="div-addfolder" style="display: none;" >
    <h1>${Context.getMessage('label.PageClasseur.form.folder.title')}</h1>
  <form class="ajax form-horizontal" id="form-folder" action="${This.path}" method="post" enctype="multipart/form-data">
      <fieldset>
      <div class="control-group">
          <label class="control-label" for="dc:title">${Context.getMessage('label.PageClasseur.form.folder.name')}</label>
            <div class="controls">
              <input id="folderName" name="dc:title" class="focused required input-xlarge"/>
            </div>
          </div>
      </fieldset>

  <div class="actions">
    <button class="btn btn-primary required-fields" form-id="form-folder">Envoyer</button>
  </div>
  </form>

</div>

<div id="div-renameTitleElement" style="display: none;" >
    <h1>${Context.getMessage('label.PageClasseur.form.rename.title')}</h1>
  <form id="form-renameTitleElement" class="form-horizontal" action="" method="post">
      <fieldset>
	      <div class="control-group">
	          <label class="control-label" for="dc:title">${Context.getMessage('label.PageClasseur.form.title')}</label>
	          <div class="controls">
	            <input id="renameTitleElement" name="dc:title" class="required input-xlarge"/>
	          </div>
	      </div>
	      <div class="control-group">
	          <label class="control-label" for="dc:description">${Context.getMessage('label.PageClasseur.form.description')}</label>
	          <div class="controls">
	            <textarea name="dc:description" id="descriptionElement" class="input-xlarge"></textarea>
	          </div>
	      </div>
      </fieldset>
	  <div class="actions">
	    <button class="btn btn-primary required-fields" form-id="form-renameTitleElement">${Context.getMessage('label.PageClasseur.form.rename.save')}</button>
	  </div>
  </form>

</div>

<#macro mainButtons >
    <#if canWrite && Session.hasPermission(Document.ref, 'AddChildren') >
    <button class="btn btn-primary btn-small addFolder" ><i class="icon-plus"></i>${Context.getMessage('command.' + Document.type + '.addFolder')}</button>
    </#if>
    <#if canWrite>
    <div class="btn-group" style="margin-left: 11em;" >
        <a class="btn btn-small dropdown-toggle arrowOpacity" data-toggle=""><i class="icon-cog"></i>${Context.getMessage('command.PageClasseur.selectionActions')} <span class="caret"></span></a>
        <ul class="dropdown-menu" style="min-width: 40px;" >
    <#assign iconName = "" />
    <#list This.getLinks("PageClasseur_SELECTION_ACTIONS") as link>
      <#if link.id == "showSelection" >
        <#assign iconName = "eye-open" />
      <#elseif link.id == "hideSelection" >
        <#assign iconName = "eye-close" />
      <#elseif link.id == "deleteSelection" >
        <#assign iconName = "remove" />
      </#if>
      <li><a href="#" class="selectionActionsBt ${link.id}" ><i class="icon-${iconName}"></i>${Context.getMessage('command.' + Document.type + '.' + link.id)}</a>
      </li>
    </#list>
        </ul>
    </div>
    </#if>
</#macro>

<#macro displayChildren folder recurse=false>

  <#if folder.files?size &gt; 0>
  <div class="folder-collapsable" >
  <table class="table table-striped classeurFiles bs table-bordered labstable" >
  <thead>
    <tr>
      <th class="header">&nbsp;</th>
      <#if canWrite>
      <th class="header editblock" style="min-width: 25px">
        <input type="checkbox" name="checkoptionsFolder" value="${folder.document.id}" title="${Context.getMessage('label.PageClasseur.folder.checkbox')}"/>
      </th>
      </#if>
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
  <tr class="main ${child.id}<#if child.facets?seq_contains("LabsHidden")> hidden editblock</#if>">
    <td>
      <img title="${child.type}" alt="${child.type}/" src="/nuxeo${child.common.icon}" />
    </td>
    <#if canWrite>
	    <td <#if !child.facets?seq_contains("LabsHidden")>class="editblock"</#if>>
	      <#if (child.facets?seq_contains("Folderish") == false) >
	        <input type="checkbox" name="checkoptions" value="${child.id}" />
	      </#if>
	    </td>
    </#if>
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
      <#assign max_lenght = This.getPropertyMaxSizeFileRead() />
      <td>
      	<#if (isModifiedFilename)>
      		<span title="${blob.filename} - ${child.name} - ${child.dublincore.description?html}">${filename?substring(0, max_len_word)}...</span>
      	<#else>
      		<span title="${blob.filename} - ${child.dublincore.description?html}">${filename?html}</span>
      	</#if>
      </td>
      <td>${bytesFormat(blobLenght, "K", "fr_FR")}<span class="sortValue">${blobLenght?string.computer}</span></td>
      <#-- <td>${child.versionLabel}</span></td> -->
      <td><span title="${modifDateStr}" >${Context.getMessage('label.PageClasseur.table.dateInWordsFormat',[dateInWords(modifDate)])}</span><span class="sortValue">${modifDate?string("yyyyMMddHHmmss")}</span></td>
      <td><span title="${child.dublincore.creator}" >${userFullName(child.dublincore.creator)}</span></td>
      <td>
      <#if canWrite>
      	<div  class="<#if !child.facets?seq_contains("LabsHidden")>editblock</#if> btn-group" >
        <a class="btn btn-mini dropdown-toggle" data-toggle="dropdown"><i class="icon-cog"></i><span class="caret"></span></a>
        <ul class="dropdown-menu" style="min-width: 40px;" >
      		<li><a href="#" onclick="openRenameTitleElement('${child.dublincore.title?js_string?html}', '${child.dublincore.description?js_string?html}', '${This.path}/${folder.document.name}/${Common.quoteURIPathComponent(child.name)}/@put');return false;"><i class="icon-edit"></i>${Context.getMessage('command.PageClasseur.renameFile')}</a></li>
            <li><a href="#" onclick="$('#docdelete_${child.id}').submit()"><i class="icon-remove"></i>${ Context.getMessage('command.PageClasseur.deleteFile')}</a></li>
        </ul>
        </div>
      </#if>
        <a rel="nofollow" class="btn btn-small classeurDownload" href="${This.path}/${folder.document.name}/${Common.quoteURIPathComponent(child.name)}/@blob/">${Context.getMessage('command.PageClasseur.download')}</a>
      <#if (max_lenght > blobLenght) && This.hasConvertersForHtml(blob.mimeType)>
       	<a rel="nofollow" class="btn btn-small classeurDisplay" href="${This.path}/${folder.document.name}/${Common.quoteURIPathComponent(child.name)}/@blob/preview" target="_blank">${Context.getMessage('command.PageClasseur.display')}</a>
      </#if>
        <form id="docdelete_${child.id}" action="${This.path}/${folder.document.name}/${Common.quoteURIPathComponent(child.name)}/@delete" onsubmit="return confirm('${Context.getMessage('label.PageClasseur.delete.file.confirm')}')">
        </form>
       	</td>


    </#if>
  </tr>
  </#list>
  </tbody>
  </table>
  </div>
  </#if>
</#macro>
  </div>
  </@tableOfContents>
  </div>
  </@block>
</@extends>