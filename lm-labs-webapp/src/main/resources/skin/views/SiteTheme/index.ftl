<@extends src="/views/templates/labs-base.ftl">


  <@block name="tabs">
    <#include "macros/admin_menu.ftl" />
	<@adminMenu item="theme" basePath="${This.previous.path}"/>
  </@block>

  <@block name="content">
    <div class="container">

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
	            			<option value="${template.getTemplate()}"  <#if site.template.templateName == template.getTemplate() >selected</#if>>${Context.getMessage(template.getI18n())}</option>
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