package com.demo.user;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Attr;

import com.demo.common.model.Ordertab;
import com.demo.common.model.Shop;
import com.demo.common.model.User;
import com.demo.push.Push;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * UserController 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller
 * 中，养成好习惯，有利于大型项目的开发与维护
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

	public void getData() {
		List<User> UserData = User.dao.find("select * from User where id =" + 1);
		renderJson(UserData);
	}

	public void delete() {
		User.dao.deleteById(getParaToInt());
		redirect("/user");
	}

	public void test() {
		// 本机访问 localhost/User/test 或127.0.0.1/User/test
		// 内网访问 本机ip/User/test
		// 服务器端获取请求后 返回结果
		// 返回字符串 renderText
		// 返回html render（）；
		// 快捷键 alt+/
		// 2.2返回htmlsetAttr("UserPage", User.me.paginate(getParaToInt(0, 1),
		// 10));render("User.html");

		// 2.3返回xml (无参数）renderXml("NewFile.xml");
		// 返回xml（有参数）
		//
		// setAttr("UserPage", User.me.paginate(getParaToInt(0, 1), 10));
		// renderXml("NewFile.xml");

		// 2.4返回jason

		HttpServletRequest r = getRequest();
		String username = r.getParameter("username");
		List<User> user = User.dao.find("select * from user where user_tel" + "=" + "\"" + username + "\"");
		System.out.println(user.size());
		if (user.isEmpty()) {
			// 用户名不存在
			renderText("0");
			System.out.println("0");
		} else {
			// 用户名存在
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
		// //或者 List<User> ls =User.me.find("select * from blo g");
		//
		// 3 获取客户端请求参数
		// 3.1 jfinal框架支持的url映射方式：http://10..7.88.11/User/edit/21
		// 获取方法：getPareToInt()
		// 3.2 传统web请求方式
		// HttpServletRequest r = getRequest();
		// String uname = r.getParameter("name");//参数key 返回value
	}

	public void login() {
		HttpServletRequest r = getRequest();
		String user_tel;
		String user_id;
		// 判断是第三方登录还是手机登录
		if (r.getParameter("user_tel") == null) {
			user_id = r.getParameter("user_id");
			List<User> userid = User.dao.find("select * from user where open_id" + "=" + "\"" + user_id + "\"");
//			 判断是否是新用户
			 if(userid.isEmpty()){
				 User u = new User();
				 u.setOpenId(user_id);
				 u.save();
			 }
			List<User> id = User.dao.find("select * from user where open_id" + "=" + "\"" + user_id + "\"");
			if (id.isEmpty()) {
				renderText("loginfail");
			} else {
				renderText(id.get(0).getId().toString());
			}
		} else if (r.getParameter("user_id") == null) {
			user_tel = r.getParameter("user_tel");
			System.out.println(user_tel);
			List<User> userid = User.dao.find("select * from user where user_tel" + "=" + "\"" + user_tel + "\"");
			// 判断是否是新用户
			if (userid.isEmpty()) {
				 User u = new User();
				 u.setUserTel(user_tel);
				 u.save();
			}

			List<User> id = User.dao.find("select * from user where user_tel" + "=" + "\"" + user_tel + "\"");
			if (id.isEmpty()) {
				renderText("loginfail");
			} else {
				renderText(id.get(0).getId().toString());
			}

		} else {
			renderText("loginfail");
		}
	}

	public void changeimg() {
		HttpServletRequest r = getRequest();
		String user_img = r.getParameter("user_img");
		String id = r.getParameter("id");
		List<User> list = User.dao.find("select * from user where id" + "=" + "\"" + id + "\"");
		User u = list.get(0);
		u.setUserImg(user_img);
		boolean b = u.update();
		if (b == true) {
			renderText("changeimgok");
		} else {
			renderText("changeimgfail");
		}
	}

	public void changename() {
		HttpServletRequest r = getRequest();
		String user_name = r.getParameter("user_name");
		String id = r.getParameter("id");
		List<User> list = User.dao.find("select * from user where id" + "=" + "\"" + id + "\"");
		User u = list.get(0);
		u.setUserName(user_name);
		boolean b = u.update();
		if (b == true) {
			renderText("changenameok");
		} else {
			renderText("changenamefail");
		}
	}

	public void changesex() {
		HttpServletRequest r = getRequest();
		String user_sex = r.getParameter("user_sex");
		String id = r.getParameter("id");
		List<User> list = User.dao.find("select * from user where id" + "=" + "\"" + id + "\"");
		User u = list.get(0);
		u.setUserSex(user_sex);
		boolean b = u.update();
		if (b == true) {
			renderText("changesexok");
		} else {
			renderText("changesexfail");
		}

	}

	public void inquiry() {
		HttpServletRequest r = getRequest();
		String id = r.getParameter("user_id");
		JSONArray json = new JSONArray();
		
		List<Record> R = Db.find("select shop_name,num from ordertab, shop where shop.id = ordertab.shop_id and ordertab.user_id ="+id);
		for (int i = 0; i < R.size(); i++) {
			System.out.println(R.get(i).getColumns());
			json.put(R.get(i).getColumns());
		}
		System.out.println(json.toString());
		renderText(json.toString());
	}
}
