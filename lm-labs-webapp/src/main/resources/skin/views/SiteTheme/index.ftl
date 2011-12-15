<#if site?? && Session.hasPermission(site.document.ref, "Everything")>
<@extends src="/views/labs-admin-base.ftl">

	<@block name="scripts">
	    <@superBlock/>
	    <script type="text/javascript" src="${skinPath}/js/siteTheme.js"></script>
	</@block>


  <@block name="tabs">
    <#include "macros/admin_menu.ftl" />
	<@adminMenu item="theme" basePath="${This.previous.path}"/>
  </@block>

  <@block name="breadcrumbs">
    <#include "views/common/breadcrumbs_siteadmin.ftl" >
  </@block>

  <@block name="content">
    <div class="container">
  
      
		<!------------------    TEMPLATE THEME ------------------------->
      <section>
        <div class="page-header">
          <h3>
          	${Context.getMessage('label.labssites.appearance.title')}
          	<small><a href="${Context.modulePath}/${Common.siteDoc(Document).site.URL}" target="_blank">${Context.getMessage('label.labssites.appearance.preview')}</a><small>
          </h3>
        </div>
        <div class="row">
          <div class="span4 columns">
			&nbsp;
          </div>
          <div class="span12 columns">
            <form action="${This.path}/appearance" method="post" id="form-appearance">
              <fieldset>
                <!--<legend>${Context.getMessage('label.labssites.template.title.legend')}</legend>-->
                <div class="clearfix">
                  <label for="template">${Context.getMessage('label.labssites.appearance.template.label')}</label>
                  <div class="input">
                    <select name="template" id="template">
	            		<#list This.getTemplates() as template>
	            			<option value="${template}"  <#if site.template.templateName == template >selected</#if>>${template}</option>
	            		</#list>
	            	</select>
                    <span class="help-block">${Context.getMessage('label.labssites.appearance.template.help.block')}</span>
                  </div>
                </div><!-- /clearfix -->
                
                <div class="clearfix">
                  <label for="template">${Context.getMessage('label.labssites.appearance.theme.label')}</label>
                  <div class="input">
	            	<a href="#" id="modifyThemeParameters"><br/>${Context.getMessage('label.labssites.appearance.theme.parameters')}</a> 
                    <select name="theme" id="theme" onChange="javascript:manageDisplayModifyParameters('${site.getThemeManager().getTheme().getName()}');">
	            		<#list This.getThemes() as theme>
	            			<option value="${theme}"  <#if site.getThemeManager().getTheme().getName() == theme >selected</#if>>${theme}</option>
	            		</#list>
	            	</select>
                    <span class="help-block">${Context.getMessage('label.labssites.appearance.theme.help.block')}</span>
                  </div>
                </div><!-- /clearfix -->

               </fieldset>
              <div class="actions">
                <button class="btn primary">${Context.getMessage('label.labssites.appearance.save')}</button>
              </div>
            </form>
          </div>
        </div>
      </section>
          

		<div id="div-editTheme" style="display:none;">
	      <h1>${Context.getMessage('label.labssites.appearance.theme.edit.title')}</h1>
	      
	      <form action="${This.path}/parameters" method="post" enctype="multipart/form-data" id="form-parameter">
	      	<fieldset>
	      		<!------------------    BANNER   ------------------------->
	      		<div class="clearfix">
                  <label for="banner">${Context.getMessage('label.labssites.appearance.theme.edit.banner.label')}</label>
                  <div class="input">
		      		    <a href="#" style="float:right;" id="deleteBanner" onClick="javascript=deleteBanner('${This.path}/banner', '${This.path}', '${Context.getMessage('label.labssites.appearance.theme.edit.banner.delete.confirm')}');">${Context.getMessage('label.labssites.appearance.theme.edit.banner.delete')}</a>
                    <input name="banner" type="file" size="1" enctype="multipart/form-data"/>
                    <span class="help-block">${Context.getMessage('label.labssites.appearance.theme.edit.banner.help.block')}</span>
                  </div>
                </div><!-- /clearfix -->
                
                <!------------------    LOGO   ------------------------->
                <div class="clearfix">
                  <#assign logoWidth = site.themeManager.theme.logoWidth />
                  <#if logoWidth &gt; 0 >
                  <div style="float: right; margin-right: 50px;" >
                    <img title="${Context.getMessage('label.labssites.appearance.theme.edit.logo.delete.title')}" style="width:60px;cursor:pointer;" onclick="if (confirm('${Context.getMessage('label.labssites.appearance.theme.edit.logo.delete.confirm')}')) jQuery.ajax({url:'${This.path}/logo', type:'DELETE', success:function() {window.location.reload();}});"
    					src="${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@theme/${site.themeManager.theme.name}/logo"/>
                  </div>
                  </#if>
                  <label for="logo">${Context.getMessage('label.labssites.appearance.theme.edit.logo.label')}</label>
                  <div class="input">
                    <input name="logo" type="file" size="1" enctype="multipart/form-data"/>
                    <span class="help-block">${Context.getMessage('label.labssites.appearance.theme.edit.logo.help.block')}</span>
                  </div>
                </div><!-- /clearfix -->
                <hr style="margin: 20px 0 0px;">
                
                <!------------------    LOGO PARAMETERS   ------------------------->
                <h5 style="color: black;">${Context.getMessage('label.labssites.appearance.theme.edit.logo_params.title')}</h5>
                <div class="clearfix">
                  <label for="logo_posx">${Context.getMessage('label.labssites.appearance.theme.edit.logo_params.posx')}</label>
                  <div class="input clearfix">
                    <input id="logo_posx" name="logo_posx" type="text" value="${site.themeManager.theme.logoPosX}" class="small" />
                  </div>
                  <label for="logo_posy">${Context.getMessage('label.labssites.appearance.theme.edit.logo_params.posy')}</label>
                  <div class="input clearfix">
                    <input id="logo_posy" name="logo_posy" type="text" value="${site.themeManager.theme.logoPosY}" class="small" />
                  </div>
                  <label for="resize_ratio">${Context.getMessage('label.labssites.appearance.theme.edit.logo_params.resize_ratio')}</label>
                  <div class="input">
                    <input id="resize_ratio" name="resize_ratio" type="text" value="${site.themeManager.theme.logoResizeRatio}" class="small" />
                  </div>
                </div><!-- /clearfix -->
                <hr style="margin: 20px 0 0px;">
                
                <!------------------    Style   ------------------------->
	      		<h5 style="color: black;">${Context.getMessage('label.labssites.appearance.theme.edit.style.title')}</h5>
	      		<div class="clearfix">
                  <label for="style">${Context.getMessage('label.labssites.appearance.theme.edit.style.label')}</label>
                  <div class="input">
		      		 <textarea name="style" style="width: 350px;height: 135px;">${site.themeManager.theme.style}</textarea>
                    <span class="help-block" style="color: red;">${Context.getMessage('label.labssites.appearance.theme.edit.style.help.block')}</span>
                  </div>
                </div><!-- /clearfix -->
	      	</fieldset>
	      
		      <div class="actions">
		      	<button class="btn primary">${Context.getMessage('label.labssites.appearance.theme.edit.save')}</button>
		      	<a href="#" class="btn" onclick="javascript:jQuery('#div-editTheme').dialog2('close');">${Context.getMessage('label.labssites.appearance.theme.edit.cancel')}</a>
		      </div>
	      </form>
	   </div>



	</div>
  </@block>
</@extends>
<#else>
	<#include "error/error_404.ftl" >
</#if>
