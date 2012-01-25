<@extends src="/views/TemplatesBase/" + site.template.templateName + "/template.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${site.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
    <script type="text/javascript" src="${skinPath}/js/PageNews.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
    <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/wysiwyg_editor.css"/>
  </@block>

  <@block name="docactionsaddpage"></@block>
  <@block name="docactionsonpage"></@block>
  
  <#assign isContributor = site?? && site.isContributor(Context.principal.name) />

  <@block name="content">
    <#if news??>
      <div id="content" class="container">
          <section>
              <div class="page-header">
                <h1>${news.title} <small>${Context.getMessage('label.labsNews.display.by')} ${news.lastContributorFullName}</small></h1>
                <small>${Context.getMessage('label.labsNews.display.publish')} ${news.startPublication.time?string('dd MMMMM yyyy')}</small>
              </div>

			  <#if isContributor >
				<a class="btn open-dialog editblock" rel="editprops">Modifier les propriétés</a>
				<div id="editprops">
					<h1>Editer les information de la news</h1>
					<form method="post" action="${This.path}" class="well">
					  <fieldset>
					    <legend>Propriétés de la news</legend>
					    <div class="clearfix">
					      <label for="newsTitle">${Context.getMessage('label.labsNews.edit.title')}</label>
					      <div class="input">
					        <input class="xlarge" name="dc:title"  value="<#if news?? >${news.title}</#if>">
					      </div>
					    </div>
					
					    <div class="clearfix">
					      <label for="newsPeriod">${Context.getMessage('label.labsNews.edit.period')}</label>&nbsp;:&nbsp;
					      <div class="input">
					        <input class="date-pick" name="newsStartPublication"  <#if news?? && news.startPublication!=null> value="${This.labsNews.startPublication.time?string('dd/MM/yyyy')}" </#if>">
					        ${Context.getMessage('label.labsNews.edit.au')}
					        <input class="date-pick" name="newsEndPublication"  <#if news?? && news.endPublication!=null> value="${This.labsNews.endPublication.time?string('dd/MM/yyyy')}" </#if>">
					      </div>
					    </div>
					
					    <div class="actions">
					      <input type="submit" class="btn" value="${Context.getMessage('label.labsNews.edit.valid')}" />
					  	</div	>
					  </fieldset>
					</form>
				</div>
		      	<script type="text/javascript">
				  $(document).ready(function() {
				      initEditDateNews();
				  });
			  	</script>
			  </#if>
				
		  	<#assign section_index=0/>
		  	<#list news.rows as row>
		  		<#if isContributor >
		          <div class="row" id="row_s${section_index}_r${row_index}">
		              <#list row.contents as content>
			              <div class="span${content.colNumber} columns editblock">
			                <div id="s_${section_index}_r_${row_index}_c_${content_index}">${content.html}</div>
			                <script type="text/javascript">
			                  $('#s_${section_index}_r_${row_index}_c_${content_index}').ckeip({
			                    e_url: '${This.path}/s/${section_index}/r/${row_index}/c/${content_index}',
			                    ckeditor_config: ckeditorconfig,
			                    emptyedit_message: "${Context.getMessage('label.PageHtml.double_click_to_edit_content')}",
			                    view_style: "span${content.colNumber} columns "
			                    });
			                </script>
			                <noscript>
			                  <a  class="btn editblock" href="${This.path}/s/${section_index}/r/${row_index}/c/${content_index}/@views/edit">Modifier</a>
			                </noscript>
			                &nbsp; <!-- Needed to give an empty cell a content -->
			              </div>
		              </#list>
		              <div style="margin-left:20px;clear:both;" class="editblock">
			              <form id="rowdelete_s${section_index}_r${row_index}" action="${This.path}/s/${section_index}/r/${row_index}/@delete" method="get" onsubmit="return confirm('Voulez vous vraiment supprimer la ligne ?');">
			              	<button type="submit" class="btn small danger">Supprimer la ligne</button>
			              </form>
		              </div>
		          </div>
		        <#else>
		        	<div class="row" id="row_s${section_index}_r${row_index}">
	                  <#list row.contents as content>
	                    <div class="span${content.colNumber} columns">
	                      <#if content.html == "">
	                        &nbsp;
	                      <#else>
	                        ${content.html}
	                      </#if>
	                    </div>
	                  </#list>
	                </div>
		        </#if>
		    </#list>
			
			<#if isContributor >
	          <div class="well editblock">
	            <form id="addrow" action="${This.path}/s/0" method="post" >
	              <input type="hidden" name="action" value="addrow"/>
	              <fieldset>
	                <legend>Ajouter une ligne</legend>
	                <div class="clearfix">
	                  <label for="title">Type de ligne</label>
	                  <div class="input">
	                    <select name="rowTemplate">
	                        <option value="1COL">1 colonne</option>
		                    <option value="2COL_5050">2 colonnes (50/50)</option>
		                    <option value="2COL_2575">2 colonnes (25/75)</option>
		                    <option value="2COL_7525">2 colonnes (75/25)</option>
		                    <option value="2COL_3366">2 colonnes (33/66)</option>
		                    <option value="2COL_6633">2 colonnes (66/33)</option>
		                    <option value="3COL">3 colonnes (33/33/33)</option>
		                    <option value="4COL">4 colonnes (25/25/25/25)</option>
		                </select>
		                <button type="submit" class="btn small primary">Ajouter</button>
		                <span class="help-block">
		                    Sélectionnez le type de ligne à ajouter. Plusieurs modèles sont disponibles, les chiffres entre
		                    parenthèses représentent des pourcentages de taille de colonne.
		                 </span>
	                  </div>
	
	                </div><!-- /clearfix -->
	              </fieldset>
	            </form>
	          </div>
	        </#if>

		  </section>
      </div>
    </#if>
  </@block>
</@extends>