<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login</title>

<style>
form {
	border: 3px solid #f1f1f1;
}

input[type=text],input[type=password] {
	width: 50%;
	padding: 12px 20px;
	margin: 8px 0;
	display: inline-block;
	border: 1px solid #ccc;
	box-sizing: border-box;
}

button {
	background-color: #ffb600;
	color: white;
	padding: 14px 20px;
	margin: 8px 0;
	border: none;
	cursor: pointer;
	width: 40%;
}

button:hover {
	opacity: 0.8;
}

.cancelbtn {
	width: auto;
	padding: 10px 18px;
	background-color: #f44336;
}

.errorValidate{
 text-align: left;
 font-size: large;
 color: red;
    padding: 16px;
}

.imgcontainer {
	text-align: center;
}

img.avatar {
	width: 20%;
	border-radius: 50%;
}

.container {
	text-align: center;
	padding: 16px;
}

span.psw {
	float: right;
	padding-top: 16px;
}

/* Change styles for span and cancel button on extra small screens */
@media screen and (max-width: 150px) {
	span.psw {
		display: block;
		float: none;
	}
	.cancelbtn {
		width: 100%;
	}
}
</style>
</head>
<body>

	<form action="Validate" method="post">
		<div class="imgcontainer">
			<img src="pwc.png" alt="Avatar" class="avatar">
		</div>
<div class="errorValidate">
 <b> <p>Please Check User id or Password </p></b>
</div>
		<div class="container">
			<label><b>Username</b></label> <input type="text"
				placeholder="Enter Username" name="uname" required> <br>
			<label><b>Password</b></label> <input type="password"
				placeholder="Enter Password" name="psw" required> <br>
			<button type="submit">Login</button>

		</div>
</form>
<form action="NewUser" method="post">
		<div class="container" style="background-color: #f1f1f1">
			<br>
			<br>
			<br>
		</div>
	</form>

</body>
</html>