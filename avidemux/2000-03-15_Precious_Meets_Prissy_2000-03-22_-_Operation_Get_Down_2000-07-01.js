//AD  <- Needed to identify//

var app = new Avidemux();
var clips = new Array();
clips[0] = new Array(10,7477,"2000-03-15_-_Precious_meets_Prissy.mpg");
clips[1] = new Array(7486,10888,"2000-03-22_-_Precious_and_Aubri.mpg");
clips[2] = new Array(10890,13050,"2000-03-23_-_Precious_protects_Aubri_on_couch.mpg");
clips[3] = new Array(13127,14284,"2000-03-25_-_James_Dancing.mpg");
clips[4] = new Array(14285,24078,"2000-03-26_Stoned_Mango_Dream.mpg");
clips[5] = new Array(24165,25848,"2000-03-29_James_Spill.mpg");
clips[6] = new Array(25860,36450,"2000-04-01a_Chicago_Shed_Aquarium.mpg");
clips[7] = new Array(36615,45110,"2000-04-01b_Sears_Tower.mpg");
clips[8] = new Array(45120,46047,"2000-04-07_-_Aubri_upset_for_no_coffee.mpg");
clips[9] = new Array(46048,47397,"2000-04-08a_-_Aubri_making_coffee_to_go_to_new_Bloomington_Appt.mpg");
clips[10] = new Array(47398,52946,"2000-04-08b_-_Moving_in_new_Bloomington_Apartment_-_Aubri_gets_flowers.mpg");
clips[11] = new Array(52950,56236,"2000-04-08c_-_Aubri_unpacking_groceries_in_new_appartment.mpg");
clips[12] = new Array(56237,73140,"2000-04-16_-_Basement_Party.mpg");
clips[13] = new Array(73200,76002,"2000-04-21_-_Aubri_feeds_Precious_snacks.mpg");
clips[14] = new Array(76005,77130,"2000-04-23_-_Aubri_in_chair_picking_butt.mpg");
clips[15] = new Array(77220,79579,"2000-05-06a_-_Aubri_Dancing.mpg");
clips[16] = new Array(79590,81133,"2000-05-06b_-_About_to_take_Nella_Home.mpg");
clips[17] = new Array(81135,82831,"2000-05-07_-_Aubri_sitting_in_chair_eating_Crunch_n_Munch.mpg");
clips[18] = new Array(82845,87570,"2000-05-12_-_TVP_Sloppy_Joes_&_Precious_eating_&_Tour_our_appartment.mpg");
clips[19] = new Array(87630,88082,"2000-05-12_-_Aubri_fixes_hair.mpg");
clips[20] = new Array(88875,89990,"2000-05-13a_-_Aubri_made_Ruebans.mpg");
clips[21] = new Array(90120,93111,"2000-05-13b_-_Party.mpg");
clips[22] = new Array(93120,115755,"2000-05-14_-_Party.mpg");
clips[23] = new Array(115770,124635,"2000-05-15a_-_Aubri_&_Precious_in_bed_the_next_day.mpg");
clips[24] = new Array(124650,125280,"2000-05-15b_-_Aubri_in_bed_talking_to_Precious.mpg");
clips[25] = new Array(125295,129200,"2000-05-15c_-_Aubri_drying_off_Precious_from_bath.mpg");
clips[26] = new Array(127215,135479,"2000-05-16a_-_Recieve_package_from_Jason.mpg");
clips[27] = new Array(135480,137070,"2000-05-16b_-_James_shows_apartment.mpg");
clips[28] = new Array(137085,138435,"2000-05-17_-_Aubri_picks_up_Precious.mpg");
clips[29] = new Array(138450,145350,"2000-05-18_-_James_&_Precious_pick_Aubri_up_from_work.mpg");
clips[31] = new Array(145365,145911,"2000-05-21_-_Aubri_made_strawberry_shortcake.mpg");
clips[32] = new Array(145920,149730,"2000-05-27_-_Precious_eats_muffin.mpg");
clips[33] = new Array(149745,153499,"2000-05-28_-_James_skins_fish_&_talks_to_Brad.mpg");
clips[34] = new Array(153510,155990,"2000-06-01_-_Aubri_scratches_Precious_belly_on_bed.mpg");
clips[35] = new Array(156075,171915,"2000-06-03_-_Nella_&_James_shroom_&_Aubri_dances.mpg");
clips[36] = new Array(171924,173227,"2000-06-05_-_Precious_protects_Aubri.mpg");
clips[37] = new Array(173325,182250,"2000-06-11_-_James_talks_to_Jason_&_Aubri_dances.mpg");
clips[38] = new Array(182400,183585,"2000-06-21_-_Precious_in_blanket.mpg");
clips[39] = new Array(183648,187238,"2000-06-25_-_West_Bloomington_House_party.mpg");
clips[40] = new Array(187335,194050,"2000-07-01_-_Drive_to_Madison_WI.mpg");
clips[41] = new Array(194055,222345,"2000-07-02_-_Madison_Party_(Operation_Get_Down)_-_Colette_&_Frankie_Bones.mpg");

//** Video **
// 01 videos source 
app.load("/home/c8h10n4o2/Desktop/video_to_crop/2000-03-15_Precious_Meets_Prissy_2000-03-22_-_Operation_Get_Down_2000-07-01.mpg.idx");
//01 segments
app.clearSegments();
app.addSegment(0,0,222356);


//** Postproc **
app.video.setPostProc(3,3,0);

app.video.setFps1000(29970);

//** Filters **

//** Video Codec conf **
app.video.codec("DVD","CQ=4","24 88 2a 11 00 0c 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ");
app.setContainer("PS");

//** Audio **
app.audio.reset();
app.audio.codec("copy",128,0,"");
app.audio.normalizeMode=0;
app.audio.normalizeValue=0;
app.audio.delay=0;
app.audio.mixer("NONE");

for(var i=0, length=clips.length; i<length; i++)
{
	setMarkers(clips[i][0], clips[i][1]);
	app.rebuildIndex();
	app.save("/home/c8h10n4o2/Desktop/video_to_crop/done/" + clips[i][2]);
	print("--------------------" + clips[i][2] + " is done --------------------");
}

setSuccess(1);
displayInfo("Finished !");
function setMarkers(a,b)
{
	print("----------- setMarkers(" + a + ", " + b + ")");
	app.markerA=a;
	app.markerB=b;
	app.rebuildIndex();
	return 1;
}


