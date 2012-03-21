<#if !isSiteRoot>
	<#assign mySite=Common.siteDoc(Document).site />
</#if>
<#include "views/common/init_mode.ftl" />
    <div class="navbar-wrapper" style="z-index: 5;">
    <div class="navbar">
      <div class="navbar-inner">
        <div class="container-fluid">
            <#if mySite??>
              <a class="brand" href="${Context.modulePath}/${mySite.document.webcontainer.url}">${mySite.title}</a>
            <#else>
              <a class="brand" href="${Context.modulePath}">LABS</a>
            </#if>

          <div class="nav-collapse" >
          <ul class="nav pull-right">
            <#if mySite?? >
            <li>
              <form class="navbar-form pull-right" style="margin-right: 15px;" accept-charset="ISO-8859-1" action="${Context.modulePath}/${mySite.URL}/@search">
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
	                <#if mySite?? && mySite.isContributor(Context.principal.name) >
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
								<#if This.page?? && !(mySite.indexDocument.id == This.page.document.id)>
								    <script type="text/javascript">
                                    function setAsHomePage() {
                                        jQuery('#waitingPopup').dialog2('open');
                                        jQuery.ajax({
                                            type: 'PUT',
                                            async: false,
                                            url: '${This.path}' + '/@setHome',
                                            success: function(data) {
                                                window.location.reload();
                                            },
                                            error: function(jqXHR, textStatus, errorThrown) {
                                                alert(jqXHR.statusText);
                                                jQuery('#waitingPopup').dialog2('close');
                                            }
                                        });
                                        return false;
                                    }
								    </script>
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
										jQuery('#waitingPopup').dialog2('open');
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
										        jQuery('#waitingPopup').dialog2('close');
										    },
										    error: function(data) {
										        jQuery('#waitingPopup').dialog2('close');
										    }
										});
									//}
								}
								
								function draftPage(){
									//if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouDraft')}")){
										jQuery('#waitingPopup').dialog2('open');
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
										        jQuery('#waitingPopup').dialog2('close');
										    },
										    error: function(data) {
										        jQuery('#waitingPopup').dialog2('close');
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
	                <#if mySite?? && mySite.isAdministrator(Context.principal.name) >
	                	<#if mySite.visible>
	                		<li><a href="#" onclick="javascript:draftSite();"><i class="icon-file"></i>${Context.getMessage('command.siteactions.draft')}</a></li>
	                	<#else>
	                		<li><a href="#" onclick="javascript:publishSite();"><i class="icon-share-alt"></i>${Context.getMessage('command.siteactions.publish')}</a></li>
	                	</#if>
	                	<!--   delete     -->
	                	<!--li><a href="#" onclick="javascript:deleteSite();"><i class="icon-remove"></i>${Context.getMessage('command.siteactions.delete')}</a></li-->
	                	<script type="text/javascript">
	                		function publishSite(){
	                			if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouPublish')}")){
	                				jQuery('#waitingPopup').dialog2('open');
		                			jQuery.ajax({
										type: 'PUT',
									    async: false,
									    url: '${Context.modulePath}/${mySite.URL}/@labspublish/publish',
									    success: function(data) {
									    	if (data == 'publish') {
									          alert("${Context.getMessage('label.lifeCycle.site.hasPublished')}");
									          document.location.href = '${This.path}';
									        }
									        else {
									          alert("${Context.getMessage('label.lifeCycle.site.hasNotPublished')}");
									          jQuery('#waitingPopup').dialog2('open');
									        }
									    },
									    error: function(data) {
									    	jQuery('#waitingPopup').dialog2('close');
									    }
									});
								}
	                		}
	                		
	                		function draftSite(){
	                			if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouDraft')}")){
	                				jQuery('#waitingPopup').dialog2('open');
		                			jQuery.ajax({
										type: 'PUT',
									    async: false,
									    url: '${Context.modulePath}/${mySite.URL}/@labspublish/draft',
									    success: function(data) {
									    	if (data == 'draft') {
									          alert("${Context.getMessage('label.lifeCycle.site.hasDrafted')}");
									          document.location.href = '${This.path}';
									        }
									        else {
									          alert("${Context.getMessage('label.lifeCycle.site.hasNotDrafted')}");
									          jQuery('#waitingPopup').dialog2('open');
									        }
									    },
									    error: function(data) {
									    	jQuery('#waitingPopup').dialog2('close');
									    }
									});
								}
	                		}
	                		
	                		function deleteSite(){
	                			if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouDelete')}")){
	                				jQuery('#waitingPopup').dialog2('open');
		                			jQuery.ajax({
										type: 'PUT',
									    async: false,
									    url: '${Context.modulePath}/${mySite.URL}/@labspublish/delete',
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
									    	jQuery('#waitingPopup').dialog2('close');
									    }
									});
								}
	                		}
	                	</script>
	                </#if>
	                <#if mySite?? && mySite.isContributor(Context.principal.name) >
	                <li><a href="${Context.modulePath}/${mySite.URL}/@views/edit"><i class="icon-cogs"></i>${Context.getMessage('label.contextmenu.administration')}</a></li>
	                </#if>
	                <#if mySite??>
	                  <li><a href="${Context.modulePath}/${mySite.URL}/@views/sitemap"><i class="icon-map-marker"></i>${Context.getMessage('label.contextmenu.sitemap')}</a></li>
	                </#if>
                </@block>

              <#if mySite?? && mySite.isAdministrator(Context.principal.name) >
                <li><a href="${Context.baseURL}/nuxeo/nxpath/default/default-domain/sites/${mySite.document.title}/tree@view_documents?tabIds=%3A" target="_blank" ><i class="icon-pushpin"></i>${Context.getMessage('command.LabsSite.goToBackOffice')}</a></li>
              </#if>
                <li class="divider"></li>
                <li><a id="logoutLnk" href="#"<#-- onclick="doLogout();return false;"-->><i class="icon-signout"></i>${Context.getMessage('command.contextmenu.logout')}</a></li>
              </ul>
            </li>
            </#if>
          </ul>
          </div>
        </div>
      </div><#-- /fill -->
    </div><#-- /navbar -->
    </div><#-- /navbar-wrapper -->
    