<?php
$conn = connect();
//delete_people($conn);
insert_people($conn);
function connect()
{
	$ser="PSTEST"; #the name of the SQL Server
	$db="WEB"; #the name of the database
	$user="training"; #a valid username
	$pass="training"; #a password for the username

	#one line
	$conn=odbc_connect("DRIVER=SQL Server;SERVER=".$ser.";UID=".$user.";PWD=".$pass.";DATABASE=".$db.";Address=".$ser.",2433","","");
	#one line
	return $conn;
}
function delete_people($conn)
{
	/*
    $en = Array();
    $sel = "SELECT emp_num FROM ses_USER_INFO where emp_num = 111 OR role_num = 100";
    $del1 = "DELETE FROM ses_LOGIN_INFO WHERE emp_num NOT IN (";
    $rs = odbc_exec($conn, $sel);
    $num = odbc_num_rows($rs);
    while(odbc_fetch_row($rs))
    {
        //echo("num = $num");
        $fn = odbc_field_name($rs, 1);
        $f_re = odbc_result($rs, 1);
        $del1 .= $f_re . ", ";
    }
    $del1 = substr($del1, 0, strlen($del1) - 2);
    $del1 .= ")";
    echo($del1 . "<BR>");
    if(odbc_exec($conn, $del1))
	{
		echo("Logins Deleted<br>");
	}
	$del = "delete from ses_USER_INFO where emp_num > 111 AND role_num = 10";
	if(odbc_exec($conn, $del))
	{
		echo("People Deleted<br>");
	}
	*/
	
}
function insert_people($conn)
{
	$handle = fopen("./mgt_train_05.csv", "r");
	$row = 1;
	while (($data = fgetcsv($handle, 1000, ",")) !== FALSE) 
	{
   		$empID = $data[0];
        $name = $data[1];
        $pwd = $data[2];
        $spaceloc = strrpos($name, ' ');
        $first_name = substr($name, 0, $spaceloc);
        $last_name = substr($name, ($spaceloc + 1), strlen($name));
        if($pwd < 1000) $pwd = "0" . $pwd;
		$insert = "INSERT INTO ses_USER_INFO (name_first, name_last, emp_num, role_num, creation_date, active)"
                . " VALUES"
                . " ('$first_name', '$last_name', $empID, 10, " . time() . ", 1)";
        $insert2 = "INSERT INTO ses_LOGIN_INFO (emp_num, password)"
                . " VALUES"
                . "($empID, '$pwd')";
        echo($insert . "<BR>");
        echo($insert2 . "<BR>");
        if(!(odbc_exec($conn, $insert)))
		{
			echo("ERROR IN:<br>");
			echo($insert);
		}
        if(!(odbc_exec($conn, $insert2)))
		{
			echo("ERROR IN:<br>");
			echo($insert2);
		}
        $row++;
		//echo($insert . "<br>");
	}
	echo("Items Added.");
	fclose($handle);
}
?>