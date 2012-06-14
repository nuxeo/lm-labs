<h1>${Context.getMessage('label.pageList.edit.manage.title')}</h1>
<div class="container-fluid" style="padding-left: 0;height: 290px;">
  <div class="row-fluid">
	<div class="sidebar span" style="width: 232px;">
		<h4>${Context.getMessage('label.pageList.edit.listHeader.title')}</h4>
		<ul id="ul_action_on_header"></ul>
		<a href="#" onClick="javascript:addOneHeader();">${Context.getMessage('label.pageList.edit.listHeader.addHeader')}</a>
		<form class="form">
		  <fieldset>
			<!--         AllContibutors      ------->
			<div class="control-group">
				<div class="controls">
				<label class="checkbox" for="allContributors">
					<input id="allContributors" type="checkbox" name="allContributors" <#if This.allContributors >checked="true"</#if> />
				${Context.getMessage('label.pageList.edit.listHeader.allContributors')}</label>
				</div>
			</div>
			<!--         CommentableLines      ------->
			<div class="control-group">
				<div class="controls">
				<label class="checkbox" for="commentableLines">
					<input id="commentableLines" type="checkbox" name="commentableLines" <#if This.commentableLines >checked="true"</#if> />
				${Context.getMessage('label.pageList.edit.listHeader.commentableLines')}</label>
				</div>
			</div>
		  </fieldset>
		</form>
	</div>
	<div class="content span" style="width: 280px;">
		<h4 style="padding-left: 10px;" >${Context.getMessage('label.pageList.edit.listHeader.editHeader')}</h4>
		<div id="divEditHeader">
			<form method="post" name="form-manageList" id="form-manageList">
				<fieldset style="padding: 5px 0 0 20px;">
					<!--         NAME      ------->
					<div class="control-group">
						<label class="control-label" for="headerName">${Context.getMessage('label.pageList.edit.editHeader.name')}</label>
						<div class="controls">
							<input id="headerName" class="input error" type="text" size="20" name="headerName" onkeyup="javascript:changeHeaderName();" onblur="javascript:changeAllHeaderName();">
						</div>
					</div>
					<!--         TYPE      ------->
					<div class="control-group">
						<label class="control-label" for="headerType">${Context.getMessage('label.pageList.edit.editHeader.type')}</label>
						<div class="controls">
							<select name="headerType" id="headerType" onChange="javascript:manageEditType();">
			            		<#list This.getHeaderTypes() as type>
			            			<option value="${type.name()}">${Context.getMessage(type.getI18n())}</option>
			            		</#list>
			            	</select>
						</div>
						<div id="div_edit_select" style="display: none;">
		            		<div id="div_edit_options"></div>
		            		<a href="#" onClick="javascript:addOneOption();">${Context.getMessage('label.pageList.edit.editHeader.options.add')}</a>
		            	</div>
						<div id="div_edit_format_date" style="display: none;">
							<label class="control-label" for="headerFormatDate">${Context.getMessage('label.pageList.edit.editHeader.formatDate')}</label>
							<select name="headerFormatDate" id="headerFormatDate" onChange="javascript:changeAllHeaderFormatDate();">
			            		<#list This.getHeaderFormatDates() as formatDate>
			            			<option value="${Context.getMessage(formatDate.getI18n())}">${Context.getMessage(formatDate.getI18n() + '.label')}</option>
			            		</#list>
			            	</select>
		            	</div>
					</div>
					<!--         WIDTH      ------->
					<div class="control-group">
						<label class="control-label" for="headerWidth">${Context.getMessage('label.pageList.edit.editHeader.width')}</label>
			            <div class="controls">
			            	<select name="headerWidth" id="headerWidth" onChange="javascript:changeAllHeaderWidth();">
			            		<option value="${This.getDefault()}">${Context.getMessage('label.pageList.edit.editHeader.default')}</option>
			            		<#list This.getHeaderWidths() as size>
			            			<option value="${size.name()}">${size.getSize()}</option>
			            		</#list>
			            	</select>
			            </div>
		            </div>
		            <!--         FONTNAME     ------->
					<div class="control-group">
						<label class="control-label" for="headerFontName">${Context.getMessage('label.pageList.edit.editHeader.fontName')}</label>
			            <div class="controls">
			            	<select name="headerFontName" id="headerFontName" onChange="javascript:changeAllHeaderFontName();">
								<option value="${This.getDefault()}">${Context.getMessage('label.pageList.edit.editHeader.default')}</option>
								<#list Common.getFontFamilies() as font>
									<option value="${font.getCssName()}">${font.getDisplayName()}</option>
								</#list>
							</select>
			            </div>
		            </div>
		            <!--         FONTSIZE      ------->
					<div class="control-group">
						<label class="control-label" for="headerFontSize">${Context.getMessage('label.pageList.edit.editHeader.fontSize')}</label>
			            <div class="controls">
			            	<select name="headerFontSize" id="headerFontSize" onChange="javascript:changeAllHeaderFontSize();">
								<option value="${This.getDefault()}">${Context.getMessage('label.pageList.edit.editHeader.default')}</option>
								<#list Common.getFontSizes() as size>
									<option value="${size.getSize()}">${size.getSize()}</option>
								</#list>
							</select>
			            </div>
		            </div>
		            <!--         ALTERABLE      ------->
					<div class="control-group">
						<label class="control-label" for="headerAlterable">${Context.getMessage('label.pageList.edit.editHeader.alterable')}</label>
			            <div class="controls">
			            	<label class="radio inline">
                				<input type="radio" id="headerAlterable" name="headerAlterable" value="true" onchange="javascript:changeAllHeaderAlterable();">
                				OUI
              				</label>
			            	<label class="radio inline">
                				<input type="radio" id="headerAlterable" name="headerAlterable" value="false" onchange="javascript:changeAllHeaderAlterable();">
                				NON
              				</label>
			            </div>
		            </div>
		            <!--         MANDATORY      ------->
					<!--<div class="control-group">
						<label class="control-label" for="headerMandatory">${Context.getMessage('label.pageList.edit.editHeader.mandatory')}</label>
			            <div class="controls">
			            	<label class="radio inline">
                				<input type="radio" id="headerMandatory" name="headerMandatory" value="true" onchange="javascript:changeAllHeaderMandatory();">
                				OUI
              				</label>
			            	<label class="radio inline">
                				<input type="radio" id="headerMandatory" name="headerMandatory" value="false" onchange="javascript:changeAllHeaderMandatory();">
                				NON
              				</label>
			            </div>
		            </div>-->
				</fieldset>
			</form>
		</div>
	</div>
  </div>
</div>
<div  class="actions">
	<button id="saveHeaderList" class="btn btn-primary" onClick="javascript:saveHeaderList('${This.path}');" title="${Context.getMessage('label.pageList.edit.manage.save')}">${Context.getMessage('label.pageList.edit.manage.save')}</button>
	<button id="cancel" class="btn" onClick="javascript:closeManageList();" title="${Context.getMessage('label.pageList.edit.manage.cancel')}">${Context.getMessage('label.pageList.edit.manage.cancel')}</button>
	<!-- <br /><br />
	<button id="StructureJsHeaders" class="btn btn-info" onClick="javascript:alert(headersCollection.toString());" >StructureJsHeaders</button>-->
</div>
