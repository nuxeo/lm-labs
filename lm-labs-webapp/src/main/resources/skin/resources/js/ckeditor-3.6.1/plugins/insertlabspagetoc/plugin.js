(function(){
// Section 1 : Code to execute when the toolbar button is pressed
	var a = {
			exec:function(editor){
				editor.insertText('[[TOC]]');
			}
	},
	b = "insertlabspagetoc";
// Section 2 : Create the button and add the functionality to it
	CKEDITOR.plugins.add(b,{
		lang: [ 'fr','en' ],
		init: function(editor){
			editor.addCommand(b,a);
			editor.ui.addButton("InsertLabsPageTableOfContents",{
				label: editor.lang.insertlabspagetoc.button,
				icon: this.path + "toc.gif",
				command:b
			});
		}
	});
})();
