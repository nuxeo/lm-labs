<@extends src="/views/labs-manage-base.ftl"> 


<@block name="title">
       500 - Erreur interne
</@block>

	<@block name="css">
		<@superBlock/>
        <link rel="stylesheet/less" href="${skinPath}/less/theme/labs/specific.less">
    </@block>



<@block name="content">
  <div class="alert alert-block alert-error no-fade">
  <p><strong>Une erreur inattendue est survenue</strong> Merci de contacter l'administrateur

  <div id="stacktrace" style="display:none">
    <pre class="prettyprint">
      ${trace}
    </pre>

  </div>
  <div class="alert-actions">
    <a class="btn btn-small" href="#" onclick="$('#stacktrace').toggle();return false;">Détail de l'erreur</a>
  </div>
</div>
</@block>
</@extends>
