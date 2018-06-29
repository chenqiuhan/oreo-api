package com.kawnnor.oreo.api.model;

public class Result<T>{
    private Integer code;
    private String msg;
    private T data;
	public Integer getCode()
	{
		return this.code;
	}

	public void setCode(Integer code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return this.msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public T getData()
	{
		return this.data;
	}

	public void setData(T data)
	{
		this.data = data;
	}


}