<script type="text/javascript">
    jQuery(document).ready(function() {
        // Select columns to sort
        jQuery("table[class*='classeurFiles']").tablesorter({
<#if canWrite >
            sortList: [[2,0]],
<#else>
            sortList: [[1,0]],
</#if>
            headers: {
                0: {
                    sorter:false
                },
<#if canWrite >
                1: {
                    sorter:false
                },
                6:        
<#else>
                5:
</#if>
                {
                    sorter:false
                }
            },
            textExtraction: function(node) {
                // extract data from markup and return it
                var sortValues = jQuery(node).find('span[class=sortValue]');
                if (sortValues.length > 0) {
                    return sortValues[0].innerHTML;
                }
                return node.innerHTML;
            }
        });
        jQuery.each(jQuery("[id^=spanFolderTitle]"), function(i, n){
            var encoded = jQuery(this).html();
            jQuery(this).html(decodeURIComponent(encoded.replace(/\+/g,  " ")));
        });
        jQuery.each(jQuery("form.form-upload-file"), function(i, n){
            var encoded = jQuery(this).attr('action');
            jQuery(this).attr('action', decodeURIComponent(encoded.replace(/\+/g,  " ")));
        });
        <#list folders as folder>
        jQuery("#form-upload-file-${folder.document.id}").ajaxForm({
            beforeSubmit: function(arr, form, options) {
                jQuery('#waitingPopup').find('div.bar').width('0%');
                jQuery('#waitingPopup').dialog2('open');
            },
            uploadProgress: function(event, position, total, percentComplete) {
                var percentVal = percentComplete + '%';
                jQuery('#waitingPopup').find('div.bar').width(percentVal);
                    jQuery('#waitingPopup').find('h3').html(percentVal + ' (' + Math.round(position/1024) + ' Ko /' + Math.round(total/1024) + ' Ko)');
            },
            success: function() {
                jQuery('#waitingPopup').find('div.bar').width('100%');
                jQuery('#waitingPopup').find('h3').html('Téléchargements en cours de finalisation ...');
                reloadAfterAddingFile("${folder.document.id}");
            }
        });
        </#list>
    });
    
    function reloadAfterAddingFile(folderId) {
    <#--
        jQuery("#addfile_" + folderId + "_modal").dialog2('close');
        jQuery('#waitingPopup').dialog2('close');
        document.location.href = "${This.path}" /* + "#" + folderId */;
        -->
        window.location.reload();
    }
    function updateBtLabels(bt, title, alt) {
        jQuery(bt).attr("title", title);
        jQuery(bt).attr("alt", alt);
    }
    function changeFolderBt(imgObj, newStatus) {
        if (newStatus === 'open') {
            jQuery(imgObj).attr("src", "${skinPath}/images/toggle_plus.png");
        } else {
            jQuery(imgObj).attr("src", "${skinPath}/images/toggle_minus.png");
        }
    }
    function slideFolder(imgObj, action) {
        var collapsables = jQuery(imgObj).closest(".Folder").find("div[class*='folder-collapsable']");
        if (action === '') {
            if (collapsables.is(":visible")) {
                action = 'open';
            } else {
                action = 'close';
            }
        }
        if (action === "open") {
            changeFolderBt(imgObj, 'open');
            updateBtLabels(imgObj, "${Context.getMessage('label.PageClasseur.open')}", "${Context.getMessage('command.PageClasseur.open')}");
            collapsables.slideUp("fast");
        } else {
            changeFolderBt(imgObj, 'close');
            updateBtLabels(imgObj, "${Context.getMessage('label.PageClasseur.collapse')}", "${Context.getMessage('command.PageClasseur.collapse')}");
            collapsables.slideDown("fast");
            refreshDisplayMode(collapsables);
        }
    }
    function slideAllFolders(imgObj) {
        if (jQuery(imgObj).hasClass('allFoldersOpened')) {
            jQuery(imgObj).removeClass('allFoldersOpened');
            jQuery(imgObj).addClass('allFoldersClosed');
            changeFolderBt(imgObj, 'open');
            updateBtLabels(imgObj, "${Context.getMessage('label.PageClasseur.allFolders.open')}", "${Context.getMessage('command.PageClasseur.allFolders.open')}");
            jQuery("img[class*='openCloseBt']").each(function(i, e) {
                slideFolder(jQuery(this), 'open');
            });
        } else {
            jQuery(imgObj).removeClass('allFoldersClosed');
            jQuery(imgObj).addClass('allFoldersOpened');
            changeFolderBt(imgObj, 'close');
            updateBtLabels(imgObj, "${Context.getMessage('label.PageClasseur.allFolders.collapse')}", "${Context.getMessage('command.PageClasseur.allFolders.collapse')}");
            jQuery("img[class*='openCloseBt']").each(function(i, e) {
                slideFolder(jQuery(this), 'close');
            });
        }
    }
    function filesSelected(event) {
        if(event != null) {
            if (multipleFileUploads) {
                var input;
                if(event.target) {
                    //firefox, safari
                    input = event.target;
                } else {
                    //IE
                    input = event.srcElement;
                }
                otherInputs = jQuery(input).closest('form').find('input[type=text],textarea').closest('div.control-group');
                if (input.files.length > 1) {
                    otherInputs.hide();
                    var totalBytes = 0;
                    for (var x = 0; x < input.files.length; x++) {
                        totalBytes += input.files[x].size;
                    }
                    var htmlStr = input.files.length + " ${Context.getMessage('label.PageClasseur.form.word.files')} (" + Math.round(totalBytes/1024) + " Ko)";
                    //htmlStr += "(${This.maxNbrUploadFiles} fichiers maximum)"
                    htmlStr += "\n";
                    htmlStr += '<table class="table table-striped bs table-bordered labstable attached-files-table" style="margin-top:3px;">';
                    htmlStr += '<thead><tr>';
                    htmlStr += '<th class="header noSort" >' + "${Context.getMessage('label.PageClasseur.tableheader.filename')}" + '</th>';
                    htmlStr += '<th class="header noSort" >' + "${Context.getMessage('label.PageClasseur.tableheader.size')}" + '</th>';
                    htmlStr += '</tr></thead>';
                    htmlStr += '<tbody>';
                    
                    for (var x = 0; x < input.files.length; x++) {
                        totalBytes += input.files[x].size;
                        htmlStr += "<tr><td>" + input.files[x].name + "</td><td>" + Math.round(input.files[x].size/1024) + " Ko</td></tr>";
                        <#--
                        if (x >= ${This.maxNbrUploadFiles}) {
                            htmlStr += " ignoré (au-delà des ${This.maxNbrUploadFiles} fichiers maximum)";
                        }
                        -->
                    }
                    htmlStr += "</tbody></table>";
                    <#--
                    var inputLen = input.files.length;
                    input.files.slice(${This.maxNbrUploadFiles}, (inputLen - ${This.maxNbrUploadFiles}));
                    -->
                    <#--
                    for (var x = (input.files.length-1); x >= ${This.maxNbrUploadFiles}; x--) {
                        input.files.removeChild(input.files[x]);
                    }
                    -->
                    var infoDiv = jQuery(input).closest('div.control-group').siblings('div.control-group').find('div.selectedFiles');
                    jQuery(infoDiv).html(htmlStr);
                    jQuery(infoDiv).closest('div.control-group').show();
                } else {
                    otherInputs.show();
                }
            }
        } else {
            alert("damn !!!");
        }
    }
</script>
