package com.queueup.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ContactInfo implements Serializable {
	
	public String contactName;  //联系人姓名
	public String userNumber;   //联系人电话号码
	public Boolean isChecked;

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}
	
	public Boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
}