<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/header.jsp" %>

<title>角色列表</title>
</head>
<body>
<div class="pd-20">
    <table class="table table-border table-bordered table-bg table-hover">
        <thead>
            <tr>
                <th>角色名称</th>
                <th>描述</th>
                <th>操作 <button class="btn size-M radius" onclick="showLayer('添加角色','<%=ctxPath %>/page/role/add.do')"> 添加</button></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${roleList }" var="role">
                <tr>
                    <td>${role.name }</td>
                    <td>${role.description }</td>
                    <td>
                        <button class="btn size-MINI radius" onclick="ajaxDelete('<%=ctxPath %>/page/role/delete','id=${ role.id}')">删除</button>
                        <button class="btn size-MINI radius" onclick="showLayer('修改角色','<%=ctxPath %>/page/role/update.do?id=${role.id }')">修改</button>
                        <button class="btn size-MINI radius" onclick="showLayer('分配权限','<%=ctxPath %>/page/rolePermission/update.do?roleId=${role.id }')">分配权限</button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>