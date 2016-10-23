/**
 * Created by joseph on 23/10/16.
 */
folder = {
	foldAll : function(){
		var methodsNames = document.querySelectorAll(".methodContainer > h4");
		for(var i=0; i<methodsNames.length; i++)
		{
			methodsNames[i].classList.add('folded');
			methodsNames[i].nextSibling.nextSibling.classList.add('folded');
		}

		methodsNames = document.querySelectorAll(".responseContainer > h6");
		for(i=0; i<methodsNames.length; i++)
		{
			methodsNames[i].classList.add('folded');
			methodsNames[i].nextSibling.nextSibling.classList.add('folded');
		}

		methodsNames = document.querySelectorAll(".routeDetails > div");
		for(i=0; i<methodsNames.length; i++)
		{
			methodsNames[i].classList.add('folded');
		}
	},

	foldEvent1 : function(e){
		var source = event.target || event.srcElement;
		if(source.classList.contains('folded'))
		{
			source.classList.remove('folded');
			source.nextSibling.nextSibling.classList.remove('folded');
		}
		else
		{
			source.classList.add('folded');
			source.nextSibling.nextSibling.classList.add('folded');
		}
	},

	foldEvent2 : function(e){
		var source = event.target || event.srcElement;
		if(source.nextSibling.nextSibling.nextSibling.nextSibling.classList.contains('folded'))
		{
			source.classList.remove('folded');
			source.nextSibling.nextSibling.nextSibling.nextSibling.classList.remove('folded');
		}
		else
		{
			source.classList.add('folded');
			source.nextSibling.nextSibling.nextSibling.nextSibling.classList.add('folded');
		}
	},

	setUpFoldingOnLoad : function(){
		window.addEventListener("load", function(){
			folder.foldAll();

			var methodsNames = document.querySelectorAll(".methodContainer > h4");
			for(var i=0; i<methodsNames.length; i++)
			{
				methodsNames[i].addEventListener("click", folder.foldEvent1);
			}

			methodsNames = document.querySelectorAll(".responseContainer > h6");
			for(i=0; i<methodsNames.length; i++)
			{
				methodsNames[i].addEventListener("click", folder.foldEvent1);
			}

			methodsNames = document.querySelectorAll(".routeDetails h3");
			for(i=0; i<methodsNames.length; i++)
			{
				methodsNames[i].addEventListener("click", folder.foldEvent2);
			}
		})
	}
};