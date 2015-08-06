#!/usr/bin/perl
$tst = "14:24:17,163 - INFO com.phurnace.model.report.Log4jExporter - WebSphere:Name=BindJndiForEJBNonMessageBinding,Type=AppDeploymentTask     installed           BindJndiForEJBNonMessageBinding.EJB[0]           BackOrderStock";
$tst =~ /Log4jExporter\ -\ (.*?)\s+(.*?)\s+(.*?)\s+(.*?)$/; 
print("1 = " . $1 . "\n");
print("2 = " . $2 . "\n");
print("3 = " . $3 . "\n");
print("4 = " . $4 . "\n");
