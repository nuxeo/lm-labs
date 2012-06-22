<#assign modeSelectParam = Context.request.getParameter('modeSelect') />
<#assign folders = Session.getChildren(Document.ref, 'Folder') />
<#if 0 < folders?size >
<label>2. Choisissez un répertoire :</label>
<div class="" >
	<#list folders as folder >
	<div class="" >
		<label class="radio">
			<input type="radio" value="${folder.id}" name="optionPageClasseurFolder">${folder.dublincore.title}</input>
			<input type="hidden" class="selectedFolderPath" disabled='' value="${Common.getPathAsString(folder)}" >
			<input type="hidden" class="selectedFolderId" disabled='' value="${folder.id}" >
			<input type="hidden" class="selectedFolderTitle" disabled='' value="${folder.dublincore.title}" >
		</label>
	</div>
	</#list>
</div>
<btn class="btn btn-mini btn-primary selectPcFolder" disabled >
	<i class="icon-ok" style="padding-right:0px;" ></i>Valider la sélection
</btn>
<#else>
Pas de répertoire dans cette Page Classeur
</#if>

<script type="text/javascript">
jQuery(document).ready(function() {
	jQuery('input[name=optionPageClasseurFolder]').change(function() {
		jQuery('btn.selectPcFolder').removeAttr('disabled');
	});
<#if modeSelectParam == null >
	jQuery('btn.selectPcFolder').click(function() {
		jQuery('#selectFolderBtn').show();
		var thisForm = jQuery(this).closest('form');
		var checkedFolder = jQuery(this).siblings('div').find('input[name=optionPageClasseurFolder]:checked');
		var folderPath = jQuery(checkedFolder).siblings('input[class~=selectedFolderPath]').val();
		var folderId = jQuery(checkedFolder).siblings('input[class~=selectedFolderId]').val();
		var folderTitle = jQuery(checkedFolder).siblings('input[class~=selectedFolderTitle]').val();
		jQuery(thisForm).find('input[name=pageClasseurFolderPath]').val(folderPath);
		jQuery(thisForm).find('input[name=pageClasseurFolderShortPath]').val(getShortFolderPath(folderPath));
		jQuery(thisForm).find('input[name=pageClasseurFolderId]').val(folderId);
		jQuery(thisForm).find('input[name=pageClasseurFolderTitle]').val(folderTitle);
		jQuery(thisForm).find('input[name=NX_FOLDER]').val(buildGadgetIdProp(folderPath, folderId, folderTitle));
		jQuery('#foldersSelection').hide();
	});
<#else>
	<#-- In a iFrame !!! -->
	jQuery('btn.selectPcFolder').click(function() {
		var checkedFolder = jQuery(this).siblings('div').find('input[name=optionPageClasseurFolder]:checked');
		var folderPath = jQuery(checkedFolder).siblings('input[class~=selectedFolderPath]').val();
		var folderId = jQuery(checkedFolder).siblings('input[class~=selectedFolderId]').val();
		var folderTitle = jQuery(checkedFolder).siblings('input[class~=selectedFolderTitle]').val();
		gadgets.lmselectvalue.selectFromFrame('{\'pageClasseurFolderPath\':\''+ folderPath +'\', \'pageClasseurFolderId\':\''+ folderId +'\', \'pageClasseurFolderTitle\':\''+ folderTitle +'\'}');
	});
</#if>
});
</script>
