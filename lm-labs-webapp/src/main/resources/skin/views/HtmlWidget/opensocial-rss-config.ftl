<#assign userPreferenceValues = This.userPreferenceValues />
<#assign x=1>
<#list 1..x as i>
    <#assign key = "rssUrl" + i />
<div class="control-group">
    <label class="control-label" for="${key}" >URL du flux RSS</label>
    <div class="controls" >
        <input type="text" class="input" name="${key}" value="${userPreferenceValues[key]}" />
    </div>
</div>
</#list>
