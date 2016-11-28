package com.demo.ordernum;

import com.demo.common.model.Blog;
import com.demo.common.model.Ordernum;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * BlogValidator.
 */
public class OrdernumValidator extends Validator {
	
	protected void validate(Controller controller) {
		
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Ordernum.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/ordernum/save"))
			controller.render("add.html");
		else if (actionKey.equals("/ordernum/update"))
			controller.render("edit.html");
	}
}
