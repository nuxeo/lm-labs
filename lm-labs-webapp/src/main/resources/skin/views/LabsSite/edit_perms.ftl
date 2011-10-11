<@extends src="/views/labs-base.ftl">

  <@block name="breadcrumbs">
    <a href="${This.getPath()}">${site.title}</a> > Administration
  </@block>


  <@block name="content">
    <div class="container">
      <ul class="pills">
        <li><a href="${This.path}/@views/edit">Général</a></li>
        <li><a href="${This.path}/theme/${site.siteThemeManager.theme.name}">Thème</a></li>
        <li class="active"><a href="${This.path}/@labspermissions">Permissions</a></li>
      </ul>

      <section>
        <div class="page-header">
          <h3>Permissions</h3>
        </div>
		<#include "views/LabsPermissions/permissionsManager.ftl" >
		
      </section>

    </div>
  </@block>
</@extends>