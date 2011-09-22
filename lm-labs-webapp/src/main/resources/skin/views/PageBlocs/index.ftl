<@extends src="/views/labs-base.ftl">

  <@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
  <@block name="content">

    <div id="content" class="container">
      <div class="row">
        <div class="span16 columns">
        <#include "views/common/comment_area.ftl" />

        <div class="bloc">
          <div class="header">
            ${Document.dublincore.description}
          </div>
            <ul class="unstyled">
              <#list Session.getChildren(This.document.ref) as child>
                <li><a href="${This.path}/${child.name}">${child.title}</a></li>
              </#list>
            </ul>
        </div>
      </div>
    </div>
  </@block>
</@extends>