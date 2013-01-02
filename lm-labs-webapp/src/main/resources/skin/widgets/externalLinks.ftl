<#include "macros/externalLinksList.ftl"/>
<div class="bloc external-links" >
    <div class="header">${Context.getMessage('label.externalURL.title')}</div>
    <div class="loading-image" style="display: none;" ><img src="${skinPath}/images/loading.gif" /></div>
    <ul class="unstyled">
    	<@generateExternalLinksList />
    </ul>
</div>
