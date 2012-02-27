<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">

  <@block name="title">${site.title}-${This.document.title}</@block>

    <@block name="scripts">
      <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery.tablesorter.min.js"></script>
    </@block>

    <@block name="css">
      <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/tablesorter.css"/>
    </@block>

    <@block name="content">


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
          <small>${Context.getMessage('label.search.displayResults', result?size, result?size)} <span>${query?split('"')[1]}</small>
        </#if>
      </div>
    <#if result?size &gt; 0>
      <table class="table table-striped table-bordered bs" id="resultsSearch" >
        <thead>
          <tr>
          	<th></th>
          	<th>${Context.getMessage('label.search.head.title')}</th>
          	<th>${Context.getMessage('label.search.head.lastModification')}</th>
          	<th>${Context.getMessage('label.search.head.size')}</th>
          	<th>${Context.getMessage('label.search.head.page')}</th>
          	<th></th>
          </tr>
        </thead>
        <tbody>
          <#list result as doc>
            <#assign sd = Common.siteDoc(doc) />
            <tr>
              <td class="colIcon"><img title="${doc.type}" alt="&gt;&gt;" <#if doc.schemas?seq_contains("common") >src="/nuxeo/${doc.common.icon}"</#if> /></td>

                <td>
                	<#if (doc.dublincore.title?length > 0)>
                		<a href="${Context.modulePath}/${sd.resourcePath}" target="_blank">${doc.dublincore.title}</a>
                	<#else>
              			(${Context.getMessage('label.search.result.noTitle')})
              		</#if>
                </td>
                <td>${userFullName(doc.dublincore.lastContributor)}</td>
                <#assign formattedFilesize = "(" + Context.getMessage('label.search.result.noFile') + ")" />
                <#assign filesize = 0 />
                <#assign hasFile = false />
                <#if sd.blobHolder?? && sd.blobHolder.blob != null >
                  <#assign hasFile = true />
                  <#assign filesize = sd.blobHolder.blob.length />
                  <#assign formattedFilesize = bytesFormat(filesize, "K", "fr_FR") />
                </#if>
              <td class="colFilesize">${formattedFilesize}<span class="sortValue">${filesize?string.computer}</span></td>

              <td>
              	<a href="${Context.modulePath}/${sd.parentPage.path}">${sd.parentPage.title}</a>
              </td>
                <td>
                  <#if !hasFile>
                  <a href="#" class="btn disabled">
                  <#elseif sd.document.type != "LabsNews">
                  <a href="${Context.modulePath}/${sd.resourcePath}/@blob/preview" target="_blank" class="btn">
                  </#if>
                  <i class="icon-eye-open"></i>${Context.getMessage('command.LabsSite.latestuploads.display')}</a>
                  <#if !hasFile>
                  <a disabled="disabled" href="#" class="btn disabled">
                  <#elseif sd.document.type != "LabsNews">
                  <a href="${Context.modulePath}/${sd.resourcePath}/@blob" target="_blank" class="btn">
                  </#if>
                  <i class="icon-download"></i>${Context.getMessage('command.LabsSite.latestuploads.download')}</a>
                </td>
            </tr>
          </#list>
        </tbody>
      </table>
    <#else>
      <div class="resultsSearchSubtitle">Pas de résultats pour <span>${query?split('"')[1]}</span></div>
    </#if>


    <#if Context.principal.administrator >
    <small>Query performed: ${query}</small>
    </#if>

  </section>
</div>
</@block>
</@extends>




