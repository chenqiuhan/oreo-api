package com.kawnnor.oreo.api.model;

import java.util.Date;

public class ProjectThreeFlow{
    private Integer id;
    private Integer projectId;
    private String transactionNum; //交易流水号
    private String payAccount;  //付款支付账号
    private String payBank;  //付款银行卡银行名称
    private String payBankNum;  //付款银行卡号
    private Double money;  //交易金额
    private Double balance;  //交易余额
    private String receiveAccount;  //收款支付账号
    private String receiveBank;  //收款银行卡银行名称
    private String receiveBankNum;  //收款银行卡号
    private String transactionType;  //交易类型
	private Date transactionDate;  //交易时间
	private String transactionDateStr;
    private String payType;  //支付类型
    private String outIn;  //交易主体的出入账标识
    private String moneyType;  //币种
    private String posNum;  //消费pos机编号
    private String business;  //收款方的商户号
    private String equipment;  //交易设备类型
    private String ip;  //交易支付设备ip
    private String mac;  //mac地址
    private String longitude;  //交易地点经度
    private String latitude;  //交易地点纬度
    private String equipmentNum;  //交易设备号
    private String bankTransactionNum;  //银行外部渠道交易流水号
    private String note;  //备注
    private Date createDate;
    private Integer status;  //数据是否已清洗   0未清洗，1已清洗，2已清洗未通过
    private String remark;
	



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

	public String getTransactionNum()
	{
		return this.transactionNum;
	}

	public void setTransactionNum(String transactionNum)
	{
		this.transactionNum = transactionNum;
	}

	public String getPayAccount()
	{
		return this.payAccount;
	}

	public void setPayAccount(String payAccount)
	{
		this.payAccount = payAccount;
	}

	public String getPayBank()
	{
		return this.payBank;
	}

	public void setPayBank(String payBank)
	{
		this.payBank = payBank;
	}

	public String getPayBankNum()
	{
		return this.payBankNum;
	}

	public void setPayBankNum(String payBankNum)
	{
		this.payBankNum = payBankNum;
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

	public String getReceiveAccount()
	{
		return this.receiveAccount;
	}

	public void setReceiveAccount(String receiveAccount)
	{
		this.receiveAccount = receiveAccount;
	}

	public String getReceiveBank()
	{
		return this.receiveBank;
	}

	public void setReceiveBank(String receiveBank)
	{
		this.receiveBank = receiveBank;
	}

	public String getReceiveBankNum()
	{
		return this.receiveBankNum;
	}

	public void setReceiveBankNum(String receiveBankNum)
	{
		this.receiveBankNum = receiveBankNum;
	}

	public String getTransactionType()
	{
		return this.transactionType;
	}

	public void setTransactionType(String transactionType)
	{
		this.transactionType = transactionType;
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

	public String getPayType()
	{
		return this.payType;
	}

	public void setPayType(String payType)
	{
		this.payType = payType;
	}

	public String getOutIn()
	{
		return this.outIn;
	}

	public void setOutIn(String outIn)
	{
		this.outIn = outIn;
	}

	public String getMoneyType()
	{
		return this.moneyType;
	}

	public void setMoneyType(String moneyType)
	{
		this.moneyType = moneyType;
	}

	public String getPosNum()
	{
		return this.posNum;
	}

	public void setPosNum(String posNum)
	{
		this.posNum = posNum;
	}

	public String getBusiness()
	{
		return this.business;
	}

	public void setBusiness(String business)
	{
		this.business = business;
	}

	public String getEquipment()
	{
		return this.equipment;
	}

	public void setEquipment(String equipment)
	{
		this.equipment = equipment;
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

	public String getLongitude()
	{
		return this.longitude;
	}

	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}

	public String getLatitude()
	{
		return this.latitude;
	}

	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}

	public String getEquipmentNum()
	{
		return this.equipmentNum;
	}

	public void setEquipmentNum(String equipmentNum)
	{
		this.equipmentNum = equipmentNum;
	}

	public String getBankTransactionNum()
	{
		return this.bankTransactionNum;
	}

	public void setBankTransactionNum(String bankTransactionNum)
	{
		this.bankTransactionNum = bankTransactionNum;
	}

	public String getNote()
	{
		return this.note;
	}

	public void setNote(String note)
	{
		this.note = note;
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
}