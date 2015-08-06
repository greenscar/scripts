<?php
//require_once("http://www.e-sharis.net/includes/php_files/Rics_Settings.inc");
require_once("Settings.inc");
class DB_Mgr
{
	var $settings;
	var $rs;
	var $conn;
	
	function DB_Mgr($db)
	{
		$this->settings = new Settings();
		$this->connect($db);
	}
	
	function connect($db)
	{
		$this->conn = odbc_connect($this->settings->db[$db], $this->settings->id[$db], $this->settings->pwd[$db]);
	}
	
	function disconnect()
	{
		odbc_close($this->conn);
	}
	
	function get_conn()
	{
		return $this->conn;
	}
	
	function query($query)
	{
		$this->rs = odbc_exec($this->conn, $query);
	}
	
	function fetch_row()
	{
		return(odbc_fetch_row($this->rs));
	}
	
	function fetch_array()
	{
		return(odbc_fetch_array($this->rs));
	}
	
	function get_field($num)
	{
		return odbc_result($this->rs, $num);
	}
	
	function get_num_rows()
	{
		return odbc_num_rows($this->rs);
	}
	
	function get_num_fields()
	{
		return odbc_num_fields($this->rs);
	}
	function get_field_name($field_num)
	{
		return odbc_field_name($this->rs, $field_num);
	}
}
?>