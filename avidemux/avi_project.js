//AD  <- Needed to identify//
//--automatically built--
//--Project: /home/c8h10n4o2/Desktop/video_to_crop/2000-05-17_-_Aubri_picks_up_Precious.mp4.project

var app = new Avidemux();

//** Video **
// 01 videos source 
app.load("/home/c8h10n4o2/Desktop/video_to_crop/2000-03-15_Precious_Meets_Prissy_2000-03-22_-_Operation_Get_Down_2000-07-01.mpg.idx");
//01 segments
app.clearSegments();
app.addSegment(0,0,222356);
//app.markerA=137077;
//app.markerB=138446;
//setMarkers(138450,148356);

setMarkers(138450,138460);
app.rebuildIndex();

//** Postproc **
app.video.setPostProc(3,3,0);

app.video.setFps1000(29970);

//** Filters **

//** Video Codec conf **
app.video.codec("X264","2PASSBITRATE=1000","156 00 00 00 00 00 00 00 00 00 00 00 00 28 00 00 00 1e 00 00 00 3c 00 00 00 0a 00 00 00 33 00 00 00 04 00 00 00 01 00 00 00 28 00 00 00 19 00 00 00 fa 00 00 00 01 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00 01 00 00 00 01 00 00 00 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00 04 00 00 00 10 00 00 00 01 00 00 00 01 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 ");

//** Audio **
app.audio.reset();
app.audio.codec("copy",128,0,"");
app.audio.normalizeMode=0;
app.audio.normalizeValue=0;
app.audio.delay=0;
app.audio.mixer("NONE");
app.setContainer("AVI");
//app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/2000-05-17_-_Aubri_picks_up_Precious.avi");
app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/test1.avi");

print("========== test1.avi done ==================");
//setSuccess(1);

//app.markerA=137077;
//app.markerB=138446;
//processVideo(137077,137100, "test2.avi");
setMarkers(137077,137100);
app.rebuildIndex();
app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/test2.avi");
print("========== test2.avi done ==================");
setSuccess(1);

app.Exit();

function processVideo(startPoint, endPoint, fileName)
{
	print("processVideo(" + startPoint + ", " + endPoint + ", " + fileName + ");");
	app.markerA=startPoint;
	app.markerB=endPoint;
	app.rebuildIndex();
	app.save("/home/c8h10n4o2/Desktop/videos_to_crop/done/" + fileName);
	return(1);
}

function setMarkers(a,b)
{
	app.markerA=a;
	app.markerB=b;
	app.rebuildIndex();
	return 1;
}
//End of script
