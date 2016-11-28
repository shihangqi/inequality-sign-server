package com.demo.server;

import com.demo.common.model.Server;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * ServerController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(ServerInterceptor.class)
public class ServerController extends Controller {
	public void index() {
		setAttr("serverPage", Server.dao.paginate(getParaToInt(0, 1), 10));
		render("server.html");
	}
	
	public void add() {
	}
	
	@Before(ServerValidator.class)
	public void save() {
		getModel(Server.class).save();
		redirect("/server");
	}
	
	public void edit() {
		setAttr("server", Server.dao.findById(getParaToInt()));
	}
	
	@Before(ServerValidator.class)
	public void update() {
		getModel(Server.class).update();
		redirect("/server");
	}
	
	public void delete() {
		Server.dao.deleteById(getParaToInt());
		redirect("/server");
	}
}


