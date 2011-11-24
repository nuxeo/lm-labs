<#macro children_block parentDoc spanClass="span5" uniqueId="1" >
    <#if This.isAuthorizedToDisplay(Context.principal.name, Context.principal.anonymous, parentDoc)>
        <div id="bloc${parentDoc.id}_${uniqueId}" class="bloc ${spanClass} column">
          <div class="header">
            <a href="${Context.modulePath}/${Common.siteDoc(parentDoc).resourcePath}">${parentDoc.title}</a>
          </div>

          <ul class="unstyled">
            <#if parentDoc.type == 'PageNews'>
              <#assign nbNews = 0 />
              <#list This.getNews(parentDoc.ref) as child>
                <#if nbNews < maxNbLabsNews >
                  <li>${child.title}</li>
                </#if>
                <#assign nbNews = nbNews + 1 />
              </#list>
            <#else>
              <#list Session.getChildren(parentDoc.ref) as child>
                <#if !child.facets?seq_contains("HiddenInNavigation") >
                  <#if child.type == 'Folder'>
                    <li>${child.title}</li>
                  <#else>
                    	<li><a href="${Context.modulePath}/${Common.siteDoc(child).resourcePath}">${child.title}</a></li>
                  </#if>
                </#if>
              </#list>
            </#if>
          </ul>
        </div> <!-- bloc -->
    </#if>
</#macro>