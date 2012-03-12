			<div class="editblock" style="width: 772px;">
				<div id="editprops" style="display: none;">
					  <#if news != null>
					  	<div>
  					  	  <h4>Prévisualisation du résumé</h4>
						  <section class="labsnews well">
						  	<div class="row-fluid" id="summaryNews${news.documentModel.ref}">
			          			<@generateSummaryNews news=news path=This.path withHref=false />
				          		<#-- Collapse -->
				          		<div class="span1" style="margin-left: 15px;float: right;">
				          			<img src="${skinPath}/images/newsOpen.png" style="margin-top:5px;"/>
				          		</div>
				          	</div>
				          </section>
				        </div>
				       </#if>
				  	<#--<div style="width: 100%;text-align: right;">
						<a id="btnModifyPropsNews" class="btn" style="cursor: pointer;margin-right: 5px;" onclick="javascript:actionPropsNews();">Modifier les propriétés</a>
					</div>-->
					<div class="well" style="width: 94%;">
						<#--<h1>Editer les information de l'actualité</h1>-->
						<form class="form-horizontal" id="form-editNews" method="post" action="${This.path}" class="well" enctype="multipart/form-data" >
						  <fieldset>
						    <legend>Propriétés de l'actualité</legend>
						    <#--Titre de la news-->
						    <div class="control-group">
						      <label class="control-label" for="dc:title">${Context.getMessage('label.labsNews.edit.title')}</label>
						      <div class="controls">
						        <input type="text" required-error-text="Le titre est obligatoire !" class="focused required span7" name="dc:title"  value="<#if news?? && news != null >${news.title?html}</#if>" />
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
						        <textarea class="span7" style="height:60px;" id="newsAccroche" name="newsAccroche"><#if news?? && news != null >${news.accroche}</#if></textarea>
						      </div>
						    </div>
						    <#--Photo de la news-->
						    <a id="btnSetSummaryPicture" style="cursor: pointer;" onclick="javascript:openDownloadPicture();">Associer une image au résumé</a>
						    <#if news?? && news != null && news.hasSummaryPicture() >
							    <div id="div-editPicture" style="float: right;">
							    	<#--Suppression de la photo -->
							    	<img src="${This.path}/summaryPictureTruncated" style="width:20ps;height: 20px;"/>
							    	<span onclick="javascript:deleteSummaryPicture('Voulez-vous supprimer l\'image?');" style="cursor: pointer;">
								    	<img title="${Context.getMessage('label.delete')}" src="${skinPath}/images/x.gif"/>
								  	</span>
								    <#--Editeur de la photo -->
								    <a style="cursor: pointer;" onclick="javascript:openCropPicture();">Recadrer l'image du résumé</a>
								    <input type="hidden" name="cropSummaryPicture" id="cropSummaryPicture" value="${news.cropCoords?html?js_string}" />
								    <input type="hidden" id="cropSummaryPictureOrigin" value="${news.cropCoords?html?js_string}" />
								</div>
							</#if>
						    <div class="actions" style="margin-left: 200px;">
						      <button class="btn required-fields" form-id="form-editNews"><i class='icon-ok'></i>${Context.getMessage('label.labsNews.edit.save')}</button>
						      <a class="btn" id="btnCloseProps" onclick="javascript:closePropsNews();"><i class='icon-eye-close'></i>Fermer</a>
						  	</div>
						  </fieldset>
						</form>
						<form id="formDeleteSummaryPicture" method="post" action="${This.path}/deleteSummaryPicture"></form>
					</div>
				</div>
			</div>
		      	<script type="text/javascript">
				  $(document).ready(function() {				  		
				  	  initEditDateNews();
				  	  if (location.search.indexOf("props=open") > -1){
				  	  	actionPropsNews();
				  	  }
				  	  <#if !(news?? && news != null)>
				  	  	actionPropsNews();
				  	  	jQuery("#form-editNews").clearForm();
				  	  	jQuery("#btnModifyPropsNews").remove();
				  	  	jQuery("#btnCloseProps").remove();
				  	  	jQuery("#btnSetSummaryPicture").remove();
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
			    
			  	<#if news?? && news != null >
				  	<div id="divDownloadPicture" style="display: none;">
				  		<h1>
				  			Télécharger une image
				  		</h1>
				  		<form class="form-horizontal" id="form-downloadPicture" enctype="multipart/form-data" onSubmit="javascript:return false;" >
						  <fieldset>
						    <div id="divPictureNews" class="control-group">
						      <label class="control-label" for="newspicture">${Context.getMessage('label.labsNews.edit.picture')}</label>
						      <div class="controls">
						        <input type="file" class="input-xlarge input-file required" name="newsPicture" id="newsPicture"/>
						      </div>
						    </div>
						    <div class="actions">
						      <input type="submit" class="btn required-fields" form-id="form-downloadPicture" onClick="javascript:savePicture();" value="${Context.getMessage('label.labsNews.edit.save')}" />
						  	</div>
						  </fieldset>
						</form>  	
				    </div>
			    </#if>