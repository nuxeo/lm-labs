<#if site?? && Session.hasPermission(site.document.ref, "Everything")>
<@extends src="/views/templates/labs-base.ftl">

<@block name="scripts">
    <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-twipsy.js"></script>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
  </@block>

  <@block name="breadcrumbs">
    <#include "views/common/breadcrumbs_siteadmin.ftl" >
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
<#else>
	<#include "error/error_404.ftl" >
</#if>
