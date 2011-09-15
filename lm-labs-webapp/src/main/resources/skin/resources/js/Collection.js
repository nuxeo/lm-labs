function Collection() {
	var collection = {};
	var order = [];
	var index = -1;
	var selected = null;
	
	this.setSelected = function(name){
		this.selected = name;
	}
	
	this.getSelectedItem = function(){
		if(this.selected != null){
			return this.collection[this.selected];
		}
		return null;
	}
	
	this.getSelectedName = function(){
		return this.selected
	}
	
	this.getItem = function(name){
		if(this.exists(name)){
			return this.collection[name];
		}
		return null;
	}
	
	this.reset = function(){
		this.index = -1;
	}
	
	this.hasNext = function(){
		return this.index < this.order.length && this.hasNotNulValueAfter();
	}
	
	this.hasNotNulValueAfter = function(){
		var ii = this.index + 1;
		while (ii < this.order.length) {
			if (this.order[ii] != null) {
				this.index = ii;
				return true;
			}
			ii++;
		}
		return false;
	}
	
	this.nextItem = function(){
		return this.collection[this.order[this.index]];
	}
	
	this.nextKey = function(){
		return this.order[this.index];
	}

	this.setCollection = function(pCollection, pOrder){
		this.collection = pCollection;
		this.order = pOrder;
	}
	
	this.getCollection = function(){
		return this.collection;
	}
	
	this.getOrder = function(){
		return this.order;
	}
	
	this.add = function(property, value) {
		if (!this.exists(property)) {
			this.collection[property] = value;
			this.order.push(property);
		}
	}
	
	this.remove = function(property) {
		this.collection[property] = null;
		var ii = this.order.length;
		while (ii-- > 0) {
			if (this.order[ii] == property) {
				this.order[ii] = null;
				break;
			}
		}
	}
	
	this.toString = function() {
		var output = [];
		for ( var ii = 0; ii < this.order.length; ++ii) {
			if (this.order[ii] != null) {
				output.push(this.collection[this.order[ii]]);
			}
		}
		return output;
	}
	
	this.getKeys = function() {
		var keys = [];
		for ( var ii = 0; ii < this.order.length; ++ii) {
			if (this.order[ii] != null) {
				keys.push(this.order[ii]);
			}
		}
		return keys;
	}
	
	this.update = function(property, value) {
		if (value != null) {
			this.collection[property] = value;
		}
		var ii = this.order.length;
		while (ii-- > 0) {
			if (this.order[ii] == property) {
				this.order[ii] = null;
				this.order.push(property);
				break;
			}
		}
	}
	
	this.exists = function(property) {
		return this.collection[property] != null;
	}
	
	this.size = function() {
		var nb = 0;
		for ( var ii = 0; ii < this.order.length; ++ii) {
			if (this.order[ii] != null) {
				nb ++;
			}
		}
		return nb;
	}
	
	this.totalSize = function() {
		return this.order.length;
	}
	
	this.moveUp = function(property) {
		var first = true;
		var before = 0;
		for ( var ii = 0; ii < this.order.length; ii++) {
			if (this.order[ii] != null) {
				if (first){
					first = false;
				}
				if (!first && this.order[ii] == property){
					var tmpOrder = this.order[before];
					this.order[before] = this.order[ii];
					this.order[ii] = tmpOrder;
					break;
				}
				before = ii;
			}
		}
	}
	
	this.moveDown = function(property) {
		var founded = false;
		var before = 0;
		for ( var ii = 0; ii < this.order.length; ++ii) {
			if (this.order[ii] != null) {			
				if (founded){
					var tmpOrder = this.order[before];
					this.order[before] = this.order[ii];
					this.order[ii] = tmpOrder;
					break;
				}
				if (this.order[ii] == property){
					founded = true;
				}
				before = ii;
			}
		}
	}
}