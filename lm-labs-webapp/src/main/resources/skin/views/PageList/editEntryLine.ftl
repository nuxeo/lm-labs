<div class="container" style="width: auto;">
	<section id="grid-system">
		<div class="row">
			<h3>${Context.getMessage('label.pageList.edit.listHeader.editHeader')}</h3>
			<form method="post" name="form-editEntryLine" id="form-editEntryLine" class="form">
				<fieldset>

					<#list This.getHeaders().getHeadersSet() as header>
							<p>${header.name}</p>
					</#list>
					
					
				</fieldset>
			</form>
		</div>
	</section>
	<button id="saveHeaderList" class="btn primary" onClick="javascript:saveHeaderList('${This.path}');" title="${Context.getMessage('label.pageList.edit.manage.save')}">${Context.getMessage('label.pageList.edit.manage.save')}</button>
	<button id="cancel" class="btn" onClick="javascript:closeManageList();" title="${Context.getMessage('label.pageList.edit.manage.cancel')}">${Context.getMessage('label.pageList.edit.manage.cancel')}</button>
	<br /><br />
	<button id="grosCul" class="btn info" onClick="javascript:alert(headersCollection.toString());" >headerList</button>
</div>