<#assign detailedPageUrl = This.previous.path + "/@views/latestuploads?page=0" />
<#assign pageProvider = latestUploadsPageProvider(Document, 5) />
<#assign pagesListTile = Context.getMessage('title.LabsSite.latestuploads') />
<#assign pagesListClass = "sidebarzone" />

<#include "views/common/pageslist.ftl" />