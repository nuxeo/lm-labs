
<div class="container" style="width: auto;">
	<form class="form-horizontal" method="post" name="form-editLine" id="form-editLine" action="${This.path}">
		<fieldset>
			<#list bean.headersSet as header>
				<div class="control-group">
				<#if header.type?lower_case == 'checkbox' >
				  <#include "/views/PageList/" + header.type?lower_case + "/edit.ftl" />
				<#else>
					<label class="control-label" for="${header.idHeader}">${header.name}</label>
						<div class="controls">
						<#-- ???????? 
							<#assign header = header />
						-->
							<#include "/views/PageList/" + header.type?lower_case + "/edit.ftl" />
						</div>
				</#if>
				</div>
			</#list>
		</fieldset>
	</form>
</div>
<#if line != null>
	<div id="divBtnDeleteLine" style="text-align: right;">
		<button id="deleteLine" class="btn" onClick="javascript:if(confirm('${Context.getMessage('label.pageList.line_deleted.confirm')?js_string}')){deleteLine('${This.previous.path}');}{return false;}" title="${Context.getMessage('label.pageList.edit.manage.delete')}">${Context.getMessage('label.pageList.edit.manage.delete')}</button>
	</div>
</#if>
