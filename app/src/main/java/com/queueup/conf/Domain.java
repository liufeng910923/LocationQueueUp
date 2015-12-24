package com.queueup.conf;

public class Domain {
	 public static String Ip = "http://221.123.145.218:8085";
//	public static String Ip = "http://192.168.1.221:8080";
	static String cmIp = Ip + "/QueueServer/resources/";

	// 登入
	public static String insert_login = cmIp + "Member/login/";
	// 注册
	public static String insert_register = cmIp + "Member/register/";
	// 发送验证码
	public static String send_code = cmIp + "Member/sendCodeToPhone/";
	// 重置密码
	public static String reset_password = cmIp + "Member/updatepassClient/";
	// 查看我的排队
	public static String get_myQueueUp = cmIp + "Queue/getMyQueueUp/";
	// 查看过往排队
	public static String get_alreadyQueue = cmIp + "Queue/getAlreadyQueue/";
	// 查看附近企业
	public static String get_NearCompany = cmIp + "Company/getNearCompany/";
	// 提交意见反馈
	public static String str_feedback = cmIp + "Opinion/feedBack/";
	// 添加关注
	public static String attentionToCompany = cmIp + "Attention/attentionToCompany/";
	// 取消关注
	public static String cancelAttention = cmIp + "Attention/cancelAttention/";
	// 查看我的关注
	public static String getMyAttention = cmIp + "Attention/getMyAttention/";
	// 短信分享记录
	public static String messageShare = cmIp + "Share/messageShare/";
	// 检查版本更新
	public static String versionUpdate = cmIp + "Version/versionUpdate/";
	public static String get_download = cmIp + "Version/getNewApk/";
	// 查看个人资料
	public static String getPersonInfo = cmIp + "Member/getPersonInfo/";
	// 修改个人资料
	public static String updatePersonInfo = cmIp + "Member/updatePersonInfo/";

	// 查看企业信息
	public static String companyIntroduce = cmIp + "Company/companyIntroduce/";

	// 关注列表查看排队信息
	public static String getQueueUpInfo = cmIp + "Queue/getQueueUpInfo/";

	// 获取优惠卷信息
	public static String getDiscountInfo = cmIp + "Coupons/getCoupons/";
	
	// 使用优惠券
	public static String useCouponInfo = cmIp + "Coupons/useCoupon/";

}
