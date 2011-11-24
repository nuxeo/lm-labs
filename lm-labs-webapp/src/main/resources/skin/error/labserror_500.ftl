<@extends src="/views/labs-manage-base.ftl"> 


<@block name="title">
       500 - Erreur interne
</@block>



<@block name="content">
  <div class="alert-message block-message error no-fade">
  <p><strong>Une erreur inatendue est survenue</strong> Merci de contacter l'administrateur

  <div id="stacktrace" style="display:none">
    <pre class="prettyprint">
      ${trace}
    </pre>

  </div>
  <div class="alert-actions">
    <a class="btn small" href="#" onclick="$('#stacktrace').toggle();return false;">Détail de l'erreur</a>
  </div>
</div>
</@block>
</@extends>
