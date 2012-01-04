<#if site?? && (Session.hasPermission(site.document.ref, "Everything") || Session.hasPermission(site.document.ref, "ReadWrite"))>
<@extends src="/views/labs-admin-base.ftl">

  <@block name="breadcrumbs">
    <#include "views/common/breadcrumbs_siteadmin.ftl" >
  </@block>

  <@block name="tabs">
    <#include "macros/admin_menu.ftl" />
	<@adminMenu item="general"/>
  </@block>

  <@block name="content">
    <div class="container">

      <section>
        <div class="page-header">
          <h3>Propriétés</h3>
        </div>
        <div class="row">
          <div class="span4 columns">
&nbsp;
          </div>
          <div class="span12 columns">
<script>
jQuery(document).ready(function() {
	jQuery('#siteTemplate').click(function() {
		if (jQuery(this).is(':checked')) {
			jQuery('#siteTemplatePreviewDiv').show();
		} else {
			jQuery('#siteTemplatePreviewDiv').hide();
		}
	});
});
</script>
<style>
#form-labssite .input input[type="checkbox"] {
	margin-top: 6px;
	float: left;
}
	
#form-labssite .input label {
	text-align: left;
	width: 90%;
}
</style>
          
            <form action="${This.path}/@put" method="post" id="form-labssite" enctype="multipart/form-data">
              <fieldset>
                <legend>Mettez à jour les propriétés du site</legend>
                <div class="clearfix">
                  <label for="labsSiteTitle">${Context.getMessage('label.labssite.edit.title')}</label>
                  <div class="input">
                    <input class="required" name="dc:title" value="${site.title}" id="labsSiteTitle"/>
                  </div>
                </div><!-- /clearfix -->

                <div class="clearfix">
                  <label for="labsSiteURL">${Context.getMessage('label.labssite.edit.url')}</label>
                  <div class="input">
                    ${Context.modulePath}/<input class="required" name="webc:url" value="${site.URL}" id="labsSiteURL" />
                    <span class="help-block">C'est par ce lien que le site sera accessible</span>
                  </div>
                </div><!-- /clearfix -->

                <div class="clearfix">
                  <label for="labsSiteDescription">${Context.getMessage('label.labssite.edit.description')}</label>
                  <div class="input">
                    <textarea name="dc:description" id="labsSiteDescription" >${site.description}</textarea>
                  </div>
                </div><!-- /clearfix -->

                <div class="clearfix">
                  <label for="piwik:piwikId">${Context.getMessage('label.labssite.edit.piwikId')}</label>
                  <div class="input">
                    <textarea name="piwik:piwikId" id="piwik:piwikId" >${site.piwikId}</textarea>
                  </div>
                </div><!-- /clearfix -->

			    <div class="clearfix">
			      <div class="input">
			        <input id="siteTemplate" type="checkbox" name="labssite:siteTemplate" <#if site.siteTemplate>checked="true"</#if> />
			        <label for="siteTemplate">&nbsp;${Context.getMessage('label.labssite.edit.siteTemplate')}</label>
			      </div>
			    </div><!-- /clearfix -->
			
				<div class="clearfix" id="siteTemplatePreviewDiv" <#if !site.siteTemplate>style="display:none;"</#if>>
			      <#if site.siteTemplate && site.siteTemplatePreview?? >
			      <div style="float: right; margin-right: 25px;" >
			        <img style="width:60px;cursor:pointer;"
			        <#--
			        title="${Context.getMessage('label.labssite.edit.siteTemplatePreview.delete')}" 
			        onclick="if (confirm('${Context.getMessage('label.labssite.edit.siteTemplatePreview.delete.confirm')}')) jQuery.ajax({url:'${This.path}/logo', type:'DELETE', success:function() {window.location.reload();}});"
			        -->
						src="${Context.modulePath}/${Common.siteDoc(Document).site.URL}/@blob"/>
			      </div>
			      </#if>
			      <label for="siteTemplatePreview">${Context.getMessage('label.labssite.edit.siteTemplatePreview')}</label>
			      <div class="input">
			        <input name="labssite:siteTemplatePreview" type="file" size="25" id="siteTemplatePreview" />
			      </div>
			    </div><!-- /clearfix -->
                
              </fieldset>
              <div class="actions">
                <button class="btn primary">${Context.getMessage('label.labssites.edit.valid')}</button>
              </div>
            </form>
          </div>
        </div>
      </section>

      <section>
        <div class="page-header">
          <h3>Catégories</h3>
        </div>
        <div class="row">
          <div class="span4 columns">
&nbsp;
          </div>
          <div class="span12 columns">

          </div>
        </div>
      </section>
    </div>
  </@block>
</@extends>
<#else>
	<#include "error/error_404.ftl" >
</#if>
