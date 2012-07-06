<#assign id = "externalContent" + "_" + section_index + "_r_" + row_index + "_c_" + content_index />
<#assign thisColumnUrl = This.path + "/s/" + section_index + "/r/" + row_index + "/c/" + content_index />
<#assign url = content.html />

<div id="${id}" >
	<div class="external-content">
		<img src="${skinPath}/images/loading.gif" />
	</div>
	<div class="external-content-error" style="display:none;" >
		<div class="alert alert-block alert-error no-fade" >
			<h4 class="alert-heading"><i class="icon-warning-sign" style="font-size: 48px;" ></i>Impossible d'afficher ce contenu</h4>
			<ul class="supplychain-unstyled-ul" >
			<li>Soit le contenu exposé ne provient pas d'une colonne de type "texte enrichi" d'un site de l'usine à sites,</li>
			<li>Soit vous n'avez pas le droit d'accéder au contenu exposé.</li>
			</ul>
			<div class="extra-error" ></div>
		</div>
	</div>
<script type="text/javascript">
jQuery('#${id}').ready(function() {
	var externalContentUrl = '${url}';
	renderWidgetExternalContent('${id}', externalContentUrl);
});
</script>
</div>