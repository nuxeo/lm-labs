<div class="fixed-container">
	<ul style="list-style: none;margin-left: 0px;">
		<#assign comments = comments?reverse/>
		<#list comments as comment>
			<li class="labscomments">

				<div class="labscomments by">
					<#assign isNotRejected = Common.isNotRejectedComment(comment)/>
					<#if isNotRejected || Session.hasPermission(Document.ref, 'Everything')>
						<span>${This.activeAdapter.getFullName(comment.comment.author)}</span>
					</#if>
					<#assign isFist = (comment.id == comments?first.id)/>
					<#if (isNotRejected && isFist && Context.principal.name == comment.comment.author) || (Session.hasPermission(Document.ref, 'Everything') && isNotRejected)>
						<p class="labscomments delete">
							<button class="btn btn-mini btn-danger" onClick="javascript:if(confirm('${Context.getMessage('label.comments.deleted.confirm')?js_string}')){${deleteComment}('${This.path}/@labscomments', '${comment.id}', <#if isFist>true<#else>false</#if>);}{return false;}"><i class="icon-remove" style="padding-right:0px;"></i></button>
						</p>
					</#if>
				</div>
				<div class="labscomments comment">
					<div class="row-fluid">
						<div class="span2">
							<#if isNotRejected || Session.hasPermission(Document.ref, 'Everything')>
				  				<span><img class="imgComment" width="50px;" height="50px;" src="http://intralm2.fr.corp.leroymerlin.com/contact/id/${comment.comment.author}/picture"></span>
				  			</#if>
						</div>
						<div class="span10">
							<span>
								<#if isNotRejected>
									${comment.comment.text}
								<#else>
									${Context.getMessage('label.comment.desactived')}
									<#if Session.hasPermission(Document.ref, 'Everything')>
										<div class="hidden">${comment.comment.text}</div>
									</#if>
								</#if>
							</span>
						</div>
					</div>
					<#if isNotRejected || Session.hasPermission(Document.ref, 'Everything')>
	      				<p class="labscomments footer" >${Context.getMessage('label.comment.date')} ${comment.comment.creationDate}</p>
	      			</#if>
    			</div>
    		</li>
		</#list>
	</ul>
	<div id="${divTitleComments}" style="display: none">
		${Context.getMessage('label.comments')} <span class="badge badge-info" style="vertical-align: top;" >${comments?size}</span>
	</div>
</div>