<#assign userPreferenceValues = This.userPreferenceValues />
<#assign key = "rssTitle1" />
<div class="control-group">
    <label class="control-label" for="${key}" >Titre du flux RSS</label>
    <div class="controls" >
        <input type="text" class="input" name="${key}" value="${userPreferenceValues[key]}" />
    </div>
</div>
<#assign key = "rssUrl1" />
<div class="control-group">
    <label class="control-label" for="${key}" >URL du flux RSS</label>
    <div class="controls" >
        <input type="text" class="input" name="${key}" value="${userPreferenceValues[key]}" />
    </div>
</div>
