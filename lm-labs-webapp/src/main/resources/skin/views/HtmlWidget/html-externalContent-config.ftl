<div class="alert alert-block no-fade" >
	<h4 class="alert-heading">
		Information importante
	</h4>
	<p>Le rendu du contenu externe peut être affecté si ce contenu se trouve dans une colonne ayant une largeur différente.</p>
	<p>De même, seule une colonne contenant du texte peut être rendue dans ce widget.</p>
	<button class="btn btn-mini" onClick="return false;" data-toggle="collapse" data-target="#divConfigGadget div.extra-info" ><i class="icon-question-sign" ></i>Plus d'infos</button>
	<div id="configExternalContentExtraInfo" class="extra-info collapse" >
		<p><strong>Pour obtenir la référence d'une colonne d'un site où vous êtes seulement <u>'Visiteur'</u> :</strong>
		<ul>
			<li>Demandez au gestionnaire du site la référence de la colonne désirée.</li>
		</ul>
		</p>
		<p><strong>Pour obtenir la référence d'une colonne d'un site où vous êtes au moins <u>'Contributeur'</u> :</strong>
		<ul>
			<li>Rendez-vous sur la page contenant la colonne,</li>
			<li>passez en mode 'Edition',</li>
			<li>cliquez sur l'icône '<i class="icon-link" ></i>' qui apparaît lorsque le curseur survole la colonne,</li>
			<li>une fenêtre s'ouvre affichant la référence de la colonne,</li>
			<li>copiez cette référence dans votre presse-papier,</li>
			<li>Revenez dans la configuration de ce widget et collez (CTRL + C) la référence dans la zone de saisie,</li>
			<li>cliquez sur le bouton 'Testez la référence' afin de connaître la validité de la référence.</li>
		</ul>
		</p>
	</div>
	<div class="extra-error" ></div>
</div>
<div class="control-group" >
    <label class="control-label" for="content" >${Context.getMessage('label.HtmlPage.column.url.title')}</label>
    <div class="controls" >
        <input id="configExternalContentUrl" type="text" class="span4" name="content" value='${content.html}' />
    </div>
</div>
<div class="control-group" >
    <label class="control-label" >&nbsp</label>
    <div class="controls" >
        <btn id="configExternalContentTestBtn" class="btn btn-primary" data-toggle="button" data-loading-text="Test en cours ..." data-complete-text="Référence valide" data-failed-text="Référence invalide !" >Tester la référence</btn>
    </div>
</div>
<#if 0 < content.html?length >
	<#assign externalContentPageUrl = content.html />
	<#assign patt = "/s/" />
	<#assign pos = content.html?last_index_of(patt) />
	<#assign externalContentPageUrl = content.html?substring(0, pos) />
<a href="${externalContentPageUrl}" >Aller sur la page contenant la colonne référencée</a>
</#if>
<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery('#configExternalContentTestBtn').button();
	jQuery('#configExternalContentTestBtn').click(function() {
		btnObj = this;
		jQuery(btnObj).removeClass('btn-warning');
		jQuery(btnObj).removeClass('btn-success');
		jQuery(btnObj).addClass('btn-primary');
		jQuery(btnObj).button('loading');
		var url = jQuery('#configExternalContentUrl').val();
		if (!isValidExternalContentUrl(url)) {
			jQuery(btnObj).button('failed');
			jQuery(btnObj).removeClass('btn-primary');
			jQuery(btnObj).addClass('btn-warning');
		} else {
			jQuery.ajax({
				type: 'GET',
				url: url,
				success: function(msg) {
					if (!isValidExternalContent(msg)) {
						jQuery(btnObj).button('failed');
						jQuery(btnObj).removeClass('btn-primary');
						jQuery(btnObj).addClass('btn-warning');
					} else {
						jQuery(btnObj).button('complete');
						jQuery(btnObj).removeClass('btn-primary');
						jQuery(btnObj).addClass('btn-success');
					}
				},
				error: function(xhr, textStatus, errorThrown) {
					jQuery(btnObj).button('failed');
					jQuery(btnObj).removeClass('btn-primary');
					jQuery(btnObj).addClass('btn-warning');
				}
			});
		}
	});
	var formObj = jQuery('#divConfigGadget form');
	var actionUrl = jQuery(formObj).attr('action').replace('/w/@put', '');
    jQuery('#divConfigGadget form').attr('action', actionUrl);
    jQuery('#configExternalContentUrl').focus(function() {
    <#--
    	this.select();
    -->
    	var testBtnObj = jQuery('#configExternalContentTestBtn');
    	jQuery(testBtnObj).button('reset');
		jQuery(testBtnObj).removeClass('btn-warning');
		jQuery(testBtnObj).removeClass('btn-success');
		jQuery(testBtnObj).addClass('btn-primary');
    });
})
</script>