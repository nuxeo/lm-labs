<script type="text/javascript">
</script>
    <div class="topbar-wrapper" style="z-index: 5;">
    <div class="topbar">
      <div class="topbar-inner">
        <div class="container">
          <h3>
            <#if site??>
              <a href="${Context.modulePath}/${site.document.webcontainer.url}">${site.title}</a>
            <#else>
              <a href="${Context.modulePath}">LABS</a>
            </#if>
          </h3>


          <ul class="nav secondary-nav">
            <#if site?? >
            <li>
              <form accept-charset="ISO-8859-1" action="${Context.modulePath}/${site.URL}/@search">
              <input class="normal" placeholder="${Context.getMessage('label.search')}" name="fullText"/>
              </form>
            </li>
            </#if>
            <li><#include "common/login.ftl" /></li>
            <#if Context.principal.isAnonymous() == false>
            <li class="dropdown	">
              <a href="#" class="dropdown-toggle">${Context.principal.firstName} ${Context.principal.lastName}</a>
              <ul class="dropdown-menu">
                <@block name="docactions">
                <#if site?? && Session.hasPermission(This.document.ref, "ADD_CHILDREN")>
                <li><a class="open-dialog" rel="add_content_dialog" href="${This.path}/@addContentView">${Context.getMessage('command.docactions.addcontent')}</a></li>
                </#if>
                </@block>
                <li class="divider"></li>
                <@block name="siteactions">
                <#if site?? && Session.hasPermission(site.document.ref, "Everything")>
                	<#if site.visible>
                		<li><a href="#" onclick="javascript:draftSite();">${Context.getMessage('command.siteactions.draft')}</a></li>
                	<#else>
                		<li><a href="#" onclick="javascript:publishSite();">${Context.getMessage('command.siteactions.publish')}</a></li>
                	</#if>
                	<!--   delete     -->
                	<li><a href="#" onclick="javascript:deleteSite();">${Context.getMessage('command.siteactions.delete')}</a></li>
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
                <#if site?? && Session.hasPermission(site.document.ref, "WRITE")>
                <li><a href="${Context.modulePath}/${site.URL}/@views/edit">${Context.getMessage('label.contextmenu.administration')}</a></li>
                </#if>
                <#if site??>
                  <li><a href="${Context.modulePath}/${site.URL}/@views/sitemap">${Context.getMessage('label.contextmenu.sitemap')}</a></li>
                </#if>
                </@block>

              <#if site?? && Session.hasPermission(site.document.ref, 'Everything') >
                <li><a href="${Context.baseURL}/nuxeo/nxpath/default/default-domain/sites/${site.document.title}/tree@view_documents?tabIds=%3A" target="_blank" >${Context.getMessage('command.LabsSite.goToBackOffice')}</a></li>
              </#if>
                <li class="divider"></li>
                <li><a id="logout" href="#">${Context.getMessage('command.contextmenu.logout')}</a></li>
              </ul>
            </li>
            </#if>
          </ul>
        </div>
      </div><!-- /fill -->
    </div><!-- /topbar -->
    </div><!-- /topbar-wrapper -->
    &nbsp;