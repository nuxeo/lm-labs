<@extends src="/views/labs-base.ftl">

  <@block name="title">${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/jquery/jquery.hotkeys.js"></script>
        <script type="text/javascript" src="${skinPath}/js/assets/prettify/prettify.js"></script>
        <script type="text/javascript" src="${skinPath}/js/PageHtml.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/js/assets/prettify/prettify.css"/>
  </@block>

  <@block name="docactions">
    <@superBlock/>
      <li><a id="page_edit" href="${This.path}/@views/edit"/>Modifier la page</a></li>
      <#include "views/common/page_actions.ftl" />
  </@block>

  <@block name="content">
       <div class="container">

    <h1>${page.title}</h1>
    ${page.description}

    <#list page.sections as section>
    <section id="section_${section_index}">
        <div class="page-header">
            <h1>${section.title} <small>${section.description}</small></h1>
        </div>

        <#list section.rows as row>
          <div class="row" id="row_s${section_index}_r${row_index}">
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





  </@block>
</@extends>