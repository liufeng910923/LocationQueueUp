/**
 * @(#)RestaurantInfo.java
 * Copyright (c) 2013-2020  Simon  All rights reserved.
 */
package com.queueup.entity;

import java.io.Serializable;

/**
 * @Title:RestaurantInfo.java
 * @Description:TODO
 * @Author:SimonYang
 * @Date 2014-9-18 下午5:11:57
 */
public class RestaurantInfo implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String pictureAddress;
	private String companyId;
	private String companyName;
	private String distance;
	private String companyType;
	private String queueUpCount;
	private String companyAddress;
	private String companySimpleIntro;
	private String companyFeature;
	private String companyContract;
	private String attention;
	private String latitude;
	private String longitude;

	/**
	 * @return the pictureAddress
	 */
	public String getPictureAddress() {
		return pictureAddress;
	}

	/**
	 * @param pictureAddress
	 *            the pictureAddress to set
	 */
	public void setPictureAddress(String pictureAddress) {
		this.pictureAddress = pictureAddress;
	}

	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId
	 *            the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(String distance) {
		this.distance = distance;
	}

	/**
	 * @return the companyType
	 */
	public String getCompanyType() {
		return companyType;
	}

	/**
	 * @param companyType
	 *            the companyType to set
	 */
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	/**
	 * @return the queueUpCount
	 */
	public String getQueueUpCount() {
		return queueUpCount;
	}

	/**
	 * @param queueUpCount
	 *            the queueUpCount to set
	 */
	public void setQueueUpCount(String queueUpCount) {
		this.queueUpCount = queueUpCount;
	}

	/**
	 * @return the companyAddress
	 */
	public String getCompanyAddress() {
		return companyAddress;
	}

	/**
	 * @param companyAddress
	 *            the companyAddress to set
	 */
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	/**
	 * @return the companySimpleIntro
	 */
	public String getCompanySimpleIntro() {
		return companySimpleIntro;
	}

	/**
	 * @param companySimpleIntro
	 *            the companySimpleIntro to set
	 */
	public void setCompanySimpleIntro(String companySimpleIntro) {
		this.companySimpleIntro = companySimpleIntro;
	}

	/**
	 * @return the companyFeature
	 */
	public String getCompanyFeature() {
		return companyFeature;
	}

	/**
	 * @param companyFeature
	 *            the companyFeature to set
	 */
	public void setCompanyFeature(String companyFeature) {
		this.companyFeature = companyFeature;
	}

	/**
	 * @return the companyContract
	 */
	public String getCompanyContract() {
		return companyContract;
	}

	/**
	 * @param companyContract
	 *            the companyContract to set
	 */
	public void setCompanyContract(String companyContract) {
		this.companyContract = companyContract;
	}

	/**
	 * @return the attention
	 */
	public String getAttention() {
		return attention;
	}

	/**
	 * @param attention
	 *            the attention to set
	 */
	public void setAttention(String attention) {
		this.attention = attention;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * Title: toString
	 * 
	 * @Description:
	 * @return
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RestaurantInfo [pictureAddress=" + pictureAddress + ", companyId=" + companyId + ", companyName=" + companyName + ", distance=" + distance + ", companyType=" + companyType
				+ ", queueUpCount=" + queueUpCount + ", companyAddress=" + companyAddress + ", companySimpleIntro=" + companySimpleIntro + ", companyFeature=" + companyFeature + ", companyContract="
				+ companyContract + ", attention=" + attention + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}

}
