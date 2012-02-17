<section>
  <div class="page-header">
    <h1>${news.title} <small>${Context.getMessage('label.labsNews.display.by')} ${news.lastContributorFullName}</small> </h1>
    <small>${Context.getMessage('label.labsNews.display.publish')} ${news.startPublication.time?string('dd MMMMM yyyy')}</small>
  </div>

  <a class="btn open-dialog" rel="editprops">Modifier les propriétés</a>
<div id="editprops">
	<h1>Editer les information de la news</h1>
	<form method="post" action="${This.path}" class="well">
	  <fieldset>
	    <legend>Propriétés de la news</legend>
	    <div class="clearfix">
	      <label for="newsTitle">${Context.getMessage('label.labsNews.edit.title')}</label>
	      <div class="input">
	        <input class="xlarge required" name="dc:title"  value="<#if news?? >${news.title}</#if>" />
	      </div>
	    </div>
	
	    <div class="clearfix">
	      <label for="newsPeriod">${Context.getMessage('label.labsNews.edit.period')}</label>&nbsp;:&nbsp;
	      <div class="input">
	        <input type="text" class="date-pick required" name="newsStartPublication"  <#if news?? && news.startPublication!=null> value="${This.labsNews.startPublication.time?string('dd/MM/yyyy')}" </#if> />
	        ${Context.getMessage('label.labsNews.edit.au')}
	        <input class="date-pick" name="newsEndPublication"  <#if news?? && news.endPublication!=null> value="${This.labsNews.endPublication.time?string('dd/MM/yyyy')}" </#if> />
	      </div>
	    </div>
	
	
	
	    <div class="actions">
	      <input type="submit" class="btn" value="${Context.getMessage('label.labsNews.edit.valid')}" />
	  </div	>
	  </fieldset>
	</form>

</div>
  <#if news??>
  <#list news.rows as row>
          <div class="row" id="row_s${section_index}_r${row_index}">
              <#list row.contents as content>
              <div class="span${content.colNumber} columns">
                <div id="s_${section_index}_r_${row_index}_c_${content_index}">${content.html}</div>
                <script type="text/javascript">
                  $('#s_${section_index}_r_${row_index}_c_${content_index}').ckeip({
                    e_url: '${This.path}/s/${section_index}/r/${row_index}/c/${content_index}',
                    ckeditor_config: ckeditorconfig,
                    emptyedit_message: "${Context.getMessage('label.PageHtml.double_click_to_edit_content')}"
                    });
                </script>
                <noscript>
                  <a  class="btn editblock" href="${This.path}/s/${section_index}/r/${row_index}/c/${content_index}/@views/edit">Modifier</a>
                </noscript>
                &nbsp; <!-- Needed to give an empty cell a content -->
              </div>
              </#list>

               <div style="margin-left:20px" class="editblock">
              <form id="rowdelete_s${section_index}_r${row_index}" action="${This.path}/s/${section_index}/r/${row_index}/@delete" method="get" onsubmit="return confirm('Voulez vous vraiment supprimer la ligne ?');">
                    <button type="submit" class="btn small danger">Supprimer la ligne</button>
                  </form>
            </div>
            </div>
          </#list>

          <div class="well editblock">
          <form id="addrow_${section_index}" action="${This.path}/s/${section_index}" method="post" >
          <input type="hidden" name="action" value="addrow"/>
              <fieldset>
                <legend>Ajouter une ligne</legend>
                <div class="clearfix">
                <label for="title">Type de ligne</label>
                  <div class="input">
                    <select name="rowTemplate">
	                    <#assign layouts = This.columnLayoutsSelect />
	                    <#list layouts?keys as layoutCode >
	                      <option value="${layoutCode}">${Context.getMessage(layouts[layoutCode])}</option>
	                    </#list>
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
<script type="text/javascript">
  initCheckeditor();
  initEditDateNews();

  function submitForm(){
    $("newsContent").val(CKEDITOR.instances.newsContent.getData());
    if ($("#form-news").valid()){
      $("#form-news").submit();
    }
  }

  </script>
