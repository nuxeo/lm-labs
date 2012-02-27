				<div class="editblock">
				  	<div style="width: 100%;text-align: right;">
						<a id="btnModifyPropsNews" style="cursor: pointer;margin-right: 5px;" onclick="javascript:actionPropsNews();">Ouvrir les propriétés</a>
					</div>
					<div id="editprops" class="well" style="display: none; margin-left: 100px;margin-right: 100px;">
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
						    <a style="cursor: pointer;" onclick="javascript:jQuery('#divPictureNews').slideDown('slow');">Affecter la photo du résumé</a>
						    <#--Photo de la news-->
						    <div id="divPictureNews" class="control-group" style="display: none;">
						      <label class="control-label" for="newspicture">${Context.getMessage('label.labsNews.edit.picture')}</label>
						      <div class="controls">
						        <input type="file" class="input-xlarge input-file" name="newsPicture"/>
						      </div>
						    </div>
						    <#if news?? && news != null && news.hasSummaryPicture() >
							    <#--Editeur de la photo -->
							    <div id="div-editPicture">
								    <a style="cursor: pointer;" onclick="javascript:openCropPicture();">Découper la photo du résumé</a>
								    <input type="hidden" name="cropSummaryPicture" id="cropSummaryPicture" value="${news.cropCoords?html?js_string}" />
								    <input type="hidden" id="cropSummaryPictureOrigin" value="${news.cropCoords?html?js_string}" />
								</div>
							</#if>
						    <div class="actions" style="text-align: center;">
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
				  	  	jQuery("#div-editPicture").remove();
				  	  </#if>
				  });
			  	</script>
			  	<#if news?? && news != null && news.hasSummaryPicture() >
				  	<div id="divCropPicture" class="jcrop" style="display: none;vertical-align: middle">
				  		<h1>
				  			Découper une image
				  		</h1>
				  		<img id="summaryPicture" src="${This.path}/summaryPicture"/>
				  		<table cellpadding="5" cellspacing="0" border="0">
				  			<tbody>
				  				<trcellspacing="0">
				  					<td>
				  						<div style="width:120px;height:90px;overflow:hidden;">
				      						<img  src="${This.path}/summaryPictureTruncated"/>
				      					</div>
				  					</td>
				  					<td>
				  						<div style="width:120px;height:90px;overflow:hidden;">
				  							<img id="summaryPicturePreview" src="${This.path}/summaryPicture"/>
				  						</div>
				  					</td>
				  				</tr>
				  			</tbody>
				  		</table>		  	
				    </div>
			    </#if>