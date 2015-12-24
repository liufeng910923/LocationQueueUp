package com.queueup.entity;

import java.io.Serializable;


@SuppressWarnings("serial")
public class MyQueueUpInfo implements Serializable {
	private String companyId;  //企业ID
	private String companyName;   //企业名称
	private String PictureAddress;  //图片下载地址
	private String companyType;   //企业类型
	private String companyAddress;  //企业地址
	private String companyContract;  //企业联系方式
	private String queueUpCount ;  //排队人数
	private String currentNumber;  //当前排号
	private String alreadyCount;   //已排过人数
	/*private String myQueueNumber;   //已排过人数
*/	private String myQueueUpNum;    //我的排队号码
	private String overdue; //排队是否过期
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyType() {
		return companyType;
	}
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
	public String getCompanyContract() {
		return companyContract;
	}
	public void setCompanyContract(String companyContract) {
		this.companyContract = companyContract;
	}
	public String getQueueUpCount() {
		return queueUpCount;
	}
	public void setQueueUpCount(String queueUpCount) {
		this.queueUpCount = queueUpCount;
	}
	public String getCurrentNumber() {
		return currentNumber;
	}
	public void setCurrentNumber(String currentNumber) {
		this.currentNumber = currentNumber;
	}
	public String getAlreadyCount() {
		return alreadyCount;
	}
	public void setAlreadyCount(String alreadyCount) {
		this.alreadyCount = alreadyCount;
	}
/*	public String getMyQueueNumber() {
		return myQueueNumber;
	}
	public void setMyQueueNumber(String myQueueNumber) {
		this.myQueueNumber = myQueueNumber;
	}*/
	public String getPictureAddress() {
		return PictureAddress;
	}
	public void setPictureAddress(String pictureAddress) {
		PictureAddress = pictureAddress;
	}
	public String getMyQueueUpNum() {
		return myQueueUpNum;
	}
	public void setMyQueueUpNum(String myQueueUpNum) {
		this.myQueueUpNum = myQueueUpNum;
	}
	public String getOverdue() {
		return overdue;
	}
	public void setOverdue(String overdue) {
		this.overdue = overdue;
	}

}
