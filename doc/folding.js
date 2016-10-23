/**
 * Created by joseph on 23/10/16.
 */
var folder = {
	foldedClassValue : "folded",

	doOn(selector, action){
		let selectedNode = document.querySelectorAll(selector);
		for(let node of selectedNode)
		{
			action(node);
		}
	},

	foldAll(bool){
		let action;
		if(bool){
			action = function(classList){
				classList.add(folder.foldedClassValue)
			}
		}
		else{
			action = function(classList){
				classList.remove(folder.foldedClassValue)
			}
		}

		this.doOn(".routeDetails > div", function(node){
			action(node.classList);
		});

		this.doOn(".methodContainer > h4", function(node) {
			action(node.classList);
			action(node.nextSibling.nextSibling.classList);
		});

		this.doOn(".responseContainer > h6", function(node){
			action(node.classList);
			action(node.nextSibling.nextSibling.classList);
		});
	},

	foldEventRoute(event){
		let source = event.target || event.srcElement;
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

	foldEventMethodResponse(event){
		let source = event.target || event.srcElement;
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

	setUpFoldingOnLoad(){
		window.addEventListener("load", function(){
			folder.foldAll(true);

			folder.doOn(".routeDetails h3", function(node){
				node.addEventListener("click", folder.foldEventRoute);
			});

			folder.doOn(".methodContainer > h4", function(node){
				node.addEventListener("click", folder.foldEventMethodResponse);
			});

			folder.doOn(".responseContainer > h6", function(node){
				node.addEventListener("click", folder.foldEventMethodResponse);
			});
		});
	}
};