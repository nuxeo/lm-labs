<style>
  label.error { float: none; color: red; font-size:12px; padding-left: .5em;  }
</style>
<h1>
  ${Context.getMessage('label.labsNews.edit.edit')}
</h1>
<form method="post" action="${This.path}">
  <fieldset>
    <div class="clearfix">
      <label for="newsTitle">${Context.getMessage('label.labsNews.edit.title')}</label>
      <div class="input">
        <input id="newsTitle" class="error" type="text" size="20" name="dc:title"  value="<#if This.labsNews != null >${This.labsNews.title}</#if>">
      </div>
    </div>

    <div class="clearfix">
      <label for="newsPeriod">${Context.getMessage('label.labsNews.edit.period')}</label>&nbsp;:&nbsp;
      <div class="input">
        <input id="newsStartPublication" class="date-pick formfield" type="text" size="20" name="newsStartPublication"  <#if This.labsNews!=null && This.labsNews.startPublication!=null> value="${This.labsNews.startPublication.time?string('dd/MM/yyyy')}" </#if>">
        ${Context.getMessage('label.labsNews.edit.au')}
        <input id="newsEndPublication" class="date-pick formfield" type="text" size="20" name="newsEndPublication"  <#if This.labsNews!=null && This.labsNews.endPublication!=null> value="${This.labsNews.endPublication.time?string('dd/MM/yyyy')}" </#if>">
      </div>
    </div>
    <div class="clearfix">
      <div class="input">
        <textarea name="newsContent" id="newsContent" class="contentText"><#if This.labsNews != null >${This.labsNews.content}</#if></textarea>
      </div>
    </div>


  </fieldset>
    <div class="actions">
      <input type="submit" class="btn" value="${Context.getMessage('label.labsNews.edit.valid')}" />
  </div	>
</form>
<script type="text/javascript">
  $(document).ready(function() {
      $.validator.messages.required = "${Context.getMessage('label.externalURL.edit.required')}";
  });

  initCheckeditor();
  initEditDateNews();

  function submitForm(){
    $("newsContent").val(CKEDITOR.instances.newsContent.getData());
    if ($("#form-news").valid()){
      $("#form-news").submit();
    }
  }

  </script>
