<#if site?? && Session.hasPermission(site.document.ref, "Everything")>
<@extends src="/views/labs-admin-base.ftl">

	<@block name="scripts">
	    <@superBlock/>
	    <script type="text/javascript" src="${skinPath}/js/siteTheme.js"></script>
	    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.miniColors.min.js"></script>
	</@block>
	
	<@block name="css">
	    <@superBlock/>
        <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery.miniColors.css"/>
	</@block>

	<@block name="docactions"></@block>

  <@block name="tabs">
    <#include "macros/admin_menu.ftl" />
	<@adminMenu item="theme" basePath="${This.previous.path}"/>
  </@block>

  <@block name="breadcrumbs">
    <#include "views/common/breadcrumbs_siteadmin.ftl" >
  </@block>

  <@block name="content">
    <div class="container">
      
		<!--    TEMPLATE THEME -->
      <section>
        <div class="page-header">
          <h3>
          	${Context.getMessage('label.labssites.appearance.title')}
          	<small><a href="${Context.modulePath}/${Common.siteDoc(Document).site.URL}" target="_blank">${Context.getMessage('label.labssites.appearance.preview')}</a></small>
          </h3>
        </div>
        <div class="row">
          <div class="span3 columns">
			&nbsp;
          </div>
          <div class="span8 columns">
            <form class="form-horizontal" action="${This.path}/appearance" method="post" id="form-appearance">
              <fieldset>
                <div class="control-group">
                  <label class="control-label" for="template">${Context.getMessage('label.labssites.appearance.template.label')}</label>
                  <div class="controls">
                    <select name="template" id="template">
	            		<#list This.getTemplates() as template>
	            			<option value="${template}"  <#if site.template.templateName == template >selected</#if>>${template}</option>
	            		</#list>
	            	</select>
                    <p class="help-block">${Context.getMessage('label.labssites.appearance.template.help.block')}</p>
                  </div>
                </div>
                
                <div class="control-group">
                  <label class="control-label" for="theme">${Context.getMessage('label.labssites.appearance.theme.label')}</label>
                  <div class="controls">
	            	<a href="#" id="modifyThemeParameters"><br/>${Context.getMessage('label.labssites.appearance.theme.parameters')}</a> 
                    <select name="theme" id="theme" onChange="javascript:manageDisplayModifyParameters('${site.getThemeManager().getTheme().getName()}');">
	            		<#list This.getThemes() as theme>
	            			<option value="${theme}"  <#if site.getThemeManager().getTheme().getName() == theme >selected</#if>>${theme}</option>
	            		</#list>
	            	</select>
                    <p class="help-block">${Context.getMessage('label.labssites.appearance.theme.help.block')}</p>
                  </div>
                </div>

               </fieldset>
              <div class="form-actions">
                <button class="btn btn-primary">${Context.getMessage('label.labssites.appearance.save')}</button>
              </div>
            </form>
          </div>
        </div>
      </section>
          

		<div id="div-editTheme" style="display:none;">
	      <h1>${Context.getMessage('label.labssites.appearance.theme.edit.title')}</h1>
	      
	      <form class="form-horizontal" action="${This.path}/parameters" method="post" enctype="multipart/form-data" id="form-parameter">
	      	<fieldset>
	      		<!--    BANNER   -->
	      		<div class="control-group">
                  <label class="control-label" for="banner">${Context.getMessage('label.labssites.appearance.theme.edit.banner.label')}</label>
                  <div class="controls">
                  	<#if (site.themeManager.theme.banner != null)>
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
                
                <!--   LOGO  -->
                <div class="control-group">
                  <label class="control-label" for="logo">${Context.getMessage('label.labssites.appearance.theme.edit.logo.label')}</label>
                  <div class="controls">
                  	 <#assign logoWidth = site.themeManager.theme.logoWidth />
                  	 <#if logoWidth &gt; 0 >
	                    <div id="actionMediaLogo" style="float: right;">
	                    	<img src="${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@theme/${site.themeManager.theme.name}/logo" style="width: 40px;border:1px dashed black;"/>
		                  	<span onclick="javascript:deleteElement('${This.path}/logo', 'hideLogo()', '${Context.getMessage('label.labssites.appearance.theme.edit.logo.delete.confirm')}');" style="cursor: pointer;">
						    	<img title="${Context.getMessage('label.labssites.appearance.theme.edit.logo.delete')}" src="${skinPath}/images/x.gif"/>
						  	</span>                  
	                    </div>
	                  </#if>
                    <input class="input-file" name="logo" type="file" size="1" enctype="multipart/form-data"/>
                    <p class="help-block">${Context.getMessage('label.labssites.appearance.theme.edit.logo.help.block')}</p>
                  </div>
                </div>
                <hr style="margin: 20px 0 0px;">
                
                <#assign properties = This.getThemeProperties() />
                <#if (properties?size > 0)>
	                <!--    Properties  -->
		      		<h5 style="color: black;">${Context.getMessage('label.labssites.appearance.theme.edit.properties.title')}</h5>
		      		<#assign cptProperties = 0 />
		      		<#list properties as property>
			      		<#if (property.key != null)>
		                    <label class="control-label" for="property${cptProperties}">${property.label}</label>
				      		<div class="control-group">
			                  <#assign property = property />
			                  <#assign typeProperty = "string" />
			                  <#if (property.type != null)>
			                  	<#assign typeProperty = property.type />
			                  </#if>
			                  
			                  <div class="controls">
					      		 <#include "views/SiteTheme/typeProperty/input-" + typeProperty + ".ftl" >
			                     <p class="help-block">${property.description?html}</p>
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
			      		 <textarea class="input" name="style" style="width: 350px;height: 135px;">${site.themeManager.theme.style}</textarea>
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
	                    <input class="input" id="logo_posx" name="logo_posx" type="text" value="${site.themeManager.theme.logoPosX}" class="small" />
	                  </div>
                    </div>
                    <div class="control-group">
	                  <label class="control-label" for="logo_posy">${Context.getMessage('label.labssites.appearance.theme.edit.logo_params.posy')}</label>
	                  <div class="controls">
	                    <input class="input" id="logo_posy" name="logo_posy" type="text" value="${site.themeManager.theme.logoPosY}" class="small" />
	                  </div>
                    </div>
                    <div class="control-group">
	                  <label class="control-label" for="resize_ratio">${Context.getMessage('label.labssites.appearance.theme.edit.logo_params.resize_ratio')}</label>
	                  <div class="controls">
	                    <input class="input" id="resize_ratio" name="resize_ratio" type="text" value="${site.themeManager.theme.logoResizeRatio}" class="small" />
	                  </div>
	                </div>
	              </div>
                
	      	</fieldset>
	      
		      <div class="form-actions">
		      	<button class="btn btn-primary">${Context.getMessage('label.labssites.appearance.theme.edit.save')}</button>
		      	<a href="#" class="btn" onclick="javascript:resetPropertiesTheme();">${Context.getMessage('label.labssites.appearance.theme.edit.resetProperties')}</a>
		      	<a href="#" class="btn" onclick="javascript:jQuery('#div-editTheme').dialog2('close');">${Context.getMessage('label.close')}</a>
		      </div>
	      </form>
	    </div>
	    
	    
	</div>
	
	<script type="text/javascript">
		function resetPropertiesTheme(){
			<#assign cptProperties = 0 />
		    <#list properties as property>
		    	jQuery('#valueProperty${cptProperties}').val("");
		    	if (jQuery('input[name=quizBackgroundColorEdit${cptProperties}]')){
		    		jQuery('input[name=quizBackgroundColorEdit${cptProperties}]').val("");
		    		jQuery("input[name=quizBackgroundColorEdit${cptProperties}]").miniColors('value', '');
		    	}
		    	<#assign cptProperties = cptProperties + 1 />
	        </#list>
		}
	</script>
	
  </@block>
</@extends>
<#else>
	<#include "error/error_404.ftl" >
</#if>
