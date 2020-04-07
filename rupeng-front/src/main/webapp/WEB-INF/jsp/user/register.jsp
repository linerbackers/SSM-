<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>注册</title>
</head>
<body>
   <%@include file="/WEB-INF/jsp/nav.jsp" %>

    
    <div id="mainDiv"  class="container" style="margin-top: 35px;">
        <style type="text/css">
            .row {
                padding: 10px;
                font-size: 16px;
            }
            .row div {
                padding-left: 5px;
            }
        </style>
        <form class="form-horizontal" action="<%=ctxPath %>/user/register.do" method="post">
            <div class="form-group">
                <label class="col-md-3 control-label">邮箱</label>
                <div class="col-md-6">
                    <input id="email" name="email" type="text" class="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">密码</label>
                <div class="col-md-6">
                    <input name="password" class="form-control" type="password" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">确认密码</label>
                <div class="col-md-6">
                    <input name="repassword" type="password" class="form-control" />
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">验证码 :</label>
                <div class="col-md-6">
                    <div class="col-md-5" style="padding-left:0px;">
                        <input type="text" name="emailCode" class="form-control" />
                    </div>
                    <div class="col-md-4 text-center">
                <!-- 此处按钮使用input=button,如果使用submit,会提交整个from表单 -->
                        <input type="button" class="btn btn-default" value="获取邮箱验证码" onclick="sendEmailCode('<%=ctxPath %>/emailCode.do',$('#email').val(),this)" />
                    </div>
                    <div class="col-md-3"></div>
                </div>
            </div>
            <!-- 提示信息 -->
             <div class="form-group">
                <div class="col-md-offset-3 col-md-6" style="color:red">${error }</div>
            </div>
            <div class="form-group">
                <div class="col-md-offset-3 col-md-9">
                    <input class="btn btn-success" type="submit" value="注册" />
                    <a class="btn btn-link" href="<%=ctxPath %>/user/login.do">去登录&gt;&gt;</a>
                    <span class="glyphicon glyphicon-question-sign"></span>登录、注册遇到问题？<a href="#" target="_blank">点击此处联系客服</a>
                </div>
            </div>
            <div class="form-group">
                <div class="col-md-offset-3 col-md-9">
                    注册前请阅读<a href="#" target="_blank">《服务条款》</a>，您完成注册过程即表明同意服务条款中的全部内容。
                </div>
            </div>
        </form>
    </div>
    
                   <%@include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>