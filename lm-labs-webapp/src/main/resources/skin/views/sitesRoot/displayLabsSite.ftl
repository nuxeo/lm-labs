<div>
	<div class="titleLabsSite">
		<a class="titleLabsSite" style="cursor:pointer;color: black;" href="${This.path}/${labssite.URL}">${labssite.title}</a>
	</div>
	<#if isAuthorized>
		<div class="actionNews">
			<!--  <img src="${skinPath}/images/PictureBook/bin.png"/>  -->
			<ul class="labsSiteActions">
				<li><a class="deleteActionLabsSite" style="cursor:pointer" id="deleteLabsSite${labssite.documentModel.id}" onClick="javascript:deleteLabsSite('${This.path}/delete/${labssite.documentModel.id}', '${This.path}');">delete</a></li>
				<li><a class="modifyActionLabsSite" style="cursor:pointer" id="modifyLabsSite${labssite.documentModel.id}" onClick="javascript:modifyLabsSite('${This.path}/edit/${labssite.documentModel.id}');">modify</a></li>
			</ul>
		</div>
	</#if>
</div>