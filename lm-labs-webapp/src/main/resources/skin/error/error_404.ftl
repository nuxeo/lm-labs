<@extends src="/views/labs-manage-base.ftl">


<@block name="title">
       404 - Resource Non trouvée
</@block>


<@block name="content">
  <div class="alert-message block-message error">
  <p><strong>La page recherchée n'existe pas</strong> Cela peut être du au fait que :
  <ul>
    <li>La page n'existe plus</li>
    <li>L'adresse est incorrecte</li>
    <li>Vous n'avez pas le droit de voir la page (en vous connectant, cela peut
      rêgler le problème)</li>
  </ul></p>
  <div class="alert-actions">
    <a class="btn small" href="${Context.modulePath}">Liste des sites</a>
  </div>
</div>
  <a href="
</@block>
</@extends>


