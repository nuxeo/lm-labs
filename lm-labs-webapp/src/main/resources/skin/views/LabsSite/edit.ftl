<#assign mySite=Common.siteDoc(Document).getSite() />
<#if mySite?? && (Session.hasPermission(mySite.document.ref, "Everything") || Session.hasPermission(mySite.document.ref, "ReadWrite"))>
<@extends src="/views/labs-admin-base.ftl">

  <@block name="scripts">
    <@superBlock/>
    <script type="text/javascript" src="${skinPath}/js/LabsSiteUrl.js"></script>
    <script type="text/javascript" >
function beforeSubmitCheckSiteUrl() {
	if (jQuery('#urlAvailability').val() !== 'true') {
		var ok = verifyUrlAvailability('${This.path}/@urlAvailability', function() {setCheckUrlButton('complete');}, function() {jQuery('#urlAvailability').val('false');setCheckUrlButton('failed');});
		return ok;
	}
	return true;
}

jQuery(document).ready(function() {
	jQuery('#siteTemplate').click(function() {
		if (jQuery(this).is(':checked')) {
			jQuery('#siteTemplatePreviewDiv').show();
		} else {
			jQuery('#siteTemplatePreviewDiv').hide();
		}
	});
    jQuery('#form-labssite').ajaxForm({
        beforeSubmit:  beforeSubmitCheckSiteUrl
        //error: function(){},
        //success: function(){}
    });
	jQuery('#verifyUrlAvailability').button();
	jQuery('#verifyUrlAvailability').unbind('click');
	jQuery('#verifyUrlAvailability').click(function(evt) {
		var btnObj = evt.target;
		if (!jQuery(btnObj).hasClass('disabled')) {
			jQuery(btnObj).button('loading');
			verifyUrlAvailability('${This.path}/@urlAvailability',
				function() {
					jQuery('#urlAvailability').val('true');
					setCheckUrlButton('complete');
				},
				function() {
					jQuery('#urlAvailability').val('false');
					setCheckUrlButton('failed');
				}
			);
		}
	});
});
</script>
  </@block>

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
          <div class="span2 columns">
&nbsp;
          </div>
          <div class="span8 columns">
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

<#include "/macros/LabsSiteInputUrl.ftl" />
<@LabsSiteInputUrl value=mySite.URL />

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
                
                <div class="control-group">
			      <label class="control-label" for="labsSiteCategory">${Context.getMessage('label.labssite.edit.category')}</label>
			      <div class="controls">
			        <select class="input" name="labssite:category" id="labsSiteCategory" >
			        	<#assign allLabsCategories = Common.getAllLabsCategory()/>
			        	<#assign labsCategories = Common.getCategories()/>
			        	<#list labsCategories as category>
			        		<#assign underLabsCategories = Common.getChildrenCategories(category)/>
			        		<#if (underLabsCategories?size > 0)>
			        			<optgroup label="${category.labscategory.label?html}">
					        		<#list underLabsCategories as underCategory>
					        			<option value="${underCategory.labscategory.label}"<#if underCategory.labscategory.label == mySite.category> selected</#if>>${underCategory.labscategory.label}</option>
					        		</#list>
					        	</optgroup>
					        <#else>
					        	<option value="${category.labscategory.label}"<#if category.labscategory.label == mySite.category> selected</#if>>${category.labscategory.label}</option>
				        	</#if>
			        	</#list>
			        </select>
			      </div>
			    </div>

			    <#if Context.principal.administrator >
			    <div class="control-group">
			      <div class="controls">
			        <label class="checkbox" for="siteTemplate">
			          <input class="checkbox" id="siteTemplate" type="checkbox" name="labssite:siteTemplate" <#if mySite.elementTemplate>checked="true"</#if> />
			        &nbsp;${Context.getMessage('label.labssite.edit.siteTemplate')}</label>
			      </div>
			    </div>
			    </#if>
			
				<div class="control-group" id="siteTemplatePreviewDiv" <#if !mySite.elementTemplate>style="display:none;"</#if>>
			      <#if mySite.hasElementPreview() >
			      <div style="float: right; margin-right: 25px;" >
			        <img style="width:400px;cursor:pointer;"
			          title="${Context.getMessage('label.element.template.preview.delete')}" 
			          onclick="if (confirm('${Context.getMessage('label.element.template.preview.delete.confirm')?js_string}')){jQuery('#waitingPopup').dialog2('open'); jQuery.ajax({url:'${Root.getLink(mySite.document)}/@blob', type:'DELETE', success:function() {window.location.reload();}, error:function(data){jQuery('#waitingPopup').dialog2('close');}});}"
			          src="${Context.modulePath}/${mySite.URL}/@blob"/>
			      </div>
			      </#if>
			      <label class="control-label" for="siteTemplatePreview">${Context.getMessage('label.labssite.edit.siteTemplatePreview')}</label>
			      <div class="controls">
			        <input name="labssite:siteTemplatePreview" type="file" size="25" id="siteTemplatePreview" />
			      </div>
			    </div>
                
              </fieldset>
              <div class="form-actions" >
                <button class="btn btn-primary">${Context.getMessage('label.labssites.edit.valid')}</button>
              </div>
            </form>
          </div>
        </div>
      </section>

      <section>
        <div class="page-header">
          <h3>Import / Export</h3>
        </div>
        <div class="row">
          <div class="span2 columns">
&nbsp;
          </div>
          <div class="span8 columns">
            <a class="btn btn-link" href="/nuxeo/restAPI/default/${mySite.document.id}/exportTree" >Exporter le site</a>
          </div>
        </div>
      </section>

      <section>
        <div class="page-header">
          <h3>Catégories</h3>
        </div>
        <div class="row">
          <div class="span2 columns">
&nbsp;
          </div>
          <div class="span8 columns">

          </div>
        </div>
      </section>
    </div>
  </@block>
</@extends>
<#else>
	<#include "error/error_404.ftl" >
</#if>
