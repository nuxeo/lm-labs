<#assign templateLabsSites = This.templateLabsSites />
<#if (Context.principal.isAnonymous() == false && templateLabsSites?size > 0) >
<section>
	<div class="page-header">
		<h4>${Context.getMessage('label.labssite.list.template.sites.title')} <span class="badge badge-info" style="vertical-align: middle;" >${templateLabsSites?size}</span></h4>
	</div>
	<#assign hasAtLeastOneAdminSite = false />
	<#list templateLabsSites as labsSite>
		<#if labsSite.isAdministrator(Context.principal.name) >
			<#assign hasAtLeastOneAdminSite = true />
			<#break>
		</#if>
	</#list>
  <table class="table table-bordered table-striped bs" id="templateSites" >
    <thead>
      <tr>
        <th>${Context.getMessage('label.labssite.list.headers.site')}</th>
        <th>${Context.getMessage('label.labssite.list.headers.owner')}</th>
        <th>${Context.getMessage('label.labssite.list.headers.created')}</th>
        <#if hasAtLeastOneAdminSite>
        <th style="width: 57px;">&nbsp;</th>
        <th style="width: 88px;"></th>
        </#if>
      </tr>
    </thead>
    <tbody>
      <#list templateLabsSites as labsSite>
        <tr data-category="${labsSite.category}" >
          <td>${labsSite.title}</td>
          <#if (labsSite.administratorsSite?size > 0)>
          	<td>
          		<#list labsSite.administratorsSite as siteAdministrator>
          			${userFullName(siteAdministrator)}<#if (siteAdministrator != labsSite.administratorsSite?last)>,&nbsp;</#if>
          		</#list>
          	</td>
          <#else>
          	<td>${userFullName(labsSite.document['dc:creator'])}</td>
          </#if>
	      <#assign creationDate = labsSite.document['dc:created']?datetime />
	      <#assign creationDateStr = creationDate?string("EEEE dd MMMM yyyy HH:mm") />
	      <td><span title="${creationDateStr}" >${Context.getMessage('label.labssite.list.dateInWordsFormat',[dateInWords(creationDate)])}</span><span class="sortValue">${creationDate?string("yyyyMMddHHmmss")}</span></td>
          <#if hasAtLeastOneAdminSite>
          <#if labsSite.isAdministrator(Context.principal.name) >
            <td><a class="btn" href="${This.path}/${labsSite.URL}">${Context.getMessage('command.labssite.list.open')}</a></td>
          	<td><a href="#" class="btn btn-danger" onclick="javascript:deleteSite('${Context.modulePath}/${labsSite.URL}/@labspublish/delete');">${Context.getMessage('command.siteactions.delete')}</a></td>
          <#else>
            <td></td>
            <td></td>
          </#if>
          </#if>
        </tr>
      </#list>
  </table>
</section>
</#if>
