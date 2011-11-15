<@extends src="/views/labs-base.ftl">
  <#assign isAuthorized = Session.hasPermission(Document.ref, 'Write')>

  <@block name="title">${Context.module.name} - ${This.document.type} ${This.document.title}</@block>

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
  
  <@block name="docactions">
  </@block>

  <@block name="content">
      <div id="content" class="container">



<div id="editprops">
<h1>Créer une news</h1>
<form method="post" action="${This.path}" id="form-add-news-pageNews" >
  <fieldset>
    <div class="clearfix">
      <label for="newsTitle">${Context.getMessage('label.labsNews.edit.title')}</label>
      <div class="input">
        <input type="text" class="xlarge required" required-error-text="${Context.getMessage('label.labsNews.edit.required.title')}" name="dc:title"/>
      </div>
    </div>

    <div class="clearfix">
      <label for="newsPeriod">${Context.getMessage('label.labsNews.edit.period')}</label>&nbsp;:&nbsp;
      <div class="input">
        <input id="newsStartPublication" class="date-pick required" required-error-text="${Context.getMessage('label.labsNews.edit.required.startPublication')}" name="newsStartPublication" />
        ${Context.getMessage('label.labsNews.edit.au')}
        <input id="newsEndPublication"  class="date-pick" name="newsEndPublication"/>
      </div>
    </div>

    <div class="clearfix">
      <label for="newsModel">${Context.getMessage('label.labsNews.edit.model')}</label>
      <div class="input">
        <label><input type="radio" name="newsModel"  value="1COL"> News simple</label>
      </div>
      <div class="input">
        <label><input type="radio" name="newsModel" checked value="2COL"> News avec photo</label>
      </div>
    </div>

    <div class="clearfix">
      <label for="newsContent">${Context.getMessage('label.labsNews.edit.content')}</label>
      <div class="input">
        <textarea name="newsContent" class="ckedit"></textarea>
      </div>
    </div>





    <div class="actions">
      <input type="submit" class="btn required-fields" form-id="form-add-news-pageNews" value="${Context.getMessage('label.labsNews.edit.valid')}" />
  </div	>
  </fieldset>
</form>

</div>


<script type="text/javascript">
  $(document).ready(function() {
        initCheckeditor();
      initEditDateNews();
  });




  </script>

      </div>
  </@block>
  <@block name="pageCommentable"></@block>
</@extends>