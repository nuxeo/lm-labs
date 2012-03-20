<#assign mySite=Common.siteDoc(Document).site />
<#if mySite.themeManager.theme.getBanner() != null >	
	<div id="labssite-banner" style="text-align: center">
	    <img src="${Context.modulePath}/${mySite.URL}/@theme/${mySite.themeManager.theme.name}/banner" id="bannerImgId" />
	</div>
</#if>
