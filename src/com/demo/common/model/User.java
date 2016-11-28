package com.demo.common.model;

import com.demo.common.model.base.BaseUser;
import com.jfinal.plugin.activerecord.Page;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class User extends BaseUser<User> {
	public static final User dao = new User();
	public Page<User> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from user order by id asc");
	}
}
