package com.rupeng.pojo;

import java.util.Date;
import java.util.List;

public class QuestionAnswer {
    private Long id;

    private Long userId;

    private String username;

    private Long questionId;

    private Long parentId;

    private Date createTime;

    private Boolean isAdopted;

    private Boolean isDeleted;

    private String content;
    
    private List<QuestionAnswer> childrenAnswerList;
    

	public List<QuestionAnswer> getChildrenAnswerList() {
		return childrenAnswerList;
	}

	public void setChildrenAnswerList(List<QuestionAnswer> childrenAnswerList) {
		this.childrenAnswerList = childrenAnswerList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getIsAdopted() {
		return isAdopted;
	}

	public void setIsAdopted(Boolean isAdopted) {
		this.isAdopted = isAdopted;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
}