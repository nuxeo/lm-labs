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

	<#assign deletedLabsSites = This.deletedLabsSites />
	<#assign undeletedLabsSites = This.undeletedLabsSites />
    <#if (deletedLabsSites?size > 0 || undeletedLabsSites?size > 0) >
    	<#if (undeletedLabsSites?size > 0) >
	      <table class="zebra-striped bs" id="MySites" >
	        <thead>
	          <tr>
	            <th>Nom du site</th>
	            <th>Responsable</th>
	            <th style="width: 100px;">&nbsp;</th>
	          </tr>
	        </thead>
	        <tbody>
	          <#list undeletedLabsSites as sit>
	            <tr>
	              <td>${sit.title}</td>
	              <td>${sit.document.creator}</td>
	              <td><a class="btn" href="${This.path}/${sit.URL}">Voir</a></td>
	            </tr>
	          </#list>
	      </table>
	    </#if>
	    <#if (deletedLabsSites?size > 0) >
	    	<!--   delete     -->
	    	<section>
				<div class="page-header">
					<h4>${Context.getMessage('label.labssite.list.deleted.sites.title')}</h4>
				</div>
			      <table class="zebra-striped bs" id="MySites" >
			        <thead>
			          <tr>
			            <th>Nom du site</th>
			            <th>Responsable</th>
			            <th style="width: 100px;">&nbsp;</th>
			          </tr>
			        </thead>
			        <tbody>
			          <#list deletedLabsSites as deletedSite>
			            <tr>
			              <td>${deletedSite.title}</td>
			              <td>${deletedSite.document.creator}</td>
			              <td>
			              	<a id="undeleteSite" href="#" class="btn" onclick="javascript:undeleteSite('${Context.modulePath}/@labspublish/undelete/${deletedSite.document.ref}');">${Context.getMessage('command.siteactions.undelete')}</a>
			              </td>
			            </tr>
			          </#list>
			      </table>
			</section>
			<script type="text/javascript">
				function undeleteSite(url){
        			if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouUndelete')}")){
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
						        }
						    }
						});
					}
        		}
			</script>
	    </#if>
    <#else>
      Aucun site trouvé
    </#if>
  </section>
  </@block>
</@extends>