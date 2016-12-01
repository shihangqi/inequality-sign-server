package com.demo.server;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.common.model.Server;
import com.demo.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * ServerController 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller
 * 中，养成好习惯，有利于大型项目的开发与维护
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

	//查询周排行的商家名单
	public void list() {
		JSONArray json = new JSONArray();
		List<Record> R = Db.find("select server_id, count(server_id)from ordertabgroup by server_id");
		for (int i = 0; i < R.size(); i++) {
			System.out.println(R.get(i).getColumns());
			json.put(R.get(i).getColumns());
		}
		System.out.println(json.toString());
		renderJson(json);
	}

	public void comment() {
		JSONArray json = new JSONArray();
		List<Record> R = Db.find("select user_name,star,time,content, server_name from comment,user,server where comment.user_id = user.id and comment.server_id = server.id order by time DESC");
		for (int i = 0; i < R.size(); i++) {
			System.out.println(R.get(i).getColumns());
			json.put(R.get(i).getColumns());
		}
		System.out.println(json.toString());
		renderJson(json);
	}
	
	public void scene() {
		JSONArray json = new JSONArray();
		List<Record> R = Db.find("select server_id, count(server_id)from ordertabgroup by server_id");
		for (int i = 0; i < R.size(); i++) {
			System.out.println(R.get(i).getColumns());
			json.put(R.get(i).getColumns());
		}
		System.out.println(json.toString());
		renderJson(json);
	}

	public void uploadscene() {
		HttpServletRequest r = getRequest();
		String scene_img = r.getParameter("scene_img");
		List<User> list = User.dao.find("select * from user");
		User u = list.get(0);
		u.setUserImg(scene_img);
		boolean b = u.update();
		if (b == true) {
			renderText("uploadsceneok");
		} else {
			renderText("uploadscenefail");
		}
	}
	public void home(){
		JSONArray json = new JSONArray();
		List<Record> R = Db.find("select server_id, count(server_id)from ordertabgroup by server_id");
		for (int i = 0; i < R.size(); i++) {
			System.out.println(R.get(i).getColumns());
			json.put(R.get(i).getColumns());
		}
		System.out.println(json.toString());
		renderJson(json);
	}
	
	public void changelocale(){
		HttpServletRequest r = getRequest();
		String city = r.getParameter("city");
		JSONArray json = new JSONArray();
		
		List<Record> R = Db.find("select shop_name,num from ordertab, shop where shop.id = ordertab.shop_id and ordertab.user_id =");
		for (int i = 0; i < R.size(); i++) {
			System.out.println(R.get(i).getColumns());
			json.put(R.get(i).getColumns());
		}
		System.out.println(json.toString());
		renderJson(json);
	}
	public void search(){
		HttpServletRequest r = getRequest();
		String content = r.getParameter("content");
		
	}
	public void line_dining(){
		JSONArray json = new JSONArray();

		List<User> list1 = User.dao.find("select * from user where id = 1 or id = 2");
		List<User> list2 = User.dao.find("select * from user where id = 1 or id = 2");
		List<User> list3 = User.dao.find("select * from user where id = 1 or id = 2");
		if (!(list1.isEmpty() || list2.isEmpty())) {
			for (int i = 0; i < 10; i++) {
				JSONObject j = new JSONObject();

				try {
					j.put("shop_name", list1.get(i).getUserName());
					j.put("content", list2.get(i).getUserId());
					j.put("img",list3.get(i).getUserImg());
					json.put(j);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			renderText("diningfail");
		}
		System.out.println(json.toString());
		renderJson(json);
	}
	
	public void line_hall(){
		JSONArray json = new JSONArray();

		List<User> list1 = User.dao.find("select * from user where id = 1 or id = 2");
		List<User> list2 = User.dao.find("select * from user where id = 1 or id = 2");
		List<User> list3 = User.dao.find("select * from user where id = 1 or id = 2");
		if (!(list1.isEmpty() || list2.isEmpty())) {
			for (int i = 0; i < 10; i++) {
				JSONObject j = new JSONObject();

				try {
					j.put("shop_name", list1.get(i).getUserName());
					j.put("content", list2.get(i).getUserId());
					j.put("img",list3.get(i).getUserImg());
					json.put(j);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			renderText("hallfail");
		}
		System.out.println(json.toString());
		renderJson(json);
	}
	public void sao(){
		HttpServletRequest r = getRequest();
		String shopid = r.getParameter("shop_id");
		//根据shopid获取商家页面图片
		List<User> list = User.dao.find("select * from user where id = 1 or id = 2");
		//根据shopid查询服务类型
		List<User> list1 = User.dao.find("select * from user where id = 1 or id = 2");
		//根据shopid查询队列人数
		List<User> list2 = User.dao.find("select * from user where id = 1 or id = 2");
		//根据shopid查询商店地址
		List<User> list3 = User.dao.find("select * from user where id = 1 or id = 2");
		JSONObject json = new JSONObject();
		try {
			json.put("img", list.get(0).getUserImg());
			for (int i = 0; i < list1.size(); i++) {
				JSONObject json1 = new JSONObject();
				json1.put("type", list1.get(i).getUserId());
				json1.put("num", list2.get(i).getUserName());
				json.put("line", json1);
			}
			json.put("address", list3.get(0).getUserName());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json.toString());
		renderJson(json);
		
	}
	public void store(){
		HttpServletRequest r = getRequest();
		String shopid = r.getParameter("shop_id");
		//根据shopid获取商家页面图片
		List<User> list = User.dao.find("select * from user where id = 1 or id = 2");
		//根据shopid查询服务类型
		List<User> list1 = User.dao.find("select * from user where id = 1 or id = 2");
		//根据shopid查询队列人数
		List<User> list2 = User.dao.find("select * from user where id = 1 or id = 2");
		//根据shopid查询商店地址
		List<User> list3 = User.dao.find("select * from user where id = 1 or id = 2");
		JSONObject json = new JSONObject();
		try {
			json.put("img", list.get(0).getUserImg());
			for (int i = 0; i < list1.size(); i++) {
				JSONObject json1 = new JSONObject();
				json1.put("type", list1.get(i).getUserId());
				json1.put("num", list2.get(i).getUserName());
				json.put("line", json1);
			}
			json.put("address", list3.get(0).getUserName());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json.toString());
		renderJson(json);
	}
	public void join(){
		HttpServletRequest r = getRequest();
		String shopid = r.getParameter("shop_id");
		String type = r.getParameter("type");
		
	}
}
