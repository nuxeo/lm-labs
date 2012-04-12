<#macro fileDownloadLink url inBtnGroup=false tooltip="" >
    <#if inBtnGroup>
        <a rel="nofollow" class="classeurDownload" href="${url}" ><i class="icon-download"></i>${tooltip}</a>
    <#else>
        <a rel="nofollow" class="btn btn-primary btn-mini classeurDownload" href="${url}" title="${tooltip}" ><i class="icon-download"></i></a>
    </#if>
</#macro>
<#macro fileDisplayLink url inBtnGroup=false tooltip="" >
    <#if inBtnGroup>
        <a rel="nofollow" class="classeurDisplay" href="${url}" target="_blank" ><i class="icon-eye-open"></i>${tooltip}</a>
    <#else>
        <a rel="nofollow" class="btn btn-mini classeurDisplay" href="${url}" target="_blank" title="${tooltip}" ><i class="icon-eye-open"></i></a>
    </#if>
</#macro>
