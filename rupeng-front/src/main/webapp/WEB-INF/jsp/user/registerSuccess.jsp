<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<!-- 刷新动作，5秒之后跳转到login页面 -->
    <meta http-equiv="Refresh" content="5,url=<%=ctxPath%>/user/login.do">
    <title>注册成功</title>
</head>
<body>
  <%@include file="/WEB-INF/jsp/nav.jsp" %>
    <div  id="mainDiv" class="container" style="margin-top: 35px;">
        <h1>注册成功，<span id="remainTime">5</span>秒后跳转到登录页面</h1>
        <a href="<%=ctxPath%>/user/login.do">去登陆&gt;&gt;</a>
        <script type="text/javascript">
            $(function(){
                var remainTime = 4;
                setInterval(function(){
                    $("#remainTime").html(remainTime);
                    remainTime--;
                }, 1000);
            });
        </script>
    </div>
    
                    <%@include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>