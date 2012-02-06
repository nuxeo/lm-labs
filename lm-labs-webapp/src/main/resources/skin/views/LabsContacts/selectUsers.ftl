<ul style="list-style: none;">
	<#if errorMessage != null>
		<i>(${errorMessage})</i>
	<#else>
	  <#list suggests as suggest >
		  <li>
		    <input type="radio" name="radioUserInput" <#if suggests?size == 1>checked="checked"</#if> value="${suggest.name}">${suggest.name}<#if suggest.fullName?length &gt; 0> (${suggest.fullName})</#if>
		    </input>
		  </li>
	  </#list>
	</#if>
</ul>