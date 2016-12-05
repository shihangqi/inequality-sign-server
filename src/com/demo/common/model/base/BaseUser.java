package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setUserTel(java.lang.String userTel) {
		set("user_tel", userTel);
	}

	public java.lang.String getUserTel() {
		return get("user_tel");
	}

	public void setUserName(java.lang.String userName) {
		set("user_name", userName);
	}

	public java.lang.String getUserName() {
		return get("user_name");
	}

	public void setUserImg(java.lang.String userImg) {
		set("user_img", userImg);
	}

	public java.lang.String getUserImg() {
		return get("user_img");
	}

	public void setUserSex(java.lang.String userSex) {
		set("user_sex", userSex);
	}

	public java.lang.String getUserSex() {
		return get("user_sex");
	}

	public void setOpenId(java.lang.String openId) {
		set("open_id", openId);
	}

	public java.lang.String getOpenId() {
		return get("open_id");
	}

}
