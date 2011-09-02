<div class="titleManageList">
	${Context.getMessage('label.pageList.edit.manage.title')}
</div>
<div class="explainManageList">
	${Context.getMessage('label.pageList.edit.manage.explain')}
</div>
<form method="post" name="form-manageList" id="form-manageList">
	<div id="divFormManageList">
		<div id="divListHeader">
			<span class="listHeaderTitle">${Context.getMessage('label.pageList.edit.listHeader.title')}</span>
			<ul>
				<li>
					<a href="#" onClick="javascript:modifyHeader(1);">Colonne 1</a>
				</li>
			</ul>
			<a href="#" onClick="javascript:addHeader();">${Context.getMessage('label.pageList.edit.listHeader.addHead')}</a>
		</div>
		<div id="divEditHeader">
			<div id="headerText">
				<span class="listHeaderTitle">${Context.getMessage('label.pageList.edit.listHeader.title')}</span>
			</div>
			
		</div>
	</div>
</form>	
<button id="saveHeaderList" onClick="javascript:saveHeaderList();" title="${Context.getMessage('label.pageList.edit.manage.save')}">${Context.getMessage('label.pageList.edit.manage.save')}</button>
<button id="cancel" onClick="javascript:closeManageList();" title="${Context.getMessage('label.pageList.edit.manage.cancel')}">${Context.getMessage('label.pageList.edit.manage.cancel')}</button>

