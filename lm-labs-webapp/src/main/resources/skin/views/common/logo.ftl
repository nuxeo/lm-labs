<#assign logoWidth = site.themeManager.theme.logoWidth />
<#if logoWidth &gt; 0>
<img style="position:absolute;left:${site.themeManager.theme.logoPosX}px;top:${site.themeManager.theme.logoPosY}px;z-index:9999;width:${logoWidth}px" 
src="${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@theme/${site.themeManager.theme.name}/logo" id="logoImgId"/>
</#if>
