<#assign bsMinified = ".min" />
<@extends src="/views/labs-manage-base.ftl">
  <#assign canCreateSite = Common.canCreateSite(Context.principal.name)>

	<@block name="css">
		<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/labs.sitesroot.css" />
		<link rel="stylesheet/less" media="all" href="${Context.modulePath}/@views/variables.less" />
		<style type="text/css">
		  label {
		  font-weight: bold;
		  }
		</style>
    </@block>

  <@block name="scripts">
    <script type="text/javascript" src="${contextPath}/wro/labs.sitesroot.js"></script>
    <script type="text/javascript">
jQuery(document).ready(function() {
  jQuery.filtrify('MySites-tbody', 'category-selector');
  jQuery("table[class*='table-striped']").tablesorter({
    headers: { 3: { sorter: false}},
    sortList: [[0,0]],
    textExtraction: function(node) {
      // extract data from markup and return it
      var sortValues = jQuery(node).find('span[class=sortValue]');
      if (sortValues.length > 0) {
        return sortValues[0].innerHTML;
      }
      return node.innerHTML;
    }
  });
});

function deleteSite(url){
	if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouDelete')}")){
		jQuery('#waitingPopup').dialog2('open');
		jQuery.ajax({
			type: 'PUT',
		    async: false,
		    url: url,
		    success: function(data) {
		    	if (data == 'delete') {
		          alert("${Context.getMessage('label.lifeCycle.site.hasDeleted')}");
		          document.location.href = '${Context.modulePath}';
		        }
		        else {
		          alert("${Context.getMessage('label.lifeCycle.site.hasNotDeleted')}");
      		      jQuery('#waitingPopup').dialog2('close');
		        }
		    },
		    error: function(data) {
		    	alert(data);
		    	jQuery('#waitingPopup').dialog2('close');
		    }
		});
	}
}
function undeleteSite(url){
	if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouUndelete')}")){
		jQuery('#waitingPopup').dialog2('open');
		jQuery.ajax({
			type: 'PUT',
		    async: false,
		    url: url,
		    success: function(data) {
		    	if (data == 'undelete') {
		          alert("${Context.getMessage('label.lifeCycle.site.hasUndeleted')}");
		          document.location.href = '${Context.modulePath}';
		        }
		        else {
		          alert("${Context.getMessage('label.lifeCycle.site.hasNotUndeleted')}");
		          jQuery('#waitingPopup').dialog2('close');
		        }
		    },
		    error: function(data) {
		    	alert(data);
		    	jQuery('#waitingPopup').dialog2('close');
		    }
		});
	}
}
function deleteDefinitelySite(url){
	if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouDefinitelyDelete')}")){
		jQuery('#waitingPopup').dialog2('open');
		jQuery.ajax({
			type: 'DELETE',
		    async: false,
		    url: url,
		    success: function(data) {
	          alert("${Context.getMessage('label.lifeCycle.site.hasDefinitelyDelete')}");
	          document.location.href = '${Context.modulePath}';
		    },
		    error: function(data) {
	          alert("${Context.getMessage('label.lifeCycle.site.hasNotDefinitelyDelete')}");
	          jQuery('#waitingPopup').dialog2('close');
		    }
		});
	}
}        		
    </script>
  </@block>

  <@block name="css">
    <@superBlock/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.dialog2.css"/>
  </@block>


  <@block name="docactions">
    <@superBlock/>
    <#if canCreateSite>
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
	<#--assign undeletedLabsSites = This.undeletedLabsSites /-->
    <div class="page-header">
      <h1>${Context.getMessage('label.labssite.list.sites.title')} <span class="badge badge-info" style="vertical-align: middle;" >${undeletedLabsSites?size}</span>
      </h1>
      <div id="category-selector" class="pull-right"></div>
    </div>
    
	 <ul class="nav nav-tabs">
	  	 <li <#if (idCurrentCategory == -1) >class="active"</#if>>
	  	 	<a href="${This.path}">${Context.getMessage('label.labssite.nav.category.allsites')}</a>
	  	 </li>
	  	 <#assign labsCategories = This.getDisplayableCategories()/>
		 <#list labsCategories as category>
		 	<#if (category.labscategory.id != 0)>
			  	 <li <#if (idCurrentCategory == category.labscategory.id) >class="active"</#if>>
			  	 	<a href="${This.path}?idCategory=${category.labscategory.id}">${category.labscategory.label}</a>
			  	 </li>
			</#if>
	  	 </#list>
	  	 <li <#if (idCurrentCategory == 0) >class="active"</#if>>
	  	 	<a href="${This.path}?idCategory=0">${Context.getMessage('label.labssite.nav.without.category.sites')}</a>
	  	 </li>
	  </ul>

	<#assign hasOneMoreDeletedSite = false />
	<#--assign deletedLabsSites = This.deletedLabsSites /-->
	<#--assign templateLabsSites = This.templateLabsSites /-->
    <#if (deletedLabsSites?size > 0 || undeletedLabsSites?size > 0 || templateLabsSites?size > 0) >
    	<#if (undeletedLabsSites?size > 0) >
	    	<#assign hasAtLeastOneAdminSite = false />
	    	<#list undeletedLabsSites as undeletedSite>
	    		<#if undeletedSite.isAdministrator(Context.principal.name) >
	    			<#assign hasAtLeastOneAdminSite = true />
	    			<#break>
	    		</#if>
	    	</#list>
	      <table class="table table-striped table-bordered bs" id="MySites" >
	        <thead>
	          <tr>
	            <th>${Context.getMessage('label.labssite.list.headers.site')}</th>
	            <th>${Context.getMessage('label.labssite.list.headers.owner')}</th>
	            <th>${Context.getMessage('label.labssite.list.headers.created')}</th>
	            <th style="width: 57px;">&nbsp;</th>
	            <#if hasAtLeastOneAdminSite>
	            <th style="width: 88px;"></th>
	            </#if>
	          </tr>
	        </thead>
	        <tbody id="MySites-tbody">
	          <#list undeletedLabsSites as sit>
	            <tr data-category="${sit.document['labssite:category']}" >
	              <td>${sit.title}<#if !sit.visible>&nbsp;<i class="icon-eye-close"></i></#if></td>
	              <#if (sit.administratorsSite?size > 0)>
	              	<td>
	              		<#list sit.administratorsSite as siteAdministrator>
	              			${userFullName(siteAdministrator)}<#if (siteAdministrator != sit.administratorsSite?last)>,&nbsp;</#if>
	              		</#list>
	              	</td>
	              <#else>
	              	<td>${userFullName(sit.document['dc:creator'])}</td>
	              </#if>
			      <#assign creationDate = sit.document['dc:created']?datetime />
			      <#assign creationDateStr = creationDate?string("EEEE dd MMMM yyyy HH:mm") />
			      <td><span title="${creationDateStr}" >${Context.getMessage('label.labssite.list.dateInWordsFormat',[dateInWords(creationDate)])}</span><span class="sortValue">${creationDate?string("yyyyMMddHHmmss")}</span></td>
	              <td><a class="btn" href="${This.path}/${sit.URL}">${Context.getMessage('command.labssite.list.open')}</a></td>
	              <#if hasAtLeastOneAdminSite>
	              <td>
	              <#if sit.isAdministrator(Context.principal.name) >
	              	<a href="#" class="btn btn-danger" onclick="javascript:deleteSite('${Context.modulePath}/${sit.URL}/@labspublish/delete');">${Context.getMessage('command.siteactions.delete')}</a>
	              </#if>
	              </td>
	              </#if>
	            </tr>
	          </#list>
	      </table>
	    </#if>
	    <#-- template sites -->
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
	            <tr>
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
	    	<#-- deleted sites -->
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
			            <tr>
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
			              	<a id="undeleteSite" href="#" class="btn" onclick="javascript:undeleteSite('${Root.getLink(deletedSite.document)}/@labspublish/undelete');">${Context.getMessage('command.siteactions.undelete')}</a>
			              </td>
			              <#if hasAtLeastOneAdminSite>
			              <td>
			              <a href="#" class="btn btn-danger<#if !Common.canDeleteSite(Context.principal.name) > disabled</#if>"
			              <#if Common.canDeleteSite(Context.principal.name) >
			                onclick="javascript:deleteDefinitelySite('${Root.getLink(deletedSite.document)}');"
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
    <#else>
      Aucun site trouvé
    </#if>
  </section>
  </@block>
</@extends>