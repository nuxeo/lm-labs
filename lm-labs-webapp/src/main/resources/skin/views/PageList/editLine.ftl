
<div class="container" style="width: auto;">
	<form method="post" name="form-editLine" id="form-editLine" action="${This.path}/saveline${key}">
		<fieldset>
			<#list headersSet as header>
				<div class="clearfix">
					<label for="${header.idHeader}">${header.name}</label>
						<div class="input">
							<#assign header = header />
							<#include "/views/PageList/" + header.type?lower_case + "/edit.ftl" />
						</div>
				</div>
			</#list>
		</fieldset>
	</form>
</div>
