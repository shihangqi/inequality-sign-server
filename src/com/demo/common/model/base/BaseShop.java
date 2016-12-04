package com.demo.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseShop<M extends BaseShop<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("id", id);
	}

	public java.lang.Integer getId() {
		return get("id");
	}

	public void setShopImg(java.lang.String shopImg) {
		set("shop_img", shopImg);
	}

	public java.lang.String getShopImg() {
		return get("shop_img");
	}

	public void setShopName(java.lang.String shopName) {
		set("shop_name", shopName);
	}

	public java.lang.String getShopName() {
		return get("shop_name");
	}

	public void setShopType(java.lang.String shopType) {
		set("shop_type", shopType);
	}

	public java.lang.String getShopType() {
		return get("shop_type");
	}

	public void setShopAddress(java.lang.String shopAddress) {
		set("shop_address", shopAddress);
	}

	public java.lang.String getShopAddress() {
		return get("shop_address");
	}

	public void setShopTel(java.lang.String shopTel) {
		set("shop_tel", shopTel);
	}

	public java.lang.String getShopTel() {
		return get("shop_tel");
	}

	public void setShopCity(java.lang.String shopCity) {
		set("shop_city", shopCity);
	}

	public java.lang.String getShopCity() {
		return get("shop_city");
	}

	public void setShopDescription(java.lang.String shopDescription) {
		set("shop_description", shopDescription);
	}

	public java.lang.String getShopDescription() {
		return get("shop_description");
	}

}