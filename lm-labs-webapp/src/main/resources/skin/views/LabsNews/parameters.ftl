<#if This.page??>
<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery("#divEditParametersPage").dialog2({
		height : '385px',
		autoOpen : false,
		closeOnOverlayClick : false,
		removeOnClose : false,
		showCloseHandle : false,
	});
});
</script>
<style>
#form_editParameters .input {
	width: 45%;
}
	
#form_editParameters .input input[type="checkbox"] {
	margin-top: 6px;
	float: left;
}
	
#form_editParameters .input label {
	text-align: left;
	width: 90%;
}
</style>
	
<h1>${Context.getMessage('label.parameters.page.title')}</h1>
<div id="divEditParametersPageForm">
	<form class="form-horizontal" id="form_editParameters" action="${This.path}/@managePage" method="post">
<#-- A GARDER
            <div class="control-group"<#if !This.page.isDisplayable("pg:collapseType") > style="display:none;"</#if> >
              <label class="control-label" for="pg:collapseType">${Context.getMessage('label.parameters.' + Document.type + '.collapseType')}</label>
              <div class="controls">
                <select name="pg:collapseType" class="span4" >
                    <#assign collapseTypesList = Common.collapseTypesList />
                    <#assign currentCollapseType = This.page.collapseType />
                    <#list collapseTypesList?sort as collapseType>
                        <option value="${collapseType}" <#if currentCollapseType == collapseType >selected</#if>>${Context.getMessage('label.parameters.' + Document.type + '.collapseType.' + collapseType)}</option>
                    </#list>
                </select>
              </div>
            </div>
-->
	</form>
	<hr />
	${Context.getMessage('label.parameters.page.usedModel')} <strong>${Context.getMessage('label.doctype.'+This.document.type)}</strong>
</div>
<div class="actions">
    <button id="page-parameters-submit" class="btn btn-primary" >${Context.getMessage('label.parameters.page.save')}</button>
    <button id="page-parameters-close" class="btn" >${Context.getMessage('label.parameters.page.cancel')}</button>
</div>
</#if>
<script>
<#-- A GARDER
<#if This.page?? >
	<#if Document.type == "HtmlPage" >
function showHideCollapseTypeSelect(checkboxObj, selectDivObj) {
	if (jQuery(checkboxObj).is(':checked')) {
		jQuery(selectDivObj).show();
	}
	else {
		jQuery(selectDivObj).hide();
	}
}
	</#if>
</#if>
-->
jQuery(document).ready(function() {
<#if This.page?? >
<#-- A GARDER
	<#if Document.type == "HtmlPage" >
	var selectCollapseTypeDivObj = jQuery('select[name="pg:collapseType"]').closest('div.control-group');
	var collapseTypeCheckboxObj = jQuery('input[name="display-pg:collapseType"]');
	jQuery(collapseTypeCheckboxObj).change(function() {
		showHideCollapseTypeSelect(this, selectCollapseTypeDivObj);
	});
	showHideCollapseTypeSelect(collapseTypeCheckboxObj, selectCollapseTypeDivObj);
	</#if>
-->
    jQuery("#page-parameters-submit").click(function() {
        submitParametersPage();
    });
    jQuery("#page-parameters-close").click(function() {
        closeParametersPage();
    });
</#if>
});
</script>
