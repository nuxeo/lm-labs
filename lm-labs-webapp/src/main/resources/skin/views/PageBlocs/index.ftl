<@extends src="/views/templates/" + This.page.template.templateName + ".ftl">

  <@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
  <@block name="docactions">
	<#include "views/common/page_actions.ftl" />
  </@block>
  <@block name="content">
		<#include "views/common/topnavigation_area.ftl" />

    <div id="content" class="container">
      <div class="row">
        <div class="span16 columns">

	<#include "views/common/children_area.ftl" />
		<@children_block Document "1" />
		<#--
        <div class="bloc">
          <div class="header">
            ${Document.dublincore.description}
            <br>toto
            ${"/views/templates/" + This.page.template.templateName + ".ftl"}
          </div>
            <ul class="unstyled">
              <#list Session.getChildren(This.document.ref) as child>
                <li><a href="${This.path}/${child.name}">${child.title}</a></li>
              </#list>
            </ul>
        </div>
        -->
      </div>
    </div>
  </@block>
</@extends>