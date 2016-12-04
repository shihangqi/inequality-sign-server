package com.demo.server;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.common.model.Scene;
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
		List<Record> R = Db.find("select shop_id, count(shop_id)from ordertab group by shop_id");
		if(R == null){
			renderText("listfail");
		}else{
			for (int i = 0; i < R.size(); i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			System.out.println(json.toString());
			renderJson(json);
		}
	}
	//查询评论内容页
	public void comment() {
		JSONArray json = new JSONArray();
		List<Record> R = Db.find("select user_name,star,time,content,shop_name from comment,user,shop where comment.user_id = user.id and comment.shop_id = shop.id order by time DESC");
		if(R == null){
			renderText("commentfail");
		}else{
			for (int i = 0; i < R.size(); i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			System.out.println(json.toString());
			renderJson(json);
		}
	}
	
	//返回场景中的12个场景
	public void scene() {
		JSONArray json = new JSONArray();
		List<Record> R = Db.find("select img,content from scene");
		if (R == null) {
			renderText("scenefail");
		} else {
			if (R.size() <= 12) {
				for (int i = 0; i < R.size(); i++) {
					System.out.println(R.get(i).getColumns());
					json.put(R.get(i).getColumns());
				}
			} else {
				for (int i = 0; i < 12; i++) {
					System.out.println(R.get(i).getColumns());
					json.put(R.get(i).getColumns());
				}

			}
			System.out.println(json.toString());
			renderJson(json);
		}
	}
	//根据上传的图片地址，修改数据库中的数据
	public void uploadscene() {
		HttpServletRequest r = getRequest();
		String scene_img = r.getParameter("scene_img");
		String scene_content = r.getParameter("scene_content");
		
		Scene s = new Scene();
		s.setImg(scene_img);
		s.setContent(scene_content);
		boolean b = s.save();
		
		if (b == true) {
			renderText("uploadsceneok");
		} else {
			renderText("uploadscenefail");
		}
	}
	//返回列表页
	public void home(){
		HttpServletRequest r = getRequest();
		String city = r.getParameter("city");
		JSONArray json = new JSONArray();
		
		List<Record> R = Db.find("");
		
		if(R == null){
			renderText("homefail");
		}else{
			for (int i = 0; i < 4; i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			System.out.println(json.toString());
			renderJson(json);
		}
	}
	
	//改变地址这点再想想，计划是改变地址只在商家端有，在客户端只存本地，每次上传的时候上传上城市信息，查找时查商家端的城市信息；
	
	
//	public void changelocale(){
//		HttpServletRequest r = getRequest();
//		String city = r.getParameter("city");
//		JSONArray json = new JSONArray();
//		
//		List<Record> R = Db.find("");
//		for (int i = 0; i < R.size(); i++) {
//			System.out.println(R.get(i).getColumns());
//			json.put(R.get(i).getColumns());
//		}
//		System.out.println(json.toString());
//		renderJson(json);
//	}
	//搜索功能
	public void search(){
		HttpServletRequest r = getRequest();
		String content = r.getParameter("content");
		JSONArray json = new JSONArray();
		
		List<Record> R = Db.find("");
		
		if(R == null){
			renderText("searchfail");
		}else{
			json.put(R.get(0).getColumns());
			renderJson(json);
		}
	}
	
	public void line_dining(){
		JSONArray json = new JSONArray();
		
		List<Record> R = Db.find("");
		
		if(R == null){
			renderText("diningfail");
		}else{
			for (int i = 0; i <R.size(); i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			System.out.println(json.toString());
			renderJson(json);
		}
	}
	
	public void line_hall() {
		JSONArray json = new JSONArray();

		List<Record> R = Db.find("");

		if (R == null) {
			renderText("hallfail");
		} else {
			for (int i = 0; i < R.size(); i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			System.out.println(json.toString());
			renderJson(json);
		}
	}
	public void sao(){
		HttpServletRequest r = getRequest();
		String shopid = r.getParameter("shop_id");
		JSONArray json = new JSONArray();

		List<Record> R = Db.find("");

		if (R == null) {
			renderText("saofail");
		} else {
			for (int i = 0; i < R.size(); i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			System.out.println(json.toString());
			renderJson(json);
		}

	}
	public void store(){
		HttpServletRequest r = getRequest();
		String shopid = r.getParameter("shop_id");
		JSONArray json = new JSONArray();

		List<Record> R = Db.find("");

		if (R == null) {
			renderText("storefail");
		} else {
			for (int i = 0; i < R.size(); i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			System.out.println(json.toString());
			renderJson(json);
		}
	}
	public void join(){
		HttpServletRequest r = getRequest();
		String shopid = r.getParameter("shop_id");
		String type = r.getParameter("type");
		
	}
}
