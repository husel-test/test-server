package com.foscam.test.http;


public class MessageBody  {
	
	private final static String service = "pushFep";
	
	private final static String version = "1.0.0-TEST";
	
	private long id = -1;
	private String senderTag;
	private String msgTime;
	private String userTag = null;
	private String msg = null;
	private String includeIOS;
	

	public MessageBody() {
	}
	
	public MessageBody(long id, String senderTag, String msgTime,
			String userTag, String msg) {
		this.id = id;
		this.senderTag = senderTag;
		this.msgTime = msgTime;
		this.userTag = userTag;
		this.msg = msg;
	}

	public String getUserTag() {
		return userTag;
	}

	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		
		//System.out.println(JSONObject.fromObject(this).toString());
		//return String.format("{\"userTags\":\"%s\",\"msg\":\"%s\",\"service\":\"%s\",\"version\":\"%s\"}",this.userTags==null?"":this.userTags,this.msg==null?"":this.msg,this.service,this.version);
		return String.format("id=%s&senderTag=%s&msgTime=%s&service=%s&version=%s&userTag=%s&msg=%s%s",this.id,this.senderTag,this.msgTime,service,version,this.userTag==null?"":this.userTag,this.msg==null?"":this.msg,this.includeIOS==null?"":"&includeIOS="+this.includeIOS);
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSenderTag() {
		return senderTag;
	}
	public void setSenderTag(String senderTag) {
		this.senderTag = senderTag;
	}
	public String getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
	public String getIncludeIOS() {
		return includeIOS;
	}
	public void setIncludeIOS(String includeIOS) {
		this.includeIOS = includeIOS;
	}
	

}
