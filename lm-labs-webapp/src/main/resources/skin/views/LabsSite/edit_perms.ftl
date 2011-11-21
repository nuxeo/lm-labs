<@extends src="/views/templates/labs-base.ftl">

<@block name="scripts">
    <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-twipsy.js"></script>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
  </@block>

  <@block name="breadcrumbs">
    <a href="${This.getPath()}">${site.title}</a> > Administration
  </@block>

  <@block name="tabs">
    <#include "macros/admin_menu.ftl" />
	<@adminMenu item="perms"/>
  </@block>

  <@block name="content">
    <div class="container">
      <section>
        <div class="page-header">
          <h3>Permissions</h3>
        </div>
		<#include "views/LabsPermissions/permissionsManager.ftl" >
		
      </section>

    </div>
  </@block>
</@extends>