package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseOrdernum<M extends BaseOrdernum<M>> extends Model<M> implements IBean {

	public void setShopId(java.lang.Integer shopId) {
		set("shop_id", shopId);
	}

	public java.lang.Integer getShopId() {
		return get("shop_id");
	}

	public void setNowType1(java.lang.Integer nowType1) {
		set("now_type1", nowType1);
	}

	public java.lang.Integer getNowType1() {
		return get("now_type1");
	}

	public void setNowType2(java.lang.Integer nowType2) {
		set("now_type2", nowType2);
	}

	public java.lang.Integer getNowType2() {
		return get("now_type2");
	}

	public void setNowType3(java.lang.Integer nowType3) {
		set("now_type3", nowType3);
	}

	public java.lang.Integer getNowType3() {
		return get("now_type3");
	}

	public void setAllType1(java.lang.Integer allType1) {
		set("all_type1", allType1);
	}

	public java.lang.Integer getAllType1() {
		return get("all_type1");
	}

	public void setAllType2(java.lang.Integer allType2) {
		set("all_type2", allType2);
	}

	public java.lang.Integer getAllType2() {
		return get("all_type2");
	}

	public void setAllType3(java.lang.Integer allType3) {
		set("all_type3", allType3);
	}

	public java.lang.Integer getAllType3() {
		return get("all_type3");
	}

	public void setNameType1(java.lang.String nameType1) {
		set("name_type1", nameType1);
	}

	public java.lang.String getNameType1() {
		return get("name_type1");
	}

	public void setNameType2(java.lang.String nameType2) {
		set("name_type2", nameType2);
	}

	public java.lang.String getNameType2() {
		return get("name_type2");
	}

	public void setNameType3(java.lang.String nameType3) {
		set("name_type3", nameType3);
	}

	public java.lang.String getNameType3() {
		return get("name_type3");
	}

}
