var ckeditorconfig = {
extraPlugins : 'tableresize',
filebrowserBrowseUrl : '${This.path}/@assets',
filebrowserImageBrowseUrl : '${This.path}/@assets',
filebrowserFlashBrowseUrl : '${This.path}/@assets',
filebrowserUploadUrl : '${This.path}/@assets',
filebrowserImageUploadUrl : '${This.path}/@assets',
filebrowserFlashUploadUrl : '${This.path}/@assets',
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