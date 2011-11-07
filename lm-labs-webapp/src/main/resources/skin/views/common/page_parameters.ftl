<h1>${Context.getMessage('label.parameters.page.title')}</h1>
<div id="divEditParametersPageForm" class="container-fluid">
	<form id="form_editParameters" action="${This.path}/@commentable" method="post">
		<div class="clearfix">
			<div class="input">
				<input style="margin-top: 6px;float: left;" id="commentablePage" type="checkbox" name="commentablePage" <#if This.page.commentable >checked="true"</#if> />
				<label for="commentablePage" style="text-align: left;width: 90%;">${Context.getMessage('label.parameters.page.authorizedCommentable')}</label>
			</div>
		</div>
	</form>
	<hr />
	${Context.getMessage('label.parameters.page.usedModel')} <strong>${Context.getMessage('label.doctype.'+This.document.type)}</strong>
</div>
<div class="actions">
    <button class="btn primary" onclick="javascript:submitParametersPage();">${Context.getMessage('label.parameters.page.save')}</button>
    <button class="btn" onclick="javascript:closeParametersPage();">${Context.getMessage('label.parameters.page.cancel')}</button>
</div>