<?php
define('HOST','localhost');
	    define('USER','scopeims_mohamed');
	    define('PASS','GnG@123123');
	    define('DB','scopeims_Notify');
        $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');

	      $query=mysqli_query($con,"SELECT ID,msg FROM notification ORDER BY ID DESC");
     if($query)
     {
        while($row=mysqli_fetch_array($query))
        {
            $flag[]=$row;
        }

        print(json_encode($flag));
    }
    mysqli_close($con);
?>