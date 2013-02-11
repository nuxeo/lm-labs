<ul class="storyboard thumbnails" >
<#list This.activeAdapter.getStoryboardItems(Document) as pic >
	<li>
	<div class="thumbnail" ><img src="/nuxeo/${pic.url}" alt="Image Storyboard ${pic_index}" ></img></div>
	</li>
</#list>
</ul>