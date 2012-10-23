<@extends src="/views/TemplatesBase/" + This.page.template.getTemplateName() + "/template.ftl">
<#setting url_escaping_charset="UTF-8">
  <@block name="title">${Common.siteDoc(Document).getSite().title}-${This.document.title}</@block>

    <@block name="css">
      <@superBlock/>
		<link rel="stylesheet" type="text/css" media="all" href="${contextPath}/wro/labs.search.css"/>
    </@block>

    <@block name="scripts">
      <@superBlock/>
		<script type="text/javascript" src="${contextPath}/wro/labs.search.js"></script>
    </@block>

    <@block name="content">

		<@block name="pageTags"/>

    <script type="text/javascript">
      jQuery(document).ready(function() {
        jQuery("#resultsSearch").tablesorter({
            sortList: [[1,0]],
            headers: {
              0: {
                sorter:false
              },
              5: {
                sorter:false
              }
            },
            textExtraction: function(node) {
                  // extract data from markup and return it
              var sortValues = jQuery(node).find('span[class=sortValue]');
              if (sortValues.length > 0) {
                return sortValues[0].innerHTML;
              }
                  return node.innerHTML;
              }
        });
      });
    </script>

    <div class="">
    <section>
      <div class="page-header">
        <h1>${Context.getMessage('label.search.title')}</h1>
        <#if result?size &gt; 0>
          <small>${Context.getMessage('label.search.displayResults', result?size, result?size)} <span>${fullText?html}</small>
        </#if>
      </div>
    <#if result?size &gt; 0>
      <table class="table table-striped table-bordered bs labstable" id="resultsSearch" >
        <thead>
          <tr>
          	<th class="header">&nbsp;</th>
          	<th class="header">${Context.getMessage('label.search.head.title')}</th>
          	<th class="header">${Context.getMessage('label.search.head.lastModification')}</th>
          	<th class="header">${Context.getMessage('label.search.head.size')}</th>
          	<th class="header">${Context.getMessage('label.search.head.page')}</th>
          	<th class="header">&nbsp;</th>
          </tr>
        </thead>
        <tbody>
          <#assign fullTextHighlightURL = Context.request.getParameter('fullText') />
          <#if (fullTextHighlightURL != null && fullTextHighlightURL != "") >
          	<#assign fullTextHighlightURL = "?fullText=" + fullTextHighlightURL?url />
          </#if>
          <#list result as doc>
            <#assign sd = Common.siteDoc(doc) />
            <#assign formattedFilesize = "(" + Context.getMessage('label.search.result.noFile') + ")" />
            <#assign filesize = 0 />
            <#assign hasFile = false />
            <#if sd.blobHolder?? && sd.blobHolder.blob != null >
              <#assign hasFile = true />
              <#assign filesize = sd.blobHolder.blob.length />
              <#assign formattedFilesize = bytesFormat(filesize, "K", "fr_FR") />
            </#if>
            <tr>
              <td class="colIcon"><img title="${doc.type}" alt="&gt;&gt;" <#if doc.schemas?seq_contains("common") >src="/nuxeo/${doc.common.icon}"</#if> /></td>

                <td>
            	
            		<#assign url = Context.modulePath + "/" + sd.resourcePath + fullTextHighlightURL />
            		<#assign nofollow = false />
            		<#if !hasFile>
						<#if (Context.modulePath + "/" + sd.getParentPage().getPath() == Context.modulePath + "/" + sd.getResourcePath())>
	                		<#assign url = Context.modulePath + "/" + sd.parentPage.path + fullTextHighlightURL />
						</#if>
            		<#elseif doc.type != "LabsNews">
                		<#assign url = Context.modulePath + "/" + sd.resourcePath + "/@blob/preview" />
                		<#assign nofollow = true />
            		</#if>
					<a<#if nofollow> rel="nofollow"</#if> href="${url}"<#if nofollow> target="_blank"</#if>>
						<#if (doc['dc:title']?length > 0)>
							${doc['dc:title']}
		            	<#else>
		          			(${Context.getMessage('label.search.result.noTitle')})
		          		</#if>
					</a>            		
                </td>
                <td>${userFullName(doc.dublincore.lastContributor)}</td>
              <td class="colFilesize">${formattedFilesize}<span class="sortValue">${filesize?string.computer}</span></td>

              <td>
              	<a href="${Context.modulePath}/${sd.getParentPage().getPath()}${fullTextHighlightURL}">${sd.getParentPage().title}</a>
              </td>
                <td>
                  <#if !hasFile>
                  <a href="#" class="btn disabled">
                  <#elseif doc.type != "LabsNews">
                  <a rel="nofollow" href="${Context.modulePath}/${sd.getResourcePath()}/@blob/preview" target="_blank" class="btn">
                  </#if>
                  <#if !hasFile || doc.type != "LabsNews">
                  <i class="icon-eye-open"></i>${Context.getMessage('command.LabsSite.latestuploads.display')}</a>
                  </#if>
                  <#if !hasFile>
                  <a disabled="disabled" href="#" class="btn disabled">
                  <#elseif doc.type != "LabsNews">
                  <a rel="nofollow" href="${Context.modulePath}/${sd.getResourcePath()}/@blob" target="_blank" class="btn">
                  </#if>
                  <#if !hasFile || doc.type != "LabsNews">
                  <i class="icon-download"></i>${Context.getMessage('command.LabsSite.latestuploads.download')}</a>
                  </#if>
                </td>
            </tr>
          </#list>
        </tbody>
      </table>
    <#else>
      <div class="resultsSearchSubtitle">Pas de résultats pour <span>${fullText?html}</span></div>
    </#if>


    <#if Context.principal.administrator >
    <small>Query performed: ${query}</small>
    </#if>

  </section>
</div>
</@block>
<@block name="pageCommentable">
</@block>
</@extends>




