<@extends src="/views/templates/" + This.page.template.templateName + ".ftl">
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
      <#include "views/common/page_actions.ftl" />
  </@block>

  <@block name="content">
      <div id="content" class="container">

    <div style="clear:both"></div>
        <#if isAuthorized>
          <a class="btn" href="${This.path}/@views/addnews">${Context.getMessage('label.labsNews.add.news')}</a>
        </#if>

        <#list pageNews.allNews as news>
          <section>
              <div class="page-header">
                <h1><a href="${This.path}/${news.documentModel.name}">${news.title}</a> <small>${Context.getMessage('label.labsNews.display.by')} ${news.lastContributorFullName}</small></h1>
                <small>${Context.getMessage('label.labsNews.display.publish')} ${news.startPublication.time?string('dd MMMMM yyyy')}</small>
              </div>


              <#list news.rows as row>
                <div class="row" id="row_s${news_index}_r${row_index}">
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
              </#list>
            </section>
        </#list>
        <hr />
        <a href="${This.path}/@labsrss/topnews" target="_blank"><img src="${skinPath}/images/iconRss.gif"/></a>
      </div>
  </@block>
</@extends>