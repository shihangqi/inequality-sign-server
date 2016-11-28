package com.demo.ordertab;

import com.demo.common.model.Blog;
import com.demo.common.model.Ordertab;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(OrdertabInterceptor.class)
public class OrdertabController extends Controller {
	public void index() {
		setAttr("ordertabPage",Ordertab.dao.paginate(getParaToInt(0, 1), 10));
		render("ordertab.html");
	}
	
	public void add() {
	}
	
	@Before(OrdertabValidator.class)
	public void save() {
		getModel(Ordertab.class).save();
		redirect("/ordertab");
	}
	
	public void edit() {
		setAttr("ordertab", Ordertab.dao.findById(getParaToInt()));
	}
	
	@Before(OrdertabValidator.class)
	public void update() {
		getModel(Ordertab.class).update();
		redirect("/ordertab");
	}
	
	public void delete() {
		Ordertab.dao.deleteById(getParaToInt());
		redirect("/ordertab");
	}
}


