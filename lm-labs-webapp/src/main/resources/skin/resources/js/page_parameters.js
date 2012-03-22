function openParametersPage(){
    jQuery("#divEditParametersPage").dialog2('open');
    divEditParametersPageForm = jQuery("#divEditParametersPage")[0].innerHTML;
}

function closeParametersPage(){
    jQuery("#divEditParametersPage").dialog2('close');
    jQuery("#divEditParametersPage")[0].innerHTML = divEditParametersPageForm;
}
