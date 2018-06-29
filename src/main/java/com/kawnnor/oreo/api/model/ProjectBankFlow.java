package com.kawnnor.oreo.api.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ProjectBankFlow{
    private Integer id;
	private Integer projectId;
	private String bankId;
    private String account;//查询账号
    private String toName;  //对方账号姓名
    private String toAccount; //对方账号卡号
    private Double money; //金额
    private Double balance; //余额
    private String borrowFlag; //借贷标志 
    private String transactionType; //交易类型 
    private String transactionResult;   //交易结果 
	private Date   transactionDate; //时间
	private String transactionDateStr;
    private String transactionBank;  //交易开户行 
    private String transactionOutlets; //交易网点名称
    private String transactionNum;   //交易流水号 
    private String transactionPlace; //交易发生地
    private String voucherNum;   //凭证号
    private String ending; //终端号
    private String business; //商户名称 
    private String ip; //ip地址 
	private String mac;   //mac地址 
    private Date createDate;
    private Integer status;  //数据是否已清洗   0未清洗，1已清洗，2已清洗未通过
	private String remark; //备注
	
	private String transactionDateStrStart;//开始时间
	private String transactionDateStrEnd;//结束时间
	private Double maxMoney; //金额上限
	private Double minMoney; //金额下线
	private Integer level;//级数
	private Double totalMoney;  //转入/转出总金额（绝对值）
	private Integer count;  //转入/转出次数
	private Integer intervals;  //间隔时间（小时）


    public Integer getId()
	{
		return this.id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getProjectId()
	{
		return this.projectId;
	}

	public void setProjectId(Integer projectId)
	{
		this.projectId = projectId;
	}

	public String getBankId()
	{
		return this.bankId;
	}

	public void setBankId(String bankId)
	{
		this.bankId = bankId;
	}

	public String getAccount()
	{
		return this.account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getToName()
	{
		return this.toName;
	}

	public void setToName(String toName)
	{
		this.toName = toName;
	}

	public String getToAccount()
	{
		return this.toAccount;
	}

	public void setToAccount(String toAccount)
	{
		this.toAccount = toAccount;
	}
	public Double getMoney()
	{
		return this.money;
	}

	public void setMoney(Double money)
	{
		this.money = money;
	}

	public Double getBalance()
	{
		return this.balance;
	}

	public void setBalance(Double balance)
	{
		this.balance = balance;
	}

	public String getBorrowFlag()
	{
		return this.borrowFlag;
	}

	public void setBorrowFlag(String borrowFlag)
	{
		this.borrowFlag = borrowFlag;
	}

	public String getTransactionType()
	{
		return this.transactionType;
	}

	public void setTransactionType(String transactionType)
	{
		this.transactionType = transactionType;
	}

	public String getTransactionResult()
	{
		return this.transactionResult;
	}

	public void setTransactionResult(String transactionResult)
	{
		this.transactionResult = transactionResult;
    }
    
    public Date getTransactionDate()
	{
		return this.transactionDate;
	}

	public void setTransactionDate(Date transactionDate)
	{
		this.transactionDate = transactionDate;
	}

	public String getTransactionDateStr()
	{
		return this.transactionDateStr;
	}

	public void setTransactionDateStr(String transactionDateStr)
	{
		this.transactionDateStr = transactionDateStr;
	}

	public String getTransactionBank()
	{
		return this.transactionBank;
	}

	public void setTransactionBank(String transactionBank)
	{
		this.transactionBank = transactionBank;
	}

	public String getTransactionOutlets()
	{
		return this.transactionOutlets;
	}

	public void setTransactionOutlets(String transactionOutlets)
	{
		this.transactionOutlets = transactionOutlets;
	}

	public String getTransactionNum()
	{
		return this.transactionNum;
	}

	public void setTransactionNum(String transactionNum)
	{
		this.transactionNum = transactionNum;
	}

	public String getVoucherNum()
	{
		return this.voucherNum;
	}

	public void setVoucherNum(String voucherNum)
	{
		this.voucherNum = voucherNum;
    }
    
    public String getEnding()
	{
		return this.ending;
	}

	public void setEnding(String ending)
	{
		this.ending = ending;
	}

	public String getTransactionPlace()
	{
		return this.transactionPlace;
	}

	public void setTransactionPlace(String transactionPlace)
	{
		this.transactionPlace = transactionPlace;
	}

	public String getBusiness()
	{
		return this.business;
	}

	public void setBusiness(String business)
	{
		this.business = business;
	}

	public String getIp()
	{
		return this.ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
    }
    
    public String getMac()
	{
		return this.mac;
	}

	public void setMac(String mac)
	{
		this.mac = mac;
	}

	public Date getCreateDate()
	{
		return this.createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
    }
    
    public Integer getStatus()
	{
		return this.status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}
 
    public String getRemark()
	{
		return this.remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getTransactionDateStrStart()
	{
		return this.transactionDateStrStart;
	}

	public void setTransactionDateStrStart(String transactionDateStrStart)
	{
		this.transactionDateStrStart = transactionDateStrStart;
	}

	public String getTransactionDateStrEnd()
	{
		return this.transactionDateStrEnd;
	}

	public void setTransactionDateStrEnd(String transactionDateStrEnd)
	{
		this.transactionDateStrEnd = transactionDateStrEnd;
	}

	public Integer getLevel()
	{
		return this.level;
	}

	public void setLevel(Integer level)
	{
		this.level = level;
	}

	public Double getTotalMoney()
	{
		return this.totalMoney;
	}

	public void setTotalMoney(Double totalMoney)
	{
		this.totalMoney = totalMoney;
	}

	public Integer getCount()
	{
		return this.count;
	}

	public void setCount(Integer count)
	{
		this.count = count;
	}

	public Integer getIntervals()
	{
		return this.intervals;
	}

	public void setIntervals(Integer intervals)
	{
		this.intervals = intervals;
	}

	public Double getMaxMoney()
	{
		return this.maxMoney;
	}

	public void setMaxMoney(Double maxMoney)
	{
		this.maxMoney = maxMoney;
	}

	public Double getMinMoney()
	{
		return this.minMoney;
	}

	public void setMinMoney(Double minMoney)
	{
		this.minMoney = minMoney;
	}

}