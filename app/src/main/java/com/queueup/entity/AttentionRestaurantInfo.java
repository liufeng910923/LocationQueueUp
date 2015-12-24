package com.queueup.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AttentionRestaurantInfo implements Serializable {
	private String Pic; // 照片
	private int imageView; // 箭头
	private int ratingBar; // 星级
	private String name; // 姓名
	private int queueState; // 排队状态
	private String address; // 地址
	private String queueup; // 排队的人数
	private String companyType; // 企业类型
	private String companyId; // 企业id
	private String companySimpleIntro;// 企业简介
	private String companyFeature; // 企业特色
	private String companyContract; // 企业联系方式
	private String queueStatus; // 是否已排队 true：已排队 false：未排队
	private String myQueueUpNum; // 我的排队号码

	public int getImageView() {
		return imageView;
	}

	public void setImageView(int imageView) {
		this.imageView = imageView;
	}

	public int getRatingBar() {
		return ratingBar;
	}

	public void setRatingBar(int ratingBar) {
		this.ratingBar = ratingBar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getQueueup() {
		return queueup;
	}

	public void setQueueup(String queueup) {
		this.queueup = queueup;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanySimpleIntro() {
		return companySimpleIntro;
	}

	public void setCompanySimpleIntro(String companySimpleIntro) {
		this.companySimpleIntro = companySimpleIntro;
	}

	public String getCompanyFeature() {
		return companyFeature;
	}

	public void setCompanyFeature(String companyFeature) {
		this.companyFeature = companyFeature;
	}

	public String getCompanyContract() {
		return companyContract;
	}

	public void setCompanyContract(String companyContract) {
		this.companyContract = companyContract;
	}

	public String getPic() {
		return Pic;
	}

	public void setPic(String pic) {
		Pic = pic;
	}

	public String getQueueStatus() {
		return queueStatus;
	}

	public void setQueueStatus(String queueStatus) {
		this.queueStatus = queueStatus;
	}

	public String getMyQueueUpNum() {
		return myQueueUpNum;
	}

	public void setMyQueueUpNum(String myQueueUpNum) {
		this.myQueueUpNum = myQueueUpNum;
	}

	public int getQueueState() {
		return queueState;
	}

	public void setQueueState(int queueState) {
		this.queueState = queueState;
	}

}
