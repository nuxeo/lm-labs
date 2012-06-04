<#assign mySite=Common.siteDoc(Document).getSite() />
		  <h1>${Context.getMessage('label.labssites.appearance.theme.edit.title')}</h1>
	      
	      <form class="form-horizontal" action="${This.path}/parameters" method="post" enctype="multipart/form-data" id="form-parameter">
	      	<fieldset>
	      		<!--    BANNER   -->
	      		<div class="control-group">
                  <label class="control-label" for="banner">${Context.getMessage('label.labssites.appearance.theme.edit.banner.label')}</label>
                  <div class="controls">
                  	<#if (mySite.themeManager.getTheme(Context.coreSession).banner != null)>
	                  	<div id="actionMediaBanner" style="float: right;">
		                  	<span onclick="javascript:deleteElement('${This.path}/banner', 'hideBanner()', '${Context.getMessage('label.labssites.appearance.theme.edit.banner.delete.confirm')}');" style="cursor: pointer;">
						    	<img title="${Context.getMessage('label.labssites.appearance.theme.edit.banner.delete')}" src="${skinPath}/images/x.gif"/>
						  	</span>
					  	</div>
					</#if>
                    <input class="input-file" name="banner" type="file" size="1" enctype="multipart/form-data"/>
                    <p class="help-block">${Context.getMessage('label.labssites.appearance.theme.edit.banner.help.block')}</p>
                  </div>
                </div>
                <hr style="margin: 20px 0 0px;">
                <!--    LOGO PARAMETERS  -->
                <h5 style="color: black;">
	      			${Context.getMessage('label.labssites.appearance.theme.edit.logo.label')}
	      		</h5>
		      		
                <!--   LOGO Picture -->
                <div class="control-group">
                  <label class="control-label" for="logo">${Context.getMessage('label.labssites.appearance.theme.edit.logo.label')}</label>
                  <div class="controls">
                  	 <#assign logoWidth = mySite.themeManager.getTheme(Context.coreSession).logoWidth />
                  	 <#if logoWidth &gt; 0 >
	                    <div id="actionMediaLogo" style="float: right;">
	                    	<img src="${Context.modulePath}/${mySite.URL}/@theme/${mySite.themeManager.getTheme(Context.coreSession).name}/logo" style="width: 40px;border:1px dashed black;"/>
		                  	<span onclick="javascript:deleteElement('${This.path}/logo', 'hideLogo()', '${Context.getMessage('label.labssites.appearance.theme.edit.logo.delete.confirm')}');" style="cursor: pointer;">
						    	<img title="${Context.getMessage('label.labssites.appearance.theme.edit.logo.delete')}" src="${skinPath}/images/x.gif"/>
						  	</span>                  
	                    </div>
	                  </#if>
                    <input class="input-file" name="logo" type="file" size="1" enctype="multipart/form-data"/>
                    <p class="help-block">${Context.getMessage('label.labssites.appearance.theme.edit.logo.help.block')}</p>
                  </div>
                </div>
                
                <#if logoWidth &gt; 0 >
                	<!--   LOGO ratio -->
                    <div class="control-group">
	                  <label class="control-label" for="resize_ratio">${Context.getMessage('label.labssites.appearance.theme.edit.logo_params.resize_ratio')}</label>
	                  <div class="controls">
	                    <input id="resize_ratio" name="resize_ratio" type="text" value="${mySite.themeManager.getTheme(Context.coreSession).logoResizeRatio}" class="input-small" />
	                  </div>
	                </div>
                	<!--   LOGO AREA HEIGHT -->
                	<div class="control-group" id="div_logo_area_height">
	                  <label class="control-label" for="logo_area_height">${Context.getMessage('label.labssites.appearance.theme.edit.logo_area_height')}</label>
	                  <div class="controls">
	                    <input id="logo_area_height" name="logo_area_height" type="text" value="${mySite.themeManager.getTheme(Context.coreSession).logoAreaHeight}" class="input-small" />
	                  </div>
                    </div>
                </#if>
                <hr style="margin: 20px 0 0px;">
                
                <#assign properties = This.getThemeProperties() />
                <#if (properties?size > 0)>
	                <!--    Properties  -->
		      		<h5 style="color: black;">${Context.getMessage('label.labssites.appearance.theme.edit.properties.title')}</h5>
		      		<#assign cptProperties = 0 />
		      		<#list properties as property>
			      		<#if (property.key != null)>
			      		    <#assign propLabel = Context.getMessage(property.label?html) />
			      		    <#if propLabel?starts_with('!') >
			      		        <#assign propLabel = property.label />
			      		    </#if>
                            <#assign property = property />
                            <#assign typeProperty = "string" />
                            <#if (property.type != null)>
                              <#assign typeProperty = property.type />
                            </#if>
                            <label class="control-label" for="property${cptProperties}">
                            <#if typeProperty == "void" >
                            <strong>${propLabel}</strong>
                            <#else>
                            ${propLabel}
                            </#if>
                            </label>
				      		<div class="control-group">
			                  
			                  <div class="controls">
					      		 <#include "views/SiteTheme/typeProperty/input-" + typeProperty + ".ftl" >
                                 <#assign propDesc = Context.getMessage(property.description) />
                                 <#if propDesc?starts_with('!') >
                                  <#assign propDesc = property.description />
                                 </#if>
			                     <p class="help-block">${propDesc?html}</p>
			                  </div>
			                </div><!-- /control-group -->
			                <input type="hidden" name="keyProperty${cptProperties}" value="${property.key}"/>
			                <input type="hidden" name="labelProperty${cptProperties}" value="<#if (property.label != null)>${property.label?html}</#if>"/>
			                <input type="hidden" name="descriptionProperty${cptProperties}" value="<#if (property.description != null)>${property.description?html}</#if>"/>
			                <input type="hidden" name="typeProperty${cptProperties}" value="<#if (property.type != null)>${property.type}</#if>"/>
			                <#assign cptProperties = cptProperties + 1 />
			            </#if>
	                </#list>
	                <input type="hidden" name="cptProperties" value="${cptProperties}"/>
	                <hr style="margin: 20px 0 0px;" />
	            </#if>
                <small><a href="#" onclick="javascript:jQuery('#div_style_logo').show();jQuery(this).hide();">${Context.getMessage('label.labssites.appearance.theme.edit.expertMode')}</a></small>
                <div id="div_style_logo" style="display: none;">
	                <!--    Style   -->
		      		<h5 style="color: black;">
		      			${Context.getMessage('label.labssites.appearance.theme.edit.style.title')}
		      		</h5>
		      		<div class="control-group">
	                  <label class="control-label" for="style">${Context.getMessage('label.labssites.appearance.theme.edit.style.label')}</label>
	                  <div class="controls">
			      		 <textarea class="input" name="style" style="width: 350px;height: 135px;">${mySite.themeManager.getTheme(Context.coreSession).style}</textarea>
	                    <p class="help-block" style="color: red;">${Context.getMessage('label.labssites.appearance.theme.edit.style.help.block')}</p>
	                  </div>
	                </div>
	                <hr style="margin: 20px 0 0px;">
	                
	                <!--    LOGO PARAMETERS  -->
	                <h5 style="color: black;">
		      			${Context.getMessage('label.labssites.appearance.theme.edit.logo_params.title')}
		      		</h5>
	                <div class="control-group">
	                  <label class="control-label" for="logo_posx">${Context.getMessage('label.labssites.appearance.theme.edit.logo_params.posx')}</label>
	                  <div class="controls">
	                    <input id="logo_posx" name="logo_posx" type="text" value="${mySite.themeManager.getTheme(Context.coreSession).logoPosX}" class="input-small" />
	                  </div>
                    </div>
                    <div class="control-group">
	                  <label class="control-label" for="logo_posy">${Context.getMessage('label.labssites.appearance.theme.edit.logo_params.posy')}</label>
	                  <div class="controls">
	                    <input id="logo_posy" name="logo_posy" type="text" value="${mySite.themeManager.getTheme(Context.coreSession).logoPosY}" class="input-small" />
	                  </div>
                    </div>
	              </div>
                
	      	</fieldset>
	      
		      <div class="actions">
		      	<button class="btn btn-primary">${Context.getMessage('label.labssites.appearance.theme.edit.save')}</button>
		      	<a href="#" class="btn" onclick="javascript:resetPropertiesTheme();">${Context.getMessage('label.labssites.appearance.theme.edit.resetProperties')}</a>
		      	<a href="#" class="btn" onclick="javascript:jQuery('#div-editTheme').dialog2('close');">${Context.getMessage('label.close')}</a>
		      </div>
	      </form>
	      
	      	
	<script type="text/javascript">
		function resetPropertiesTheme(){
			<#assign cptProperties = 0 />
		    <#list properties as property>
		    	jQuery('#valueProperty${cptProperties}').val("");
		    	if(jQuery('#valueProperty${cptProperties}').attr("checked") == "checked"){
		    		jQuery('#valueProperty${cptProperties}').removeAttr("checked");
		    	}
		    	if (jQuery('input[name=quizBackgroundColorEdit${cptProperties}]')){
		    		jQuery('input[name=quizBackgroundColorEdit${cptProperties}]').val("");
		    		jQuery("input[name=quizBackgroundColorEdit${cptProperties}]").miniColors('value', '');
		    	}
		    	<#assign cptProperties = cptProperties + 1 />
	        </#list>
		}
	</script>