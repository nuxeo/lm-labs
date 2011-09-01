<div class="news">
	<div class="titleNews">
		${news.title}
	</div>
	<div class="publicationNews">
		${Context.getMessage('label.labsNews.display.publish')} ${news.startPublication.time?string('dd MMMMM yyyy')} ${Context.getMessage('label.labsNews.display.by')} ${news.lastContributorFullName}
	</div>
	<div class="contentNews">
		${news.content}
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