    <input type="hidden" id="urlAvailability" value="false" />
    <div class="control-group">
      <label class="control-label" for="labsSiteURL">${Context.getMessage('label.labssite.edit.url')}</label>
      <div class="controls">
        <span>${This.URL}/</span><input class="input required" name="webc:url" id="labsSiteURL" required-error-text="${Context.getMessage('label.labssites.edit.required.url')}"/>
        <btn id="verifyUrlAvailability" class="btn btn-primary disabled" data-toggle="button" data-loading-text="En cours ..." data-complete-text="<i class='icon-ok-circle'></i>disponible" data-failed-text="<i class='icon-ban-circle'></i>indisponible" >Vérifier</btn>
        <p class="help-block">C'est par ce lien que le site sera accessible</p>
      </div>
    </div>
