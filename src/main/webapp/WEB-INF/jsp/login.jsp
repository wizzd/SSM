<%@page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>login</title>
</head>
<body>
<form action="http://localhost:8000/logining.html" method="post">
    用户名 ： <input type="text" name="name">
    密码： <input type="password" name="password">
    <input type="submit" value="登录">
    <input type="submit" value="注册">
    <input type="reset" value="重置">
</form>
</body>
</html>
