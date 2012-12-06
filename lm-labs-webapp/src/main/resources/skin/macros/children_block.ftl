<#assign hiddenSubDocTypes = ["LabsNews", "LMForumTopic"] />
<#import "libs/LabsUtils.ftl" as LabsUtils />

<#macro children_block parentDoc title="" spanClass="span5" showGrandChildPages=false sidebarPosition="left" uniqueId="" displayChevron=false >
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery(function () {
			jQuery(".bloc.children-pages a[rel=popover]").popover({offset: 10, html:true , placement: '<#if sidebarPosition == "left" >right<#elseif sidebarPosition == "right" >left<#else>bottom</#if>'});
		});
	});
</script>
	<#assign mySite=Common.siteDoc(Document).getSite() />
    <#assign isContributor = mySite?? && mySite.isContributor(Context.principal.name) />
	<#if parentDoc.id != Document.id || (parentDoc.id == Document.id <#--&& parentDoc.type != 'PageNews'-->)  >
	<#if parentDoc.type != 'Site' && ((This.isInstanceOf("LabsPage") && This.isAuthorizedToDisplay(parentDoc)) || parentDoc.type == 'Tree') >
    	<#assign childrenNbr = 0 />
    	<#--
    	<#if parentDoc.type == 'PageNews'>
	    	<#assign children = This.getNews(parentDoc.ref) />
	    	<#assign childrenNbr = children?size />
    	<#else>
    	-->
	    	<#assign children = Common.siteDoc(parentDoc).getChildrenPageDocuments() />
	    	<#list children as child>
                <#assign isChildVisible = LabsUtils.isDocumentVisible(child) />
	    		<#if !child.facets?seq_contains("HiddenInNavigation")
                    && ((isContributor && !hiddenSubDocTypes?seq_contains(child.type)) || (!isContributor && isChildVisible)) >
	    			<#assign childrenNbr = childrenNbr + 1 />
	    		</#if>
	    	</#list>
    	<#--
    	</#if>
    	-->
    	<#if 0 < childrenNbr >
        <div<#if 0 < uniqueId?length > id="bloc${parentDoc.id}_${uniqueId}"</#if> class="bloc children-pages ${spanClass} column" <#if spanClass?contains('children-pages') >style="padding: 0 0 0 0;"</#if> >
          <#if 0 < title?length >
          <div class="header">
            <#if parentDoc.id == Document.id >
            ${title}
            <#else>
            <a href="${Context.modulePath}/${Common.siteDoc(parentDoc).getResourcePath()?html}">${title}</a>
            </#if>
          </div>
          </#if>

          <ul class="unstyled nav nav-tabs nav-stacked">
            <#--
            <#if parentDoc.type == 'PageNews'>
              <#assign nbNews = 0 />
              <#list children as child>
                <#if nbNews < maxNbLabsNews >
                  <li>${child.title}</li>
                </#if>
                <#assign nbNews = nbNews + 1 />
              </#list>
            <#else>
            -->
              <#list children as child>
                <#assign isChildVisible = LabsUtils.isDocumentVisible(child) />
                <#if !child.facets?seq_contains("HiddenInNavigation")
                    && ((isContributor && !hiddenSubDocTypes?seq_contains(child.type)) || (!isContributor && isChildVisible)) >
					<#assign childSubPages = [] />
					<#if showGrandChildPages >
						<#assign childSubPages = Common.siteDoc(child).getChildrenNavigablePages(Context.principal.name) />
					</#if>
                	<li<#if 0 < childSubPages?size > class="dropdown"</#if> >
                	  <#assign url = Context.modulePath + "/" + Common.siteDoc(child).resourcePath />
                	  <a href="${url?html}"
                	    <#if 0 < childSubPages?size > class="dropdown-toggle" data-toggle="dropdown" data-target="#" </#if>
                        <#assign childDescr = child['dc:description']?replace("[[TOC]]", "") />
                		<#if (childDescr?length > 0) >
                	  		rel="popover" data-content="${childDescr?html}"
                	  		data-original-title="${Context.getMessage('label.description')}"
                	  	</#if>
                	  ><#if displayChevron ><i style="font-size: 9px;" class="icon-chevron-right bullet-icon" ></i></#if>
                	  <span<#if 0 < childSubPages?size > onclick="stopEventPropagation(event); window.location.href = '${url?html}'; return false;"</#if> >${child.title}</span>
                	<#if 0 < childSubPages?size ><b style="float:right;" class="caret" ></b></#if>
                	  </a>
   					<#if 0 < childSubPages?size >
                    <ul class="dropdown-menu">
                        <#list childSubPages as childSubPage >
                            <#assign url = Context.modulePath + "/" + Common.siteDoc(childSubPage.document).resourcePath />
                        <li><a href="${url?html}" >${childSubPage.document.title}</a></li>
                        </#list>
                    </ul>
					</#if>
                	</li>
                </#if>
              </#list>
            <#--
            </#if>
            -->
          </ul>
        </div> <!-- bloc -->
    	</#if>
    </#if> <#-- isAuthorizedToDisplay -->
    </#if>
</#macro>