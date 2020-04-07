<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <script type="text/javascript" src="<%=ctxPath%>/lib/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" src="<%=ctxPath%>/lib/ueditor/ueditor.all.min.js"></script>
    
    <title>我要提问</title>
    <script type="text/javascript">
    
    function changeCard(cardId) {
        $.post('<%=ctxPath%>/question/cardList','cardId='+cardId,
        function(ajaxResult) {
            if (ajaxResult.status == 'success') {
              var chapters= ajaxResult.data;
              $("#chapter").html('');
              for(var i=0,j=chapters.length;i<j;i++){
            	  var chapter=chapters[i];
            	  $("#chapter").append("<option value='"+chapter.id+"'>"+chapter.seqNum+' '+chapter.name+"</option>");
              }
              
              $("#segment").html('');
  			  changeChapter($("#chapter").val());
            }
        }, 'json');
    }
    
    function changeChapter(chapterId){
    	$.post('<%=ctxPath%>/question/segmentList','chapterId='+chapterId,function(ajaxResult){
    		if(ajaxResult.status=='success'){
    			var segments=ajaxResult.data;
    			$("#segment").html('');
    			for(var i=0,j=segments.length;i<j;i++ ){
    				var segment=segments[i];
    				$("#segment").append("<option value='"+segment.id+"'>"+segment.seqNum+segment.name+"</optino>");
    			}
    		}
    	},'json')
    }
    </script>
</head>
<body>
 <%@include file="/WEB-INF/jsp/nav.jsp" %>
    <div id="mainDiv"  class="container" style="margin-top: 35px;">
        <div class="panel panel-info">
            <div class="panel-heading"> 
                <h3 class="panel-title">我要提问</h3> 
            </div> 
            <div class="panel-body">
                <form onsubmit="ajaxSubmitForm(this)" action="<%=ctxPath %>/question/formsubmit">
                    <div class="row">
                        <div class="col-sm-4">
                            <select class="form-control"  onchange="changeCard(this.value)">
                            <c:forEach items="${cardList }" var="card">
                                <option value="${card.id }"
                                <c:if test="${card.id==latestCardId }">
                                	selected="selected"
                                </c:if>
                                >${card.name}学习卡</option>
                            </c:forEach>
                            </select>
                        </div>
                        <div class="col-sm-4">
                            <select class="form-control" id="chapter"  onchange="changeChapter(this.value)">
                            <c:forEach items="${chapterList }" var="chapter">
                                    <option value="${chapter.id }"
                                    <c:if test="${chapter.id==latestChapterId }">
                                		selected="selected"
                                	</c:if>
                                    >${chapter.seqNum} ${chapter.name }</option>
                          	</c:forEach>
                            </select>
                        </div>
                        <div class="col-sm-4">
                            <select class="form-control" id="segment" name="segmentId">
                            <c:forEach items="${segmentList }" var="segment">
                                    <option value="${segment.id }"
                                    <c:if test="${segment.id==latestSegmentId }">
                                		selected="selected"
                                	</c:if>
                                    >${segment.seqNum } ${segment.name }</option>
                            </c:forEach>
                            </select>
                        </div>
                    </div>
                    
                    <br/>
                    <span class="label label-danger">全部！！报错信息：</span>
                    <script id="errorInfo" name="errorInfo"  type="text/plain"></script>
                    <script type="text/javascript">
                        <!-- 实例化编辑器 -->
                        var ue1 = UE.getEditor('errorInfo',{
                            "elementPathEnabled" : false,
                            "wordCount":false,
                            "initialFrameHeight":200,
                            "toolbars":[ ['simpleupload','emotion','attachment'] ]
                        });
                    </script>                        
                    
                    <br/>
                    <span class="label label-danger">相关代码：</span>
                    <script id="errorCode" name="errorCode"  type="text/plain"></script>
                    <script type="text/javascript">
                        <!-- 实例化编辑器 -->
                        var ue2 = UE.getEditor('errorCode',{
                            "elementPathEnabled" : false,
                            "wordCount":false,
                            "initialFrameHeight":200,
                            "toolbars":[ ['simpleupload','emotion','attachment'] ]
                        });
                    </script>                        
                    
                    <br/>
                    <span class="label label-danger">问题描述：</span>
                    <script id="description" name="description"  type="text/plain"></script>
                    <script type="text/javascript">
                        <!-- 实例化编辑器 -->
                        var ue3 = UE.getEditor('description',{
                            "elementPathEnabled" : false,
                            "wordCount":false,
                            "initialFrameHeight":200,
                            "toolbars":[ ['simpleupload','emotion','attachment'] ]
                        });
                    </script>
                    <br/>
                    <input type="submit"  class="btn btn-default" value="提交问题" />
                </form>
            </div> 
        </div>
    </div>
    
           <%@include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>