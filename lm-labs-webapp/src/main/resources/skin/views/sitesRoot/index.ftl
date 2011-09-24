<@extends src="/views/labs-manage-base.ftl">
  <#assign isAuthorized = !Context.principal.isAnonymous()>

  <@block name="scripts">
    <@superBlock/>
    <script type="text/javascript" src="${skinPath}/js/sitesRoot.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.controls.js"></script>
    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.dialog2.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.dialog2.css"/>
  </@block>


  <@block name="docactions">
    <@superBlock/>
    <#if isAuthorized>
      <li>
        <a class="open-dialog" rel="divEditSite" href="#">${Context.getMessage('label.labssite.add.site')}</a>
        <div id="divEditSite" class="dialog2" style="display:none;">
            <#include "/views/sitesRoot/addSite.ftl" />
        </div>
      </li>
    </#if>
  </@block>



  <@block name="content">
  <section>
    <div class="page-header">
      <h1>${Context.getMessage('label.labssite.list.sites.title')}</h1>
    </div>

    <#assign sites = This.labsSites />
    <#if (sites?size > 0) >
      <table class="zebra-striped" id="MySites" >
        <thead>
          <tr>
            <th>Nom du site</th>
            <th>Responsable</th>
            <th>&nbsp;</th>
          </tr>
        </thead>
        <tbody>
          <#list sites as site>
            <tr>
              <td>${site.title}</td>
              <td>${site.document.creator}</td>
              <td><a class="btn" href="${This.path}/${site.URL}">Voir</a></td>
            </tr>
          </#list>
      </table>
    <#else>
      Aucun site trouvé
    </#if>
  </section>
  </@block>
</@extends>