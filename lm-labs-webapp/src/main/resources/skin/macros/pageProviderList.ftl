<#macro pageProviderList pageProvider divId divTitle tooltipDocProp detailedPageUrl divClass="" >
<div class="bloc ${divClass}" id="${divId}" >
  <div class="header">${divTitle}</div>
  <ul class="unstyled">
  <#list pageProvider.setCurrentPage(0) as doc >
    <li>
      <#assign parentPagePath = Common.siteDoc(doc).getParentPagePath() />
      <a href="${Context.modulePath}/${parentPagePath}" title="/${parentPagePath}<#if doc[tooltipDocProp] &gt; 0> - ${doc[tooltipDocProp]}</#if>" >
        <i style="font-size: 9px; text-decoration: none;" class="icon-chevron-right" ></i>${doc.title}
      </a>
    </li>
  </#list>
  </ul>
  <a class="btn btn-mini" href="${detailedPageUrl}"><i class="icon-list"></i>Détails</a>
</div>
</#macro>
