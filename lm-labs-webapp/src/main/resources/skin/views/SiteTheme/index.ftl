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

		<!------------------    BANNER   ------------------------->
      <section>
        <div class="page-header">
          <h3>${Context.getMessage('label.labssites.banner.title')}</h3>
        </div>
        <div class="row">
          <div class="span4 columns">
&nbsp;
          </div>
          <div class="span12 columns">
            <form action="${This.path}/banner" method="post" enctype="multipart/form-data" id="form-banner">
              <fieldset>
                <legend>${Context.getMessage('label.labssites.banner.title.legend')}</legend>
                <div class="clearfix">
                  <label for="banner">${Context.getMessage('label.labssites.banner.file')}</label>
                  <div class="input">
                    <input class="required" name="banner" type="file" enctype="multipart/form-data"/>
                    <span class="help-block">${Context.getMessage('label.labssites.banner.help.block')}</span>
                  </div>
                </div><!-- /clearfix -->

                              </fieldset>
              <div class="actions">
                <button class="btn primary required-fields" form-id="form-banner">${Context.getMessage('label.labssites.banner.edit.download')}</button>
            	 <a class="btn" onClick="javascript:deleteBanner('${This.path}/banner', '${This.path}');">${Context.getMessage('label.labssites.banner.delete')}</a>
              </div>
            </form>
          </div>
        </div>
      </section>
      
      
      
		<!------------------    LOGO   ------------------------->
      <section>
        <div class="page-header">
          <h3>${Context.getMessage('label.labssites.logo.title')}</h3>
        </div>
        <div class="row">
          <div class="span4 columns">
&nbsp;
          </div>
          <div class="span12 columns">
            <form action="${This.path}/logo" method="post" enctype="multipart/form-data" id="form-logo">
              <fieldset>
                <legend>${Context.getMessage('label.labssites.logo.title.legend')}</legend>
                <div class="clearfix">
                  <#assign logoWidth = site.themeManager.theme.logoWidth />
                  <#if logoWidth &gt; 0 >
                  <div style="float: right; margin-right: 50px;" >
                    <img title="${Context.getMessage('label.labssites.logo.delete.title')}" style="width:60px;cursor:pointer;" onclick="if (confirm('${Context.getMessage('label.labssites.logo.delete.confirm')}')) jQuery.ajax({url:'${This.path}/logo', type:'DELETE', success:function() {window.location.reload();}});"
    src="${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@theme/${site.themeManager.theme.name}/logo"/>
                  </div>
                  </#if>
                  <label for="logo">${Context.getMessage('label.labssites.logo.file')}</label>
                  <div class="input">
                    <input class="required" name="logo" type="file" enctype="multipart/form-data"/>
                    <span class="help-block">${Context.getMessage('label.labssites.logo.help.block')}</span>
                  </div>
                </div><!-- /clearfix -->
              </fieldset>
              <div class="actions">
                <button class="btn primary required-fields" form-id="form-logo">${Context.getMessage('label.labssites.logo.edit.download')}</button>
              </div>
            </form>
          </div>
        </div>
      </section>
      
		<!------------------    LOGO PARAMETERS   ------------------------->
      <section>
        <div class="page-header">
          <h3>${Context.getMessage('label.labssites.logo_params.title')}</h3>
        </div>
        <div class="row">
          <div class="span4 columns">
&nbsp;
          </div>
          <div class="span12 columns">
            <form action="${This.path}/logoParameters" method="post" enctype="multipart/form-data" id="form-logo-position">
              <fieldset>
                <legend>${Context.getMessage('label.labssites.logo_params.title.legend')}</legend>
                <div class="clearfix">
                  <label for="logo_posx">${Context.getMessage('label.labssites.logo_params.posx')}</label>
                  <div class="input clearfix">
                    <input id="logo_posx" name="logo_posx" type="text" value="${site.themeManager.theme.logoPosX}" />
                  </div>
                  <label for="logo_posy">${Context.getMessage('label.labssites.logo_params.posy')}</label>
                  <div class="input clearfix">
                    <input id="logo_posy" name="logo_posy" type="text" value="${site.themeManager.theme.logoPosY}" />
                  </div>
                  <label for="resize_ratio">${Context.getMessage('label.labssites.logo_params.resize_ratio')}</label>
                  <div class="input">
                    <input id="resize_ratio" name="resize_ratio" type="text" value="${site.themeManager.theme.logoResizeRatio}" />
                  </div>
                </div><!-- /clearfix -->
              </fieldset>
              <div class="actions">
                <button class="btn primary">${Context.getMessage('label.labssites.logo_params.save')}</button>
              </div>
            </form>
          </div>
        </div>
      </section>
      
		<!------------------    THEME   ------------------------->
      <section>
        <div class="page-header">
          <h3>${Context.getMessage('label.labssites.theme.title')}</h3>
        </div>
        <div class="row">
          <div class="span4 columns">
&nbsp;
          </div>
          <div class="span12 columns">
            <form action="${This.path}/theme" method="post" id="form-theme">
              <fieldset>
                <legend>${Context.getMessage('label.labssites.theme.title.legend')}</legend>
                <div class="clearfix">
                  <label for="template">${Context.getMessage('label.labssites.theme.label')}</label>
                  <div class="input">
                    <select name="theme" id="theme">
	            		<#list This.getThemes() as theme>
	            			<option value="${theme}"  <#if site.getThemeManager().getTheme().getName() == theme >selected</#if>>${theme}</option>
	            		</#list>
	            	</select>
                    <span class="help-block">${Context.getMessage('label.labssites.theme.help.block')}</span>
                  </div>
                </div><!-- /clearfix -->

               </fieldset>
              <div class="actions">
                <button class="btn primary">${Context.getMessage('label.labssites.theme.save')}</button>
              </div>
            </form>
          </div>
        </div>
      </section>
      
		<!------------------    TEMPLATE   ------------------------->
      <section>
        <div class="page-header">
          <h3>${Context.getMessage('label.labssites.template.title')}</h3>
        </div>
        <div class="row">
          <div class="span4 columns">
&nbsp;
          </div>
          <div class="span12 columns">
            <form action="${This.path}/template" method="post" id="form-template">
              <fieldset>
                <legend>${Context.getMessage('label.labssites.template.title.legend')}</legend>
                <div class="clearfix">
                  <label for="template">${Context.getMessage('label.labssites.template.label')}</label>
                  <div class="input">
                    <select name="template" id="template">
	            		<#list This.getTemplates() as template>
	            			<option value="${template}"  <#if site.template.templateName == template >selected</#if>>${template}</option>
	            		</#list>
	            	</select>
                    <span class="help-block">${Context.getMessage('label.labssites.template.help.block')}</span>
                  </div>
                </div><!-- /clearfix -->

               </fieldset>
              <div class="actions">
                <button class="btn primary">${Context.getMessage('label.labssites.template.save')}</button>
              </div>
            </form>
          </div>
        </div>
      </section>



    </div>
  </@block>
</@extends>
<#else>
	<#include "error/error_404.ftl" >
</#if>
