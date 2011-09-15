<@extends src="views/LabsSite/sitemap-base.ftl">
     <@block name="sitemap-content">
        <table class="zebra-striped">
            <thead>
            <tr>
              <th width="30%">Elément</th>
              <th width="35%">Créé par</th>
              <th width="35%">Dernière mise à jour par</th>
            </tr>
          </thead>
          <tbody>
          <#list site.allPages as page>
            <tr>
              <#assign doc=page.document />
              <td class="nameCol"><a href="${This.path}/${doc.title}">${doc.title}</a></td>
              <td class="createdCol">${userFullName(doc.dublincore.creator)} (${doc.dublincore.created?string.medium}}</td>
              <td class="updatedCol">${userFullName(doc.dublincore.lastContributor)} (${doc.dublincore.modified?string.medium})</td>
            </tr>
          </#list>
          </tbody>
      </table>

  </@block>
</@extends>