<#-- genere le contenu entier de la news -->
<#macro displayLabsTags page >
	<#assign display = false />
	<#if pageCommentable != null>
		<#assign commentable = pageCommentable.commentable />
	</#if>
	<#if commentable>
		<hr />
		<div id="divCommentablePage"  >
			<center><h4 id="titleCommentsPage">${Context.getMessage('label.comments.title')}</h4></center>
			<div id="divEditCommentable"  class="fixed-container dialog2" style="display: none;">
				<h1 id="titleComments">${Context.getMessage('label.comments.title')}</h1>
				<form id="form-commentablePage" method="post" class="form form-horizontal" action="${This.path}/@labscomments">
					<fieldset>
						<#--       Comment      -->
						<div class="control-group">
							<div class="controls" style="margin-bottom: 0px;margin-left: 0px;">
								<#if ckeditor?? && ckeditor>
					                <textarea rows="10" name="text" id="text" cols="80" class="labscomments text required" required-error-text="${Context.getMessage('label.comments.required')}">
					                </textarea>
					            <#else>
					            	<textarea name="text" id="text" class="labscomments text required" required-error-text="${Context.getMessage('label.comments.required')}"></textarea>
					            </#if>
							</div>
						</div>
					</fieldset>
					<div  class="actions">
						<button class="btn btn-primary required-fields" form-id="form-commentablePage" title="${Context.getMessage('label.comments.save')}">${Context.getMessage('label.comments.save')}</button>
					</div>
				</form>
			</div>
			<#if !This.context.principal.anonymous>
				<a href="#" class="btn btn-small open-dialog" rel="divEditCommentable" onClick="javascript:openCommentsPage();"><i class="icon-comment"></i>${Context.getMessage('command.Page.CommentAdd')}</a>
			</#if>
			<#if ckeditor?? && ckeditor>
				<script type="text/javascript">
		       		$('#text').ckeditor(ckeditorconfigUser);
		        </script>
		    </#if>
			
			<div id="divListCommentsPage" class="container-fluid" style="width: 99%;"></div>
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
		        	showCloseHandle : true
				});
			}
		
			function openCommentsPage(){
				jQuery("#divEditCommentable").dialog2('open');
				jQuery("#form-commentablePage").clearForm();
			}
		
			function closeCommentsPage(){
				jQuery("#divEditCommentable").dialog2('close');
			}
		
			function deleteCommentPage(url, id, isFirst){
				jQuery('#waitingPopup').dialog2('open');
				jQuery.ajax({
					type : "DELETE",
					url : url + '?property=' + id + '&isFirst=' + isFirst,
					data : '',
					success : function(msg) {
						jQuery('#waitingPopup').dialog2('close');
						getCommentsPage();
					},
					error : function(msg) {
						alert('ERROR' + msg.responseText);
						jQuery('#waitingPopup').dialog2('close');
					}
				});
			}
			
			function getCommentsPage() {
				jQuery("#divListCommentsPage")[0].innerHTML = '<img src="${skinPath}/images/loading.gif" />';
				jQuery.ajax({
					type : "GET",
					url : '${This.path}/@labscomments?isPage=yes',
					data : '',
					success : function(msg) {
						jQuery("#divListCommentsPage")[0].innerHTML = msg;
						jQuery("#titleCommentsPage")[0].innerHTML = $('#divTitleCommentsPage')[0].innerHTML;
						changeImgError();
					},
					error : function(msg) {
						alert('ERROR' + msg.responseText);
						jQuery("#divListCommentsPage")[0].innerHTML = '';
					}
				});
			}
		</script>
	</#if>
</#macro>

			<div id="divEditTags" style="display: none;">
				<h1 id="titleComments">${Context.getMessage('label.labstags.title.add')}</h1>
				<form id="form-labstags" method="post" class="form form-horizontal" action="${This.path}/addtag">
					<fieldset>
						<div class="control-group">
							<div class="controls">
								<input type="text" class="input-xlarge" placeholder="Saisissez un ou plusieurs mots-clés séparés par des points-virgules ';'" />
							</div>
						</div>
					</fieldset>
					<div  class="actions">
						<button class="btn btn-primary required-fields" form-id="form-commentablePage" title="${Context.getMessage('label.comments.save')}">${Context.getMessage('label.comments.save')}</button>
					</div>
				</form>
			</div>


<#if (This.page?? && This.page != null) >
	<#assign listTags=This.page.getLabsTags()>
	<hr />
	${Context.getMessage('label.labstags.title.add')}
	<div id="divTagsPage">
		<a href="#"
		${Context.getMessage('label.labstags.title')}&nbsp:&nbsp
		<#if listTags?size != 0>
			<#list listTags as tag>
				<@displayTagsPage tag=tag />
			</#list>
		</#if>
	</div>
</#if>

<#macro displayTagsPage tag>
  <li style="font-size: 8px;">
      ${tag}
      <#if This.page.isContributor(Context.principal.name)>
        <a href="#" style="btn btn-mini btn-danger" onClick="deleteTag();">
            <i class="icon-remove" style="padding-right:0px;"></i>
        </a>
        <script type="text/javascript">
          jQuery('#deletetag_${idea.document.id}_${section}_${index}').click(function() {
            var url = '${This.path}/tag/' + encodeURIComponent('${tag.tagName?js_string}');
            jQuery.ajax({
              type: 'DELETE',
              url: url,
              success: function() {
                document.location.href='${This.path}';
              },
              error:function() {
                alert("Error when removing tag");
              }
            });
            return false;
          });
        </script>
      </#if>
  </li>
</#macro>