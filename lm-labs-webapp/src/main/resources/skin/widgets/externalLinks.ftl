<#include "macros/externalLinksList.ftl"/>
<#assign id = "${widgetMode}_external-links" + "_" + section_index + "_r_" + row_index + "_c_" + content_index />
<div class="bloc external-links" >
    <div class="header">${Context.getMessage('label.externalURL.title')}</div>
    <div class="loading-image" style="display: none;" ><img src="${skinPath}/images/loading.gif" /></div>
    <ul class="unstyled">
    	<@generateExternalLinksList />
    </ul>
</div>
