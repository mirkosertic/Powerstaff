
function openWordFile(aFileName) {
	var myApp;
	if (document.all) {
		myApp = new ActiveXObject("Word.Application");
	} else {
		myApp = new GeckoActiveXObject("Word.Application");
	}
	
	if (myApp != null) {
		myApp.Visible = true;
		myApp.Documents.Open(aFileName);
	} else {
		alert("Fehler beim Starten von Word : " + aFileName);
	}
}

function openTextFile(aFileName) {
	var myApp;
	if (document.all) {
		myApp = new ActiveXObject("WScript.shell");
	} else {
		myApp = new GeckoActiveXObject("WScript.shell");
	}
	
	myApp.run("NOTEPAD.EXE " + aFileName,5);
}
