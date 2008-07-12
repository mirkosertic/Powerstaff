var mogwaiComboboxSearchString=''; 
var mogwaiComboboxSearchTimer=-1; 

function mogwaiComboboxAutoComplete() 
{ 
	var i; 
	var j; 
	var eltOpt; 
	var elt=event.srcElement; 
	
	if (mogwaiComboboxSearchTimer!=-1) 
	  clearTimeout(mogwaiComboboxSearchTimer); 

	switch (event.keyCode)   { 
	  case 8: mogwaiComboboxSearchString=mogwaiComboboxSearchString.substr(0, mogwaiComboboxSearchString.length-1); break;
	  case 13: break;
	  default: mogwaiComboboxSearchString+=unescape("%"+event.keyCode.toString(16)); 
	} 
	
	j=elt.options.length; 
	for (i=0; i<j; i++) { 
	  	eltOpt=elt.options(i); 
		if (eltOpt.text.toUpperCase().substr(0, 
		mogwaiComboboxSearchString.length)==mogwaiComboboxSearchString.toUpperCase()) { 
    		eltOpt.selected=true; 
		    break; 
	    } 
  	} 
	mogwaiComboboxSearchTimer=setTimeout('clearMogwaiComboboxSearchTimer();', 1000); 
	event.returnValue=false; 
} 

function clearMogwaiComboboxSearchTimer() { 

	mogwaiComboboxSearchTimer=-1; 
	mogwaiComboboxSearchString=''; 
}

var registeredKeyListeners=new Array();

function fireEvent (eventType, elementID)
{
    var o = document.getElementById(elementID);
    if (document.createEvent)
    {
        var evt = document.createEvent("Events");
        evt.initEvent(eventType, true, true);
        o.dispatchEvent(evt);
    } else if (document.createEventObject) {
        var evt = document.createEventObject();
        o.fireEvent('on' + eventType, evt);
    }
}

function mogwaiFormKeyListener(aEvt) {
	var keycode; 
	
	aEvt = (aEvt) ? aEvt : ((event) ? event : null);
	if (aEvt) {
	
		var keycode = aEvt.keyCode;
		if (aEvt.charCode) {
			keycode = aEvt.charCode;
		}
	
		var theEntry = registeredKeyListeners[keycode];
		if (theEntry) {
		
			var theElementID = theEntry[0];
			
			var theElement = document.getElementById(theElementID);
			if (theElement) {	
				fireEvent("click",theElementID);		
			} else {
				alert("Element "+theElementID+" nicht gefunden!");
			}
		
			return false;
		}	
	
	} 
	
	return true;
} 

function registerHotKey(aFormID,aHotKey,aClientID) {

	var theEntry = new Array();
	theEntry[0] = aClientID;
	theEntry[1] = aFormID;
	
	registeredKeyListeners[aHotKey] = theEntry;
}

//
// Validierung der Länge eines Textareas
//
function validateTextAreaLength(aTextArea,aLength,aEvt) {
	
	var keycode; 
	
	aEvt = (aEvt) ? aEvt : ((event) ? event : null);
	if (aEvt) {
	
		var theSize = aTextArea.value.length;		
		
		var keycode = aEvt.keyCode;
		if (aEvt.charCode) {
			keycode = aEvt.charCode;
		}
		
		if ((keycode == 8) || (keycode == 35) || (keycode == 36) || (keycode == 9) || (keycode == 37) || (keycode == 38) || (keycode == 39) || (keycode == 40) || (keycode == 46)) {
			return true;
		} else {
		
			var theSelection = null;

			if (window.getSelection) {
				theSelection = window.getSelection();
			} else if (document.getSelection) {
				theSelection = document.getSelection();
			} else if (document.selection) {
				theSelection = document.selection.createRange().text;
			}
			
			if (typeof aTextArea.selectionStart != "undefined") {
			
				var theStart = aTextArea.selectionStart;
				var theEnd = aTextArea.selectionEnd;
				
				if (theEnd > theStart) {
					return true;
				}
			
				return theSize < aLength;
			
			} else {
				if ("" == theSelection) {
					return theSize < aLength;			
				}
			}
		}
	}
	
	return true;
}

//
// Zurechtscheinden des Inhaltes eines Textareas
//
function stripTextAreaLength(aTextArea,aLength,aEvt) {
	
	var theValue = aTextArea.value;
	if (theValue.length > aLength) {
		aTextArea.value = theValue.substr(0, aLength);
	}
}

//
// Kopieren der Scroll - Position in die versteckten HTML - Felder
//
function saveScrollareaPositions(aFormID) {

	var theElements = document.getElementsByTagName("div");
	var theCounter;
								
	for (theCounter = 0; theCounter < theElements.length;theCounter++) {
		var theElement = theElements[theCounter];
		if (theElement.className == "mogwaiScrollPreserve") {
		
			var theID = theElement.id;
			
			document.forms[aFormID].elements[theID+'_scrollLeft'].value = theElement.scrollLeft;
			document.forms[aFormID].elements[theID+'_scrollTop'].value = theElement.scrollTop;
		}
	}
}

function restoreScrollareaPositions() {

	var theElements = document.getElementsByTagName("div");
	var theCounter;
								
	for (theCounter = 0; theCounter < theElements.length;theCounter++) {
		var theElement = theElements[theCounter];
		if (theElement.className == "mogwaiScrollPreserve") {
		
			var theID = theElement.id;
			
			var theLeftFormElement = document.getElementById(theID+'_scrollLeft');
			theElement.scrollLeft = theLeftFormElement.value;

			var theTopFormElement = document.getElementById(theID+'_scrollTop');
			theElement.scrollTop = theTopFormElement.value;
			
			//alert(theLeftFormElement.value+" "+theTopFormElement.value);
		}
	}

}

//
// Form - Submit mit MyFaces
//
function submitFormMyFaces(aFormID,aComponentID) {

	saveScrollareaPositions(aFormID);
	return oamSubmitForm(aFormID,aComponentID);
}

function prepareSubmitFormMyFaces(aFormID,aComponentID) {

	saveScrollareaPositions(aFormID);
	oamSetHiddenInput(aFormID,aFormID +':'+'_idcl',aComponentID);
	
	var a = 0;
	a = Math.random();
	a *= 100000;
	a = Math.ceil(a);
	
	oamSetHiddenInput(aFormID,aFormID +'::BedagGUID',a);
}

function postPrepareSubmitFormMyFaces(aFormID) {
}

//
// Form -Submit mit ADF
//
function submitFormADF(aFormID,aComponentID) {

	saveScrollareaPositions(aFormID);
	submitForm(aFormID,1,{source:aComponentID});
	return false;
}

//
// Common OnLoad - Funktion
//
function mogwaiOnLoad() {
	restoreScrollareaPositions();
}

function setTitle(aTitle) {
	document.title = aTitle;
}