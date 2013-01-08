<#assign mySite=Common.siteDoc(Document).site />
<#assign logoWidth = mySite.themeManager.getTheme(Context.coreSession).logoWidth />
<#assign logoBlob = mySite.themeManager.getTheme(Context.coreSession).logo />
<input id="valuePropertyLogo" name="valuePropertyLogo" type="hidden" value="" />
<a href="#" onclick="javascript:openAssets('${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@assets?callFunction=setLogoInput&calledRef=0')">Associer un média</a>
<span id="spanTextAssetLogo">&nbsp;</span>
<div id="actionMediaLogo" style="float: right;">
	<img class="actionMediaImage" src="${Context.modulePath}/${mySite.URL}/@theme/${mySite.themeManager.getTheme(Context.coreSession).name}/logo" style="width: 40px;border:1px dashed black;<#if (logoBlob = null)> display:none;</#if>"/>
	<span onclick="javascript:deleteElement('${This.path}/logo', 'hideConfigLogo()', '${Context.getMessage('label.labssites.appearance.theme.edit.element.delete.confirm')}');" style="cursor: pointer;">
    	<img title="${Context.getMessage('label.labssites.appearance.theme.edit.banner.delete')}" src="${skinPath}/images/x.gif"<#if (logoBlob == null)> style="display: none;"</#if> />
  	</span>
</div>
