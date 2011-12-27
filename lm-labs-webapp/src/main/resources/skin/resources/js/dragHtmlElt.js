function addEventSimple(obj,evt,fn) {
	if (obj.addEventListener)
		obj.addEventListener(evt,fn,false);
	else if (obj.attachEvent)
		obj.attachEvent('on'+evt,fn);
}

function removeEventSimple(obj,evt,fn) {
	if (obj.removeEventListener)
		obj.removeEventListener(evt,fn,false);
	else if (obj.detachEvent)
		obj.detachEvent('on'+evt,fn);
}

dragDrop = {
	initialMouseX: undefined,
	initialMouseY: undefined,
	startX: undefined,
	startY: undefined,
	startMsgX: undefined,
	startMsgY: undefined,
	draggedObject: undefined,
	draggedMsg: undefined,
	addionnalStyle: undefined,
	confirmMsg: undefined,
	updateFunction: undefined,
	initElement: function (objId, msgId, objStyle, confirmMsg, updateFunction) {
		dragDrop.draggedMsg = document.getElementById(msgId);
		dragDrop.addionnalStyle = objStyle;
		dragDrop.confirmMsg = confirmMsg;
		dragDrop.updateFunction = updateFunction;
		element = document.getElementById(objId);
		element.onmousedown = dragDrop.startDragMouse;
		var links = $('a');
		var lastLink = links[links.length-1];
		lastLink.relatedElement = element;
	},
	startDragMouse: function (e) {
		dragDrop.startDrag(this);
		var evt = e || window.event;
		dragDrop.initialMouseX = evt.clientX;
		dragDrop.initialMouseY = evt.clientY;
		addEventSimple(document,'mousemove',dragDrop.dragMouse);
		addEventSimple(document,'mouseup',dragDrop.releaseElement);
		return false;
	},
	startDrag: function (obj) {
		if (dragDrop.draggedObject)
			dragDrop.releaseElement();
		dragDrop.startX = obj.offsetLeft;
		dragDrop.startY = obj.offsetTop;
		dragDrop.startMsgX = obj.offsetLeft;
		dragDrop.startMsgY = obj.offsetTop+4;
		dragDrop.draggedObject = obj;
		$(obj).addClass("dragged");
		$(obj).addClass(dragDrop.addionnalStyle);
	},
	dragMouse: function (e) {
		var evt = e || window.event;
		var dX = evt.clientX - dragDrop.initialMouseX;
		var dY = evt.clientY - dragDrop.initialMouseY;
		dragDrop.setPosition(dX,dY);
		return false;
	},
	setPosition: function (dx,dy) {
		dragDrop.draggedMsg.style.left = dragDrop.startMsgX + dx + 'px';
		dragDrop.draggedMsg.style.top = dragDrop.startMsgY + dy + 'px';
		dragDrop.draggedObject.style.left = dragDrop.startX + dx + 'px';
		dragDrop.draggedObject.style.top = dragDrop.startY + dy + 'px';
	},
	releaseElement: function() {
		if(confirm(dragDrop.confirmMsg)) {
			dragDrop.updateFunction(dragDrop.draggedObject.offsetLeft, dragDrop.draggedObject.offsetTop);
		} else {
			dragDrop.setPosition(0,0)
		}
		removeEventSimple(document,'mousemove',dragDrop.dragMouse);
		removeEventSimple(document,'mouseup',dragDrop.releaseElement);
		dragDrop.draggedObject.className = dragDrop.draggedObject.className.replace(/dragged/,'');
		jQuery(dragDrop.draggedObject).removeClass(dragDrop.addionnalStyle);
		dragDrop.draggedObject = null;
	}
}