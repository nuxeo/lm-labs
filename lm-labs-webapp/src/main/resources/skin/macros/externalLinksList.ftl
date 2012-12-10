<#macro generateExternalLinksList formDivId="div_persistExternalURL" >
<#assign mySite=Common.siteDoc(Document).site />
<#list mySite.externalURLs as e >
   <li class="${e.document.id}">
	    <a href="${e.URL}" style="word-wrap: break-word;" target="_blank" title="${e.URL}">${e.name}</a>
	    <#if mySite.isContributor(Context.principal.name)>
	      <div class="actionExternalURL editblock btn-group">
	      	<a class="btn btn-primary btn-mini dropdown-toggle" data-toggle="dropdown" style="padding: 0px 4px 2px 3px;margin-top: 4px;"><span class="caret"></span></a>
			<ul class="dropdown-menu"  style="left: auto;right: 0px;min-width: 0px;">
				<li>
					<a href="#" onClick="javascript:moveUpExternalURL('${Context.modulePath}/${mySite.URL}/@externalURL/${e.document.id}', '${This.path}', '${e.document.id}');" title="Monter" alt="Monter"><i class="icon-arrow-up"></i>Monter</a>
					<a href="#" onClick="javascript:moveDownExternalURL('${Context.modulePath}/${mySite.URL}/@externalURL/${e.document.id}', '${This.path}', '${e.document.id}');" title="Descendre" alt="Descendre"><i class="icon-arrow-down"></i>Descendre</a>
					<a href="#" onClick="javascript:modifyExternalURL('${formDivId}', '${e.name?js_string}', '${e.getURL()}', '0', '${e.document.id}');" title="Modifier" alt="Modifier"><i class="icon-edit"></i>Modifier</a>
					<a href="#" onClick="externalLinksHelper.deleteExternalURL('${Context.modulePath}/${mySite.URL}/@externalURL/${e.document.id}', '${This.path}', function(msg) {jQuery('.external-links > ul').load('${Context.modulePath}/${mySite.URL}/@views/externalLinksList');}, '${Context.getMessage('label.externalURL.delete.confirm')}');" title="Supprimer" alt="Supprimer"><i class="icon-remove"></i>Supprimer</a>
				</li>
			</ul>
	      </div>
	    </#if>
    </li>
</#list>
</#macro>