function payCheck() {
	
	var inputs = document.getElementsByTagName('sale');
	  

	  for (var i=0; i < inputs.length; i++) {
		if (inputs[i].getAttribute('pay') == '0.0') {
			 alert("You don't have yeat sale Enter any of Stock Item!");
		}
	  }

}

