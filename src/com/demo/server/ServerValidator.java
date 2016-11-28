package com.demo.server;

import com.demo.common.model.Server;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * BlogValidator.
 */
public class ServerValidator extends Validator {
	
	protected void validate(Controller controller) {
//		
//		validateRequiredString("server.name", "nameMsg", "请输入服务商家名称!");
//		validateRequiredString("server.url", "urlMsg", "请输入服务者Logo!");
//		validateRequiredString("server.type", "typeMsg", "请输入服务类型!");
//		validateRequiredString("server.address", "addressMsg", "请输入服务者的地址!");
//		validateRequiredString("server.call", "callMsg", "请输入服务者电话!");
		
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Server.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/server/save"))
			controller.render("add.html");
		else if (actionKey.equals("/server/update"))
			controller.render("edit.html");
	}
}
