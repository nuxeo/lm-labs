<#if This.type.name != "sitesRoot" && !mySite?? >
   <#assign mySite=Common.siteDoc(Document).getSite() />
</#if>
<#assign isAdministrator = (mySite?? && mySite.isAdministrator(Context.principal.name) ) />
<#assign isContributor = (mySite?? && mySite.isContributor(Context.principal.name) ) />

<script type="text/javascript">

<#if isAdministrator >
function publishSite(){
    if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouPublish')}")){
        jQuery('#waitingPopup').dialog2('open');
        jQuery.ajax({
            type: 'PUT',
            async: false,
            url: '${Context.modulePath}/${mySite.URL}/@labspublish/publish',
            success: function(data) {
                if (data == 'publish') {
                  //alert("${Context.getMessage('label.lifeCycle.site.hasPublished')}");
                  document.location.href = '${This.path}';
                }
                else {
                  alert("${Context.getMessage('label.lifeCycle.site.hasNotPublished')}");
                  jQuery('#waitingPopup').dialog2('open');
                }
            },
            error: function(data) {
                jQuery('#waitingPopup').dialog2('close');
                alert("${Context.getMessage('label.lifeCycle.site.hasNotPublished')}");
            }
        });
    }
}

function draftSite(){
    if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouDraft')}")){
        jQuery('#waitingPopup').dialog2('open');
        jQuery.ajax({
            type: 'PUT',
            async: false,
            url: '${Context.modulePath}/${mySite.URL}/@labspublish/draft',
            success: function(data) {
                if (data == 'draft') {
                  //alert("${Context.getMessage('label.lifeCycle.site.hasDrafted')}");
                  document.location.href = '${This.path}';
                }
                else {
                  alert("${Context.getMessage('label.lifeCycle.site.hasNotDrafted')}");
                  jQuery('#waitingPopup').dialog2('open');
                }
            },
            error: function(data) {
                jQuery('#waitingPopup').dialog2('close');
                alert("${Context.getMessage('label.lifeCycle.site.hasNotDrafted')}");
            }
        });
    }
}

function deleteSite(){
    if (confirm("${Context.getMessage('label.lifeCycle.site.wouldYouDelete')}")){
        jQuery('#waitingPopup').dialog2('open');
        jQuery.ajax({
            type: 'PUT',
            async: false,
            url: '${Context.modulePath}/${mySite.URL}/@labspublish/delete',
            success: function(data) {
                if (data == 'delete') {
                  alert("${Context.getMessage('label.lifeCycle.site.hasDeleted')}");
                  document.location.href = '${Context.modulePath}';
                }
                else {
                  alert("${Context.getMessage('label.lifeCycle.site.hasNotDeleted')}");
                  jQuery('#waitingPopup').dialog2('close');
                }
            },
            error: function(data) {
                jQuery('#waitingPopup').dialog2('close');
            }
        });
    }
}
</#if>

<#if isContributor >
function publishPage(){
    if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouPublish')}")){
        jQuery('#waitingPopup').dialog2('open');
        jQuery.ajax({
            type: 'PUT',
            async: false,
            url: '${This.path}/@labspublish/publish',
            success: function(data) {
                if (data == 'publish') {
                  <#--alert("${Context.getMessage('label.lifeCycle.page.hasPublished')}");-->
                  document.location.href = '${This.path}';
                }
                else {
                  alert("${Context.getMessage('label.lifeCycle.page.hasNotPublished')}");
                  jQuery('#waitingPopup').dialog2('close');
                }
            },
            error: function(data) {
                jQuery('#waitingPopup').dialog2('close');
            }
        });
    }
}

function publishPage_new() {
    alert('publishPage_new');
    //if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouPublish')}")){
        jQuery.ajax({
            type: 'PUT',
            async: false,
            url: '${This.path}/@labspublish/publish',
            success: function(data) {
                pagePublishStateChanged = true;
                jQuery('#publishBt').html('${Context.getMessage('command.parameters.page.publish.published')}');
                jQuery('#publishBt').off('click', publishPage_new);
                jQuery('#draftBt').on('click', draftPage_new);
                /*if (data == 'publish') {
                  alert("${Context.getMessage('label.lifeCycle.page.hasPublished')}");
                  document.location.href = '${This.path}';
                }
                else {
                  alert("${Context.getMessage('label.lifeCycle.page.hasNotPublished')}");
                }*/
            }
        });
    //}
    return false;
}

function draftPage(){
    if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouDraft')}")){
        jQuery('#waitingPopup').dialog2('open');
        jQuery.ajax({
            type: 'PUT',
            async: false,
            url: '${This.path}/@labspublish/draft',
            success: function(data) {
                if (data == 'draft') {
                  alert("${Context.getMessage('label.lifeCycle.page.hasDrafted')}");
                  document.location.href = '${This.path}';
                }
                else {
                  alert("${Context.getMessage('label.lifeCycle.page.hasNotDrafted')}");
                  jQuery('#waitingPopup').dialog2('close');
                }
            },
            error: function(data) {
                jQuery('#waitingPopup').dialog2('close');
            }
        });
    }
}

function draftPage_new() {
    alert('draftPage_new');
    //if (confirm("${Context.getMessage('label.lifeCycle.page.wouldYouDraft')}")){
        jQuery.ajax({
            type: 'PUT',
            async: false,
            url: '${This.path}/@labspublish/draft',
            success: function(data) {
                pagePublishStateChanged = true;
                jQuery('#publishBt').html('${Context.getMessage('command.parameters.page.publish.publish')}');
                jQuery('#draftBt').off('click', draftPage_new);
                jQuery('#publishBt').on('click', publishPage_new);
                /*if (data == 'draft') {
                  alert("${Context.getMessage('label.lifeCycle.page.hasDrafted')}");
                  document.location.href = '${This.path}';
                }
                else {
                  alert("${Context.getMessage('label.lifeCycle.page.hasNotDrafted')}");
                }*/
            }
        });
    //}
    return false;
}

function submitParametersPage(){
    jQuery("#form_editParameters").submit();
}
</#if>
</script>
