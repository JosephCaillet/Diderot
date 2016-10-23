/**
 * Created by joseph on 23/10/16.
 */
var folder = {
	foldedClassValue : "folded",
	foldAll(){
		var methodsNames = document.querySelectorAll(".methodContainer > h4");
		for(let i=0; i<methodsNames.length; i++)
		{
			methodsNames[i].classList.add(folder.foldedClassValue);
			methodsNames[i].nextSibling.nextSibling.classList.add(folder.foldedClassValue);
		}

		methodsNames = document.querySelectorAll(".responseContainer > h6");
		for(let i=0; i<methodsNames.length; i++)
		{
			methodsNames[i].classList.add(folder.foldedClassValue);
			methodsNames[i].nextSibling.nextSibling.classList.add(folder.foldedClassValue);
		}

		methodsNames = document.querySelectorAll(".routeDetails > div");
		for(let i=0; i<methodsNames.length; i++)
		{
			methodsNames[i].classList.add(folder.foldedClassValue);
		}
	},

	foldEvent1(event){
		var source = event.target || event.srcElement;
		if(source.classList.contains(folder.foldedClassValue))
		{
			source.classList.remove(folder.foldedClassValue);
			source.nextSibling.nextSibling.classList.remove(folder.foldedClassValue);
		}
		else
		{
			source.classList.add(folder.foldedClassValue);
			source.nextSibling.nextSibling.classList.add(folder.foldedClassValue);
		}
	},

	foldEvent2(event){
		var source = event.target || event.srcElement;
		if(source.nextSibling.nextSibling.nextSibling.nextSibling.classList.contains(folder.foldedClassValue))
		{
			source.classList.remove(folder.foldedClassValue);
			source.nextSibling.nextSibling.nextSibling.nextSibling.classList.remove(folder.foldedClassValue);
		}
		else
		{
			source.classList.add(folder.foldedClassValue);
			source.nextSibling.nextSibling.nextSibling.nextSibling.classList.add(folder.foldedClassValue);
		}
	},

	setUpFoldingOnLoad(){
		window.addEventListener("load", function(){
			folder.foldAll();

			var methodsNames = document.querySelectorAll(".methodContainer > h4");
			for(let i=0; i<methodsNames.length; i++)
			{
				methodsNames[i].addEventListener("click", folder.foldEvent1);
			}

			methodsNames = document.querySelectorAll(".responseContainer > h6");
			for(let i=0; i<methodsNames.length; i++)
			{
				methodsNames[i].addEventListener("click", folder.foldEvent1);
			}

			methodsNames = document.querySelectorAll(".routeDetails h3");
			for(let i=0; i<methodsNames.length; i++)
			{
				methodsNames[i].addEventListener("click", folder.foldEvent2);
			}
		});
	}
};