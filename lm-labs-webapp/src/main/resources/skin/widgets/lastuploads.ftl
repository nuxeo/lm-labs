<#include "macros/pageProviderList.ftl" />
<@pageProviderList pageProvider=latestUploadsPageProvider(Document, 5, Context.getCoreSession())
    divId="latestuploads" + "_" + section_index + "_r_" + row_index + "_c_" + content_index
    divTitle=Context.getMessage('title.LabsSite.latestuploads')
    tooltipDocProp="dc:description"
    detailedPageUrl=Root.getLink(Common.siteDoc(Document).site.document) + "/@views/latestuploads?page=0"
    divClass="last-uploads supplychain-unstyled-bloc" />