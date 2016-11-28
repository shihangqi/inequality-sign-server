package com.demo.ordernum;

import com.demo.common.model.Blog;
import com.demo.common.model.Ordernum;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * BlogController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(OrdernumInterceptor.class)
public class OrdernumController extends Controller {
	public void index() {
		setAttr("ordernumPage", Ordernum.dao.paginate(getParaToInt(0, 1), 10));
		render("ordernum.html");
	}
	
	public void add() {
	}
	
	@Before(OrdernumValidator.class)
	public void save() {
		getModel(Ordernum.class).save();
		redirect("/ordernum");
	}
	
	public void edit() {
		setAttr("ordernum", Ordernum.dao.findById(getParaToInt()));
	}
	
	@Before(OrdernumValidator.class)
	public void update() {
		getModel(Ordernum.class).update();
		redirect("/ordernum");
	}
	
	public void delete() {
		Ordernum.dao.deleteById(getParaToInt());
		redirect("/ordernum");
	}
}


