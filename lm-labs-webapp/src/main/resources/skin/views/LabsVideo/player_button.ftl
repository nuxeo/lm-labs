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
	<btn class="btn btn-mini" title="${Context.getMessage('status.video.conversionPending')}"><i class="icon-spinner"></i></btn>
</#if>
