<section>
	<div class="page-header">
		<h3>
			${Context.getMessage("label.contact")}
				<#if contactsAdmin?size &gt; 0 ><span class="badge badge-info" style="vertical-align: middle;" >${contactsAdmin?size}</span>&nbsp;</#if>
			<small>
				<a href="#" rel="popover" data-trigger="hover" data-content="${Context.getMessage('label.admin.contact.explanation')}" data-original-title="${Context.getMessage('label.security.labs.permission.Everything')}">Description</a>
			</small>
		</h3>
	</div>
	<div style="text-align: right;margin-top: -46px;">
		<button id="displayAddPerm" onClick="javascript:displayAddContact();" title="${Context.getMessage('label.admin.contact.addContact.title')}" class="btn btn-mini" style="margin-left:20px;" >
			<i class="icon-plus" style="padding-right:0px;" ></i>
		</button>
	</div>
	<#list contactsAdmin as contact>
		<div class="row">
			<div class="span1 columns">
				&nbsp;
			</div>
			<div class="span4 columns">
				${contact.displayName} (${contact.ldap})&nbsp;
			</div>
			<div class="span4 columns">
				${contact.email}&nbsp;
			</div>
			<div class="span2 columns">
            	<button class="btn btn-mini btn-danger" title="${Context.getMessage('command.contact.remove')}" 
            		onclick="javascript:labsContactDelete('${This.path}/@labscontacts/delete?ldap=${contact.ldap}', '${Context.getMessage('label.admin.contact.confirm_delete', contact.displayName)?js_string}');" > 
	            	<i class="icon-remove" style="padding-right:0px;"></i>
            	</button>
	        </div>
		</div>
	</#list>
	<#if contactsAdmin?size == 0>
		<i>${Context.getMessage("label.admin.noContact")}</i>
	</#if>
</section>
<div>&nbsp;</div>

<script type="text/javascript">
$(function () {
		$("a[rel=popover]")
			.popover({offset: 10})
			.click(
				function(e) {e.preventDefault()}
			)
	}
)         
</script>