    <div class="topbar-wrapper" style="z-index: 5;">
    <div class="topbar">
      <div class="topbar-inner">
        <div class="container">
          <h3>
            <#if site??>
              <a href="${Context.modulePath}/${site.document.webcontainer.url}">${site.title}</a>
            <#else>
              <a href="${Context.modulePath}">LABS</a>
            </#if>
          </h3>


          <ul class="nav secondary-nav">
            <#if site?? >
            <li>
              <form accept-charset="ISO-8859-1" action="${Context.modulePath}/${site.URL}/@search">
              <input class="normal" placeholder="${Context.getMessage('label.search')}" name="fullText"/>
              </form>
            </li>
            </#if>
            <li><#include "common/login.ftl" /></li>
            <#if Context.principal.isAnonymous() == false>
            <li class="dropdown	">
              <a href="#" class="dropdown-toggle">${Context.principal.firstName} ${Context.principal.lastName}</a>
              <ul class="dropdown-menu">
                <@block name="docactions">
                <#if site?? && Session.hasPermission(This.document.ref, "ADD_CHILDREN")>
                <li><a class="open-dialog" rel="add_content_dialog" href="${This.path}/@views/manage">Ajouter du contenu</a></li>
                </#if>
                </@block>
                <li class="divider"></li>
                <@block name="siteactions">
                <#if site?? && Session.hasPermission(site.document.ref, "WRITE")>
                <li><a href="${Context.modulePath}/${site.URL}/@views/edit">Administration</a></li>
                </#if>
                </@block>

              <#if site?? && Session.hasPermission(site.document.ref, 'Everything') >
                <li><a href="${Context.baseURL}/nuxeo/nxpath/default/default-domain/sites/${site.document.title}/tree@view_documents?tabIds=%3A" target="_blank" >${Context.getMessage('command.LabsSite.goToBackOffice')}</a></li>
              </#if>
                <li class="divider"></li>
                <li><a id="logout" href="#">Déconnexion</a></li>
              </ul>
            </li>
            </#if>
          </ul>
        </div>
      </div><!-- /fill -->
    </div><!-- /topbar -->
    </div><!-- /topbar-wrapper -->
    &nbsp;