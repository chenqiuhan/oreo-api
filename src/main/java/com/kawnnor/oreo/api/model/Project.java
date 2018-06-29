package com.kawnnor.oreo.api.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
public class Project{
    private Integer id;
    private String name;
    private String instructions;
    //@DateTimeFormat(pattern="yyyy-mm-dd HH:MM:ss")
    private Date createDate;
    private Date modifyDate;
	public Date getModifyDate()
	{
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate)
	{
		this.modifyDate = modifyDate;
	}


    public Integer getId() {
    return id;
    }
    public void setId(Integer id) {
    this.id = id;
    }

    public String getName() {
    return name;
    }
    public void setName(String name) {
    this.name = name;
    }

    public String getInstructions() {
    return instructions;
    }
    public void setInstructions(String instructions) {
    this.instructions = instructions;
    }

    public Date getCreateDate() {
    return createDate;
    }
    public void setCreateDate(Date createDate) {
    this.createDate = createDate;
    }






}