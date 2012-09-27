<#if This.page??>
<#include "views/common/template_description_js.ftl">
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
    <div class="btn-group" data-toggle="buttons-radio" style="float: right;" >
<#--
command.parameters.page.publish.publish=Publier
command.parameters.page.publish.published=Publiée
command.parameters.page.publish.draft=Brouillon
-->
       <button id="publishBt" class="btn btn-warning<#if This.page.visible> active</#if>">
       <#if This.page.visible>
       ${Context.getMessage('command.parameters.page.publish.published')}
       <#else>
       ${Context.getMessage('command.parameters.page.publish.publish')}
       </#if>
       </button>
       <button id="draftBt" class="btn btn-warning<#if !This.page.visible> active</#if>">
       ${Context.getMessage('command.parameters.page.publish.draft')}
       </button>
    </div>
    <div id="divEditParametersPageForm">
        <form class="form-horizontal" id="form_editParameters" action="${This.path}/@managePage" method="post">
            <div class="control-group">
                <label class="control-label" for="updateTitlePage">${Context.getMessage('label.parameters.page.updateTitlePage')}</label>
                <div class="controls">
                    <input id="updateTitlePage" type="text" name="updateTitlePage" value="${This.document.title?html}" maxlength="90"/>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <label class="checkbox" for="commentablePage">
                    <input class="checkbox" id="commentablePage" type="checkbox" name="commentablePage" <#if This.page.commentable >checked="true"</#if> />
                    ${Context.getMessage('label.parameters.page.authorizedCommentable')}</label>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <label class="checkbox" for="${This.DC_TITLE}">
                    <input class="checkbox" id="${This.DC_TITLE}" type="checkbox" name="${This.DC_TITLE}" value="${This.DC_TITLE}" <#if This.page.isDisplayable(This.DC_TITLE) >checked="true"</#if> />
                    ${Context.getMessage('label.parameters.page.displayableTitlePage')}</label>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <label class="checkbox" for="${This.DC_DESCRIPTION}">
                    <input class="checkbox" id="${This.DC_DESCRIPTION}" type="checkbox" name="${This.DC_DESCRIPTION}" value="${This.DC_DESCRIPTION}" <#if This.page.isDisplayable(This.DC_DESCRIPTION) >checked="true"</#if> />
                    ${Context.getMessage('label.parameters.page.displayableDescriptionPage')}</label>
                </div>
            </div>
            <div class="control-group">
              <label class="control-label" for="template">${Context.getMessage('label.labssites.appearance.template.label')}</label>
              <div class="controls">
                <select name="template" id="template" class="span4" onchange="updateTemplateDescription(this, 'template-description');" >
                    <#include "views/common/getTemplatesMap.ftl">
                    <#assign templatesMap = getTemplatesMap("") />
                    <#assign documentTemplateName = This.page.template.documentTemplateName />
                    <#list templatesMap?sort_by('title') as template>
                        <option value="${template.name}" <#if documentTemplateName == template.name >selected</#if>>${template.title}</option>
                    </#list>
                    <option value="" <#if documentTemplateName == "" >selected</#if>>${Context.getMessage('label.labssites.appearance.templates.none')}</option>
                </select>
                <#assign selectedTemplateName = documentTemplateName />
                <#if selectedTemplateName == "" >
                    <#assign selectedTemplateName = "none" />
                </#if>
                <p id="template-description" class="help-block"><small>${Context.getMessage('label.labssites.appearance.templates.' + selectedTemplateName + '.description')}</small></p>
                <p class="help-block">${Context.getMessage('label.labssites.appearance.template.help.block')}</p>
              </div>
            </div>
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
jQuery(document).ready(function() {
<#if This.page?? >
    jQuery("#page-parameters-submit").click(function() {
        submitParametersPage();
    });
    jQuery("#page-parameters-close").click(function() {
        closeParametersPage();
    });
    <#if This.page.visible>
    jQuery('#draftBt').on('click', draftPage_new);
    <#else>
    jQuery('#publishBt').on('click', publishPage_new);
    </#if>
</#if>
});
</script>