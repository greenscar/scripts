function AppAssistant(appController) {
	this.appController = appController;
}

function StageAssistant(stageController) {
	this.stageController = stageController;
	this.stageController.pushScene("flashlight");
}

AppAssistant.prototype.handleRelaunch = function() {
	var stageMgr = Mojo.Controller.getAppController().getStageMgr();
	if (stageMgr.stageExists("flashlight")) {
		this.appController.getStageMgr().focusStage("flashlight");
	} else {
		var f = function(stageController) {
			this.stageController = stageController;
			stageController.pushScene('flashlight');
		}.bind(this);
		Mojo.Controller.getAppController().createStageWithCallback("flashlight", f);
	}
}