package com.rupeng.web.tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.rupeng.pojo.Question;
import com.rupeng.pojo.QuestionAnswer;
import com.rupeng.pojo.User;
import com.rupeng.util.CommonUtils;

public class QuestionAnswersTag extends SimpleTagSupport{

	//自定义标签属性
	private List<QuestionAnswer> rootAnswerList;
	
	private Question question;
	
	private User user=null;
	
	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public List<QuestionAnswer> getRootAnswerList() {
		return rootAnswerList;
	}


	public void setRootAnswerList(List<QuestionAnswer> rootAnswerList) {
		this.rootAnswerList = rootAnswerList;
	}


	public Question getQuestion() {
		return question;
	}


	public void setQuestion(Question question) {
		this.question = question;
	}


	@Override
	public void doTag() throws JspException, IOException {

		//从session中把user拿到
		user=(User) getJspContext().getAttribute("user",PageContext.SESSION_SCOPE);
		//1、生成一段html文本
			StringBuilder builder=new StringBuilder();
			builder.append("<ul class=\"media-list\">");
			process(builder,rootAnswerList);
			builder.append("</ul>");
			builder.append("</ul>");
		
		//2、把这段html 文本输出到浏览器
		getJspContext().getOut().append(builder);
		
		
	}

	/*
    <div class="media">
        <div class="pull-left">
            <span class="label label-success">蛋蛋</span>
        </div>
        <div class="media-body">
            <div>
                <span>2016-10-26 16:30:47</span>&nbsp;&nbsp;&nbsp;
                <a class="btn btn-xs btn-default" reanswer="7">补充回答、追问</a>
                <a class="btn btn-xs btn-default" adopt="7">采纳</a>
            </div>
            <div style="background-color:#F9F9F9;">
                <p>这样可以了</p>
            </div>
        </div>
     </div>
*/
	private void process(StringBuilder builder, List<QuestionAnswer> questionAnswerList) {
		if(CommonUtils.isEmpty(questionAnswerList)){
			return ;
		}
		
		for(QuestionAnswer questionAnswer:questionAnswerList){
			builder.append("<div class=\"media\">");
			builder.append("	<div class=\"pull-left\">");
			builder.append(" 		<span class=\"label label-success\">").append(questionAnswer.getUsername()).append("</span>");
			builder.append(" 	</div>");
			builder.append("  	<div class=\"media-body\">");
			builder.append("		<div>");
			builder.append("			<span>").append(dateFormat.format(questionAnswer.getCreateTime())).append("</span>&nbsp;&nbsp;&nbsp; ");
			if(question.getIsResolved()==null||question.getIsResolved()==false){//问题没有被解决
				builder.append(" 			<a class=\"btn btn-xs btn-default\" reanswer=\"").append("\">补充回答、追问</a>");
				if(user.getId().equals(question.getUserId())||(user.getIsTeacher()!=null&&user.getIsTeacher())){//如果当前用户为本人和老师，才能点击为采纳
					//拼接采纳按钮
					builder.append("<a class=\"btn btn-xs btn-default\" adopt=\"").append(questionAnswer.getId()).append("\">采纳</a>");
				}
			}else{
				PageContext pageContext=(PageContext) getJspContext();
				//项目路径 pageContext.getServletContext().getContextPath();
				builder.append(" <img src=\""+ pageContext.getServletContext().getContextPath()+"/images/correct.png\"  />");
			}
			builder.append("</div>");
			builder.append("<div style=\"background-color:#F9F9F9;\">");
			builder.append("<p>").append(questionAnswer.getContent()).append("</p>");
			builder.append("  </div>");
			//递归调用，拼接直接子答案
			process(builder, questionAnswer.getChildrenAnswerList());
			builder.append("</div>");
			builder.append("</div>");
                                   
                                        
                                   
                                
		}
	}
	
	
}
