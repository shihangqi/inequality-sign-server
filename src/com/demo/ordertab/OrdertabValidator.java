package com.demo.ordertab;

import com.demo.common.model.Blog;
import com.demo.common.model.Ordertab;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * BlogValidator.
 */
public class OrdertabValidator extends Validator {
	
	protected void validate(Controller controller) {
//		validateRequiredString("blog.title", "titleMsg", "请输入Blog标题!");
//		validateRequiredString("blog.content", "contentMsg", "请输入Blog内容!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Ordertab.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/ordertab/save"))
			controller.render("add.html");
		else if (actionKey.equals("/ordertab/update"))
			controller.render("edit.html");
	}
}
