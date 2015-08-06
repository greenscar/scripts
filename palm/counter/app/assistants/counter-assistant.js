function CounterAssistant() {
	/* this is the creator function for your scene assistant object. It will be passed all the 
	   additional parameters (after the scene name) that were passed to pushScene. The reference
	   to the scene controller (this.controller) has not be established yet, so any initialization
	   that needs the scene controller should be done in the setup function below. */
}

CounterAssistant.prototype.setup = function() {
	/* this function is for setup tasks that have to happen when the scene is first created */
		
	/* use Mojo.View.render to render view templates and add them to the scene, if needed. */
	
	/* setup widgets here */
	
	/* add event handlers to listen to events from widgets */
    /* set the widget up here */
		var attributes = {
				hintText: 'hint',
				textFieldName:	'name', 
				modelProperty:		'original', 
				multiline:		false,
				disabledProperty: 'disabled',
				focus: 			true, 
				modifierState: 	Mojo.Widget.capsLock,
				//autoResize: 	automatically grow or shrink the textbox horizontally,
				//autoResizeMax:	how large horizontally it can get
				//enterSubmits:	when used in conjunction with multline, if this is set, then enter will submit rather than newline
				limitResize: 	false, 
				holdToEnable:  false, 
				focusMode:		Mojo.Widget.focusSelectMode,
				changeOnKeyPress: true,
				textReplacement: false,
				maxLength: 30,
				requiresEnterKey: false
		};
		this.model = {
			'original' : 'initial value',
			disabled: false
		};

		this.controller.setupWidget('textField', attributes, this.model);
		
		//Set up our event listeners.  One for button presses and one for the textfield's propertyChange event.
		this.handleClicked = this.handleClicked.bind(this);
		this.propertyChanged = this.propertyChanged.bind(this);
		Mojo.Event.listen(this.controller.get('start_button'), Mojo.Event.tap, this.handleClicked);
		Mojo.Event.listen(this.controller.get('textField'), Mojo.Event.propertyChange, this.propertyChanged);
}
CounterAssistant.prototype.handleClicked = function(event){
	var usrInput = this.model['original']
	var isNumber = usrInput.match(/^\d+$/)
	if(isNumber)
	{
		this.controller.get('textFieldValue').style.color="#00FF00"
		this.controller.get('textFieldValue').update(usrInput)
		this.interval = window.setInterval(function () {
			var appController, bannerParams;
			this.updateValue();
		}, 1000);
		//window.setInterval("updateValue", 1000)
	}
	else{
		this.controller.get('textFieldValue').style.color="#FF0000"
		this.controller.get('textFieldValue').update("Please enter a number")
	}
	//this.controller.get('textFieldValue').update(this.model['original'])
	//this.controller.get('textFieldValue').update(usrInput.match(isNumber))
}
CounterAssistant.prototype.updateValue = function()
{
	$("textFieldValue").innerHTML = "xxx";
	//this.controller.get('textFieldValue').update("x")
}
CounterAssistant.prototype.activate = function(event) {
	/* put in event handlers here that should only be in effect when this scene is active. For
	   example, key handlers that are observing the document */
}



CounterAssistant.prototype.propertyChanged = function(event){
	/* log the text field value when the value changes */
		Mojo.Log.info("********* property Change *************");       
}
CounterAssistant.prototype.deactivate = function(event) {
	/* remove any event handlers you added in activate and do any other cleanup that should happen before
	   this scene is popped or another scene is pushed on top */
}

CounterAssistant.prototype.cleanup = function(event) {
	/* this function should do any cleanup needed before the scene is destroyed as 
	   a result of being popped off the scene stack */
	  Mojo.event.stopListening(this.controller.get('textField'), Mojo.Event.propertyChange, this.propertyChanged)
}
