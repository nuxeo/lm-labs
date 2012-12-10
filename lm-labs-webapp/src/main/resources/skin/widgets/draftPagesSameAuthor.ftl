<#include "macros/documentModelList.ftl" />
<#assign pagesSameAuthorDivId = "draftPagesSameAuthor" + "_" + section_index + "_r_" + row_index + "_c_" + content_index />
<#assign params = "/default-domain/sites/" + Common.siteDoc(Document).site.document.name+","+Document['dc:creator'] >
<@documentModelList documentModelList=Common.getPageProviderDocs(Session, "draft_pages_same_author", params, 5)
    divId=pagesSameAuthorDivId
    divTitle=Context.getMessage('label.HtmlPage.widget.html.draftPagesSameAuthor')
    tooltipDocProp="dc:modified"
    divClass="draftPagesSameAuthor supplychain-unstyled-bloc"
    showDate=true  />