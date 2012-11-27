<#assign modeSelectParam = Context.request.getParameter('modeSelect') />
<#assign params = "PageClasseur,/default-domain/sites/" + Common.siteDoc(Document).site.document.name >
<#assign mySite = Common.siteDoc(Document).site />
<#assign isContributor = mySite?? && mySite.isContributor(Context.principal.name) />
<#assign myProviderName = "list_Pages" />
<#if isContributor >
	<#assign myProviderName = myProviderName + "_contributor" />
</#if>

<#assign docs = Common.getPageProviderDocs(Session, myProviderName, params, 0) />
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span6">
			<label>1. Choisissez une Page Classeur :</label>
			<select id="pageClasseurChooser" >
		<#list docs as doc >
				<option value="${Root.getLink(doc)?html}" <#if docs?first.id == doc.id >selected='selected'</#if> >${doc['dc:title']?html}</option>
		</#list>
			</select>
		</div>
		<div class="selectFolderDiv span6" >
		</div>
	</div>
</div>
</div>
<script type="text/javascript">
function updateFoldersDiv() {
	var url = jQuery('#pageClasseurChooser option:selected').val().replace(' ', '%20');
	jQuery('.selectFolderDiv').load(url + '/@views/selectPageClasseurFolder<#if modeSelectParam != null>?modeSelect=true</#if>', function(response, status, xhr) {
		if (status == "error") {
			var msg = 'Un problème est survenu, impossible de récupérer les répertoires: ';
			jQuery(this).html('<div class="alert alert-error no-fade"><strong>' + msg + '</strong>' + xhr.status + ' ' + xhr.statusText + ' (' + url + ')' + '</div>');
		}
	});
}
jQuery(document).ready(function() {
	updateFoldersDiv();
	jQuery('#pageClasseurChooser').change(updateFoldersDiv);
});
</script>
