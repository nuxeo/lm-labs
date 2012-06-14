<#assign mySite=Common.siteDoc(Document).getSite() />
<#if mySite.themeManager.getTheme(Context.coreSession).getBanner() != null >	
	<div id="labssite-banner" style="text-align: center">
	    <img src="${Context.modulePath}/${mySite.URL}/@theme/${mySite.themeManager.getTheme(Context.coreSession).name}/banner" id="bannerImgId" />
	</div>
</#if>
