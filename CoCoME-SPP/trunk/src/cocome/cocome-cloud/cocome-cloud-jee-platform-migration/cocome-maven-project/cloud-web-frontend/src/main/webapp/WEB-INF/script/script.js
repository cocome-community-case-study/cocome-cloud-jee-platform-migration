function changeText(a)
 {
   var x = document.getElementById('barcodetext');
   x.value = x.value.toString() + a;
 }
 
 function enableButton() {
	 
	document.getElementById('barcodetext').disabled = false;
	document.getElementById('scan').disabled = false;
	var elems = document.getElementsByName('number');
	for(var i = 0; i < elems.length; i++) {
	    elems[i].disabled = false;
	}
    var elems1 = document.getElementsByName('action');
	for(var i = 0; i < elems1.length; i++) {
	   if (eles1[i].id.equals("paycard")) {
		   continue;
	   }
	   else {
		   elems1[i].disabled = false;
	   }
	}
	var elems2 = document.getElementsByName('sale');
	for(var i = 0; i < elems2.length; i++) {
	    elems2[i].disabled = false;
	}
	
 }
 
 function expressMode() {
	 var modes = document.getElementsByName('action');
	 
	 for (var i = 0; i < modes.length; i ++) {
		 switch (modes[i].value) {
		 
		 case "Express Mode" : 
			   document.getElementById('light').value = "green";
			   modes[i].value = "Disable Express Mode";
			   document.getElementById('paycard').disabled = false;
			   break;
		 case "Disable Express Mode" :
			   modes[i].value = "Express Mode";
			   document.getElementById('light').value = "black";
			   document.getElementById('paycard').disabled = true;
			   break;
		 }
	 }
	 
 }