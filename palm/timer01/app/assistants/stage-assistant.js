function StageAssistant () {
}
/*
 * The setup method calls the timerView-assistant->setup method
 * 	by callin pushScene to "timerView"
 */
StageAssistant.prototype.setup = function () {
	this.controller.pushScene("timer-view");
};

// StageAssistant.prototype.cleanup = function () {
// };

