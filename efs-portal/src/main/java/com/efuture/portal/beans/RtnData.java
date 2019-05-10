package com.efuture.portal.beans;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class RtnData {
	private int code;
	private String message;
	private Object data;
	private int count;
	
	public RtnData(){
		this.code = 0;
		this.message = "";
		this.data = null;
		this.count = 0;
	}
	
	public RtnData(int code, String message){
		this.code = code;
		this.message = message;
		this.data = null;
	}
	
	public RtnData(Object obj){
		this.code = 0;
		this.message = "";
		setData(obj);
	}
	
	public RtnData(Object obj,int count){
		setData(obj);
		this.count = count;
	}
	
	public RtnData(int code, String message,Object obj){
		this.code = code;
		this.message = message;
		setData(data);
	}
	
	public JSONObject returnData(JSONObject json){
		if(json == null){
			json = new JSONObject();
		}
		json.put("code", this.code);
		json.put("message", this.message);
		json.put("data", this.data);
		json.put("count", this.count);
		return json;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * 输出json字符串
	 */
	public String toString(){
		return returnData(null).toJSONString();
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object obj) {
		if(obj instanceof String){
			this.data = obj;
		}else{
			this.data = (JSON) JSON.toJSON(obj);
		}
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
