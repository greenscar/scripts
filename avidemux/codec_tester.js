//AD  <- Needed to identify//
//--automatically built--
//--Project: /home/c8h10n4o2/Desktop/video_to_crop/test2.huffy.lossless.js

var app = new Avidemux();

//** Video **
// 01 videos source 
app.load("/home/c8h10n4o2/Desktop/video_to_crop/2000-03-15_Precious_Meets_Prissy_2000-03-22_-_Operation_Get_Down_2000-07-01.mpg.idx");
//01 segments
app.clearSegments();
app.addSegment(0,25199,1001);
app.markerA=275;
app.markerB=300;
app.rebuildIndex();

//** Postproc **
app.video.setPostProc(3,3,0);

app.video.setFps1000(29970);

//** Filters **

//** Video Codec conf **
/*
* HUFFY IS TOO LARGE AND QUALITY NOT GREAT

app.video.codec("HUFF","CQ=4","140 05 00 00 00 00 01 00 00 02 00 00 00 1f 00 00 00 03 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 fe ff ff ff 01 00 00 00 fb ff ff ff cd cc 4c 3d 01 00 00 00 0a d7 23 3c 01 00 00 00 00 00 00 3f 00 00 00 3f 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 00 00 00 40 1f 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");

//** Audio **
app.audio.reset();
app.audio.codec("copy",128,0,"");
app.audio.normalizeMode=0;
app.audio.normalizeValue=0;
app.audio.delay=0;
app.audio.mixer("NONE");
app.setContainer("AVI");
app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/test1_huffy.avi");
*/


/*
* MPG SIZE => 36.8 MB
* 2PASS was fuzzy.
* CQ=4 looked best
* CBR=1500000 looked about same as CQ=4 but was smaller in file size
*/

//** Video Codec conf **
//** Video Codec conf **
app.video.codec("DVD","CQ=1","24 88 2a 11 00 0c 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
app.setContainer("PS");
app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/test1_dvdcq1.mpg");
setSuccess(1);
app.exit();

app.video.codec("DVD","CQ=4","24 88 2a 11 00 0c 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
app.setContainer("PS");
app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/test1_dvdcq4.mpg");

app.video.codec("DVD","CQ=6","24 88 2a 11 00 0c 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
app.setContainer("PS");
app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/test1_dvdcq6.mpg");

app.video.codec("DVD","CQ=10","24 88 2a 11 00 0c 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
app.setContainer("PS");
app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/test1_dvdcq10.mpg");

app.video.codec("DVD","CBR=1500000","24 88 2a 11 00 0c 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
app.setContainer("PS");
app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/test1_dvdcbr_1500000.mpg");

app.video.codec("DVD","CBR=2000000","24 88 2a 11 00 0c 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
app.setContainer("PS");
app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/test1_dvdcbr_2000000.mpg");


/*
* x264 SIZE => 5.7 MB
* SMALL BUT CAN SEE BLOCKS LIKE DIVX
*/
app.video.codec("X264","2PASSBITRATE=1000","156 00 00 00 00 00 00 00 00 00 00 00 00 28 00 00 00 1e 00 00 00 3c 00 00 00 0a 00 00 00 33 00 00 00 04 00 00 00 01 00 00 00 28 00 00 00 19 00 00 00 fa 00 00 00 01 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00 01 00 00 00 01 00 00 00 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00 04 00 00 00 10 00 00 00 01 00 00 00 01 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 ");
app.setContainer("AVI");
app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/test1_x264.avi");


app.exit();

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


















setSuccess(1);
//app.Exit();

//End of script
