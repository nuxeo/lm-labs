<div class="fixed-container">
	<ul style="list-style: none;margin-left: 0px;">
	<#if reverseComments?? && reverseComments>
		<#assign comments=comments?reverse>
	</#if>
		<#list comments as comment>
			<li class="labscomments">

				<div class="labscomments by">
					<span>${This.activeAdapter.getFullName(comment.comment.author)}</span>
					<#if (((removeOnlyLastComment && comment.id == comments?first.id) || !removeOnlyLastComment) && (Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(This.document.ref, 'ReadWrite') || Context.principal.name == comment.comment.author))>
						<p class="labscomments delete">
							<button class="btn btn-mini btn-danger" onClick="javascript:if(confirm('${Context.getMessage('label.comments.deleted.confirm')?js_string}')){${deleteComment}('${This.path}/@labscomments', '${comment.id}');}{return false;}"><i class="icon-remove" style="padding-right:0px;"></i></button>
						</p>
					</#if>
				</div>
				<div class="labscomments comment">
					<div class="row-fluid">
						<div class="span2">
				  			<span><img class="imgComment" width="50px;" height="50px;" src="http://intralm2.fr.corp.leroymerlin.com/contact/id/${comment.comment.author}/picture"></span>
						</div>
						<div class="span10">
							<span>${comment.comment.text}</span>
						</div>
					</div>
	      			<p class="labscomments footer" >${Context.getMessage('label.comment.date')} ${comment.comment.creationDate}</p>
    			</div>
    		</li>
		</#list>
	</ul>
	<div id="${divTitleComments}" style="display: none">
		${Context.getMessage('label.comments')} <span class="badge badge-info" style="vertical-align: top;" >${comments?size}</span>
	</div>
</div>