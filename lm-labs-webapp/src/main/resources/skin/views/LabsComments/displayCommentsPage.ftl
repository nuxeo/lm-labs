<hr />
<div id="divCommentablePage"  class="container" >
	<center><h4 id="titleCommentsPage">${Context.getMessage('label.comments.title')}</h4></center>
	<div id="divEditCommentable"  class="fixed-container dialog2" style="display: none;">
		<h1 id="titleComments">${Context.getMessage('label.comments.title')}</h1>
		<form id="form-commentablePage" method="post" class="form" action="${This.path}/@labscomments">
			<fieldset>
				<!--         Comment      ------->
				<div class="clearfix">
					<div class="input" style="margin-bottom: 0px;margin-left: 0px;">
						<textarea name="text" id="text" class="labscomments text required" required-error-text="${Context.getMessage('label.comments.required')}"></textarea>
					</div>
				</div>
			</fieldset>
			<div  class="actions">
				<button class="btn primary required-fields" form-id="form-commentablePage" title="${Context.getMessage('label.comments.save')}">${Context.getMessage('label.comments.save')}</button>
			</div>
		</form>
	</div>
	<#if !This.context.principal.anonymous>
		<a href="#" class="btn open-dialog" rel="divEditCommentable" onClick="javascript:openCommentsPage();">${Context.getMessage('command.Page.CommentAdd')}</a>
	</#if>
	<div id="divListCommentsPage" class="container" style=""></div>
</div>
<script type="text/javascript">
	jQuery(document).ready(function(){
			initCommentsPage('divEditCommentable');
			getCommentsPage();
		});
	
	function initCommentsPage(name){
		jQuery("#" + name).dialog2({
			autoOpen : false, 
        	closeOnOverlayClick : true, 
        	removeOnClose : false, 
        	showCloseHandle : true,
		});
	}

	function openCommentsPage(){
		jQuery("#divEditCommentable").dialog2('open');
		jQuery("#form-commentablePage").clearForm();
	}

	function closeCommentsPage(){
		jQuery("#divEditCommentable").dialog2('close');
	}

	function deleteCommentPage(url, id){
		jQuery.ajax({
			type : "DELETE",
			url : url + '?property=' + id,
			data : '',
			success : function(msg) {
				getCommentsPage();
			},
			error : function(msg) {
				alert('ERROR' + msg.responseText);
			}
		});
	}
	
	function getCommentsPage() {
		jQuery.ajax({
			type : "GET",
			url : '${This.path}/@labscomments?isPage=yes',
			data : '',
			success : function(msg) {
				jQuery("#divListCommentsPage")[0].innerHTML = msg;
				jQuery("#titleCommentsPage")[0].innerHTML = $('#divTitleCommentsPage')[0].innerHTML;
			},
			error : function(msg) {
				alert('ERROR' + msg.responseText);
			}
		});
	}
</script>