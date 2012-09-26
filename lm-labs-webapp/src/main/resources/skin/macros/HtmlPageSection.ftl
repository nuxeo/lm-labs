<#macro displayHtmlPageSection page section section_index viewMode idsPrefix >
		<div id="${idsPrefix}div_section_${section_index}" 
			class="<#if section_index == 0>active </#if><#if viewMode == "tabbed" && 1 < sections?size >tab-pane<#elseif viewMode == "carousel">item</#if>" 
			>
		    <section id="${idsPrefix}section_${section_index}">
		        <div class="page-header"
		        <#if viewMode == "tabbed" && 1 < sections?size > style="margin-top: 0px; padding-bottom: 0px; border-bottom: 0px;"
		        <#elseif section.title?length == 0 && section.description?length == 0 > style="padding-bottom: 0px;"</#if>
		        >
		            <#if viewMode != "tabbed" || (viewMode == "tabbed" && sections?size <= 1) ><h1 style="display:inline;">${section.title}</h1></#if><h2 style="display:inline;"> <small>${section.description}</small></h2>
					<#if page.isDisplayable("pg:collapseType") && viewMode != "tabbed" >
					    <div style="font-size: 32px; float: right; margin-top: 7px;" >
					    	<i class="icon-minus-sign openCloseBt" title="${Context.getMessage('label.HtmlPage.collapse')}" onclick="slideSection(this, '');" ></i>
					    </div>
					</#if>
		        </div>
				<div id="${idsPrefix}div_section_${section_index}_rows">
				<#assign rows = section.rows />
		        <#list rows as row>
			           <div class="row-fluid<#if row.cssClass??> ${row.cssClass}</#if>" id="${idsPrefix}row_s${section_index}_r${row_index}">
			              <#list row.contents as content>
		                      <#assign isWidgetCol = false />
		                      <#assign isOsGadgetCol = false />
		                      <#assign widgets = [] />
		                      <@determineWidgetType content=content />
		                      <#if isOsGadgetCol >
			                        <div id="${idsPrefix}gadgetCol-s_${section_index}_r_${row_index}_c_${content_index}" class="span<#if maxSpanSize != content.colNumber >${content.colNumber}</#if> columns" >
			                        <#assign nbrOsGadgets = nbrOsGadgets + 1 />
			                        <script type="text/javascript">
			                        	userPrefsTab['${widgets[0].doc.id}'] = eval ( '(${This.getUserPrefsFormatJS(widgets[0].userPrefs)?js_string})' );
			                        </script>
			                        
			                        <div id="${widgets[0].doc.id}" class="opensocialGadgets gadget-${widgets[0].name} bloc"
			                        	data-gadget-specurl="${widgets[0].specUrl}"
										data-gadget-title="${widgets[0].name}"
			                        >
			                        </div>
			                        </div>
		                      <#elseif isWidgetCol >
		                        <div class="span<#if maxSpanSize != content.colNumber >${content.colNumber}</#if> columns" >
		                        <@displayContentHtmlWidget widget=widgets[0] widgetMode="view" sectionIdx=section_index rowIdx=row_index columnIdx=content_index content=content />
		                        </div>
		                      <#else>
		    	                <div class="span<#if maxSpanSize != content.colNumber >${content.colNumber}</#if> columns">
		                           <@displayContentHtml content=content />
		    	                </div>
		                      </#if>
			              </#list>
			           </div>
		        </#list>
				</div>
		    </section>
		</div>
</#macro>
