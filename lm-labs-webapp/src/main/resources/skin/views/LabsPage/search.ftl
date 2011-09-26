<@extends src="/views/labs-base.ftl">

  <@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>

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

    <div class="container">
    <section>
      <div class="page-header">
        <h1>Résultats de recherche</h1>
        <#if result?size &gt; 0>
          <small>Affichage des résultats 1-${result?size} sur ${result?size} pour <span>${query?split('"')[1]}</small>
        </#if>
      </div>
    <#if result?size &gt; 0>
      <table class="zebra-striped" id="resultsSearch" >
        <thead>
          <tr><th></th><th>Titre</th><th>Dernière Modification</th><th>Taille Fichier</th><th>Page</th><th></th></tr>
        </thead>
        <tbody>
          <#list result as doc>
            <#assign sd = Common.siteDoc(doc) />
            <tr>
              <td class="colIcon"><img title="${doc.type}" alt="&gt;&gt;" <#if doc.schemas?seq_contains("common") >src="/nuxeo/${doc.common.icon}"</#if> /></td>

                <td><a href="${Context.modulePath}/${sd.resourcePath}" target="_blank">${doc.dublincore.title}</a></td>
                <td>${userFullName(doc.dublincore.lastContributor)}</td>
                <#assign formattedFilesize = "(Pas de fichier)" />
                <#assign filesize = 0 />
                <#if sd.blobHolder?? && sd.blobHolder.blob != null >
                  <#assign filesize = sd.blobHolder.blob.length />
                  <#assign formattedFilesize = bytesFormat(filesize, "K", "fr_FR") />
                </#if>
              <td class="colFilesize">${formattedFilesize}<span class="sortValue">${filesize?string.computer}</span></td>

              <td><a href="${Context.modulePath}/${sd.page.path}">${sd.page.title}</a></td>
                <td>
                    <a href="${Context.modulePath}/${sd.resourcePath}/@blob/preview" target="_blank" class="btn">${Context.getMessage('command.LabsSite.latestuploads.display')}</a>
                    <a href="${Context.modulePath}/${sd.resourcePath}/@blob" class="btn">${Context.getMessage('command.LabsSite.latestuploads.download')}</a>
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




