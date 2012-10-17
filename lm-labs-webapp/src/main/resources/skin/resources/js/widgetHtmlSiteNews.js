function getEntryAhref(title, description, dateString, url, idPrefix, elemIndex, feedType) {
    var newcontent = '';
//    if(description === '') {
//        description = '${Context.getMessage("label.last_message.no_desc")}';
//    }
    newcontent += '<a href="' + url + '"><div class="itemList-item">';
    newcontent += '<div class="date">' + dateString + '</div>';
    newcontent += '<div class="title"><div id="' + idPrefix + '-' + feedType + 'TitleEllipsisText' + elemIndex +'" class="ellipsisText" ellipsisTextOptions="{ max_rows:1, alt_text_e:true, alt_text_t:true }">' + title + '</div></div>';
    newcontent += '<div class="ellipsisText desc" id="' + idPrefix + '-' + feedType + 'DescEllipsisText' + elemIndex +'"" ellipsisTextOptions="{ max_rows:2, alt_text_e:true, alt_text_t:true }">' + jQuery("<div>" + description + "</div>").text() + '</div>';
    newcontent += '</div></a>';
    
    return newcontent;
}
