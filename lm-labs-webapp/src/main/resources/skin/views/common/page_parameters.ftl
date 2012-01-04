<style>
	#form_editParameters .input {
		width: 45%;
	}
		
	#form_editParameters .input input[type="checkbox"] {
		margin-top: 6px;
		float: left;
	}
		
	#form_editParameters .input label {
		text-align: left;
		width: 90%;
	}
</style>

<h1>${Context.getMessage('label.parameters.page.title')}</h1>
<div id="divEditParametersPageForm">
	<form id="form_editParameters" action="${This.path}/@managePage" method="post">
		<div class="clearfix">
			<div class="input">
				<label for="updateTitlePage">${Context.getMessage('label.parameters.page.updateTitlePage')}</label>
				<input id="updateTitlePage" type="text" name="updateTitlePage" value="${This.document.title}" maxlength="90"/>
			</div>
		</div>
		<div class="clearfix">
			<div class="input">
				<input id="publishPage" type="checkbox" name="publishPage" <#if page.visible>checked="true"</#if> />
				<label for="publishPage">${Context.getMessage('label.parameters.page.publishPage')}</label>
			</div>
			<div class="input">
				<input id="commentablePage" type="checkbox" name="commentablePage" <#if This.page.commentable >checked="true"</#if> />
				<label for="commentablePage">${Context.getMessage('label.parameters.page.authorizedCommentable')}</label>
			</div>
			<div class="input">
				<input id="${This.DC_TITLE}" type="checkbox" name="${This.DC_TITLE}" value="${This.DC_TITLE}" <#if This.page.isDisplayable(This.DC_TITLE) >checked="true"</#if> />
				<label for="${This.DC_TITLE}">${Context.getMessage('label.parameters.page.displayableTitlePage')}</label>
			</div>
			<div class="input">
				<input id="${This.DC_DESCRIPTION}" type="checkbox" name="${This.DC_DESCRIPTION}" value="${This.DC_DESCRIPTION}" <#if This.page.isDisplayable(This.DC_DESCRIPTION) >checked="true"</#if> />
				<label for="${This.DC_DESCRIPTION}">${Context.getMessage('label.parameters.page.displayableDescriptionPage')}</label>
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