<#function shortDateTime calendar >
	<#assign todayDate = Common.getNow().time?datetime?date>
	<#assign calendarDate = calendar?date>
	<#if calendarDate?date?string.short == todayDate?date?string.short >
		<#assign aTime = calendar?time >
		<#return aTime?string.short>
	<#else>
		<#assign aDate = calendar?date>
		<#return aDate?string.short>
	</#if>
</#function>

<#function validDocProperty property doc >
	<#if property?length == 0 >
		<#return false >
	</#if>
	<#if !property?contains(":") >
		<#return false >
	</#if>
	<#assign schema = property?split(":", 'f') />
	<#return true >
</#function>

<#function isDocumentVisible doc >
    <#assign isChildVisible = false />
    <#if doc.type != 'LabsNews' >
        <#assign childSitePage = Common.sitePage(doc) />
        <#if childSitePage?? && childSitePage.visible && !childSitePage.hiddenInNavigation >
            <#assign isChildVisible = true />
        </#if>
    <#else>
        <#assign isChildVisible = false />
    </#if>
    <#return isChildVisible >
</#function>

<#function getHomePageDocIdsTab doc >
	<#assign mySite = Common.siteDoc(doc).site />
	<#assign parents = Session.getParentDocuments(mySite.indexDocument.ref) />
	<#assign ids = [] />
	<#list parents?reverse as parent>
		<#if parent.type == "Tree" >
			<#break>
		</#if>
		<#assign ids = ids + [ parent.id ] />
	</#list>
	<#return ids >
</#function>

<#function getHomePageDocIdSelectorsStr doc >
	<#assign ids = getHomePageDocIdsTab(doc) />
	<#assign parentIds = "[" />
	<#list ids as id >
		<#if 0 < id_index >
			<#assign parentIds = parentIds + "," />
		</#if>
		<#assign parentIds = parentIds + "'#" + id + "'" />
	</#list>
	<#assign parentIds = parentIds + "]" />
	<#return parentIds >
</#function>

<#function getTopNavigationPageDoc doc >
	<#assign parents = Session.getParentDocuments(doc.ref) />
	<#assign topNavPageDoc = doc />
	<#list parents?reverse as parent>
		<#if parent.type == "Tree" >
			<#break>
		</#if>
		<#assign topNavPageDoc = parent />
	</#list>
	<#return topNavPageDoc >
</#function>
