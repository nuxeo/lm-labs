<#assign videoConversions = This.activeAdapter.availableVideoConversions />
<#if 0 < videoConversions?size >
<script>
jQuery('table.conversions-table').ready(function() {
<#list videoConversions as conversion >
	<#if This.activeAdapter.getTranscodedVideo(Document, conversion.name) == null>
jQuery('[name=conversion_${conversion_index}]').ajaxForm();
jQuery('[name=conversion_${conversion_index}]').submit(function() {
	jQuery(this).ajaxSubmit();
	jQuery('btn.refresh-conversions-table').click();
});
	</#if>
</#list>
});
</script>
<table class="table table-striped table-bordered table-condensed conversions-table" >
	<thead>
		<tr>
			<th style="width:250px;" >${Context.getMessage('label.video.format')}</th>
			<th style="width:250px;" >Statut</th>
		</tr>
	</thead>
	<tbody>
<#list videoConversions as conversion >
	<#assign conversionStatus = This.activeAdapter.getVideoConversionStatus(Document, conversion.name) />
		<tr<#if conversionStatus != null > class="warning"</#if>>
			<td>${conversion.name}</td>
			<td>
	<#if conversionStatus != null>
		<#assign conversionStatusMsg = This.activeAdapter.getVideoConversionStatusMessage(conversionStatus) />
		<#if conversionStatusMsg?contains("conversionQueued") >
		<span>${Context.getMessage(conversionStatusMsg, conversionStatus.positionInQueue, conversionStatus.queueSize)}</span>
		<#else>
		<span>${Context.getMessage(conversionStatusMsg)}</span>
		</#if>
	<#else>
		<#assign transcodedVideo = This.activeAdapter.getTranscodedVideo(Document, conversion.name) />
		<#if transcodedVideo != null>
			<a class="btn btn-link btn-small" rel="nofollow" href="/nuxeo/${This.activeAdapter.getTranscodedVideoURL(Document, conversion.name)}"><i class="icon-download-alt"></i>${Context.getMessage('command.video.download')}</a>
		<#else>
			<form style="display: none;" name="conversion_${conversion_index}" action="${This.activeAdapter.path}/convert" method="post">
				<input type="text" value="${conversion.name}" name="conversion" ></input>
			</form>
			<btn class="btn btn-small" onclick="jQuery('[name=conversion_${conversion_index}]').submit();return false;" >${Context.getMessage('command.video.launch.conversion')}</btn>
		</#if>
	</#if>
			</td>
		</tr>
</#list>
	</tbody>
</table>
<btn class="btn refresh-conversions-table" onclick="jQuery(this).closest('div').load('${This.activeAdapter.path}/@views/conversions');return false;" ><i class="icon-refresh"></i>${Context.getMessage('command.video.conversions.table.refresh')}</btn>
</#if>
