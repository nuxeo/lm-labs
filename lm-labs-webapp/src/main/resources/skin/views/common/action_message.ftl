<div class="container-fluid">
	<div class="row-fluid">
    	<#assign messages = This.getMessages() />
        <#list messages?keys as key >
			<div class="alert alert-block alert-${key}">
             	<a class="close" data-dismiss="alert" href="#">&times;</a>
             	${Context.getMessage(messages[key])}
			</div>
		</#list>
	</div>
</div>