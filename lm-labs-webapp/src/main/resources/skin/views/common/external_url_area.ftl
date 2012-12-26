<#include "macros/externalLinksList.ftl"/>
<#assign mySite=Common.siteDoc(Document).getSite() />
<div class="bloc external-links" >
    <div class="header">${Context.getMessage('label.externalURL.title')}</div>
    <ul class="unstyled">
    	<@generateExternalLinksList />
    </ul>
	  <#if mySite.isContributor(Context.principal.name)>
	    <div class="editblock">
	    	<a href="#" class="addExternalURLBtn" title="Ajouter" alt="Ajouter"><i class="icon-plus"></i></a>
	    </div>
	  </#if>
</div>
