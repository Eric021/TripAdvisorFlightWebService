package com.tripAdvisorIR.errors;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Error {
	/*
	 * Error Codes
	 */
	
	// codes: 1xx  Informational responses
	public static final int Continue_CODE = 100;
	public static final String Continue_FA = "ادامه";
	public static final String Continue_EN = "Continue";
	
	public static final int Switching_Protocols_CODE = 101;
	public static final String Switching_Protocols_FA = "جابجایی پروتوکلها";
	public static final String Switching_Protocols_EN = "Switching Protocols";
	
	public static final int Processing_CODE = 102;
	public static final String Processing_FA = "درحال پردازش";
	public static final String Processing_EN = "Processing";

	// codes: 2xx  Success
	public static final int OK_CODE = 200;
	public static final String OK_FA = "موفقیت آمیز";
	public static final String OK_EN = "OK";
	
	public static final int Created_CODE = 201;
	public static final String Created_FA = "ساخته شد";
	public static final String Created_EN = "Created";
	
	public static final int Accepted_EN_CODE = 202;
	public static final String Accepted_FA = "پذیرفته شد";
	public static final String Accepted_EN = "Accepted";
	
	public static final int Non_Autherative_Information_CODE = 203;
	public static final String Non_Autherative_Information_FA = "اطلاعات غیر معتبر";
	public static final String Non_Autherative_Information_EN = "Non-Autherative Information";
	
	public static final int No_Content_From_Server_CODE = 204;
	public static final String No_Content_From_Server_FA = "اطلاعاتی برای بازگشت وجود ندارد";
	public static final String No_Content_From_Server_EN = "No Content";
	
	public static final int Reset_Content_CODE = 205;
	public static final String Reset_Content_FA = "تنظیم دوباره محتوا";
	public static final String Reset_Content_EN = "Reset Content";
	
	public static final int Partial_Content_CODE = 206;
	public static final String Partial_Content_FA = "محتوای ناقص";
	public static final String Partial_Content_EN = "Partial Content";
	
	public static final int Multi_Status_CODE = 207;
	public static final String Multi_Status_FA = "چند موقعیتی";
	public static final String Multi_Status_EN = "Multi-Status";
	
	public static final int Already_Reported_CODE = 208;
	public static final String Already_Reported_FA = "گزارش شده";
	public static final String Already_Reported_EN = "Already Reported";
	
	// codes: 3xx  Redirection
	public static final int Multiple_Choices_CODE = 300;
	public static final String Multiple_Choices_FA = "چند انتخابی";
	public static final String Multiple_Choices_EN = "Multiple Choices";
	
	public static final int Moved_Permanently_CODE = 301;
	public static final String Moved_Permanently_FA = "سریعا منتقل شده";
	public static final String Moved_Permanently_EN = "Moved Permanently";
	
	public static final int Found_CODE = 302;
	public static final String Found_FA = "سریعا منتقل شده";
	public static final String Found_EN = "Found";
	
	public static final int See_Other_CODE = 303;
	public static final String See_Other_FA = "دیگری را ببین";
	public static final String See_Other_EN = "See Other";
	
	public static final int Not_Modified_CODE = 304;
	public static final String Not_Modified_FA = "مودیفای نشده";
	public static final String Not_Modified_EN = "Not Modified";
	
	public static final int Use_Proxy_CODE = 305;
	public static final String Use_Proxy_FA = "از پراکسی استفاده شود";
	public static final String Use_Proxy_EN = "Use Proxy";
	
	public static final int Switch_Proxy_CODE = 306;
	public static final String Switch_Proxy_FA = "تغییر پراکسی";
	public static final String Switch_Proxy_EN = "Switch Proxy";
	
	public static final int Temporary_Redirect_CODE = 307;
	public static final String Temporary_Redirect_FA = "ریدایرکت موقتی";
	public static final String Temporary_Redirect_EN = "Temporary Redirect";
	
	public static final int Permanent_Redirect_CODE = 308;
	public static final String Permanent_Redirect_FA = "ریدایرکت دائمی";
	public static final String Permanent_Redirect_EN = "Permanent Redirect";
	
	// codes: 4xx  Client Errors
	public static final int Bad_Request_CODE = 400;
	public static final String Bad_Request_FA = "درخواست نامناسب";
	public static final String Bad_Request_EN = "Bad Request";
	
	public static final int Unauthorized_CODE = 401;
	public static final String Unauthorized_FA = "غیرمجاز";
	public static final String Unauthorized_EN = "Unauthorized";
	
	public static final int Payment_Required_CODE = 402;
	public static final String Payment_Required_FA = "نیاز به پرداخت";
	public static final String Payment_Required_EN = "Payment Required";
	
	public static final int Forbidden_CODE = 403;
	public static final String Forbidden_FA = "دسترسی برای شما ممنوع می باشد";
	public static final String Forbidden_EN = "Forbidden";
	
	public static final int Not_Found_CODE = 404;
	public static final String Not_Found_FA = "صفحه مورد نظر پیدا نشد";
	public static final String Not_Found_EN = "Not Found";
	
	public static final int Method_Not_Allowed_CODE = 405;
	public static final String Method_Not_Allowed_FA = "متد درست انتخاب نشده است";
	public static final String Method_Not_Allowed_EN = "Method Not Allowed";
	
	public static final int Not_Acceptable_CODE = 406;
	public static final String Not_Acceptable_FA = "متد درخواستی مورد پذیرش نیست";
	public static final String Not_Acceptable_EN = "Not Acceptable";
	
	public static final int Requset_Timeout_CODE = 408;
	public static final String Requset_Timeout_FA = "مدت درخواست به اتمام رسیده است";
	public static final String Requset_Timeout_EN = "Requset Timeout";
	
	// codes: 5xx  Server error
	public static final int Internal_Server_Error_CODE = 500;
	public static final String Internal_Server_Error_FA = "درحال حاضر امکان اتصال به سرور وجود ندارد";
	public static final String Internal_Server_Error_EN = "Internal Server Error";
	
	public static final int Not_Implemented_CODE = 501;
	public static final String Not_Implemented_FA = "این متد در سرور پیاده سازی نشده است";
	public static final String Not_Implemented_EN = "Not Implemented";
	
	public static final int Bad_Gateway_CODE = 502;
	public static final String Bad_Gateway_FA = "دروازه نامناسب";
	public static final String Bad_Gateway_EN = "Bad Gateway";
	
	public static final int Service_Unavilable_CODE = 503;
	public static final String Service_Unavilable_FA = "درحال حاضر سرور در دسترس نیست";
	public static final String Service_Unavilable_EN = "Service Unavilable";

	// fields to generate error code and json!
	private int code;
	private String description;

	// constructor
	public Error(int code, String description) {
		this.code = code;
		this.description = description;
	}

	// setter getter for concrete fields
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
