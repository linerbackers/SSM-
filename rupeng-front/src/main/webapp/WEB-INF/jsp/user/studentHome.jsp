<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

    <title>学习中心</title>
</head>
<body>
   <%@include file="/WEB-INF/jsp/nav.jsp" %>
<div id="mainDiv" class="container" style="margin-top:35px;">
        <style>
            .panel-body a{
                cursor: pointer;
                display:block;
                margin-bottom4px;
            }
        </style>
        <div class="row">
            <div class="col-xs-6">
                <!-- 我的课程 -->
                <div class="panel panel-info">
                    <div class="panel-heading"> 
                        <h3 class="panel-title">我的课程</h3> 
                    </div> 
                    <div class="panel-body">
                            <a onclick="ajaxSubmit('<%=ctxPath%>/card/applyNext')" target="_blank" style="color:red;margin-bottom: 20px;">申请新的学习卡 &gt;&gt;</a>
                            <a href="<%=ctxPath%>/card/latest.do" target="_blank" style="color:red;margin-bottom: 20px;">上次学到这里 &gt;&gt;</a>
							<c:forEach items="${cardList }" var="card">
	                            <a href="<%=ctxPath%>/card/detail.do?cardId=${card.id}&userId=${user.id}" target="_blank" style="margin-bottom: 6px;">${card.name }学习卡</a>
							</c:forEach>
                    </div> 
                </div>
            </div>
             
            <div class="col-xs-6">
                <!-- 问题和提问 -->
                <div class="panel panel-danger">
                    <div class="panel-heading"> 
                        <h3 class="panel-title">问题和提问</h3> 
                    </div> 
                    <div class="panel-body">
                        <div> 
                            <a href="<%=ctxPath%>/question/ask" target="_blank" style="color:red;margin-bottom: 20px;">我要提问 &gt;&gt;</a>
                            <a href="<%=ctxPath%>/question/list" target="_blank"  style="color:red;margin-bottom: 20px;">所有问题列表 &gt;&gt;</a>
                        </div>
                        <div id="questionDiv" style="margin-top: 20px;">
                        <c:forEach items="${questionList }" var="question">
                            <a id="question${question.id }" onclick="$(this).remove()" href="<%=ctxPath%>/question/detail.do?id=${question.id}" target="_blank">您提问或者参与的此问题还没有解决</a>
                          	</c:forEach>
                            <a id="question2" class="blink" href="<%=ctxPath%>/question/detail.do" target="_blank" onclick="$(this).remove()">您提问的问题有新回复</a>
                            <a id="question3" class="blink" href="<%=ctxPath%>/question/detail.do" target="_blank" onclick="$(this).remove()">您提问的问题有新回复</a>
                        </div>
                    </div> 
                </div>                
            </div>
        </div>
    </div>
    <script type="text/javascript">
        $(function(){
            //通知消息闪烁
            var i=0;
            setInterval(function(){
                $(".blink").css("color",(i%2==0)?'red':'blue');
                i++;
            },500);
        });
    </script>
    
            <%@include file="/WEB-INF/jsp/footer.jsp" %>

</body>
</html>