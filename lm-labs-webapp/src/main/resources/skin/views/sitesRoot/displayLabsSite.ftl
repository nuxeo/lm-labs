<div>
	<div class="titleLabsSiteDiv">
		<a class="titleLabsSite" style="cursor:pointer;color: black;" href="${This.path}/${labssite.URL}">${labssite.title}</a>
	</div>
	<#if Session.hasPermission(labssite.documentModel.ref, 'Everything')>
		<div class="actionNews">
			<ul class="labsSiteActions">
				<li><a class="deleteActionLabsSite" style="cursor:pointer" id="deleteLabsSite" onClick="javascript:deleteLabsSite('${This.path}/${labssite.URL}', '${This.path}');">${Context.getMessage('command.LabsSite.delete')}</a></li>
				<li><a class="modifyActionLabsSite" style="cursor:pointer" id="modifyLabsSite" onClick="javascript:modifyLabsSite('${This.path}/edit/${labssite.documentModel.id}');">${Context.getMessage('command.LabsSite.modify')}</a></li>
			</ul>
		</div>
	</#if>
</div>