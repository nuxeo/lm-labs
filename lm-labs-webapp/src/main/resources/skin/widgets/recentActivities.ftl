<#assign mySite=Common.siteDoc(Document).site />
<div class="bloc recentActivities">
	<div class="header">${Context.getMessage('label.HtmlPage.widget.html.recentActivities')}</div>
		<div class="itemList">
		<img src="${skinPath}/images/loading.gif" />
		</div>
		<div class="browseLastMsg">
		</div>
</div>

<script type="text/javascript">
var recentActivitiesHelper = (function() {
	// private variables & methods
	var last_messages = new Array();
	var mois = new Array("janv.","f&eacute;v.","mars","avr.","mai","juin","juil.","ao&ucirc;t","sept.","oct.","nov.","déc.");
	var instantiated;
	
	function init() {
		return {
			// public variables & methods
			parseXml:function(xml) {
				jQuery(xml).find("item").each(function(i) {
					var item = new Array(
						jQuery(this).find("pubDate").text(),
						jQuery(this).find("title").text(),
						jQuery(this).find("description").text().replace("[[TOC]]", ""),
						jQuery(this).find("link").text()
					);
					last_messages.push(item);
				});
				jQuery(".recentActivities .browseLastMsg").pagination(last_messages.length, {
					items_per_page:2,
					num_display_entries:0,
					num_edge_entries:0,
					prev_text:'<i class="icon-backward"></i>&nbsp;&nbsp;',
					next_text:'<i class="icon-forward"></i>',
					callback:loadContents
				});
			}
		}
	}
	
	function loadContents(page_index, jq){
	    // Get number of elements per pagination page from form
	    var items_per_page = 2;
	    var max_elem = Math.min((page_index+1) * items_per_page, last_messages.length);
	    var newcontent = '';
	
	    // Iterate through a selection of the content and build an HTML string
	    for(var i=page_index*items_per_page;i<max_elem;i++) {
	    	var desc = last_messages[i][2];
	    	/*
	    	if(desc=='') {
	    		desc = '${Context.getMessage("label.last_message.no_desc")}';
	    	}
	    	*/
	    	newcontent += '<a href="'+last_messages[i][3]+'"><div class="itemList-item">';
	        newcontent += '<div class="date">' + dateAsString(last_messages[i][0]) + '</div>';
			newcontent += '<div class="title"><div id="recentActivitiesTitleEllipsisText' + i +'" class="ellipsisText" ellipsisTextOptions="{ max_rows:1, alt_text_e:true, alt_text_t:true }">' + last_messages[i][1] + '</div></div>';
			newcontent += '<div class="ellipsisText desc" id="recentActivitiesDescEllipsisText' + i +'"" ellipsisTextOptions="{ max_rows:2, alt_text_e:true, alt_text_t:true }">' + jQuery("<div>" + desc + "</div>").text() + '</div>';
			newcontent += '</div></a>';
			if (i < max_elem - 1){
				newcontent += '<hr style="margin:0;">';
			}
	    }
	    // Replace old content with new content
	    jQuery('.recentActivities .itemList').html(newcontent);
	    for(var i=page_index*items_per_page;i<max_elem;i++) {
	    	doEllipsisTextId("recentActivitiesTitleEllipsisText" + i);
	    	doEllipsisTextId("recentActivitiesDescEllipsisText" + i);
	    }
	    // Prevent click eventpropagation
	    return false;
	}
	function dateAsString(date) {
		dateObj = new Date(date);
		Date.prototype.toDateString = function () {return isNaN (this) ? 'NaN' : [this.getDate() > 9 ? this.getDate() : '0' + this.getDate(), mois[this.getMonth()], this.getFullYear()].join(' ')}
		return dateObj.toDateString();
	}
	return {
		getInstance :function() {
			if (!instantiated) {
				instantiated = init();
			}
			return instantiated; 
		}
	}
})()

jQuery(".recentActivities .itemList").ready(function() {
  jQuery.ajax({
    type: "GET",
    url: "${Context.modulePath}/${mySite.URL}/@labsrss",
    dataType: "xml",
    success: recentActivitiesHelper.getInstance().parseXml
  });
});
<#-- bad
function dateAsString(date) {
	dateObj = new Date(date);
	Date.prototype.toDateString = function () {return isNaN (this) ? 'NaN' : [this.getDate() > 9 ? this.getDate() : '0' + this.getDate(), this.getMonth() > 8 ? this.getMonth() + 1 : '0' + (this.getMonth() + 1), this.getFullYear()].join('/')}
	Date.prototype.toHourString = function () {return isNaN (this) ? 'NaN' : [this.getHours() > 9 ? this.getHours() : '0' + this.getHours(), this.getMinutes() > 9 ? this.getMinutes() : '0' + this.getMinutes(), this.getSeconds() > 9 ? this.getSeconds() : '0' + this.getSeconds()].join(':')}
	return dateObj.toDateString() + ' - ' + dateObj.toHourString();
}
-->
</script>