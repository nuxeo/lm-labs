<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">

  <@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
  <@block name="docactions">
	<#include "views/common/page_actions.ftl" />
  </@block>
  <@block name="content">

    <div id="content" class="container">
      <div class="row">
    </div>
  </@block>
</@extends>