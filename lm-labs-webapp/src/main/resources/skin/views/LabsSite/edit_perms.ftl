<@extends src="/views/labs-base.ftl">

<@block name="scripts">
    <@superBlock/>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-twipsy.js"></script>
        <script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
  </@block>

  <@block name="breadcrumbs">
    <a href="${This.getPath()}">${site.title}</a> > Administration
  </@block>

  <@block name="tabs">
    <div class="container">
      <ul class="pills">
        <li><a href="${This.path}/@views/edit">Général</a></li>
        <li><a href="${This.path}/theme/${site.siteThemeManager.theme.name}">Thème</a></li>
        <li class="active"><a href="#">Permissions</a></li>
        <li><a href="${This.path}/@views/administer_pages">Gérer les Pages</a></li>
        <li><a href="${This.path}/@views/edit_trash">Poubelle</a></li>
      </ul>
    </div>
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