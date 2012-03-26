<button class="btn" data-toggle="collapse" data-target="#list-line-input"><i class="icon-plus"></i>Attacher un fichier</button>
<div id="list-line-input" class="collapse" >
    <div >
        <input type="file" class="input-file required" id="list-line-input-file" name="list-line-input-file" />
        <button class="btn" id="list-line-add-file-btn"><i class="icon-ok"></i>Envoyer fichier</button>
    </div>
</div>
<table class="tablelabstable">
  <thead>
      <tr>
          <th>Titre</th>
          <th>&nbsp;</th>
      </tr>
  </thead>
  <tbody>
  <#list This.files as file >
      <tr>
          <td>${file.dublincore.title}</td>
          <td>&nbsp;</td>
      </tr>
  </#list>
  </tbody>
</table>

jquery(document).ready(function() {
    jQuery('#list-line-add-file-btn').click(function() {
    });
});