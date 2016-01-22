<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
<%@include file = "WEB-INF/css/style.css" %>
<%@include file = "WEB-INF/css/table_style.css" %>
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Card Pay</title>
</head>
<body>

<center>
<f:view>


</f:view>
<form action="pindispatcher" method = "post">

    Card Reader:  <input type = "text" name = "pin"> <input type = "submit" value = "Enter PIN">

</form>



</center>

</body>
</html>