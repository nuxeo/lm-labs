<#include "macros/externalLinksList.ftl"/>
<#if !mySite?? >
	<#assign mySite=Common.siteDoc(Document).site />
</#if>
<ul class="nav nav-tabs">
	<li class="active" ><a href="#configGadgetExternalLinksListDiv" data-toggle="tab" >${Context.getMessage('label.externalURL.tab.list.title')}</a></li>
	<li><a href="#configGadgetAddExternalLinksDiv" data-toggle="tab" >${Context.getMessage('label.externalURL.tab.add.title')}</a></li>
</ul>
<div class="tab-content">
	<div class="tab-pane active" id ="configGadgetExternalLinksListDiv" >
		<ul class="unstyled">
		<#list mySite.externalURLs as e >
		   <li data-docid="${e.document.id}" >
			    <input type="hidden" name="name" value="${e.name}" />
			    <input type="hidden" name="url" value="${e.URL}" />
			    <#--
			    <a href="${e.URL}" style="word-wrap: break-word;" target="_blank" title="${e.URL}">${e.name}</a>
			    -->
			    <span style="word-wrap: break-word;" title="${e.URL}">${e.name}</span>
				<div class="pull-right">
				<a href="#" class="btn btn-mini" title="${Context.getMessage('command.externalURL.up')}" alt="${Context.getMessage('command.externalURL.up')}" 
					onClick="javascript:widgetMoveUpExternalURL(this, '${Context.modulePath}/${mySite.URL}/@externalURL/${e.document.id}');"><i class="icon-arrow-up"></i></a>
				<a href="#" class="btn btn-mini" title="${Context.getMessage('command.externalURL.down')}" alt="${Context.getMessage('command.externalURL.down')}" 
					onClick="javascript:widgetMoveDownExternalURL(this, '${Context.modulePath}/${mySite.URL}/@externalURL/${e.document.id}');"><i class="icon-arrow-down"></i></a>
				<a href="#" class="btn btn-mini btn-primary" title="${Context.getMessage('command.externalURL.edit')}" alt="${Context.getMessage('command.externalURL.edit')}" 
					onClick="javascript:widgetModifyExternalURL(this);"><i class="icon-edit"></i></a>
				<a href="#" class="btn btn-mini btn-danger" title="${Context.getMessage('command.externalURL.delete')}" alt="${Context.getMessage('command.externalURL.delete')}" 
					onClick="if (confirm('${Context.getMessage('label.externalURL.delete.confirm')}')) { startRefreshExternalLinksWidgets();externalLinksHelper.deleteExternalURL('${Context.modulePath}/${mySite.URL}/@externalURL/${e.document.id}/ajax', refreshExternalLinksDialog, errorDeleteCallback, completeDeleteCallback);}"><i class="icon-remove"></i></a>
				</div>
		    </li>
		</#list>
		</ul>
		<div class="editExternalLink" style="display: none;" >
		  <h1>${Context.getMessage('label.externalURL.edit.title')}</h1>
		  <form class="form-horizontal" method="post" id="editExternalLink-form" enctype="multipart/form-data"
		  	action="${Context.modulePath}/${mySite.URL}/@externalURL">
		  	<fieldset>
		  	  <div class="control-group">
		        <label class="control-label" for="exturl:name">${Context.getMessage('label.externalURL.edit.name')}</label>
		        <div class="controls">
		          <input type="text" class="input required" name="exturl:name" />
		        </div>
		  	  </div>
		  	  <div class="control-group">
		        <label class="control-label" for="exturl:url">${Context.getMessage('label.externalURL.edit.url')}</label>
		        <div class="controls">
		          <input type="text" class="required input-xlarge " name="exturl:url" />
		        </div>
		  	  </div>
		  	</fieldset>
		    <div class="actions">
		      <button class="btn btn-primary required-fields" form-id="editExternalLink-form">${Context.getMessage('command.externalURL.edit.submit')}</button>
		    </div>
		  </form>
		</div>
	</div>
	<div class="tab-pane" id ="configGadgetAddExternalLinksDiv" >
	  <input type="hidden" name="isModify" value="false" />
	  <input type="hidden" name="addUrl" value="${Context.modulePath}/${mySite.URL}/@externalURL" />
	  <form class="form-horizontal" method="post" id="addExternalLink-form" enctype="multipart/form-data"
	  	action="${Context.modulePath}/${mySite.URL}/@externalURL">
	  	<fieldset>
	  	  <div class="control-group">
	        <label class="control-label" for="exturl:name">${Context.getMessage('label.externalURL.edit.name')}</label>
	        <div class="controls">
	          <input type="text" class="input required" name="exturl:name" />
	        </div>
	  	  </div>
	  	  <div class="control-group">
	        <label class="control-label" for="exturl:url">${Context.getMessage('label.externalURL.edit.url')}</label>
	        <div class="controls">
	          <input type="text" class="required input-xlarge " name="exturl:url" />
	        </div>
	  	  </div>
	  	</fieldset>
	    <div class="actions">
	      <button class="btn btn-primary required-fields" form-id="addExternalLink-form">${Context.getMessage('command.externalURL.edit.submit')}</button>
	    </div>
	  </form>
	</div>
</div>
<script type="text/javascript">
function refreshExternalLinksDialog(msg) {
	refreshConfigGadgetDialog();
}

