<div class="fixed-container">
	<ul style="list-style: none;margin-left: 0px;">
		<#list comments?reverse as comment>
			<li class="labscomments">

				<div class="labscomments by">
					<span>${This.activeAdapter.getFullName(comment.comment.author)}</span>
					<#if Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(This.document.ref, 'ReadWrite') || Context.principal.name == comment.comment.author>
						<p class="labscomments delete" onClick="javascript:if(confirm('${Context.getMessage('label.comments.deleted.confirm')?js_string}')){${deleteComment}('${This.path}/@labscomments', '${comment.id}');}{return false;}">&nbsp;</p>
					</#if>
				</div>
				<div class="labscomments comment">
    				<span>${comment.comment.text}</span>
	      			<p class="labscomments footer" >${Context.getMessage('label.comment.date')} ${comment.comment.creationDate}</p>
    			</div>
    		</li>
		</#list>
	</ul>
	<div id="${divTitleComments}" style="display: none">
		${Context.getMessage('label.comments')} (${comments?size})
	</div>
</div>
