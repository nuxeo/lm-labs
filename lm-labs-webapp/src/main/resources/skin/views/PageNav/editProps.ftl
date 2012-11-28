	      	<#assign listTags = pageNav.getTags()>
			<div class="editblock">
				<div id="editprops" style="display: none;">
					<div class="well" style="width: 730px;margin-left: auto;margin-right: auto;">
						<#--<h1>Editer les critères de la page de navigation</h1>-->
						<form class="form-horizontal" id="form-editPageNav" method="post" action="${This.path}" onSubmit="submitPageNavProps();return false"; class="well" >
						  <fieldset>
						    <legend>${Context.getMessage('label.pagenav.props.legend')}</legend>
						    <#--Critère de tags-->
						    <div class="control-group">
						      <label class="control-label" for="listIdTags">${Context.getMessage('label.pagenav.tags')}</label>
						      <div class="controls">
						      	<input type="hidden" id="listIdTags" name="listTags" style="width:85%" value="<#list listTags as tag>${tag?html}<#if (listTags?size > tag_index +1)>,</#if></#list>"/>
						      </div>
						    </div>
							<#--Filtre sur les dates de la page
						    <div class="control-group">
						      <label class="control-label" for="newsPeriod">${Context.getMessage('label.labsNews.edit.period')}</label>
						      <div class="controls">
						        <input type="text" id="newsStartPublication" required-error-text="La date de début est obligatoire pour la période !" class="date-pick required" name="newsStartPublication"  <#if news?? && news != null && news.startPublication!=null> value="${news.startPublication.time?string("dd/MM/yyyy 'à' HH:mm")}" </#if> />
						        &nbsp;${Context.getMessage('label.labsNews.edit.au')}&nbsp;
						        <input type="text" id="newsEndPublication" class="date-pick" name="newsEndPublication"  <#if news?? && news != null && news.endPublication!=null> value="${news.endPublication.time?string("dd/MM/yyyy 'à' HH:mm")}" </#if> />
						      </div>
						    </div>-->
						    
						    <#--User Query-->
						    
						    	<a  id="btnOpenUserQuery" style="cursor: pointer;<#if Context.principal.name != "Administrator">display: none;</#if>" onclick="javascript:openUserQuery();">Mode expert</a>
							    <div class="control-group" id="divUserQuery" style="display:none;">
							      <label class="control-label" for="userQuery">${Context.getMessage('label.pagenav.query')}</label>
							      <div class="controls">
							      	<textarea name="userQuery" id="userQuery" class="span10" style="height:100px;">${pageNav.userQuery}</textarea>
							      </div>
							    </div>
							

						    <div class="actions" style="margin-left: 200px;">
						      <button class="btn btn-primary required-fields" form-id="form-editPageNav"><i class='icon-ok'></i>${Context.getMessage('label.labsNews.edit.save')}</button>
						      <#-- a class="btn btn-danger" id="btnDeleteNews" onclick="javascript:if(confirm('${Context.getMessage('label.admin.labsnews.deleteConfirm')}')){deleteNews('${This.path}', '${This.previous.path}');};"><i class='icon-remove'></i>Supprimer l'actualité</a-->
						      <a class="btn" id="btnCloseProps" onclick="javascript:closePropsPageNav();"><i class='icon-eye-close'></i>Fermer</a>
						  	</div>
						  </fieldset>
						</form>
					</div>
				</div>
			</div>
			<script type="text/javascript">
			  var listTags = [<#list allSiteTags as tag>"${tag?js_string}"<#if (allSiteTags?size > tag_index +1)>,</#if></#list>];
			  
			  $(document).ready(function() {
			  	$("#listIdTags").select2({ 	
					placeholder: "Mots-clés",
					tags: listTags,
					formatNoMatches: function(trem){
							return '';
						}
				});
			  
			  			  		
			  	  <#-- initEditDateNews();
			  	  if (location.search.indexOf("props=open") > -1){
			  	  	actionPropsNews();
			  	  }
			  	  else{
			  	  	closePropsNews();
			  	  }
			  	  <#if !(news?? && news != null)>
			  	  	jQuery("#form-editNews").clearForm();
			  	  	jQuery("#btnModifyPropsNews").remove();
			  	  	jQuery("#btnCloseProps").remove();
			  	  	jQuery("#btnSetSummaryPicture").remove();
				  	  <#if This.page.commentable >
			  	  	jQuery('#commentablePage').attr('checked', 'checked');
				  	  </#if>
			  	  <#else>
				  	  <#if news.commentable >
			  	  	jQuery('#commentablePage').attr('checked', 'checked');
				  	  </#if>
				  	  <#if news.isTop() >
			  	  	jQuery('#isTop').attr('checked', 'checked');
				  	  </#if>
			  	  </#if>-->
			  });
			  
			  function submitPageNavProps(){
			  	jQuery.ajax({
					type: "POST",
					url: '${This.path}',
					/*data: "listTags=" + $("#listIdTags").select2("val"),*/
					data: $("#form-editPageNav").serialize(),
					success: function(msg){
						document.location.href = '${This.path}';
					},
					error: function(msg){
						alert('Mots clés non sauvegardés!');
						document.location.href = '${This.path}';
					}
				});
			  }
			  
			  function openUserQuery(){
			  	jQuery("#divUserQuery").show();
			  }
			</script>