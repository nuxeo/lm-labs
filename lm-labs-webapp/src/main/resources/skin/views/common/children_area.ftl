<script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-twipsy.js"></script>
<script type="text/javascript" src="${skinPath}/js/bootstrap/bootstrap-popover.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery(function () {
			jQuery("a[rel=popover]").popover({offset: 10, html:true});
		});
	});
</script>
<#macro children_block parentDoc spanClass="span5" uniqueId="1" >
	<#if parentDoc.id != Document.id || (parentDoc.id == Document.id && parentDoc.type != 'PageNews')  >
	<#if This.isAuthorizedToDisplay(parentDoc)>
    	<#assign childrenNbr = 0 />
    	<#if parentDoc.type == 'PageNews'>
	    	<#assign children = This.getNews(parentDoc.ref) />
	    	<#assign childrenNbr = children?size />
    	<#else>
	    	<#assign children = Session.getChildren(parentDoc.ref) />
	    	<#list children as child>
	    		<#if !child.facets?seq_contains("HiddenInNavigation") >
	    			<#assign childrenNbr = childrenNbr + 1 />
	    		</#if>
	    	</#list>
    	</#if>
    	<#if childrenNbr &gt; 0 >
        <div id="bloc${parentDoc.id}_${uniqueId}" class="bloc ${spanClass} column">
          <div class="header">
            <a href="${Context.modulePath}/${Common.siteDoc(parentDoc).resourcePath}">Sous-pages de ${parentDoc.title}</a>
          </div>

          <ul class="unstyled">
            <#if parentDoc.type == 'PageNews'>
              <#assign nbNews = 0 />
              <#list children as child>
                <#if nbNews < maxNbLabsNews >
                  <li>${child.title}</li>
                </#if>
                <#assign nbNews = nbNews + 1 />
              </#list>
            <#else>
              <#list children as child>
                <#if !child.facets?seq_contains("HiddenInNavigation") >
                  <#if child.type == 'Folder'>
                    <li>${child.title}</li>
                  <#else>
                	<li><a href="${Context.modulePath}/${Common.siteDoc(child).resourcePath}"
                	  rel="popover" data-content="${child.dublincore.description?html}"
                	  data-original-title="${Context.getMessage('label.description')}"
                	>${child.title}</a></li>
                  </#if>
                </#if>
              </#list>
            </#if>
          </ul>
        </div> <!-- bloc -->
    	</#if>
    </#if> <#-- isAuthorizedToDisplay -->
    </#if>
</#macro>