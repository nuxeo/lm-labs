<script type="text/javascript" >
jQuery(document).ready(function() {
	jQuery('a.open-fancybox').fancybox(
	{
        'width'				: '75%',
<#--
        'autoScale'			: true,
        'height'			: '75%',
        'transitionIn'		: 'none',
        'transitionOut'		: 'none',
        'type'				: 'iframe',
        'enableEscapeButton': true,
-->
        'centerOnScroll': true
	}
	);
});
</script>
<#assign mp4URL = This.activeAdapter.getTranscodedVideoURL(Document, 'MP4 480p') />
<#assign webmURL = This.activeAdapter.getTranscodedVideoURL(Document, 'WebM 480p') />
<#assign safariHTML5 = This.activeAdapter.safariHTML5 />
<#assign chromeHTML5 = This.activeAdapter.chromeHTML5 />
<#assign firefoxHTML5 = This.activeAdapter.firefoxHTML5 />
<#assign videoAvailable = false />

<#if (mp4URL != null && safariHTML5) || (webmURL != null && (chromeHTML5 || firefoxHTML5) ) >
	<#assign videoAvailable = true />
<#elseif mp4URL != null >
	<#assign videoAvailable = true />
</#if>
	
<#if videoAvailable >
	<a class="btn btn-mini open-fancybox" href="${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@assets/id/${Document.id}/@labsvideo/@views/player" title="${Context.getMessage('heading.video.player')}" ><i class="icon-play"></i></a>
<#else>
	<#assign i18nMsg = "" />
	<#assign videoConversions = This.activeAdapter.availableVideoConversions />
	<#if 0 < videoConversions?size >
		<#list videoConversions as conversion >
			<#assign conversionStatus = This.activeAdapter.getVideoConversionStatus(Document, conversion.name) />
			<#if conversionStatus != null>
				<#assign conversionStatusMsg = This.activeAdapter.getVideoConversionStatusMessage(conversionStatus) />
				<#if conversionStatusMsg?contains("conversionQueued") >
					<#assign i18nMsg = Context.getMessage(conversionStatusMsg, conversionStatus.positionInQueue, conversionStatus.queueSize) />
				<#else>
					<#assign i18nMsg = Context.getMessage(conversionStatusMsg) />
				</#if>
				<#break>
			<#else>
				<#assign transcodedVideo = This.activeAdapter.getTranscodedVideo(Document, conversion.name) />
				<#if transcodedVideo != null>
					<#--
					<a class="btn btn-link btn-small" rel="nofollow" href="/nuxeo/${This.activeAdapter.getTranscodedVideoURL(Document, conversion.name)}"><i class="icon-download-alt"></i>${Context.getMessage('command.video.download')}</a>
					-->
				<#else>
					<#--
					<form style="display: none;" name="conversion_${conversion_index}" action="${This.activeAdapter.path}/convert" method="post">
						<input type="text" value="${conversion.name}" name="conversion" ></input>
					</form>
					<btn class="btn btn-small" onclick="jQuery('[name=conversion_${conversion_index}]').submit();return false;" >${Context.getMessage('command.video.launch.conversion')}</btn>
					-->
				</#if>
			</#if>
		</#list>
	</#if>
	<#if i18nMsg != "" >
		<btn class="btn btn-mini" title="${i18nMsg}"><i class="icon-spinner"></i></btn>
	<#else>
		<btn class="btn btn-mini btn-warning" title="Pas de vidéo disponible pour votre navigateur(${mp4URL},${webmURL},${firefoxHTML5?string},${chromeHTML5?string},${safariHTML5?string})"><i class="icon-warning-sign"></i></btn>
	</#if>
</#if>
