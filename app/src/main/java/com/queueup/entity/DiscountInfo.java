package com.queueup.entity;


public class DiscountInfo {
	private String companyId;   //企业ID
	private String couponsAddress;  //优惠券图片地址
	private String couponId;    //优惠券ID

	public String getCouponsAddress() {
		return couponsAddress;
	}

	public void setCouponsAddress(String couponsAddress) {
		this.couponsAddress = couponsAddress;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	
	@Override
	public String toString() {
		return "DiscountInfo [couponsAddress=" + couponsAddress + "]";
	}

}
