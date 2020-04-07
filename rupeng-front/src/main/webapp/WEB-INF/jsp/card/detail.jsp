<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <title>学习卡详情</title>
</head>
<body>
    <%@include file="/WEB-INF/jsp/nav.jsp" %>

    <div id="mainDiv" class="container" style="margin-top: 35px; ">
        <!-- 此学习卡描述信息 -->
        <div class="well">
            <span style="font-size: 28px;">${card.name }</span>
            <span class="label label-danger" style="margin-left: 4%;">还有${calculateApartDays }天过期</span>
            <span class="label label-success" style="margin-left: 4%;">${card.courseware }</span>  
        </div>
        
        <!-- 此学习卡包含的课程章节列表 -->
        <div class="panel-group" id="accordion">
        <c:forEach items="${map}" var="entry">
                <div class="panel panel-default">
                    <!-- 章节标题 -->
                    <div class="panel-heading"> 
                        <div class="panel-title"> 
                        <!-- 此处是一个超链接，点击，能够折叠课程列表 -->
                            <a style="display: block;" data-toggle="collapse" data-parent="#accordion" href="#chapter${entry.key.id }" aria-expanded="true" class=""> 
                                ${entry.key.name }
                            </a> 
                        </div> 
                    </div> 
                    <!-- 课程列表 -->
                    <!-- 控制展开折叠的 in -->
                    <div id="chapter${entry.key.id }" class="panel-collapse collapse 
                    <c:forEach items="${entry.value }" var="segment">
                    	 <c:if test="${ segment.id==userSegmentId}">in </c:if>
                    </c:forEach>													
                    " aria-expanded="true"> 
                        <div class="panel-body">
                            <div class="list-group">
                            <c:forEach items="${entry.value }" var="segment">
                                    <a class="list-group-item" href="<%=ctxPath %>/segment/detail.do?id=${segment.id}" target="_blank">
                                        <span>${segment.name }</span>
                                        <c:if test="${ segment.id==userSegmentId}">
                                        <span class="label label-danger" style="margin-left: 100px">上次学到这里 &gt;&gt;</span>
                                        </c:if>
                                    </a>
							</c:forEach>
                            </div>
                        </div> 
                    </div> 
                </div>
           	</c:forEach>
        </div>
    </div>
    
     <%@include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>