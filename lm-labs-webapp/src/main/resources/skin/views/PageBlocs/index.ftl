<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">

  <@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>
  <@block name="docactions">
	<#include "views/common/page_actions.ftl" />
  </@block>
  <@block name="content">
		<#include "views/common/topnavigation_area.ftl" />

    <div id="content" class="container">
      <div class="row">

	<#include "views/common/children_area.ftl" />
		<@children_block Document "span16" />
    </div>
  </@block>
</@extends>