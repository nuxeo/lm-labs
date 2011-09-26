<@extends src="/views/labs-base.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
    <script type="text/javascript" src="${skinPath}/js/PageNews.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
  </@block>
  <@block name="docactions">
      <@superBlock/>
         <#if isAuthorized>
          <li><a href="${This.path}/@delete">${Context.getMessage('command.PageNews.delete')}</a></li>
            <li><a href="${This.path}/@views/edit">${Context.getMessage('command.PageNews.modify')}</a></li>
         </#if>
  </@block>
  <@block name="content">
      <div id="content" class="container">
          <section>
              <div class="page-header">
                <h1>${news.title} <small>${Context.getMessage('label.labsNews.display.by')} ${news.lastContributorFullName}</small></h1>
                <small>${Context.getMessage('label.labsNews.display.publish')} ${news.startPublication.time?string('dd MMMMM yyyy')}</small>
              </div>

              <div class="row">
                <div class="span16 columns">
                  ${news.content}
                </div>
              </div>
            </section>
      </div>
  </@block>
</@extends>