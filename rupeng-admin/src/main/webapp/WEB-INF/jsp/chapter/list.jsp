<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/header.jsp" %>

<title>篇章列表</title>
</head>
<body>
<div class="pd-20">
            <table class="table table-border table-bordered table-bg table-hover">
                <thead>
                    <tr>
                        <th>序号</th>
                        <th>篇章名称</th>
                        <th>描述</th>
                        <th>操作<button class="btn size-M radius" onclick="showLayer('添加篇章','<%=ctxPath%>/page/chapter/add.do${cardId }')"> 添加</button></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${chapterList }" var="chapter">
                     <tr>
                            <td>${chapter.id }</td>
                            <td>${chapter.name }</td>
                            <td>${chapter.description }</td>
                            <td>
                                <button class="btn size-MINI radius" onclick="ajaxDelete('<%=ctxPath %>/page/chapter/delete.do', 'id=${chapter.id }')">删除</button>
                                <button class="btn size-MINI radius" onclick="showLayer('修改篇章','<%=ctxPath %>/page/chapter/update.do?id=${chapter.id}')">修改</button>
                                <button class="btn size-MINI radius" onclick="showLayer('段落管理','<%=ctxPath %>/page/segment/list.do?id=${chapter.id }')">段落管理</button>
                            </td>
                        </tr>
                    
                    </c:forEach>
                       
            </table>
            <br>
    </div>
</body>
</html>