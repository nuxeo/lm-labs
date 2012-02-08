<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
<#include "views/common/paging.ftl" />
<#assign nbrElemPerPage = 20 />

  <@block name="title">${site.title}-${This.document.title} - ${Context.getMessage('title.LabsSite.latestuploads')}</@block>

  <@block name="content">
  <div class="container">
    <section>
      <div class="page-header">
        <h1>${Context.getMessage('title.LabsSite.latestuploads')}</h1>
      </div>

  <div class="row">
  <div class="span16 columns">
      <#assign pp = latestUploadsPageProvider(Document, nbrElemPerPage) />
      <#assign currentPage = Context.request.getParameter('page')?number?long />
      <#assign uploads = pp.setCurrentPage(currentPage) />
      <@resultsStatus pageProvider=pp />
      <@paging pageProvider=pp url=This.path+"/@views/latestuploads?page=" />

      <div class="latestuploads">
        <table class="zebra-striped bs">
          <thead>
            <tr>
              <th>&nbsp;</th>
              <th>Titre</th>
              <th>Modifié le</th>
              <th>${Context.getMessage('label.LabsSite.latestuploads.onpage')} </th>
              <th>&nbsp;</th>
          </thead>
          <tbody>
          <#list uploads as upload >
          <tr>
            <td><img title="${upload.type}" alt="&gt;" src="/nuxeo/${upload.common.icon}" /></td>
            <td>${upload.title}</td>
            <#assign modifDate = upload.dublincore.modified?datetime >
            <td>${modifDate?string("EEEE dd MMMM yyyy HH:mm")}</td>
            <#assign sd = Common.siteDoc(upload) />
            <td><a href="${Context.modulePath}/${sd.parentPagePath}">${sd.parentPage.title}</a></td>
            <td>
                <a rel="nofollow" href="${Context.modulePath}/${sd.resourcePath}/@blob/preview" target="_blank" class="btn">${Context.getMessage('command.LabsSite.latestuploads.display')}</a>
                <a rel="nofollow" href="${Context.modulePath}/${sd.resourcePath}/@blob" class="btn">${Context.getMessage('command.LabsSite.latestuploads.download')}</a>
              </td>
          </tr>
          </#list>
          </tbody>
        </table>
        </div>
        </div>

      </div>
    </div>
    </section>
    </div>
  </@block>
</@extends>
