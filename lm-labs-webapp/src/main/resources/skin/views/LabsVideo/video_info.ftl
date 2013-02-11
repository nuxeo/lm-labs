<form class="form-horizontal">
    <fieldset>
		<div class="control-group">
	    	<label class="control-label">${Context.getMessage('label.video.duration')}</label>
	    	<div class="controls">
	    		<#assign hoursStr = "00:" />
	    		<#if 3600 < Document['vid:info/duration'] >
		    		<#assign hoursStr = (Document['vid:info/duration']?long/3600)?int + ":" />
	    		</#if>
	    		<#assign minutes = Document['vid:info/duration']?long%3600 />
		    	<span class="uneditable-input input-mini" >${hoursStr}${(minutes?long/60)?int?string("00")}:${(minutes?long%60)?int?string("00")}</span>
	    	</div>
    	</div>
		<div class="control-group">
	    	<label class="control-label">${Context.getMessage('label.video.width')}</label>
	    	<div class="controls">
		    	<div class="input-append">
			    	<span class="uneditable-input input-mini" >${Document['vid:info/width']}</span>
			    	<span class="add-on">px</span>
		    	</div>
	    	</div>
    	</div>
		<div class="control-group">
	    	<label class="control-label">${Context.getMessage('label.video.height')}</label>
	    	<div class="controls">
		    	<div class="input-append">
			    	<span class="uneditable-input input-mini" >${Document['vid:info/height']}</span>
			    	<span class="add-on">px</span>
		    	</div>
	    	</div>
    	</div>
		<div class="control-group">
	    	<label class="control-label">${Context.getMessage('label.video.format')}</label>
	    	<div class="controls">
		    	<span class="uneditable-input input-small" title="${Document['vid:info/format']}" >${Document['vid:info/format']}</span>
	    	</div>
    	</div>
		<div class="control-group">
	    	<label class="control-label">${Context.getMessage('label.video.frameRate')}</label>
	    	<div class="controls">
		    	<div class="input-append">
			    	<span class="uneditable-input input-mini" >${Document['vid:info/frameRate']}</span>
			    	<span class="add-on">fps</span>
		    	</div>
	    	</div>
    	</div>
    	<#--
    	<#list Document['vid:info/streams'] as stream >
		<div class="control-group">
	    	<label class="control-label">${Context.getMessage('label.video.streams')}</label>
	    	<div class="controls">
		    	<span class="uneditable-input input-xxlarge" >${stream['streamInfo']}</span>
	    	</div>
    	</div>
    	</#list>
    	-->
    </fieldset>
</form>
