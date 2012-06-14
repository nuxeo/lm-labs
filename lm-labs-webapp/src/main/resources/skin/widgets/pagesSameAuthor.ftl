<#include "macros/documentModelList.ftl" />
<#assign pagesSameAuthorDivId = "pagesSameAuthor" + "_" + section_index + "_r_" + row_index + "_c_" + content_index />
<@documentModelList documentModelList=Common.siteDoc(Document).site.getPagesOfCreator(Context.principal.name, Session)
    divId=pagesSameAuthorDivId
    divTitle=Context.getMessage('label.HtmlPage.widget.html.pagesSameAuthor')
    tooltipDocProp="dc:description"
    divClass="pagesSameAuthor supplychain-unstyled-bloc"
    showDate=true />