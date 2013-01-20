<#assign bsMinified = ".min" />
<@extends src="/views/labs-manage-base.ftl">
  <#assign canCreateSite = Common.canCreateSite(Context.principal.name)>

	<@block name="css">
		<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/labs.sitesroot.css" />
		<link rel="stylesheet/less" media="all" href="${Context.modulePath}/@views/variables.less" />
    <#if !Context.principal.anonymous && canCreateSite>
		<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/labs.sitesroot-authenticated.css" />
    </#if>
		<style type="text/css">
		  label {
		  font-weight: bold;
		  }
		</style>
    </@block>

  <@block name="scripts">
    <script type="text/javascript" src="${contextPath}/wro/labs.sitesroot.js"></script>
    <#if canCreateSite>
    <script type="text/javascript" src="${contextPath}/wro/labs.sitesroot-authenticated.js"></script>
    </#if>
  </@block>

  <@block name="docactions">
    <@superBlock/>
    <#if !Context.principal.anonymous && canCreateSite>
      <li>
        <a class="open-dialog" modal-height="365px" modal-overflowy="auto" rel="divEditSite" href="#"><i class="icon-plus"></i>${Context.getMessage('label.labssite.add.site')}</a>
        <div id="divEditSite" class="dialog2" style="display:none;">
            <#include "/views/sitesRoot/addSite.ftl" />
        </div>
      </li>
    </#if>
	<#if Context.principal.name == "Administrator" >
    	<li class="divider"></li>
		<li><a href="${Context.baseURL}/nuxeo/nxadmin/default/default-domain@view_admin" target="_blank" ><i class="icon-pushpin"></i>${Context.getMessage('command.LabsSite.goToBackOffice')}</a></li>
	</#if>
  </@block>

  <@block name="content">
  <section>
    <div class="page-header">
      <h1>${Context.getMessage('label.labssite.list.sites.title')}</h1>
    </div>
    
    <ul class="nav nav-tabs" id="sitesTabs" >
	  	 <li class="active">
	  	 	<a href="#cat-1" data-toggle="tab">${Context.getMessage('label.labssite.nav.category.allsites')} <span class="badge badge-info" style="vertical-align: middle;" ></span></a>
	  	 </li>
	  	 <#assign labsCategories = This.displayableCategories />
		 <#list labsCategories as category>
		 	<#if (category.labscategory.id != 0)>
			  	 <li >
			  	 	<a href="#cat${category.labscategory.id}" data-toggle="tab">${category.labscategory.label} <span class="badge badge-info" style="vertical-align: middle;" ></span></a>
			  	 </li>
			</#if>
	  	 </#list>
	  	 <li >
	  	 	<a href="#cat0" data-toggle="tab">${Context.getMessage('label.labssite.nav.without.category.sites')} <span class="badge badge-info" style="vertical-align: middle;" ></span></a>
	  	 </li>
    </ul>

	<div class="tab-content">
		<div class="tab-pane active" id="cat-1" data-view-url="${This.path}/@views/undeletedSites">
		<#include "views/sitesRoot/undeletedSites.ftl" >
		</div>
		<#list labsCategories as category>
			<#if (category.labscategory.id != 0)>
		<div class="tab-pane" id="cat${category.labscategory.id}" 
			data-view-url="${This.path}/@views/undeletedSites?idCategory=${category.labscategory.id}">
		</div>
			</#if>
		</#list>
		<div class="tab-pane" id="cat0" data-view-url="${This.path}/@views/undeletedSites?idCategory=0"></div>
	</div>

	<#assign hasOneMoreDeletedSite = false />
    <#-- template sites -->
    <div class="template-sites" >
    <#include "views/sitesRoot/templateSites.ftl" >
    </div>
	<#-- deleted sites -->
    <div class="deleted-sites" id="deleted-sites" data-reload-url="${Context.modulePath}/@views/deletedSites" >
    <#include "views/sitesRoot/deletedSites.ftl" >
    </div>
  </section>
  </@block>
</@extends>