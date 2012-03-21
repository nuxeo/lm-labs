<div class="bloc" id="${pagesListId}" >
  <div class="header">${pagesListTile}</div>
  <ul class="unstyled">
  <#list pageProvider.setCurrentPage(0) as doc >
    <li>
      <#assign sd = Common.siteDoc(doc) />
      <a href="${Context.modulePath}/${sd.parentPagePath}" title="/${sd.parentPagePath}<#if doc[pagesListTooltip] &gt; 0> - ${doc[pagesListTooltip]}</#if>" >${doc.title}</a>
    </li>
  </#list>
  </ul>
  <a class="btn btn-mini" href="${detailedPageUrl}"><i class="icon-list"></i>Détails</a>
</div>

