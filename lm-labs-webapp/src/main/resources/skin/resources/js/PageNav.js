var isOpen = false;
function openPropsPageNav(){
	jQuery("#editprops").slideDown("slow");
	jQuery("#btnModifyPropsPageNav").html("<i class='icon-eye-close'></i>Fermer les critères");
	isOpen = true;
}

function closePropsPageNav(){
	jQuery("#editprops").slideUp("slow");
	jQuery("#btnModifyPropsPageNav").html("<i class='icon-eye-open'></i>Modifier les critères");
	isOpen = false;
}

function actionPropsPageNav(){
	if(isOpen == false){
		openPropsPageNav();
	}
	else{
		closePropsPageNav();
	}
}
