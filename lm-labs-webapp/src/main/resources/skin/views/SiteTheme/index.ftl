<#assign mySite=Common.siteDoc(Document).getSite(Context.coreSession) />
<#if mySite?? && Session.hasPermission(mySite.document.ref, "Everything")>
<@extends src="/views/labs-admin-base.ftl">

	<@block name="scripts">
	    <@superBlock/>
	    <script type="text/javascript" src="${skinPath}/js/siteTheme.js"></script>
	    <script type="text/javascript" src="${skinPath}/js/jquery/jquery.miniColors.min.js"></script>
	    <#include "views/common/template_description_js.ftl">
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

  <@block name="content">
    <div class="container">
      
		<!--    TEMPLATE THEME -->
      <section>
        <div class="page-header">
          <h3>
          	${Context.getMessage('label.labssites.appearance.title')}
          	<small><a href="${Context.modulePath}/${mySite.URL}" target="_blank">${Context.getMessage('label.labssites.appearance.preview')}</a></small>
          </h3>
        </div>
        <div class="row">
          <div class="span3 columns">
			&nbsp;
          </div>
          <div class="span8 columns">
            <form class="form-horizontal well" action="${This.path}/appearance" method="post" id="form-appearance">
              <fieldset>
                <div class="control-group">
                  <label class="control-label" for="theme">${Context.getMessage('label.labssites.appearance.theme.label')}</label>
                  <div class="controls">
                    <a href="#" id="modifyThemeParameters" onClick="javascript:loadEditParameters('${This.path}/editParameters');"><br/>${Context.getMessage('label.labssites.appearance.theme.parameters')}</a> 
                    <select name="theme" id="theme" onChange="javascript:manageDisplayModifyParameters('${mySite.getThemeManager().getTheme(Context.coreSession).getName()}');linkThemeTemplate();">
                        <#assign themesMap = [] />
                        <#list This.getThemes() as theme>
                            <#assign trad = Context.getMessage('label.labssites.appearance.themes.' + theme) />
                            <#if trad?starts_with('!') >
                                <#assign themeName = theme />
                            <#else>
                                <#assign themeName = trad />
                            </#if>
                            <#assign themesMap = themesMap + [ {"name" : theme, "title" : themeName} ] />
                        </#list>
                        <#list themesMap?sort_by('title') as theme>
                            <option value="${theme.name}"  <#if mySite.getThemeManager().getTheme(Context.coreSession).getName() == theme.name >selected</#if>>${theme.title}</option>
                        </#list>
                    </select>
                    <p class="help-block">${Context.getMessage('label.labssites.appearance.theme.help.block')}</p>
                  </div>
                </div>
                <div class="control-group">
                  <label class="control-label" for="template">${Context.getMessage('label.labssites.appearance.template.label')}</label>
                  <div class="controls">
                    <select name="template" id="template" onchange="updateTemplateDescription(this, 'template-description');" >
                    <#include "views/common/getTemplatesMap.ftl">
                    <#assign templatesMap = getTemplatesMap() />
                    <#list templatesMap?sort_by('title') as template>
            			<option value="${template.name}"  <#if mySite.template.getTemplateName(Context.coreSession) == template.name >selected</#if>>${template.title}</option>
            		</#list>
	            	</select>
                    <p id="template-description" class="help-block"><small>${Context.getMessage('label.labssites.appearance.templates.' + mySite.template.templateName + '.description')}</small></p>
                    <p class="help-block">${Context.getMessage('label.labssites.appearance.template.help.block')}</p>
                  </div>
                </div>
               </fieldset>
              <div class="form-actions" style="margin-left: 40px;">
                <button class="btn btn-primary">${Context.getMessage('label.labssites.appearance.save')}</button>
              </div>
            </form>
          </div>
        </div>
      </section>

	  <div id="div-editTheme-container"></div>
	    
	</div>
	
  </@block>
</@extends>
<#else>
	<#include "error/error_404.ftl" >
</#if>
