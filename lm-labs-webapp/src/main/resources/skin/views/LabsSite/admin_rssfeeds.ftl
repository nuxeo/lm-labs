<#assign mySite=Common.siteDoc(Document).getSite() />
<@extends src="/views/labs-admin-base.ftl">

<#assign
	topnewsUrl = "${Context.modulePath}/${mySite.URL}/@labsrss/topnews"
	lastNewsUrl = "${Context.modulePath}/${mySite.URL}/@labsrss/lastNews"
	lastUploadUrl = "${Context.modulePath}/${mySite.URL}/@labsrss/lastUpload"
	allUrl = "${Context.modulePath}/${mySite.URL}/@labsrss/all"
/>
	<@block name="css">
		<@superBlock/>
	    <link href="${topnewsUrl}" rel="alternate" type="application/rss+xml" title="${Context.getMessage('label.rss.topnews.title')}" />
	    <link href="${lastNewsUrl}" rel="alternate" type="application/rss+xml" title="${Context.getMessage('label.rss.lastNews.title')}" />
	    <link href="${lastUploadUrl}" rel="alternate" type="application/rss+xml" title="${Context.getMessage('label.rss.lastUpload.title')}" />
	    <link href="${allUrl}" rel="alternate" type="application/rss+xml" title="${Context.getMessage('label.rss.all.title')}" />
	</@block>

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
                <li><a target="_blank" href="${topnewsUrl}">${Context.getMessage('label.rss.topnews.title')}</a></li>
                <li><a target="_blank" href="${lastNewsUrl}">${Context.getMessage('label.rss.lastNews.title')}</a></li>
                <li><a target="_blank" href="${lastUploadUrl}">${Context.getMessage('label.rss.lastUpload.title')}</a></li>
                <li><a target="_blank" href="${allUrl}">${Context.getMessage('label.rss.all.title')}</a></li>
            </ul>
        </div>

      </section>

    </div>
  </@block>
</@extends>
