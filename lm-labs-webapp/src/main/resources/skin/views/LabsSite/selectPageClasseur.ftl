<#assign modeSelectParam = Context.request.getParameter('modeSelect') />
<#assign params = "PageClasseur,/default-domain/sites/" + Common.siteDoc(Document).site.document.name >
<#assign docs = Common.getPageProviderDocs(Session, "list_Pages", params, 0) />
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span6">
			<label>1. Choisissez une Page Classeur :</label>
			<select id="pageClasseurChooser" >
		<#list docs as doc >
				<option value="${Root.getLink(doc)}" <#if docs?first.id == doc.id >selected='selected'</#if> >${doc.dublincore.title}</option>
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
	var url = jQuery('#pageClasseurChooser option:selected').val();
	jQuery('.selectFolderDiv').load(url + '/@views/selectPageClasseurFolder<#if modeSelectParam != null>?modeSelect=true</#if>');
}
jQuery(document).ready(function() {
	updateFoldersDiv();
	jQuery('#pageClasseurChooser').change(updateFoldersDiv);
});
</script>
