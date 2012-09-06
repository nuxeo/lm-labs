<#assign mySite=Common.siteDoc(Document).getSite() />
<link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/<#if mySite.themeName == "Cyborg">ui-darkness<#elseif mySite.themeName == "Spruce">le-frog<#else>ui-lightness</#if>/jquery-ui-1.8.22.datepicker.slider.css"/>
