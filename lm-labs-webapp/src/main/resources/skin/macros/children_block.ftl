<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery(function () {
			jQuery("a[rel=popover]").popover({offset: 10, html:true${popoverPlacement}});
		});
	});
</script>
<#macro children_block parentDoc title="" spanClass="span5" uniqueId="1">
	<#assign mySite=Common.siteDoc(Document).getSite(Context.coreSession) />
    <#assign isContributor = mySite?? && mySite.isContributor(Context.principal.name, Context.coreSession) />
	<#if parentDoc.id != Document.id || (parentDoc.id == Document.id <#--&& parentDoc.type != 'PageNews'-->)  >
	<#if parentDoc.type != 'Site' && ((This.isInstanceOf("LabsPage") && This.isAuthorizedToDisplay(parentDoc)) || parentDoc.type == 'Tree') >
    	<#assign childrenNbr = 0 />
    	<#--
    	<#if parentDoc.type == 'PageNews'>
	    	<#assign children = This.getNews(parentDoc.ref) />
	    	<#assign childrenNbr = children?size />
    	<#else>
    	-->
	    	<#assign children = Common.siteDoc(parentDoc).getChildrenPageDocuments(Context.coreSession) />
	    	<#list children as child>
                <#assign isChildVisible = false />
                <#if child.type != 'LabsNews' >
                        <#assign childSitePage = Common.sitePage(child) />
                        <#if childSitePage?? && childSitePage.visible >
                                <#assign isChildVisible = true />
                        </#if>
                <#else>
                        <#assign isChildVisible = true />
                </#if>
	    		<#if !child.facets?seq_contains("HiddenInNavigation")
                    && (isContributor || (!isContributor && isChildVisible)) >
	    			<#assign childrenNbr = childrenNbr + 1 />
	    		</#if>
	    	</#list>
    	<#--
    	</#if>
    	-->
    	<#if childrenNbr &gt; 0 >
        <div id="bloc${parentDoc.id}_${uniqueId}" class="bloc ${spanClass} column">
          <#if title?length == 0 >
            <#assign title = "Sous-pages de " + parentDoc.title />
          </#if>
          <div class="header">
            <#if parentDoc.id == Document.id >
            ${title}
            <#else>
            <a href="${Context.modulePath}/${Common.siteDoc(parentDoc).getResourcePath(Context.coreSession)?html}">${title}</a>
            </#if>
          </div>

          <ul class="unstyled">
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
                <#assign isChildVisible = false />
                <#if child.type != 'LabsNews' >
                        <#assign childSitePage = Common.sitePage(child) />
                        <#if childSitePage?? && childSitePage.visible >
                                <#assign isChildVisible = true />
                        </#if>
                <#else>
                        <#assign isChildVisible = true />
                </#if>
                <#if !child.facets?seq_contains("HiddenInNavigation")
                    && (isContributor || (!isContributor && isChildVisible)) >
                	<li><a href="${Context.modulePath}/${Common.siteDoc(child).getResourcePath(Context.coreSession)?html}"
                		<#if (child.dublincore.description?length > 0) >
                	  		rel="popover" data-content="${child.dublincore.description?html}"
                	  		data-original-title="${Context.getMessage('label.description')}"
                	  	</#if>
                	><i style="font-size: 9px; text-decoration: none;" class="icon-chevron-right" ></i>${child.title}</a></li>
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