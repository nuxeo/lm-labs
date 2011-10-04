<br />
<div class="fixed-container">
	<ul style="list-style: none;margin-left: 10px;">
		<#list comments?reverse as comment>
			<li style="position: relative;border-bottom: 1px solid;">			
				<div class="labscomments_author">
		          <div class="labscomments_by">
		            ${Context.getMessage('label.comment.date')} ${comment.comment.creationDate} ${Context.getMessage('label.by')} ${comment.comment.author}
		          </div>
		        </div>
				<span>${comment.comment.text?replace("\n", "<br />")}</span>
				<#if Session.hasPermission(Document.ref, 'Everything') || Context.principal.name == comment.comment.author>
					<span class="labscomments_delete" onClick="javascript:if(confirm('${Context.getMessage('label.comments.deleted.confirm')?js_string}')){deleteComment('${This.path}/@labscomments', '${comment.id}');}{return false;}">&nbsp;</span>
				</#if>
			</li>
		</#list>
	</ul>
</div>