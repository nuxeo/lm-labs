<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">

  <@block name="title">${site.title}-${This.document.title}</@block>
  <@block name="docactions">
  	<@superBlock/>
	<#include "views/common/page_actions.ftl" />
  </@block>
  <@block name="content">

    <div id="content" class="container">
      <div class="row">
    </div>
  </@block>
</@extends>