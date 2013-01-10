<#macro LabsSiteInputUrl value="" >
    <input type="hidden" id="urlAvailability" value="false" />
    <div class="control-group">
      <label class="control-label" for="labsSiteURL">${Context.getMessage('label.labssite.edit.url')}</label>
      <div class="controls">
      <#if value == "" >
      	<#assign baseUrl = This.URL />
      <#else>
      	<#assign baseUrl = Context.modulePath />
      </#if>
        <span>${baseUrl}/</span><input class="required span2" name="webc:url" value="${value}" id="labsSiteURL" required-error-text="${Context.getMessage('label.labssites.edit.required.url')}"/>
        <btn id="verifyUrlAvailability" class="btn btn-small btn-primary<#if value == "" > disabled</#if>" data-toggle="button" data-loading-text="En cours ..." data-complete-text="<i class='icon-ok-circle'></i>disponible" data-failed-text="<i class='icon-ban-circle'></i>indisponible" >Vérifier</btn>
        <p class="help-block">C'est par ce lien que le site sera accessible</p>
      </div>
    </div>
</#macro>