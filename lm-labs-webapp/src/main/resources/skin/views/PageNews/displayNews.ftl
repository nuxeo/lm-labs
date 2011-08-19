<div class="news">
	<div class="titleNews">
		${news.title}
	</div>
	<div class="publicationNews">
		publié le ${news.startPublication.time?string('dd MMMMM yyyy')} par ${news.lastContributor} 
	</div>
	<div class="contentNews">
		<#assign newsContent = This.formatHTML(news.content)>
		${newsContent}
	</div>
	<#if isAuthorized>
		<div class="actionNews">
			<!--  <img src="${skinPath}/images/PictureBook/bin.png"/>  -->
			<ul class="newsActions">
				<li><a class="deleteActionNews" style="cursor:pointer" id="deleteNews${news.documentModel.id}" onClick="javascript:deleteNews('${This.path}/news/${news.documentModel.id}', '${This.path}');">${Context.getMessage('command.PageNews.delete')}</a></li>
				<li><a class="modifyActionNews" style="cursor:pointer" id="modifyNews${news.documentModel.id}" onClick="javascript:modifyNews('${This.path}/news/${news.documentModel.id}');">${Context.getMessage('command.PageNews.modify')}</a></li>
			</ul>
		</div>
	</#if>
</div>