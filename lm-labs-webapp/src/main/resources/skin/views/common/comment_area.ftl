<#assign canWrite = Session.hasPermission(Document.ref, 'Write') />

  <#if !Context.principal.isAnonymous() && canWrite>
    <div onmouseover="tooltip('${Context.getMessage('tooltip.ckeditor.inline')}');" onmouseout="exit();">
      <div class="well">
      <div id="commentField" formWysiwyg">${Document.page.commentaire}</div>
      <div style="clear:both;"></div>
      </div>

      <script type="text/javascript">
        <#include "views/common/ckeditor_config.ftl" />

        $('#commentField').ckeip({
          e_url: '${This.path}/updateCommentaire',
          data: {
            commentaire : jQuery("#commentField").html()
          },
          ckeditor_config: ckeditorconfig
        });
      </script>
    </div>
  <#else>
    <div id="commentField">
      &nbsp;${Document.page.commentaire}
    </div>
  </#if>