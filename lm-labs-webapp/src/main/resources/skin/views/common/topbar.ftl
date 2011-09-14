<#assign site = breadcrumbsDocs(This.document)?first />
<script type="text/javascript">
function topBarFullTextSearch() {
	<#if This.type.name != "sitesRoot" >
	window.location.href = '${Context.modulePath}/${site.webcontainer.url}/@views/searchresults?fullText=' + jQuery('input[name=q]').val();
	</#if>
}
</script>
    <div class="topbar-wrapper" style="z-index: 5;">            
    <div class="topbar">
    	<div class="topbar-inner">      
        <div class="container">
          <h3><a href="${Context.modulePath}/${site.webcontainer.url}"><#if site??>${site.title}</#if></a></h3>
          
          
          <ul class="nav secondary-nav">
            <li>
          	  <form onsubmit="topBarFullTextSearch();return false;" >
            	<input class="normal" placeholder="Rechercher dans le site" name="q"/>
          	  </form>
            </li>
            <li><#include "common/login.ftl" /></li>
            <#if Context.principal.isAnonymous() == false>           
            <li class="dropdown	">
              <a href="#" class="dropdown-toggle">${Context.principal.firstName} ${Context.principal.lastName}</a>
              <ul class="dropdown-menu">
                <@block name="docactions"></@block>
                <@block name="siteactions"></@block>                                
              <#if Session.hasPermission(site.ref, 'Everything') >
                <li><a href="${Context.baseURL}/nuxeo/nxpath/default/default-domain/sites/${site.title}/tree@view_documents?tabIds=%3A" target="_blank" >${Context.getMessage('command.LabsSite.goToBackOffice')}</a></li>
              </#if>
                <li class="divider"></li>
                <li><a id="logout" href="#">Déconnexion</a></li>
              </ul>
            </li>
            </#if>
          </ul>
        </div>
      </div><!-- /fill -->
    </div><!-- /topbar -->
    </div><!-- /topbar-wrapper -->
    &nbsp;