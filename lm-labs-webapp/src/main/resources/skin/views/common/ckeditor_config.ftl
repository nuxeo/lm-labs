var ckeditorconfig = {
extraPlugins : 'tableresize',
filebrowserBrowseUrl : '${Context.modulePath}/${site.URL}/@assets',
filebrowserImageBrowseUrl : '${Context.modulePath}/${site.URL}/@assets',
filebrowserFlashBrowseUrl : '${Context.modulePath}/${site.URL}/@assets',
filebrowserUploadUrl : '${Context.modulePath}/${site.URL}/@assets',
filebrowserImageUploadUrl : '${Context.modulePath}/${site.URL}/@assets',
filebrowserFlashUploadUrl : '${Context.modulePath}/${site.URL}/@assets',
toolbar:
[
['Source'],
['Cut','Copy','Paste','PasteText','PasteFromWord'],
['NumberedList','BulletedList','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','Outdent','Indent','Blockquote'],
'/',
[ 'Bold','Italic','Underline','Strike','-','SelectAll','RemoveFormat'],
[ 'Image','Flash','Table','HorizontalRule','Smiley','SpecialChar' ] ,'/',
['Format','Font','FontSize'],
['TextColor','BGColor'],
['Maximize', 'ShowBlocks','-']
]
};