<script type="text/javascript">
    jQuery(document).ready(function() {
        // Select columns to sort
        jQuery("table[class*='classeurFiles']").tablesorter({
            sortList: [[2,0]],
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
        jQuery("#form-upload-file-${folder.document.id}").ajaxForm(function() {
            reloadAfterAddingFile("${folder.document.id}");
        });
        </#list>
    });
    
    function reloadAfterAddingFile(folderId) {
        //jQuery("#addfile_"+folderId+"_modal").dialog2('close');
        jQuery('#waitingPopup').dialog2('open');
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

</script>
