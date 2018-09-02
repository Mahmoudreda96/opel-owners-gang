		 		<?php
	header("Access-control-Allow-Origin:*");
      header('Access-control-Allow-Headers: X-Requested-With');
      header('Content-Type: application/json');


     
    define('HOST','localhost');
	    define('USER','scopeims_mohamed');
	    define('PASS','GnG@123123');
	    define('DB','scopeims_Notify');
        $con = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');



	$ID ='';  
	$msg = $_GET['msg'];;
	/*$msg = "mahmoudreda";*/
		 $sql = "INSERT INTO  notification (ID,msg) VALUES(NULL,'$msg')";
		 		if(mysqli_query($con,$sql)){
		 			echo 'post successful';

		 	}
		 		else{

		 			echo 'oops! Please try again!';

		 		}
		 		?>