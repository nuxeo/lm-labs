<div class="titleNews">
	${Context.getMessage('label.labsNews.add.news')}
</div>
<form method="post" action="${This.pathForEdit}/persistNews">
	<fieldset>	
		<p>
        	<label for="newsTitle">${Context.getMessage('label.labsNews.edit.title')}</label>&nbsp;:&nbsp;
            <input type="text" name="newsTitle" id="newsTitle" value="<#if This.labsNews != null >${This.labsNews.title}</#if>" />
        </p>
        <p>
        	<label for="newsPeriod">${Context.getMessage('label.labsNews.edit.period')}</label>&nbsp;:&nbsp;
            ${Context.getMessage('label.labsNews.edit.du')}
            <input type="text" size="8" id="newsStartPublication" class="date-pick formfield" name="newsStartPublication" <#if This.labsNews!=null && This.labsNews.startPublication!=null> value="${This.labsNews.startPublication.time?string('dd/MM/yyyy')}" </#if>/>
           	${Context.getMessage('label.labsNews.edit.au')} 
           	<input type="text" size="8" id="newsEndPublication" class="date-pick formfield" name="newsEndPublication" <#if This.labsNews!=null && This.labsNews.endPublication!=null> value="${This.labsNews.endPublication.time?string('dd/MM/yyyy')}" </#if>/>
        </p>
        <p>
        	<label class="labelArea" for="newsContent">${Context.getMessage('label.labsNews.edit.content')}&nbsp;:&nbsp;</label>
            <textarea name="newsContent" id="newsContent" class="contentText"><#if This.labsNews != null >${This.labsNews.content}</#if></textarea>
        </p>
	</fieldset>
	<div class="newsOK">
		<button id="FKNews" title="Persister news">${Context.getMessage('label.labsNews.edit.valid')}</button>
	</div>
	<input type="hidden" name="newsId" id="newsId" value="<#if This.labsNews == null >-1<#else>${This.labsNews.documentModel.id}</#if>" />
</form>
