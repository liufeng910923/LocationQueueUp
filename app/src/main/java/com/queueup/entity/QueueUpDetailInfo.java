package com.queueup.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QueueUpDetailInfo  implements Serializable{
	private String queueNo;  //排队号码
	private String eatTime;   // 就餐时间
	public String getQueueNo() {
		return queueNo;
	}
	public void setQueueNo(String queueNo) {
		this.queueNo = queueNo;
	}
	public String getEatTime() {
		return eatTime;
	}
	public void setEatTime(String eatTime) {
		this.eatTime = eatTime;
	}
}
