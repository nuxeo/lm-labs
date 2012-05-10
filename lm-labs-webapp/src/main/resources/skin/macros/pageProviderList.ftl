<#macro pageProviderList pageProvider divId divTitle tooltipDocProp detailedPageUrl >
<div class="bloc" id="${divId}" >
  <div class="header">${divTitle}</div>
  <ul class="unstyled">
  <#list pageProvider.setCurrentPage(0) as doc >
    <li>
      <#assign sd = Common.siteDoc(doc) />
      <a href="${Context.modulePath}/${sd.parentPagePath}" title="/${sd.parentPagePath}<#if doc[tooltipDocProp] &gt; 0> - ${doc[tooltipDocProp]}</#if>" >${doc.title}</a>
    </li>
  </#list>
  </ul>
  <a class="btn btn-mini" href="${detailedPageUrl}"><i class="icon-list"></i>Détails</a>
</div>
</#macro>
