<div class="well" style="width: 730px;margin-left: auto;margin-right: auto;">
		<#--<h1>Editer les information du topic</h1>-->
		<form class="form-horizontal" id="form-editTopic" method="post" action="${This.path}" class="well" enctype="multipart/form-data" >
		  <fieldset>
		    <legend>Propriétés du topic</legend>
		    <#--Titre du topic-->
		    <div class="control-group">
		      <label class="control-label" for="dc:title">${Context.getMessage('label.labsNews.edit.title')}</label>
		      <div class="controls">
		        <input type="text" required-error-text="Le titre est obligatoire !" class="focused required span7" name="dc:title"  value="<#if topic?? && topic != null >${topic.title?html}</#if>" />
		      </div>
		    </div>
			<#--Description du news-->
		    <div class="control-group">
		      <label class="control-label" for="topicDescription">Description du topic</label>
		      <div class="controls">
		        <textarea class="span7" style="height:60px;" id="dc:description" name="dc:description"><#if topic?? && topic != null >${topic.description}</#if></textarea>
		      </div>
		    </div>
		    <div class="actions" style="margin-left: 200px;">
		      <button class="btn required-fields" form-id="form-editTopic"><i class='icon-ok'></i>${Context.getMessage('label.labsNews.edit.save')}</button>
		      <a class="btn" id="btnCloseProps" onclick="javascript:closePropsNews();"><i class='icon-eye-close'></i>Fermer</a>
		  	</div>
		  </fieldset>
		</form>
	</div>
</div>
<script type="text/javascript">
  $(document).ready(function() {				  		
  	  if (location.search.indexOf("props=open") > -1){
  	  	actionPropsNews();
  	  }
  	  else{
  	  	closePropsNews();
  	  }
  	  <#if !(news?? && news != null)>
  	  	jQuery("#form-editTopic").clearForm();
  	  	jQuery("#btnModifyPropsNews").remove();
  	  	jQuery("#btnCloseProps").remove();
  	  	jQuery("#btnSetSummaryPicture").remove();
  	  </#if>
  });
</script>