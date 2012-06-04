<#assign mySite=Common.siteDoc(Document).getSite() />
<#if mySite?? && (Session.hasPermission(mySite.document.ref, "Everything") || Session.hasPermission(mySite.document.ref, "ReadWrite"))>
<@extends src="/views/labs-admin-base.ftl">

  <@block name="docactions"></@block>

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
          <div class="span3 columns">
&nbsp;
          </div>
          <div class="span16 columns">
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
          
            <form class="form-horizontal well" action="${This.path}/@put" method="post" id="form-labssite" enctype="multipart/form-data">
              <fieldset>
                <legend>Mettez à jour les propriétés du site</legend>
                <div class="control-group">
                  <label class="control-label" for="labsSiteTitle">${Context.getMessage('label.labssite.edit.title')}</label>
                  <div class="controls">
                    <input class="required" name="dc:title" value="${mySite.title}" id="labsSiteTitle"/>
                  </div>
                </div>

                <div class="control-group">
                  <label class="control-label" for="labsSiteURL">${Context.getMessage('label.labssite.edit.url')}</label>
                  <div class="controls">
                    ${Context.modulePath}/<input class="required" name="webc:url" value="${mySite.URL}" id="labsSiteURL" />
                    <p class="help-block">C'est par ce lien que le site sera accessible</p>
                  </div>
                </div>

                <div class="control-group">
                  <label class="control-label" for="labsSiteDescription">${Context.getMessage('label.labssite.edit.description')}</label>
                  <div class="controls">
                    <textarea name="dc:description" id="labsSiteDescription" >${mySite.description}</textarea>
                  </div>
                </div>

                <div class="control-group">
                  <label class="control-label" for="piwik:piwikId">${Context.getMessage('label.labssite.edit.piwikId')}</label>
                  <div class="controls">
                    <textarea name="piwik:piwikId" id="piwik:piwikId" >${mySite.piwikId}</textarea>
                  </div>
                </div>

			    <#if Context.principal.administrator >
			    <div class="control-group">
			      <div class="controls">
			        <label class="checkbox" for="siteTemplate">
			          <input class="checkbox" id="siteTemplate" type="checkbox" name="labssite:siteTemplate" <#if mySite.siteTemplate>checked="true"</#if> />
			        &nbsp;${Context.getMessage('label.labssite.edit.siteTemplate')}</label>
			      </div>
			    </div>
			    </#if>
			
				<div class="control-group" id="siteTemplatePreviewDiv" <#if !mySite.siteTemplate>style="display:none;"</#if>>
			      <#if mySite.siteTemplate && mySite.siteTemplatePreview?? >
			      <div style="float: right; margin-right: 25px;" >
			        <img style="width:400px;cursor:pointer;"
			          title="${Context.getMessage('label.labssite.edit.siteTemplatePreview.delete')}" 
			          onclick="if (confirm('${Context.getMessage('label.labssite.edit.siteTemplatePreview.delete.confirm')?js_string}')){jQuery('#waitingPopup').dialog2('open'); jQuery.ajax({url:'${Root.getLink(mySite.document)}/@blob', type:'DELETE', success:function() {window.location.reload();}, error:function(data){jQuery('#waitingPopup').dialog2('close');}});}"
			          src="${Context.modulePath}/${mySite.URL}/@blob"/>
			      </div>
			      </#if>
			      <label class="control-label" for="siteTemplatePreview">${Context.getMessage('label.labssite.edit.siteTemplatePreview')}</label>
			      <div class="controls">
			        <input name="labssite:siteTemplatePreview" type="file" size="25" id="siteTemplatePreview" />
			      </div>
			    </div>
                
              </fieldset>
              <div class="form-actions" style="margin-left: 60px;">
                <button class="btn btn-primary">${Context.getMessage('label.labssites.edit.valid')}</button>
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
