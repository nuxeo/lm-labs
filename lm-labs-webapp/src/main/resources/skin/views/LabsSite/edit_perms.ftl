<#assign mySite=Common.siteDoc(Document).site />
<#if mySite?? && Session.hasPermission(mySite.document.ref, "Everything")>
<@extends src="/views/labs-admin-base.ftl">

  <@block name="docactions"></@block>

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