function widgetInitFieldsExternalURL(formObj) {
  jQuery(formObj).clearForm();
  jQuery(formObj).find('input[name="exturl:url"]').val('http://');
  jQuery(formObj).find('div.control-group.error').removeClass("error");
  jQuery(formObj).siblings('input[type="hidden"][name="isModify"]').val('false');
  jQuery(formObj).attr('action', jQuery(formObj).siblings('input[type="hidden"][name="addUrl"]').val());
}

function widgetModifyExternalURL(obj) {
	var liObj = jQuery(obj).closest('li');
	var name = jQuery(liObj).find('input[type="hidden"][name="name"]').val();
	var url = jQuery(liObj).find('input[type="hidden"][name="url"]').val();
	jQuery('#configGadgetAddExternalLinksDiv > input[type="hidden"][name="isModify"]').val('true');
	jQuery('#configGadgetAddExternalLinksDiv form').find('input[name="exturl:name"]').val(name);
	jQuery('#configGadgetAddExternalLinksDiv form').attr('action', jQuery('#configGadgetAddExternalLinksDiv form').siblings('input[type="hidden"][name="addUrl"]').val() + '/' + jQuery(liObj).data('docid') + '/@put');
	jQuery('#configGadgetAddExternalLinksDiv form').find('input[name="exturl:url"]').val(url);
	jQuery('#configGadgetAddExternalLinksDiv form').find('div.control-group.error').removeClass("error");
	jQuery(obj).closest('div.tab-content').siblings('ul.nav-tabs').find('a[href="#configGadgetAddExternalLinksDiv"]').tab('show');
}

function widgetMoveDownExternalURL(obj, url) {
	var found = false;
	var after;
	var current;
	var id = jQuery(obj).closest('li').data('docid');
	
	jQuery(obj).closest('ul').find('li').each(function (index, element){
		if (found){
			after = element;
			return false;
		}
		if (jQuery(element).data('docid') == id){
			found = true;
			current = element;
		}
	});
	
	jQuery(current).insertAfter(after);
	
	if(jQuery(after).data('docid') != undefined){
		jQuery('#waitingPopup').dialog2('open');
		externalLinksHelper.ajaxMoveExtURL(url + "/@moveDownExternalURL/" + jQuery(after).data('docid'), successMoveCallback, errorMoveCallback);
	}
}

function widgetMoveUpExternalURL(obj, url){
	var before;
	var current;
	var id = jQuery(obj).closest('li').data('docid');
	
	jQuery(obj).closest('ul').find('li').each(function (index, element){
		if (jQuery(element).data('docid') == id){
			current = element;
			return false;
		}
		else{
			before = element;
		}
	});
	jQuery(before).insertAfter(current);
	
	if(jQuery(before).data('docid') != undefined){
		jQuery('#waitingPopup').dialog2('open');
		externalLinksHelper.ajaxMoveExtURL(url + "/@moveUpExternalURL/" + jQuery(before).data('docid'), successMoveCallback, errorMoveCallback);
	}
}

function startRefreshExternalLinksWidgets() {
	jQuery('.external-links > div.loading-image').show();
    jQuery('.external-links > ul').hide();
}

function completeRefreshExternalLinksWidgets() {
	jQuery('.external-links > ul').load('${Context.modulePath}/${mySite.URL}/@views/externalLinksList');
	jQuery('.external-links > div.loading-image').hide();
	jQuery('.external-links > ul').show();
}

function errorDeleteCallback(xhr) {
	alert( xhr.responseText );
}

function completeDeleteCallback(xhr) {
	completeRefreshExternalLinksWidgets();
}

function successMoveCallback(msg) {
	jQuery('#waitingPopup').dialog2('close');
	completeRefreshExternalLinksWidgets();
}

function errorMoveCallback(msg) {
	//alert( msg.responseText );
	jQuery('#waitingPopup').dialog2('close');
	alert('${Context.getMessage('label.externalURL.error')}');
	refreshConfigGadgetDialog();
}

jQuery(document).ready(function(){
	jQuery('#configGadgetAddExternalLinksDiv').find('form').ajaxForm(function() { 
		refreshConfigGadgetDialog();
		completeRefreshExternalLinksWidgets();
	}); 
    jQuery('#divConfigGadget-content a[data-toggle="tab"]').on('shown', function (e) {
        var pattern=/#.+/gi //use regex to get anchor(==selector)
        var contentID = e.target.toString().match(pattern)[0];   
    	if (contentID == '#configGadgetAddExternalLinksDiv') {
    		var isModify = jQuery(contentID).find('input[type="hidden"][name="isModify"]').val();
    		if (isModify === 'false') {
	    		widgetInitFieldsExternalURL(jQuery(contentID).find('form'));
    			jQuery(contentID).closest('div.tab-content').siblings('ul.nav-tabs').find('a[href="#configGadgetAddExternalLinksDiv"]').html('${Context.getMessage('label.externalURL.tab.add.title')}');
    		} else {
    			jQuery(contentID).closest('div.tab-content').siblings('ul.nav-tabs').find('a[href="#configGadgetAddExternalLinksDiv"]').html('${Context.getMessage('label.externalURL.tab.edit.title')}');
    		}
    	} else {
			jQuery('#configGadgetAddExternalLinksDiv > input[type="hidden"][name="isModify"]').val('false');
			jQuery('#configGadgetAddExternalLinksDiv').closest('div.tab-content').siblings('ul.nav-tabs').find('a[href="#configGadgetAddExternalLinksDiv"]').html('${Context.getMessage('label.externalURL.tab.add.title')}');
    	}
    });
});
</script>