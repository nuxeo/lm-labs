<h1>${Context.getMessage('label.pageList.edit.manage.title')}</h1>
<div class="container" style="width: auto;height: 330px;">
	<!--<div id="error_mandatory" class="alert-message block-message error fade in" style="display: none;" data-alert="alert">
		<a class="close" href="#">×</a>
		<p>peermkljsdf mkljsdf </p>
	</div>-->
	<section id="grid-system">
		<div class="row">
			<div class="span8 columns">
				${Context.getMessage('label.pageList.edit.manage.explain')}
			</div>
		</div>
		<div class="row">
			<div class="span4 columns" id="divListHeader">
				<h4>${Context.getMessage('label.pageList.edit.listHeader.title')}</h4>
				<ul id="ul_action_on_header"></ul>
				<a href="#" onClick="javascript:addOneHeader();">${Context.getMessage('label.pageList.edit.listHeader.addHeader')}</a>
			</div>
			<div class="span4 columns" id="divContainerEditHeader">
				<h4 class="titleEditBlocs">${Context.getMessage('label.pageList.edit.listHeader.editHeader')}</h4>
				<div id="divEditHeader">
					<form method="post" name="form-manageList" id="form-manageList" class="form-stacked">
						<fieldset>
							<!--         NAME      ------->
							<div class="clearfix">
								<label for="headerName">${Context.getMessage('label.pageList.edit.editHeader.name')}</label>
								<div class="input">
									<input id="headerName" class="error" type="text" size="20" name="headerName" onkeyup="javascript:changeHeaderName();" onblur="javascript:changeAllHeaderName();">
								</div>
							</div>
							<!--         TYPE      ------->
							<div class="clearfix">
								<label for="headerType">${Context.getMessage('label.pageList.edit.editHeader.type')}</label>
								<div class="input">
									<select name="headerType" id="headerType" onChange="javascript:manageEditSelect();">
					            		<#list This.getHeaderTypes() as type>
					            			<option value="${type.name()}">${Context.getMessage(type.getI18n())}</option>
					            		</#list>
					            	</select>
								</div>
								<div id="div_edit_select" style="display: none;">
				            		<div id="div_edit_options"></div>
				            		<a href="#" onClick="javascript:addOneOption();">${Context.getMessage('label.pageList.edit.editHeader.options.add')}</a>
				            	</div>
							</div>
							<!--         WIDTH      ------->
							<div class="clearfix">
								<label for="headerWidth">${Context.getMessage('label.pageList.edit.editHeader.width')}</label>
					            <div class="input">
					            	<select name="headerWidth" id="headerWidth" onChange="javascript:changeAllHeaderWidth();">
					            		<option value="${This.getDefault()}">${Context.getMessage('label.pageList.edit.editHeader.default')}</option>
					            		<#list This.getHeaderWidths() as size>
					            			<option value="${size.name()}">${size.getSize()}</option>
					            		</#list>
					            	</select>
					            </div>
				            </div>
				            <!--         FONT      ------->
							<div class="clearfix">
								<label for="headerFont">${Context.getMessage('label.pageList.edit.editHeader.font')}</label>
					            <div class="input">
					            	<select name="headerFont" id="headerFont" onChange="javascript:changeAllHeaderFont();">
					            		<option value="${This.getDefault()}">${Context.getMessage('label.pageList.edit.editHeader.default')}</option>
					            		<#list This.getHeaderFonts() as font>
					            			<option value="${font.fontName.name()}-${font.fontSize.name()}">${Context.getMessage(font.getNameI18n())}-${font.fontSize.size}</option>
					            		</#list>
					            	</select>
					            </div>
				            </div>
						</fieldset>
					</form>
				</div>
			</div>
		</div>
	</section>
</div>
<div  class="actions">
	<button id="saveHeaderList" class="btn primary" onClick="javascript:saveHeaderList('${This.path}');" title="${Context.getMessage('label.pageList.edit.manage.save')}">${Context.getMessage('label.pageList.edit.manage.save')}</button>
	<button id="cancel" class="btn" onClick="javascript:closeManageList();" title="${Context.getMessage('label.pageList.edit.manage.cancel')}">${Context.getMessage('label.pageList.edit.manage.cancel')}</button>
	<!-<button id="error_mandatory" class="btn info" onClick="javascript:$('#error_mandatory').style='display: block;';" >error_mandatory</button>
	-<br /><br />
	<button id="StructureJsHeaders" class="btn info" onClick="javascript:alert(headersCollection.toString());" >StructureJsHeaders</button>-->
</div>