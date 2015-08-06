$str = "baseline:2008-12-17_STG_74.0.0.0_73.3.0.0_RC@/DC_Projects";
$re = "(baseline:)(.*)@\/(DC_Projects)";
$re = "(baseline:)(.*)\@\/(.*)";
$str =~ /$re/;
print("1 = $1\n");
print("2 = $2\n");
print("3 = $3\n");
