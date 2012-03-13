<#assign page = This.page />
<h1>
    <#if !page.isDisplayable(This.DC_DESCRIPTION) && (page.description?length > 0)>
        <span id="pageHeaderTitle" style="cursor: pointer;" rel="popover" data-content="${page.description?html}" data-original-title="${page.title}">${page.title}</span>
    <#else>
        ${page.title}
    </#if>
</h1>
