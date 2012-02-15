<#assign detailedPageUrl = Root.getLink(site.document) + "/@views/latestuploads?page=0" />
<#assign pageProvider = latestUploadsPageProvider(Document, 5) />
<#assign pagesListTile = Context.getMessage('title.LabsSite.latestuploads') />
<#assign pagesListId = "latestuploads" />
<#assign pagesListTooltip = "dc:description" />

<#include "views/common/pageslist.ftl" />