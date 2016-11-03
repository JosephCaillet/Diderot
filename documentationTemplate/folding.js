/**
 * Created by joseph on 23/10/16.
 */

var folder = {
	foldedClassValue : "folded",

	doOn(selector, action, target = document){
		let selectedNode = target.querySelectorAll(selector);
		for(let node of selectedNode)
		{
			action(node);
		}
	},

	//strongly inspired by AnhSirk Dasarp : http://stackoverflow.com/questions/5007530/how-do-i-scroll-to-an-element-using-javascript
	findPos(selector) {
		let obj = document.querySelector(selector);
		let curtop = 0;
		if (obj.offsetParent) {
			do {
				curtop += obj.offsetTop;
			} while (obj = obj.offsetParent);
			return [curtop];
		}
	},

	foldAll(bool){
		let action;
		if(bool){
			action = function(classList){
				classList.add(folder.foldedClassValue);
			};
		}
		else{
			action = function(classList){
				classList.remove(folder.foldedClassValue);
			};
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

	setUpMethodRouteDisplayInHeader() {
		let projectTitle = document.querySelector("h1");
		let projectName = projectTitle.textContent;

		folder.doOn(".routeDetails", function (node) {
			let method = null;

			node.addEventListener("mouseenter", function (event) {
				let source = event.target || event.srcElement;
				method = source.querySelector("h3").textContent;
				projectTitle.textContent = projectName + ": " + method;
			});

			node.addEventListener("mouseleave", function (event) {
				let source = event.target || event.srcElement;
				projectTitle.textContent = projectName;
			});

			folder.doOn(".methodContainer", function (node) {
				node.addEventListener("mouseenter", function (event) {
					let source = event.target || event.srcElement;
					projectTitle.textContent = projectName + ": " + source.querySelector("h4").textContent + " " + method;
				});

				node.addEventListener("mouseleave", function (event) {
					let source = event.target || event.srcElement;
					projectTitle.textContent = projectName + ": " + method;
				});
			}, node);
		});
	},

	setUpGoToRouteLink(){
		this.doOn(".routeSummaryName a", function(element)
		{
			element.addEventListener("click", function(event){
				event.preventDefault();
				let source = event.target || event.srcElement;
				let offset = parseInt(window.getComputedStyle(document.querySelector("header")).height);
				offset += 15;
				window.scrollTo(0, folder.findPos(source.getAttribute("href").replace(new RegExp("/", "g"), "\\/")) - offset);
			});
		});
	},

	setUpFoldingOnLoad(initialFoldStatus = true){
		window.addEventListener("load", function(){
			if(initialFoldStatus){
				folder.foldAll(true);
			}

			folder.setUpGoToRouteLink();

			folder.setUpMethodRouteDisplayInHeader();

			folder.doOn(".routeDetails h3", function(node){
				node.addEventListener("click", folder.foldEventRoute);
			});

			folder.doOn(".methodContainer > h4", function(node){
				node.addEventListener("click", folder.foldEventMethodResponse);
			});

			folder.doOn(".responseContainer > h6", function(node){
				node.addEventListener("click", folder.foldEventMethodResponse);
			});

			document.querySelector(".expand span:nth-child(1)").addEventListener("click", function(){
				folder.foldAll(false);
			});

			document.querySelector(".expand span:nth-child(2)").addEventListener("click", function(){
				folder.foldAll(true);
			});
		});
	}
};