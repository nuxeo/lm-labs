<#assign mySite=Common.siteDoc(Document).site />
<#assign bannerBlob = mySite.themeManager.getTheme(Context.coreSession).banner />
<#assign webadapter = "sharedElementBrowser" />
<#assign webadapter = "assets" />
<input id="valuePropertyBanner" name="valuePropertyBanner" type="hidden" value="" />
<a href="#" onclick="javascript:openAssets('${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@${webadapter}?callFunction=setBannerInput&calledRef=0')">Associer un média</a>
<span id="spanTextAssetBanner">&nbsp;</span>
<div id="actionMediaBanner" style="float: right;">
	<img class="actionMediaImage" src="${Context.modulePath}/${mySite.URL}/@theme/${mySite.themeManager.getTheme(Context.coreSession).name}/banner" style="width: 120px;border:1px dashed black;<#if (bannerBlob = null)> display:none;</#if>"/>
	<span onclick="javascript:deleteElement('${This.path}/banner', 'hideConfigBanner()', '${Context.getMessage('label.labssites.appearance.theme.edit.element.delete.confirm')}');" style="cursor: pointer;">
    	<img title="${Context.getMessage('label.labssites.appearance.theme.edit.banner.delete')}" src="${skinPath}/images/x.gif"<#if (bannerBlob == null)> style="display: none;"</#if> />
  	</span>
</div>
