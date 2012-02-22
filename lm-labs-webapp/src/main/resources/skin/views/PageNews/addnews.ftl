<@extends src="/views/TemplatesBase/" + This.page.template.templateName + "/template.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${site.title}-${This.document.title}</@block>

  <@block name="scripts">
    <@superBlock/>
      <script type="text/javascript" src="${skinPath}/js/jquery/jquery-ui-1.8.14.min.js"></script>
      <script type="text/javascript" src="${skinPath}/js/LabsNews.js"></script>
  </@block>

  <@block name="css">
    <@superBlock/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/PageNews.css"/>
      <link rel="stylesheet" type="text/css" media="all" href="${skinPath}/css/jquery/jquery-ui-1.8.14.datePicker.css"/>
  </@block>
  
  <@block name="docactions">
  </@block>

  <@block name="content">
      <div id="content" class="">



<#--<div id="editprops">
<h1>${Context.getMessage('label.labsNews.add.news.title')}</h1>
<form class="form-horizontal" method="post" action="${This.path}" id="form-add-news-pageNews" >
  <fieldset>
    <div class="control-group">
      <label class="control-label" for="newsTitle">${Context.getMessage('label.labsNews.edit.title')}</label>
      <div class="controls">
        <input type="text" class="xlarge required" required-error-text="${Context.getMessage('label.labsNews.edit.required.title')}" name="dc:title"/>
      </div>
    </div>

    <div class="control-group">
      <label class="control-label" for="newsPeriod">${Context.getMessage('label.labsNews.edit.period')}</label>
      <div class="controls">
        <input id="newsStartPublication" class="input date-pick required" required-error-text="${Context.getMessage('label.labsNews.edit.required.startPublication')}" name="newsStartPublication" />
        ${Context.getMessage('label.labsNews.edit.au')}
        <input id="newsEndPublication"  class="input date-pick" name="newsEndPublication"/>
      </div>
    </div>

    <div class="control-group">
      <label class="control-label" for="newsModel">${Context.getMessage('label.labsNews.edit.model')}</label>
      <div class="controls">
        <label class="inline radio"><input type="radio" name="newsModel"  value="1COL"> ${Context.getMessage('label.labsNews.edit.model.simple')}</label>
        <label class="inline radio"><input type="radio" name="newsModel" checked value="2COL"> ${Context.getMessage('label.labsNews.edit.model.picture')}</label>
      </div>
    </div>

  </fieldset>
  <div class="form-actions">
    <input type="submit" class="btn btn-primary required-fields" form-id="form-add-news-pageNews" value="${Context.getMessage('label.labsNews.edit.valid')}" />
    <button class="btn" type="reset">Reset</button>
  </div	>
</form>

</div>


<script type="text/javascript">
  $(document).ready(function() {
      initEditDateNews();
  });




  </script> TODO -->

      		<h1>Création d'une news</h1>
      		<#assign news = null/>
			<#include "views/LabsNews/editProps.ftl" />
      </div>
  </@block>
  <@block name="pageCommentable"></@block>
</@extends>