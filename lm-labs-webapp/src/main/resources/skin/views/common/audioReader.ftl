<#if Context.request.getParameter('ababa') = 'wazaa'>
	<#if Common.siteDoc(Document).getSite().getThemeManager().getTheme(Context.coreSession).getName() = 'Spruce'>
		<object type="application/x-shockwave-flash" data="${skinPath}/audioReader/playerMP3.swf" width="270" height="60">
			<param name="quality" value="high" />
			<param name="movie" value="playerMP3.swf" />
			<param name="flashvars" value="mp3=${skinPath}/audioReader/HistoireNaturelle.mp3&texte_color=ffffff&autoplay=true" />
			<param name="wmode" value="transparent" />
		</object>
	</#if>
</#if>
