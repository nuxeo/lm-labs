<#assign canWrite = Session.hasPermission(Document.ref, 'Write') />

<div id="comment" <#if canWrite >class="well"</#if>>
  <div id="comment_form">
    <div id="commentField" class="formWysiwyg">
    	&nbsp;${This.page.commentaire}
    </div>
    
    <#if !Context.principal.isAnonymous() && canWrite>
	  <script type="text/javascript">
			<#include "views/common/ckeditor_config.ftl" />
			
			$('#commentField').ckeip({
				e_url: '${This.path}/updateCommentaire',
				data: {
					commentaire : jQuery("#commentField").html()
				},	
				ckeditor_config: ckeditorconfig
			});	
	  </script>
	 </#if>
  </div>
</div>