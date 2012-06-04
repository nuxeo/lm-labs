<#assign detailedPageUrl = Root.getLink(Common.siteDoc(Document).getSite().document) + "/@views/latestuploads?page=0" />
<#assign pageProvider = latestUploadsPageProvider(Document, 5, Context.getCoreSession()) />
<#assign pagesListTile = Context.getMessage('title.LabsSite.latestuploads') />
<#assign pagesListId = "latestuploads" />
<#assign pagesListTooltip = "dc:description" />

<#include "macros/pageProviderList.ftl" />
<@pageProviderList pageProvider=pageProvider
    divId=pagesListId
    divTitle=pagesListTile
    tooltipDocProp=pagesListTooltip
    detailedPageUrl=detailedPageUrl
    divClass="last-uploads supplychain-unstyled-bloc" />