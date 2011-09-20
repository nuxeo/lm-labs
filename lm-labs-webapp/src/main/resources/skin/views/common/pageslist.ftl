<div class="bloc" >
  <div class="header">${pagesListTile}</div>
  <ul>
  <#list pageProvider.setCurrentPage(0) as page >
    <li>
      <a href="labssites/${This.siteUrlProp}${pageEndUrl(page)}" target="_blank" title="${page.title}" >${page.title}</a>
    </li>
  </#list>
  </ul>
  <a href="${detailedPageUrl}">Détails</a>
</div>

