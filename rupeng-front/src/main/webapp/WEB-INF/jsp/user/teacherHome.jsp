<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <title>教学中心</title>
</head>
<body>
     <%@include file="/WEB-INF/jsp/nav.jsp" %>

    <div class="container" id="mainDiv" style="margin-top: 35px; ">
        <style>
            .panel-body a{
                display:block;
                margin-bottom:4px;
                cursor: pointer;
            }
        </style>
        <div class="row">
            <div class="col-xs-6">
                <!-- 老师：解答问题 -->
                <div class="panel panel-danger">
                    <div class="panel-heading"> 
                        <h3 class="panel-title">解答问题</h3>
                    </div>
                    <div class="panel-body"> 
                        <a style="color: red;" href="<%=ctxPath %>/question/list?condition=allUnresolved" target="_blank">查看问题列表 &gt;&gt;</a>
                        <div id="questionDiv" style="margin-top: 20px;">
                        <c:forEach items="${questionList }" var="question">
                                <a id="question${ question.id}" onclick="$(this).remove()" href="<%=ctxPath %>/question/detail?id=${question.id}" target="_blank">此问题未回复或者还没有解决</a>
                        </c:forEach>
                        </div>
                        <div id="audio01" style="display:none;">
                        </div>
                    </div>
                </div>            
            </div>
        </div>
    </div>
    <script type="text/javascript">
    	function getNotification(){
    		//发送ajax请求到服务器或者新的通知消息
    		$.post("<%=ctxPath%>/notification","",function(ajaxResult){
    			//把新的消息通知显示在页面上
    			if(ajaxResult.status=="success"){
    				var notification=ajaxResult.data;
    				if(!notification||notification.length<1){
    					return ;
    				}
    				
    				//播放有新消息提示声音
    				$("#audio01").html('<audio src="<%=ctxPath%>/audios/notify.mp3" autoplay="autoplay"/>');
    				
    				//遍历
    				
    			}
    			
    		});
    	}
    </script>
    
                    <%@include file="/WEB-INF/jsp/footer.jsp" %>

</body>
</html>