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
			<ul>
			<li>Soit le contenu exposé ne provient pas d'une colonne de type "texte enrichi" d'un site de l'usine à sites,</li>
			<li>Soit vous n'avez pas le droit d'accéder au contenu exposé.</li>
			</ul>
			<div class="extra-error" ></div>
		</div>
	</div>
<script type="text/javascript">
jQuery('#${id}').ready(function() {
	var externalContentUrl = '${url}';
	var contentObj = jQuery('#${id} > div.external-content');
	if (isValidExternalContentUrl(externalContentUrl)) {
		jQuery.ajax({
			type: 'GET',
			url: externalContentUrl,
			dataType: 'html',
			success: function(data, textStatus, xhr) {
				if (xhr.status != 200 || !isValidExternalContent(data)) {
					jQuery(contentObj).hide();
					jQuery(contentObj).siblings('div.external-content-error').show();
				} else {
					jQuery(contentObj).html(data);
				}
			},
			error: function(xhr, textStatus, errorThrown) {
				jQuery(contentObj).siblings('div.external-content-error').find('div.extra-error').html('Erreur: ' + xhr.status + " " + xhr.statusText)
				jQuery(contentObj).hide();
				jQuery(contentObj).siblings('div.external-content-error').show();
			}
		});
	} else {
		jQuery(contentObj).siblings('div.external-content-error').find('div.extra-error').html('Erreur: référence de colonne invalide')
		jQuery(contentObj).hide();
		jQuery(contentObj).siblings('div.external-content-error').show();
	}
});
</script>
</div>