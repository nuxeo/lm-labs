<#assign mySite=Common.siteDoc(Document).site />
<@extends src="/views/labs-admin-base.ftl">

  <@block name="docactions"></@block>

  <@block name="tabs">
    <#include "macros/admin_menu.ftl" />
    <@adminMenu item="admin_rssfeeds"/>
  </@block>

  <@block name="content">
    <div class="container">
      <section>
        <div class="page-header">
          <h3>Flux RSS</h3>
        </div>
        <div class="rssfeeds-list" >
            <ul>
                <li><a target="_blank" href="${Context.modulePath}/${mySite.URL}/@labsrss/topnews">Top Actualités</a></li>
                <li><a target="_blank" href="${Context.modulePath}/${mySite.URL}/@labsrss/lastNews">Dernières Actualités</a></li>
                <li><a target="_blank" href="${Context.modulePath}/${mySite.URL}/@labsrss/lastUpload">Derniers Téléchargements</a></li>
                <li><a target="_blank" href="${Context.modulePath}/${mySite.URL}/@labsrss/all">Tous Les Flux</a></li>
            </ul>
        </div>

      </section>

    </div>
  </@block>
</@extends>
