
<?php
class Settings
{
   //var $server = "PSTEST, 2433";
   var $server = array("sites" => "DW, 1433", "ps" => "PSPROD, 2433", "rics" => "DW, 1433", "web" => "PSTEST");
   var $db = array("sites" => "SITEINFO", "ps" => "PRODSUP", "rics" => "rics_SHARIS", "web" => "WEB_DEV");
   var $id = array("sites" => "sa", "rics" => "sa", "ps" => "sa", "web" => "training");
   var $pwd = array("sites" => "sa", "rics" => "sa", "ps" => "sa", "web" => "training");
   
   //var $tables = array("site" => "SI_SITE", "phone_line" =>"SI_PHONE_LINE", "phone_company"=>"SI_PHONE_CO", "phone_type"=>"SI_PHONE_TYPE", "amenity_type"=>"SI_AMENITY_TYPE", "amenity_list"=>"SI_AMENITY_LIST");
   
   function Settings()
   {
   }
}
?>