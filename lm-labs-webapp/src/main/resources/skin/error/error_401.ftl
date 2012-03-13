<@extends src="/views/labs-manage-base.ftl"> 


<@block name="title">
       401 - Problème de sécurité
</@block>

	<@block name="css">
		<@superBlock/>
        <link rel="stylesheet/less" href="${skinPath}/less/theme/labs/specific.less">
    </@block>



<@block name="content">
  <div class="alert alert-block alert-error no-fade">
  <p><strong>Vous n'avez pas le droit d'effectuer cette action</strong> Cela peut être du au fait que :
  <ul>
    <li>L'administrateur du site ne vous a pas donné les bons droits</li>
    <li>Vous n'êtes pas connecté (en vous connectant, cela peut
      rêgler le problème)</li>
  </ul></p>
  <div class="alert-actions">
    <a class="btn btn-small" href="${Context.modulePath}">Liste des sites</a>
  </div>
  </div>
</@block>
</@extends>




