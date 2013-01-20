<#assign deletedLabsSites = This.deletedLabsSites />
<#if (deletedLabsSites?size > 0) >
	<section>
		<div class="page-header">
			<h4>${Context.getMessage('label.labssite.list.deleted.sites.title')} <span class="badge badge-info" style="vertical-align: middle;" >${deletedLabsSites?size}</span></h4>
		</div>
	      <#assign hasAtLeastOneAdminSite = false />
	      <#list deletedLabsSites as deletedSite>
	        <#if deletedSite.isAdministrator(Context.principal.name) >
	          <#assign hasAtLeastOneAdminSite = true />
	          <#break>
		    </#if>
	      </#list>
	      <table class="table table-bordered table-condensed table-striped bs" id="MyDeletedSites" >
	        <thead>
	          <tr>
	            <th>${Context.getMessage('label.labssite.list.headers.site')}</th>
	            <th>${Context.getMessage('label.labssite.list.headers.owner')}</th>
	            <th>${Context.getMessage('label.labssite.list.headers.created')}</th>
	            <th style="width: 86px;">&nbsp;</th>
	            <#if hasAtLeastOneAdminSite>
        		<th style="width: 88px;"></th>
        		</#if>
	          </tr>
	        </thead>
	        <tbody>
	          
	          <#list deletedLabsSites as deletedSite>
	            <tr data-category="${deletedSite.category}" >
	              <td>${deletedSite.title}</td>
	              <#if (deletedSite.administratorsSite?size > 0)>
	              	<td>
	              		<#list deletedSite.administratorsSite as siteAdministrator>
	              			${userFullName(siteAdministrator)}<#if (siteAdministrator != deletedSite.administratorsSite?last)>,&nbsp;</#if>
	              		</#list>
	              	</td>
	              <#else>
	              	<td>${userFullName(deletedSite.document['dc:creator'])}</td>
	              </#if>
			      <#assign creationDate = deletedSite.document['dc:created']?datetime />
			      <#assign creationDateStr = creationDate?string("EEEE dd MMMM yyyy HH:mm") />
			      <td><span title="${creationDateStr}" >${Context.getMessage('label.labssite.list.dateInWordsFormat',[dateInWords(creationDate)])}</span><span class="sortValue">${creationDate?string("yyyyMMddHHmmss")}</span></td>
	              <td>
					<#assign confirm = Context.getMessage("label.lifeCycle.site.wouldYouUndelete.extended", deletedSite.title) />
					<#assign successStatus = Context.getMessage("label.lifeCycle.site.hasUndeleted.success") />
					<#assign successMsg = Context.getMessage("label.lifeCycle.site.hasUndeleted.extended", deletedSite.title) />
					<#assign errorStatus = Context.getMessage("label.lifeCycle.site.hasUndeleted.failed") />
					<#assign errorMsg = Context.getMessage("label.lifeCycle.site.hasNotUndeleted.extended", deletedSite.title) />
	              	<a id="undeleteSite" href="#" class="btn" onclick="if (confirm('${confirm?js_string}')){undeleteSite('${Root.getLink(deletedSite.document)}/@labspublish/undelete', {successStatus:'${successStatus?js_string}', errorStatus:'${serrorStatus?js_string}', successMsg:'${successMsg?js_string}', errorMsg:'${errorMsg?js_string}'});}">${Context.getMessage('command.siteactions.undelete')}</a>
	              </td>
	              <#if hasAtLeastOneAdminSite>
	              <td>
	              <a href="#" class="btn btn-danger<#if !Common.canDeleteSite(Context.principal.name) > disabled</#if>"
	              <#if Common.canDeleteSite(Context.principal.name) >
					<#assign confirm = Context.getMessage("label.lifeCycle.site.wouldYouDefinitelyDelete.extended", deletedSite.title) />
					<#assign successStatus = Context.getMessage("label.lifeCycle.site.hasDefinitelyDelete.success") />
					<#assign successMsg = Context.getMessage("label.lifeCycle.site.hasDefinitelyDelete.extended", deletedSite.title) />
					<#assign errorStatus = Context.getMessage("label.lifeCycle.site.hasDefinitelyDelete.failed") />
					<#assign errorMsg = Context.getMessage("label.lifeCycle.site.hasNotDefinitelyDelete.extended", deletedSite.title) />
	                onclick="if (confirm('${confirm?js_string}')){deleteDefinitelySite('${Root.getLink(deletedSite.document)}', {successStatus:'${successStatus?js_string}', errorStatus:'${serrorStatus?js_string}', successMsg:'${successMsg?js_string}', errorMsg:'${errorMsg?js_string}'});}"
	              <#else>
	                title="${Context.getMessage('label.labssite.list.deletion.not.allowed')}"
	                onclick="alert('${Context.getMessage('label.labssite.list.deletion.not.allowed')?js_string}');"
	              </#if>
	              >${Context.getMessage('command.siteactions.delete')}</a>
	              </td>
	              </#if>
	            </tr>
	          </#list>
	      </table>
	</section>
</#if>
