<div class="bloc" id="${pagesListId}" >
  <div class="header">${pagesListTile}</div>
  <ul class="unstyled">
  <#list pageProvider.setCurrentPage(0) as doc >
    <li>
      <#assign sd = Common.siteDoc(doc) />
      <a href="${Context.modulePath}/${sd.parentPagePath}" title="${doc.title}" >${doc.title}</a>
    </li>
  </#list>
  </ul>
  <a href="${detailedPageUrl}">Détails</a>
</div>

