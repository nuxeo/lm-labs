<legend>${Context.getMessage('heading.video.info')}</legend>
<div class="row">
	<div class="span5">
	<#include "views/LabsVideo/video_info.ftl" >
	</div>
	<div class="span5 conversions">
	<#include "views/LabsVideo/conversions.ftl" >
	</div>
</div>
<legend>${Context.getMessage('heading.video.storyboard')}</legend>
<#include "views/LabsVideo/storyboard.ftl" >
