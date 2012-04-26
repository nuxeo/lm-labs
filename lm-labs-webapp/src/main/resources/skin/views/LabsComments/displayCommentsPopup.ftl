<div id="divCommentable"  class="fixed-container dialog2" style="display: none;">
	<h1>
		${Context.getMessage('label.comments.title')}
	</h1>

	<#if !Context.principal.anonymous>
		<form id="form-commentable" method="post" class="form form-horizontal" onsubmit="javascript:saveComment();return false;">
			<fieldset>
				<!--         Comment      ------->
				<div class="control-group">
					<div class="controls" style="margin-bottom: 0px;margin-left: 0px;">
						<textarea name="text" id="text" class="labscomments text required" required-error-text="${Context.getMessage('label.comments.required')}"></textarea>
					</div>
				</div>
			<fieldset>
			<div  class="actions">
				<button class="btn btn-primary required-fields" form-id="form-commentable" title="${Context.getMessage('label.comments.save')}">${Context.getMessage('label.comments.save')}</button>
				<a href="#" class="btn" onClick="javascript:closeComments();" title="${Context.getMessage('label.comments.cancel')}">${Context.getMessage('label.comments.cancel')}</a>
			</div>
		</form>
	<#else>
			${Context.getMessage('label.comments.mandatory.connexion')}
			<div  class="actions">
				<a href="#" class="btn" onClick="javascript:closeComments();" title="${Context.getMessage('label.comments.cancel')}">${Context.getMessage('label.comments.cancel')}</a>
			</div>
	</#if>
	<div id="divListComments" class="fixed-container"  style="margin-bottom: 0px;margin-left: 0px;"><img src="${skinPath}/images/loading.gif" /></div>
</div>
<script type="text/javascript">
	var urlActionBase = null;
	var titleComments = '${Context.getMessage('label.comments.title')?js_string}';
	var hasChangedCommentsPopup = false;
	
	jQuery(document).ready(function(){
			initModalComments('divCommentable');
		});
	
	function initModalComments(name){
		jQuery("#" + name).dialog2({
			height : '400px',
			overflowy : 'auto',
			overflowx : 'hidden',
			autoOpen : false, 
        	closeOnOverlayClick : false, 
        	removeOnClose : false, 
        	showCloseHandle : false
		});
	}

	function openComments(url){
		urlActionBase = url;
		getComments();
		jQuery("#divCommentable").dialog2('open');
		jQuery("#form-commentable").clearForm();
		hasChangedCommentsPopup = false;
	}

	function closeComments(){
		jQuery("#divCommentable").dialog2('close');
		if (hasChangedCommentsPopup === true){
			document.location.href='${This.path}';
		}
	}

	function saveComment(){
		jQuery('#waitingPopup').dialog2('open');
		jQuery.ajax({
			type : "POST",
			url : urlActionBase,
			data : jQuery("#form-commentable").serialize(),
			success : function(msg) {
				hasChangedCommentsPopup = true;
				jQuery("#form-commentable").clearForm();
				jQuery('#waitingPopup').dialog2('close');
				getComments();
			},
			error : function(msg) {
				alert('ERROR' + msg.responseText);
				jQuery('#waitingPopup').dialog2('close');
			}
		});
	}

	function deleteComment(url, id){
		jQuery('#waitingPopup').dialog2('open');
		jQuery.ajax({
			type : "DELETE",
			url : url + '?property=' + id,
			data : '',
			success : function(msg) {
				hasChangedCommentsPopup = true;
				jQuery('#waitingPopup').dialog2('close');
				getComments();
			},
			error : function(msg) {
				alert('ERROR' + msg.responseText);
				jQuery('#waitingPopup').dialog2('close');
			}
		});
	}
	
	function getComments() {
		jQuery.ajax({
			type : "GET",
			url : urlActionBase,
			data : '',
			success : function(msg) {
				jQuery("#divListComments")[0].innerHTML = msg;
				jQuery("#divCommentable").dialog2("options", {title: $('#divTitleComments')[0].innerHTML});
			},
			error : function(msg) {
				alert('ERROR' + msg.responseText);
			}
		});
	}
</script>