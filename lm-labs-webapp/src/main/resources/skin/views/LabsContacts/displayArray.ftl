<section>
	<div class="page-header">
		<h4>
			${Context.getMessage("label.contact")}
			<small>
				<#if contactsAdmin?size &gt; 0 >
					(${contactsAdmin?size})&nbsp;
				</#if>
				<a href="#" rel="popover" data-content="${Context.getMessage('label.admin.contact.explanation')}" data-original-title="${Context.getMessage('label.security.labs.permission.Everything')}">Description</a>
			</small>
		</h4>
	</div>
	<div style="text-align: right;margin-top: -40px;">
		<button id="displayAddPerm" onClick="javascript:displayAddContact();" title="${Context.getMessage('label.admin.contact.addContact.title')}" class="btn " style="margin-left:20px;" >+</button>
	</div>
	<#list contactsAdmin as contact>
		<div class="row">
			<div class="span2 columns">
				&nbsp;
			</div>
			<div class="span5 columns">
				${contact.displayName} (${contact.ldap})&nbsp;
			</div>
			<div class="span5 columns">
				${contact.email}&nbsp;
			</div>
			<div class="span2 columns">
				<span onclick="javascript:labsContactDelete('${This.path}/@labscontacts/delete?ldap=${contact.ldap}', '${Context.getMessage('label.admin.contact.confirm_delete', contact.displayName)?js_string}');" style="cursor: pointer;">
	            	<img alt="${Context.getMessage('command.contact.remove')}" src="${skinPath}/images/x.gif"/>
	            </span>
	        </div>
		</div>
	</#list>
	<#if contactsAdmin?size == 0>
		<i>${Context.getMessage("label.admin.noContact")}</i>
	</#if>
</section>

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