$test = 'OLDBL=20090210111842_89000_12345_2008-01-01_1010_33^NEWBL=20090210200315_89000__2009-02-10_16.40.59_00.0.0.0_SNBX_89.0.0.0_DEV_15^release_num=56.0.0.2^itg_num=12345^qc_num=678^team=FW^actid=PRCT300000606^ucm_stream=SNBX_89.0.0.0_LOCAL^timestamp=2009-02-10 14:30:07^project=snbx_project^owner=jsandlin^headline=test headline';


$test = 'OLDBL=20090210200315_89000__2009-02-10_16.40.59_00.0.0.0_SNBX_89.0.0.0_DEV_15^NEWBL=20090211160312_89000__2009-02-11_16.02.04_00.0.0.0_SNBX_89.0.0.0_DEV_47^actid=PRCT300000609^ucm_stream=SNBX_89.0.0.0_LOCAL^timestamp=2009-02-11 15:58:38^project=snbx_project^owner=jsandlin^headline=This is the headline';
$test = 'OLDBL=20090211160312_89000__2009-02-11_16.02.04_00.0.0.0_SNBX_89.0.0.0_DEV_47^NEWBL=20090211161159_89000__2009-02-11_16.11.02_00.0.0.0_SNBX_89.0.0.0_DEV_48^release_num=44.4.4.4^team=fxl area^itg_num=123^qc_num=4,5,6^actid=PRCT300000612^ucm_stream=SNBX_89.0.0.0_LOCAL^timestamp=2009-02-11 16:09:33^project=snbx_project^owner=jsandlin^headline=This is the headline';
print($test . "\n");

print("--------------------------------------\n");
     
$test =~ /release_num=(.*)\^itg_num=(.*)\^qc_num=(.*)\^team=(.*)\^actid=(.*)\^ucm_stream=(.*)\^timestamp=(.*)\^project=(.*)\^owner=(.*)\^headline=(.*)$/;
print("releasenum = $1\n");
print("itgnum = $2\n");
print("cqnum = $3\n");
print("team = $4\n");
print("actid = $5\n");
print("stream = $6\n");
print("timestamp = $7\n");
print("project = $8\n");
print("owner = $9\n");
print("headline = $10\n");
print("--------------------------------------\n");
print("test = $test\n");
$test =~ /^OLDBL=(.+)\^NEWBL=((.+?)\^)/;
print("1 = " . $1 . "\n");
print("3 = $3\n");
