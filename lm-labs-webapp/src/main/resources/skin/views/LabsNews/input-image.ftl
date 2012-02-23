	<div style="float: right;">
		<img src="${property.value}" style="width: 40px;border:1px dashed black;"/>
		<span onclick="javascript:deleteElement('${This.path}/propertyTheme/${property.key}', 'hidePropertyImage(${cptProperties})', '${Context.getMessage('label.labssites.appearance.theme.edit.element.delete.confirm')}');" style="cursor: pointer;">
	    	<img title="${Context.getMessage('label.delete')}" src="${skinPath}/images/x.gif"/>
	  	</span>
	</div>
