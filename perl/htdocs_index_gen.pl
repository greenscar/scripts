#!/usr/bin/perl
$some_dir='/var/apache/htdocs';
opendir(DIR, $some_dir) || die "can't opendir $some_dir: $!";
#@dirs = grep { !/^\./ && -d} readdir(DIR);
#@dirs = grep { !/^\./ && -d} sort(readdir(DIR));
@dirs = grep { !/^\./ && !/html$/ && !/htm$/ } sort(readdir(DIR));
closedir DIR;
$output = "<html><head>
<STYLE TYPE=\"text/css\">
   table{
      border: 1px solid black;
      padding-top: 10px;
      margin:20px;
      text-align:center;
   }
   th.title
   {
      font-weight: normal; 
      font-size: 18pt;
      line-height: 14pt; 
      font-family: arial, times, helvetica; 
      font-variant: normal;
      font-style: normal;
      color: #ffffff;
      background-color: #000000;
      padding-bottom: 5px;
   }
   td
   {
      font-weight: normal; 
      font-size: 12pt;
      line-height: 14pt; 
      font-family: arial, times, helvetica; 
      font-variant: normal;
      font-style: normal;
      font-color: #000000;
      text-align: left;
      border: 0px dotted blue;
      padding: 10px 5px 10px 5px;
   }
   td.grey
   {
      background-color: #EEEEEE;
   }
   td.white
   {
      background-color: #FFFFFF;
   }
   th
   {
      text-align:center;
      padding: 5px;
      border-bottom: 1px solid black;
      background-color: #CCCCCC;
   }
   a 
   {
      color: #000000;
      text-decoration:none;
      font-weight: bold; 
      font-size: 12pt;
      line-height: 14pt; 
      font-family: arial, times, helvetica; 
      font-variant: normal;
      font-style: normal;
   }
   a:hover
   {
      color: #FF0000;
      text-decoration:none;
      font-weight: bold; 
      font-size: 12pt;
      line-height: 14pt; 
      font-family: arial, times, helvetica; 
      font-variant: normal;
      font-style: normal;
   }
</STYLE>
<title>Build Box Links</title></head><body>\n";
$output .= "<div style=\"text-align:center\">\n";
$output .= "<h1 align=center>Build Box Links</h1>\n";
$output .= "<table cellpadding=0 cellspacing=0>";
$output .= "<tr><th class=\"title\">LINK</th></tr>";
$counter = 0;
foreach $line (@dirs)
{
   $output .= "<tr>\n";
   if($counter % 2 == 0)
   {
      $output .= "<td class=\"grey\"><a href=\"./$line/\">$line</a></td>";
   }
   else
   {
      $output .= "<td class=\"white\"><a href=\"./$line/\">$line</a></td>";
   }
   $output .= "</tr>\n";
   #print($line . "\n");
   $counter++;
}
$output .= "</table>";
$output .= "</body>\n</html>";
open(DAT,">/var/apache/htdocs/index.html") || die("Cannot Open File");
print DAT "$output\n"; 
close(DAT);
