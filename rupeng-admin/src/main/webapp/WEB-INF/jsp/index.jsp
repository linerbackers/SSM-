<!DOCTYPE HTML>
<html>
<head>

<%@include file="/WEB-INF/jsp/header.jsp" %>
<link href="<%=ctxPath%>/skin/default/skin.css" rel="stylesheet" type="text/css" id="skin" />
<title>rpw后台管理系统</title>
</head>
<body>
<header class="Hui-header cl"> 
    <a class="Hui-logo l" title="rpw后台管理系统">rpw后台管理系统</a> 
	<ul class="Hui-userbar">
		<li class="dropDown dropDown_hover"><a class="dropDown_A">${adminUser.account } <i class="Hui-iconfont">&#xe6d5;</i></a>
			<ul class="dropDown-menu radius box-shadow">
				<li><a onclick="showLayer('修改密码','<%=ctxPath%>/page/adminUser/updatePassword.do?id=${adminUser.id }',600,371)">修改密码</a></li>
				<li><a href="<%=ctxPath%>/page/adminUser/loginout">退出</a></li>
			</ul>
		</li>
		<li id="Hui-skin" class="dropDown right dropDown_hover"><a href="javascript:;" title="换肤"><i class="Hui-iconfont" style="font-size:18px">&#xe62a;</i></a>
			<ul class="dropDown-menu radius box-shadow">
				<li><a href="javascript:;" data-val="default" title="默认（黑色）">默认（黑色）</a></li>
				<li><a href="javascript:;" data-val="blue" title="蓝色">蓝色</a></li>
				<li><a href="javascript:;" data-val="green" title="绿色">绿色</a></li>
				<li><a href="javascript:;" data-val="red" title="红色">红色</a></li>
				<li><a href="javascript:;" data-val="yellow" title="黄色">黄色</a></li>
				<li><a href="javascript:;" data-val="orange" title="绿色">橙色</a></li>
			</ul>
		</li>
	</ul>
	<a aria-hidden="false" class="Hui-nav-toggle" href="#"></a> </header>
    <aside class="Hui-aside">
    	<input runat="server" id="divScrollValue" type="hidden" value="" />
    	<div class="menu_dropdown bk_2">
    		<dl id="menu-picture">
    			<dt><i class="Hui-iconfont">&#xe613;</i> 教学管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
    			<dd>
    				<ul>
                        <li><a _href="<%=ctxPath%>/page/subject/list.do" href="javascript:;">学科管理</a></li>
                        <li><a _href="<%=ctxPath%>/page/card/list.do" href="javascript:void(0)">学习卡管理</a></li>
                        <li><a _href="<%=ctxPath%>/page/cardSubject/order.do" href="javascript:void(0)">学习卡排序</a></li>
    					<li><a _href="<%=ctxPath%>/page/user/list.do" href="javascript:void(0)">前台用户管理</a></li>
                        <li><a _href="<%=ctxPath%>/page/classes/list.do" href="javascript:void(0)">班级管理</a></li>
    				</ul>
    			</dd>
    		</dl>
    		
    		<dl id="menu-comments">
    			<dt><i class="Hui-iconfont">&#xe62e;</i>系统管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
    			<dd>
    				<ul>
                        <li><a _href="<%=ctxPath%>/page/adminUser/list.do" href="javascript:void(0)">管理员用户管理</a></li>
                        <li><a _href="<%=ctxPath%>/page/role/list" href="javascript:void(0)">角色管理</a></li>
                        <li><a _href="<%=ctxPath%>/page/permission/list" href="javascript:void(0)">权限管理</a></li>    					
    				</ul>
    			</dd>
    		</dl>
    	</div>
    </aside>
<div class="dislpayArrow"><a class="pngfix" href="javascript:void(0);" onClick="displaynavbar(this)"></a></div>
<section class="Hui-article-box">
	<div id="Hui-tabNav" class="Hui-tabNav">
		<div class="Hui-tabNav-wp">
			<ul id="min_title_list" class="acrossTab cl">
				<li class="active"><span title="我的桌面" data-href="<%=ctxPath %>/welcome">我的桌面</span><em></em></li>
			</ul>
		</div>
		<div class="Hui-tabNav-more btn-group"><a id="js-tabNav-prev" class="btn radius btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d4;</i></a><a id="js-tabNav-next" class="btn radius btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d7;</i></a></div>
	</div>
	<div id="iframe_box" class="Hui-article">
		<div class="show_iframe">
			<div style="display:none" class="loading"></div>
			<iframe scrolling="yes" frameborder="0" src="<%=ctxPath %>/welcome"></iframe>
		</div>
	</div>
</section>

</body>
</html>