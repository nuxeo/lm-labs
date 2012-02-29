<#include "views/common/init_mode.ftl" />
    <div class="navbar-wrapper" style="z-index: 5;">
    <div class="navbar">
      <div class="navbar-inner">
        <div class="container-fluid">
            <#if site??>
              <a class="brand" href="${Context.modulePath}/${site.document.webcontainer.url}">${site.title}</a>
            <#else>
              <a class="brand" href="${Context.modulePath}">LABS</a>
            </#if>

          <div class="nav-collapse" >
          <ul class="nav pull-right">
            <#if site?? >
            <li>
              <form class="navbar-form pull-right" style="margin-right: 15px;" accept-charset="ISO-8859-1" action="${Context.modulePath}/${site.URL}/@search">
              <input placeholder="${Context.getMessage('label.search')}" name="fullText"/>
              </form>
            </li>
            </#if>
            <li id="FKerrorLogin" class="navbar-text" style="/*display: none;*/">${Context.getMessage('label.login.failed')}</li>
            <li><#include "common/labsLogin.ftl" /></li>
            <#if Context.principal.isAnonymous() == false>
            <li class="dropdown	">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">${Context.principal.firstName} ${Context.principal.lastName}<b class="caret"></b></a>
              <ul class="dropdown-menu">
                <@block name="docactions">
	                <#if site?? && site.isContributor(Context.principal.name) >
	                	<@block name="docactionsaddpage">
	                		<!--   add page     -->
	                		<li><a class="open-dialog" rel="add_content_dialog" href="${This.path}/@addContentView"><i class="icon-plus"></i>${Context.getMessage('command.docactions.addcontent')}</a></li>
	                	</@block>
	                	<!--   Mode page     -->
	                	<li><a id="page_edit" href="#" onclick="javascript:simulateKeyup69();"><i class="icon-eye-open"></i>Visualiser</a></li>
	                	<@block name="docactionsonpage">
		                	<!--   Manage parameter's page     -->
							<li><a href="#" onclick="javascript:openParametersPage();"><i class="icon-adjust"></i>${Context.getMessage('command.docactions.parameters')}</a></li>
							<div id="divEditParametersPage" style="display: none;">
								<#include "views/common/page_parameters.ftl" />
							</div>
							<!--   set homePage     -->
							<li>
								<#if This.page?? && !(Common.siteDoc(Document).site.indexDocument.id == This.page.document.id)>
									<a href="#" onclick="javascript:setAsHomePage();" ><i class="icon-home"></i>${Context.getMessage('command.docactions.homePage')}</a>
								</#if>
							</li>
							<script type="text/javascript">
								jQuery(document).ready(function() {
									jQuery("#divEditParametersPage").dialog2({
										autoOpen : false,
										closeOnOverlayClick : false,
										removeOnClose : false,
										showCloseHandle : false,
									});
								});
								
								function openParametersPage(){
									jQuery("#divEditParametersPage").dialog2('open');
									divEditParametersPageForm = jQuery("#divEditParametersPage")[0].innerHTML;
								}
								
								function closeParametersPage(){
									jQuery("#divEditParametersPage").dialog2('close');
									jQuery("#divEditParametersPage")[0].innerHTML = divEditParametersPageForm;
								}
								
								function submitParametersPage(){
									if(jQuery("#publishPage").attr("checked")=="checked") {
										publishPage();
									} else {
										draftPage();
									}
									jQuery("#form_editParameters").submit();
								}
								
								function publishPage(){
									//if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouPublish')}")){
										jQuery.ajax({
											type: 'PUT',
										    async: false,
										    url: '${This.path}/@labspublish/publish',
										    success: function(data) {
										    	/*if (data == 'publish') {
										          alert("${Context.getMessage('label.lifeCycle.page.hasPublished')}");
										          document.location.href = '${This.path}';
										        }
										        else {
										          alert("${Context.getMessage('label.lifeCycle.page.hasNotPublished')}");
										        }*/
										    }
										});
									//}
								}
								
								function draftPage(){
									//if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouDraft')}")){
										jQuery.ajax({
											type: 'PUT',
										    async: false,
										    url: '${This.path}/@labspublish/draft',
										    success: function(data) {
										    	/*if (data == 'draft') {
										          alert("${Context.getMessage('label.lifeCycle.page.hasDrafted')}");
										          document.location.href = '${This.path}';
										        }
										        else {
										          alert("${Context.getMessage('label.lifeCycle.page.hasNotDrafted')}");
										        }*/
										    }
										});
									//}
								}
							</script>
						</@block>
                <li class="divider"></li>
	                </#if>
	                
                </@block>
                <@block name="siteactions">
	                <#if site?? && site.isAdministrator(Context.principal.name) >
	                	<#if site.visible>
	                		<li><a href="#" onclick="javascript:draftSite();"><i class="icon-file"></i>${Context.getMessage('command.siteactions.draft')}</a></li>
	                	<#else>
	                		<li><a href="#" onclick="javascript:publishSite();"><i class="icon-share-alt"></i>${Context.getMessage('command.siteactions.publish')}</a></li>
	                	</#if>
	                	<!--   delete     -->
	                	<!--li><a href="#" onclick="javascript:deleteSite();"><i class="icon-remove"></i>${Context.getMessage('command.siteactions.delete')}</a></li-->
	                	<script type="text/javascript">
	                		function publishSite(){
	                			if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouPublish')}")){
		                			jQuery.ajax({
										type: 'PUT',
									    async: false,
									    url: '${Context.modulePath}/${site.URL}/@labspublish/publish',
									    success: function(data) {
									    	if (data == 'publish') {
									          alert("${Context.getMessage('label.lifeCycle.site.hasPublished')}");
									          document.location.href = '${This.path}';
									        }
									        else {
									          alert("${Context.getMessage('label.lifeCycle.site.hasNotPublished')}");
									        }
									    }
									});
								}
	                		}
	                		
	                		function draftSite(){
	                			if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouDraft')}")){
		                			jQuery.ajax({
										type: 'PUT',
									    async: false,
									    url: '${Context.modulePath}/${site.URL}/@labspublish/draft',
									    success: function(data) {
									    	if (data == 'draft') {
									          alert("${Context.getMessage('label.lifeCycle.site.hasDrafted')}");
									          document.location.href = '${This.path}';
									        }
									        else {
									          alert("${Context.getMessage('label.lifeCycle.site.hasNotDrafted')}");
									        }
									    }
									});
								}
	                		}
	                		
	                		function deleteSite(){
	                			if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouDelete')}")){
		                			jQuery.ajax({
										type: 'PUT',
									    async: false,
									    url: '${Context.modulePath}/${site.URL}/@labspublish/delete',
									    success: function(data) {
									    	if (data == 'delete') {
									          alert("${Context.getMessage('label.lifeCycle.site.hasDeleted')}");
									          document.location.href = '${Context.modulePath}';
									        }
									        else {
									          alert("${Context.getMessage('label.lifeCycle.site.hasNotDeleted')}");
									        }
									    }
									});
								}
	                		}
	                	</script>
	                </#if>
	                <#if site?? && site.isContributor(Context.principal.name) >
	                <li><a href="${Context.modulePath}/${site.URL}/@views/edit"><i class="icon-cog"></i>${Context.getMessage('label.contextmenu.administration')}</a></li>
	                </#if>
	                <#if site??>
	                  <li><a href="${Context.modulePath}/${site.URL}/@views/sitemap"><i class="icon-map-marker"></i>${Context.getMessage('label.contextmenu.sitemap')}</a></li>
	                </#if>
                </@block>

              <#if site?? && site.isAdministrator(Context.principal.name) >
                <li><a href="${Context.baseURL}/nuxeo/nxpath/default/default-domain/sites/${site.document.title}/tree@view_documents?tabIds=%3A" target="_blank" ><i class="icon-edit"></i>${Context.getMessage('command.LabsSite.goToBackOffice')}</a></li>
              </#if>
                <li class="divider"></li>
                <li><a id="logout" href="#"><i class="icon-off"></i>${Context.getMessage('command.contextmenu.logout')}</a></li>
              </ul>
            </li>
            </#if>
          </ul>
          </div>
        </div>
      </div><#-- /fill -->
    </div><#-- /navbar -->
    </div><#-- /navbar-wrapper -->
    &nbsp;