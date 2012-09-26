<div class="titleNews">
	${Context.getMessage('label.labsNews.edit.edit')}
</div>
<form class="form-horizontal" method="post" name="form-news" id="form-news" action="${This.pathForEdit}/persistNews/edit">
	<fieldset>	
		<div class="control-group">
        	<label class="control-label" for="newsTitle">${Context.getMessage('label.labsNews.edit.title')}</label>&nbsp;:&nbsp;
            <div class="controls">
            <input class="focused required input" type="text" name="newsTitle" id="newsTitle" value="<#if This.labsNews != null >${This.labsNews.title}</#if>" />
            </div>
        </div>
        <div class="control-group">
        	<label class="control-label" for="newsPeriod">${Context.getMessage('label.labsNews.edit.period')}</label>
        	<div class="controls">
            ${Context.getMessage('label.labsNews.edit.du')}
            <input type="text" size="8" id="newsStartPublication" class="input date-pick formfield" name="newsStartPublication" <#if This.labsNews!=null && This.labsNews.startPublication!=null> value="${This.labsNews.startPublication.time?string('dd/MM/yyyy')}" </#if>/>
           	${Context.getMessage('label.labsNews.edit.au')} 
           	<input type="text" size="8" id="newsEndPublication" class="input date-pick formfield" name="newsEndPublication" <#if This.labsNews!=null && This.labsNews.endPublication!=null> value="${This.labsNews.endPublication.time?string('dd/MM/yyyy')}" </#if>/>
           	</div>
        </div>
        <div class="control-group">
            <textarea name="newsContent" id="newsContent" class="contentText"><#if This.labsNews != null >${This.labsNews.content}</#if></textarea>
        </div>
	</fieldset>
	<div class="newsOK form-actions">
		<button id="FKNews" onClick="javascript:submitForm();" title="Persister news">${Context.getMessage('label.labsNews.edit.valid')}</button>
	</div>
	<input type="hidden" name="newsId" id="newsId" value="<#if This.labsNews == null >-1<#else>${This.labsNews.documentModel.id}</#if>" />
</form>
<script type="text/javascript">

	initCheckeditor();
	
	function submitForm(){
		$("newsContent").val(CKEDITOR.instances.newsContent.getData());
		$("#form-news").submit();
	}

	</script>
