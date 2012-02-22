				<div class="editblock">
				  	<div style="width: 100%;text-align: right;">
						<a id="btnModifyPropsNews" style="cursor: pointer;" onclick="javascript:actionPropsNews();">Ouvrir les propriétés</a>
					</div>
					<div id="editprops" style="display: none; margin-left: 100px;margin-right: 100px;">
						<#--<h1>Editer les information de l'actualité</h1>-->
						<form class="form-horizontal" id="form-editNews" method="post" action="${This.path}" class="well" enctype="multipart/form-data" >
						  <fieldset>
						    <legend>Propriétés de l'actualité</legend>
						    <#--Titre de la news-->
						    <div class="control-group">
						      <label class="control-label" for="dc:title">${Context.getMessage('label.labsNews.edit.title')}</label>
						      <div class="controls">
						        <input type="text" required-error-text="Le titre est obligatoire !" class="focused required input-xlarge" name="dc:title"  value="<#if news?? && news != null >${news.title?html}</#if>" />
						      </div>
						    </div>
							<#--Périodes de publication de la news-->
						    <div class="control-group">
						      <label class="control-label" for="newsPeriod">${Context.getMessage('label.labsNews.edit.period')}</label>
						      <div class="controls">
						        <input id="newsStartPublication" required-error-text="La date de début est obligatoire pour la période !" class="date-pick input-small required" name="newsStartPublication"  <#if news?? && news != null && news.startPublication!=null> value="${news.startPublication.time?string('dd/MM/yyyy')}" </#if>" />
						        ${Context.getMessage('label.labsNews.edit.au')}
						        <input id="newsEndPublication" class="date-pick input-small" name="newsEndPublication"  <#if news?? && news != null && news.endPublication!=null> value="${news.endPublication.time?string('dd/MM/yyyy')}" </#if>" />
						      </div>
						    </div>
						    <#--Accroche de la news-->
						    <div class="control-group">
						      <label class="control-label" for="newsAccroche">${Context.getMessage('label.labsNews.edit.accroche')}</label>
						      <div class="controls">
						        <textarea class="input-xlarge" style="height:60px;" name="newsAccroche"><#if news?? && news != null >${news.accroche}</#if></textarea>
						      </div>
						    </div>
						    <a style="cursor: pointer;" onclick="javascript:jQuery('#divPictureNews').slideDown('slow');">Ajouter une photo</a>
						    <#--Photo de la news-->
						    <div id="divPictureNews" class="control-group" style="display: none;">
						      <label class="control-label" for="newspicture">${Context.getMessage('label.labsNews.edit.picture')}</label>
						      <div class="controls">
						        <input type="file" class="input-xlarge input-file" name="newsPicture"/>
						      </div>
						    </div>
						
						    <div class="actions" style="margin-left: 10%;">
						      <input type="submit" class="btn required-fields" form-id="form-editNews" value="${Context.getMessage('label.labsNews.edit.valid')}" />
						      <a class="btn" id="btnCloseProps" onclick="javascript:closePropsNews();">Fermer</a>
						  	</div>
						  </fieldset>
						</form>
					</div>
				</div>
		      	<script type="text/javascript">
				  $(document).ready(function() {				  		
				  	  initEditDateNews();
				  	  <#if !(news?? && news != null)>
				  	  	actionPropsNews();
				  	  	jQuery("#form-editNews").clearForm();
				  	  	jQuery("#btnModifyPropsNews").remove();
				  	  	jQuery("#btnCloseProps").remove();
				  	  </#if>
				  });
			  	</script>