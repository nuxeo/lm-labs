
		<fieldset>
			<#list bean.headersSet as header>
				<div class="control-group">
					<label class="control-label" for="${header.idHeader}">${header.name}</label>
					<div class="controls">
						<#assign txtDisabled = ""  />
						<#if (line != null)>
							<#if (Context.principal.name == line.docLine['dc:creator'] || Session.hasPermission(Document.ref, 'Everything') || header.alterable)>
								<#assign txtDisabled = ""  />
							<#else>
								<#assign txtDisabled = "disabled" />
							</#if>
						</#if>
						<#assign txtRequired = ""  />
						<#if (header.mandatory)>
							<#assign txtRequired = "required"  />
						</#if>
						<#include "/views/PageList/" + header.type?lower_case + "/edit.ftl" />
					</div>
				</div>
			</#list>
			<#if This.previous.page?? && This.previous.page.isContributor(Context.principal.name)>
				<div class="control-group" id="divLineIsHidden">
					<label class="control-label" for="isVisible">${Context.getMessage('label.pageList.edit.line.hidden')?js_string}</label>
					<div class="controls">
						<label class="radio inline">
		                	<input type="radio" name="isHidden" class="isHidden" value="yes" <#if line?? && !line.visible >checked</#if>>
		                	Oui
		              	</label>
						<label class="radio inline">
		                	<input type="radio" name="isHidden" class="isHidden" value="no" <#if line?? && line.visible >checked</#if>>
		                	Non
		              	</label>
					</div>
				</div>
			<#else>
				<input type="hidden" name="isHidden" class="isHidden" value="no" />
			</#if>
			<input type="hidden" name="lastPage" id="lastPage" value="" />
			<input type="hidden" name="currentPage" id="currentPage" value="" />
		</fieldset>
<#if line != null>
	<div id="divBtnDeleteLine" style="text-align: right;">
		<a href="#" id="deleteLine" class="btn btn-warning" onClick="javascript:if(confirm('${Context.getMessage('label.pageList.line_deleted.confirm')?js_string}')){deleteLine('${This.previous.path}', '${line.docLine.id}');}{return false;}" title="${Context.getMessage('label.pageList.edit.manage.delete')}"><i class="icon-remove"></i>${Context.getMessage('label.pageList.edit.manage.delete')}</a>
	</div>
</#if>
