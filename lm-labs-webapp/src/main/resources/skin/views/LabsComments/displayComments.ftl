<hr />
<div id="divCommentablePage"  class="container" >
	<center><h4 id="titleCommentsPage">${Context.getMessage('label.comments.title')}</h4></center>
	<div id="divCommentableModal"  class="fixed-container dialog2" style="display: none;">
		<h1 id="titleComments">${Context.getMessage('label.comments.title')}</h1>
		<div class="fixed-container" style="">
			<form id="form-commentablePage" method="post" class="form" action="${This.path}/@comments">
				<!--         Comment      ------->
				<div class="clearfix" style="margin-bottom: 0px;margin-left: 0px;">
					<textarea name="text" id="text" class="labscomments text"></textarea>
				</div>
			</form>
		</div>
		<div  class="actions">
			<button class="btn primary" onClick="javascript:saveCommentPage();" title="${Context.getMessage('label.comments.save')}">${Context.getMessage('label.comments.save')}</button>
			<button class="btn" onClick="javascript:closeCommentsPage();" title="${Context.getMessage('label.comments.cancel')}">${Context.getMessage('label.comments.cancel')}</button>
		</div>
	</div>
	<button class="btn primary" onClick="javascript:openCommentsPage();" title="Ajouter">Ajouter</button>
	<div id="divListCommentsPage" class="container" style=""></div>
</div>
<script type="text/javascript">
	jQuery(document).ready(function(){
			initModalCommentsPage('divCommentableModal');
			getCommentsPage();
		});
	
	function initModalCommentsPage(name){
		jQuery("#" + name).dialog2({
			showCloseHandle : false,
			removeOnClose : true,
			autoOpen : false
		});
	}

	function openCommentsPage(){
		jQuery("#divCommentableModal").dialog2('open');
		jQuery("#form-commentablePage").clearForm();
	}

	function closeCommentsPage(){
		jQuery("#divCommentableModal").dialog2('close');
	}

	function saveCommentPage(){
		jQuery.ajax({
			type : "POST",
			url : '${This.path}/@labscomments',
			data : jQuery("#form-commentablePage").serialize(),
			success : function(msg) {
				jQuery("#form-commentablePage").clearForm();
				closeCommentsPage();
				getCommentsPage();
			},
			error : function(msg) {
				alert('ERROR' + msg.responseText);
			}
		});
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