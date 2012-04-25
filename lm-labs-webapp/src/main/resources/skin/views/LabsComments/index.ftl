<div class="fixed-container">
	<ul style="list-style: none;margin-left: 0px;">
	<#if reverseComments?? && reverseComments>
		<#assign comments=comments?reverse>
	</#if>
		<#list comments as comment>
			<li class="labscomments">

				<div class="labscomments by">
					<span>${This.activeAdapter.getFullName(comment.comment.author)}</span>
					<#if (((reverseComments?? && !reverseComments && comment.id == comments?last.id) || (reverseComments?? && reverseComments)) && (Session.hasPermission(Document.ref, 'Everything') || Session.hasPermission(This.document.ref, 'ReadWrite') || Context.principal.name == comment.comment.author))>
						<p class="labscomments delete">
							<button class="btn btn-mini btn-danger" onClick="javascript:if(confirm('${Context.getMessage('label.comments.deleted.confirm')?js_string}')){${deleteComment}('${This.path}/@labscomments', '${comment.id}');}{return false;}"><i class="icon-remove" style="padding-right:0px;"></i></button>
						</p>
					</#if>
				</div>
				<div class="labscomments comment">
					<div class="row-fluid">
						<div class="span2">
							<#if comment.comment.author != 'Administrator'>
				  				<span><img width="50px;" height="50px;" src="http://intralm2.fr.corp.leroymerlin.com/contact/id/${comment.comment.author}/picture"></span>
				  			<#else>
				  				<span><img width="50px;" height="50px;" src="http://intralm2.fr.corp.leroymerlin.com/contact/id/10060732/picture"></span><br/>
				  			</#if>
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
		${Context.getMessage('label.comments')} <span class="badge badge-info">${comments?size}</span>
	</div>
</div>
