<#if site?? && Session.hasPermission(site.document.ref, "Everything")>
<@extends src="/views/labs-admin-base.ftl">


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
          <h3>Bandeau</h3>
        </div>
        <div class="row">
          <div class="span4 columns">
&nbsp;
          </div>
          <div class="span12 columns">
            <form action="${This.path}" method="post" enctype="multipart/form-data" id="form-banner">
              <fieldset>
                <legend>Uploadez un bandeau</legend>
                <div class="clearfix">
                  <label for="banner">${Context.getMessage('label.labssites.banner.file')}</label>
                  <div class="input">
                    <input class="required" name="banner" type="file" enctype="multipart/form-data"/>
                    <span class="help-block">Utilisez une image (.jpg ou .png) de 980px de large. La hauteur du bandeau
                    s'ajuste automtiquement. </span>
                  </div>
                </div><!-- /clearfix -->

                              </fieldset>
              <div class="actions">
                <button class="btn primary required-fields" form-id="form-banner">${Context.getMessage('label.labssites.banner.edit.download')}</button>
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
