function addFields(){
	var number = document.getElementById("noFields").value;
	var container = document.getElementById("fields-container");
	var arrayType = ["Text Area", "Date Picker", "Radio Button", "Check Button"];

	while (container.hasChildNodes()) {
		container.removeChild(container.lastChild);
	}
	for (i=0; i<number; i++){
		var nameDiv = document.createElement("div");
		nameDiv.className = "col-xs-12 col-sm-6";
		nameDiv.appendChild(document.createTextNode("Field Name " + (i+1)));
		var input = document.createElement("input");
		input.type = "text";
		input.name = "field_name" + i;
		input.className += "form-control";
		nameDiv.appendChild(input);

		var typeDiv = document.createElement("div");
		typeDiv.className = "col-xs-12 col-sm-6";
		typeDiv.appendChild(document.createTextNode("Field Type " + (i+1)));
		var select = document.createElement("select");
		select.name = "field_type" + i;
		select.className = "form-control";
		typeDiv.appendChild(select);

		for (var t = 0; t < arrayType.length; t++) {
			var option = document.createElement("option");
			option.setAttribute("value", arrayType[t]);
			option.text = arrayType[t];
			select.appendChild(option);
		}



		container.appendChild(nameDiv);
		container.appendChild(typeDiv);


                //Append a line break
                container.appendChild(document.createElement("br"));
                container.appendChild(document.createElement("br"));
                container.appendChild(document.createElement("hr"));
           }
        }

        function old_addField(counter){   
        	var previousContent = $("#form_to_edit").first().html();  
        	var newContent = "<div class='form_to_edit'>" +
        	"<div class='col-xs-6'>"+
        	"<label for='name' class='control-label'>Field Name:</label>"+
        	"<input name='field_name' type='text' class='form-control'>"+
        	"</div>"+
        	"<div class='col-xs-6'>"+
        	"<label for='field_type' class='control-label'>Field Type:</label>"+
        	"<select name='field_type' class='form-control'>"+
        	"<option>Text Field</option>"+
        	"<option>Date Picker</option>"+
        	"<option>Radio Button</option>"+
        	"<option>Check Button</option>"+
        	"</select>"+
        	"</div>"+
        	"</div>";
        	$previousContent.append(newContent);
        	counter ++;

        }