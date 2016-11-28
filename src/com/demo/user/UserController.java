package com.demo.user;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import com.demo.common.model.User;
import com.demo.push.Push;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * UserController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(UserInterceptor.class)
public class UserController extends Controller {
	public void index() {
		setAttr("userPage", User.dao.paginate(getParaToInt(0, 1), 10));
		render("user.html");
	}
	
	public void add() {
	}
	
	@Before(UserValidator.class)
	public void save() {
		getModel(User.class).save();
		redirect("/user");
	}
	
	public void edit() {
		setAttr("user", User.dao.findById(getParaToInt()));
	}
	
	@Before(UserValidator.class)
	public void update() {
		getModel(User.class).update();
		redirect("/user");
	}
	public void getData(){
		List<User> UserData = User.dao.find("select * from User where id ="+1);
		renderJson(UserData);
	}

	
	public void delete() {
		User.dao.deleteById(getParaToInt());
		redirect("/user");
	}
	public void test(){
		//本机访问   localhost/User/test  或127.0.0.1/User/test
		//内网访问    本机ip/User/test
		//服务器端获取请求后 返回结果
		//返回字符串   renderText
		//返回html render（）；
		//快捷键    alt+/
		//2.2返回htmlsetAttr("UserPage", User.me.paginate(getParaToInt(0, 1), 10));render("User.html");
		
		//2.3返回xml (无参数）renderXml("NewFile.xml");
	//返回xml（有参数）
	//	
		//setAttr("UserPage", User.me.paginate(getParaToInt(0, 1), 10)); renderXml("NewFile.xml");
		
		//2.4返回jason

		
		HttpServletRequest r = getRequest();
		String username = r.getParameter("username");
		System.out.println(username);
		List<User> user = User.dao.
				find("select * from user where user_tel" + "=" + "\"" + username + "\"");
		System.out.println(user.size());
		if (user.isEmpty()) {
			//用户名不存在
			renderText("0");
			System.out.println("0");
		}else{
			//用户名存在
			renderText("1");
			System.out.println("1");
			Push push = new Push();
			try {
				push.sendAndroidUnicast();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

		
		
		List<User> ls = new ArrayList<User>();
		
		User object = new User();
		object.setId(123);
		object.setUserTel("测试1");
		object.setUserName("内容1");
		ls.add(object);
		User object1 = new User();
		object1.setId(1234);
		object1.setUserTel("测试2");
		object1.setUserName("内容2");
		ls.add(object1);
		User object2 = new User();
		object2.setId(1233);
		object2.setUserTel("测试3");
		object2.setUserName("内容3");
		ls.add(object2);
		renderJson(ls);
//		//或者  List<User> ls =User.me.find("select * from blo	g");
//		
		//3 获取客户端请求参数
		//3.1 jfinal框架支持的url映射方式：http://10..7.88.11/User/edit/21
		//获取方法：getPareToInt()
		//3.2 传统web请求方式
		//HttpServletRequest r = getRequest();
		//String uname = r.getParameter("name");//参数key 返回value
	}
}


