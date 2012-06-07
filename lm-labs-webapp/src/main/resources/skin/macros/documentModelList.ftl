<#import "libs/LabsUtils.ftl" as LabsUtils />

<#macro documentModelList documentModelList divId divTitle tooltipDocProp detailedPageUrl="" divClass="" showDate=false >
<div class="bloc ${divClass}" id="${divId}" >
	<div class="header">${divTitle}</div>
	<ul class="unstyled">
	<#list documentModelList as doc >
		<#assign modifDate = doc.dublincore.modified?datetime />
		<#assign modifDateStr = modifDate?string("EEEE dd MMMM yyyy HH:mm") />
		<li>
    		<#if showDate >
			<#--
			<span class="label label-info" title="${modifDateStr}" >${Context.getMessage('label.PageClasseur.table.dateInWordsFormat',[dateInWords(modifDate)])}</span>
			-->
			<span class="label label-info" title="${modifDateStr}" >${LabsUtils.shortDateTime(doc.dublincore.modified)?trim}</span>
    		</#if>
			<a href="${Root.getLink(doc)}" title="<#if 0 < doc[tooltipDocProp] >${doc[tooltipDocProp]}</#if>" >
    		<#if !showDate >
    		<i style="font-size: 9px; text-decoration: none;" class="icon-chevron-right" ></i>
    		</#if>
			${doc.title}
			</a>
		</li>
	</#list>
	</ul>
	<#if detailedPageUrl != "" >
	<a class="btn btn-mini" href="${detailedPageUrl}"><i class="icon-list"></i>Détails</a>
	</#if>
</div>
</#macro>
