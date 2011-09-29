<@extends src="/views/labs-base.ftl">

  <@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>

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
        <h1><span title="${folder.document.dublincore.description}" >${folder.document.dublincore.title}</span></h1>
      </div>


        <#if canWrite>
        <div class="row">

          <div class="span16 columns">
            <a href="#" rel="addfile_${folder.document.id}_modal" class="btn open-dialog" >Ajouter un fichier</a>


          <!-- This button submits the hidden delete form -->
          <button type="submit" class="btn danger" onclick="$('#delete_${folder.document.id}').submit();return false;">${Context.getMessage('command.PageClasseur.deleteFolder')}</button>


          <div id="addfile_${folder.document.id}_modal" >
              <h1>${Context.getMessage('command.PageClasseur.addFile')}</h1>
              <form action="${This.path}/${folder.document.name}" method="post" enctype="multipart/form-data">
                <fieldset>
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
                  </fieldset>



              <div class="actions">
                <button class="btn primary">Envoyer</button>
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
  </#list>
  </#if>

  <div id="classeurBottomActions">
    <#list This.getLinks("BOTTOM_ACTIONS") as link>
      <button class="btn" id="${link.id}">${Context.getMessage('command.' + Document.type + '.' + link.id)}</button>
    </#list>
  </div>


  </div><!-- table -->




<div id="div-addfolder" style="display: none;" >
    <h1>${Context.getMessage('label.PageClasseur.form.folder.title')}</h1>

  <form class="ajax" action="${This.path}" method="post" enctype="multipart/form-data">
      <fieldset>
      <div class="clearfix">
          <label for="folderName">${Context.getMessage('label.PageClasseur.form.folder.name')}</label>
            <div class="input">
              <input name="folderName"/>
            </div>
          </div><!-- /clearfix -->
      </fieldset>



  <div class="actions">
    <button class="btn primary">Envoyer</button>
  </div>
  </form>



</div>

<#include "views/common/loading.ftl">

<#macro displayChildren folder recurse=false>

  <#if folder.files?size &gt; 0>
  <table class="zebra-striped classeurFiles bs" >
  <thead>
    <tr>
      <th>
      <#if canWrite>
        <input type="checkbox" name="checkoptionsFolder" value="${folder.document.id}" title="${Context.getMessage('label.PageClasseur.folder.checkbox')}"/>
      </#if>
      </th>
      <th>&nbsp;</th>
      <th>${Context.getMessage('label.PageClasseur.tableheader.filename')}</th>
      <th>${Context.getMessage('label.PageClasseur.tableheader.size')}</th>
      <#-- <th>${Context.getMessage('label.PageClasseur.tableheader.version')}</th> -->
      <th>${Context.getMessage('label.PageClasseur.tableheader.modified')}</th>
      <th>${Context.getMessage('label.PageClasseur.tableheader.creator')}</th>
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
      <td>${bytesFormat(This.getBlobHolder(child).blob.length, "K", "fr_FR")}<span class="sortValue">${This.getBlobHolder(child).blob.length?string.computer}</span></td>
      <#-- <td>${child.versionLabel}</span></td> -->
      <td><span title="${modifDateStr}" >${Context.getMessage('label.PageClasseur.table.dateInWordsFormat',[dateInWords(modifDate)])}</span><span class="sortValue">${modifDate?string("yyyyMMddHHmmss")}</span></td>
      <td><span title="${child.dublincore.creator}" >${userFullName(child.dublincore.creator)}</span></td>
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