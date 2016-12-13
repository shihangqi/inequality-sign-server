package com.demo.shop;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.common.model.Ordernum;
import com.demo.common.model.Ordertab;
import com.demo.common.model.Scene;
import com.demo.common.model.Shop;
import com.demo.common.model.User;
import com.demo.logic.Client_logic;
import com.demo.logic.Shop_client_logic;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * ServerController 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller
 * 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(ShopInterceptor.class)
public class ShopController extends Controller {
	public void index() {
		setAttr("shopPage", Shop.dao.paginate(getParaToInt(0, 1), 10));
		render("shop.html");
	}

	public void add() {
	}

	@Before(ShopValidator.class)
	public void save() {
		getModel(Shop.class).save();
		redirect("/shop");
	}

	public void edit() {
		setAttr("shop", Shop.dao.findById(getParaToInt()));
	}

	@Before(ShopValidator.class)
	public void update() {
		getModel(Shop.class).update();
		redirect("/shop");
	}

	public void delete() {
		Shop.dao.deleteById(getParaToInt());
		redirect("/shop");
	}

	// 查询周排行的商家名单
	public void list() {
		HttpServletRequest r = getRequest();
		String city = r.getParameter("city");
		JSONArray json = new JSONArray();
		List<Record> R = Db
				.find("select shop_name, count(shop.id), shop_address,shop_img_small from ordertab,shop where ordertab.shop_id = shop.id and shop_city = "+city+ " group by shop.id order by count(shop.id) DESC");
		if (R == null) {
			renderText("listfail");
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
			renderText(json.toString());
		}
	}

	// 查询评论内容页
	public void comment() {
		JSONArray json = new JSONArray();
		List<Record> R = Db.find(
				"select user_name,star,time,content,shop_name from comment,user,shop where comment.user_id = user.id and comment.shop_id = shop.id order by time DESC");
		if (R == null) {
			renderText("commentfail");
		} else {
			for (int i = 0; i < R.size(); i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			System.out.println(json.toString());
			renderText(json.toString());
		}
	}

	// 返回场景中的12个场景
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
			renderText(json.toString());
		}
	}

	// 根据上传的图片地址，修改数据库中的数据
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

	// 返回列表页
	public void home() {
		HttpServletRequest r = getRequest();
		String city = r.getParameter("city");
		JSONArray json = new JSONArray();
		System.out.println(city);
		// 缺少order by 排队的人数大小
		List<Record> R = Db.find("select id,shop_name,shop_description,shop_img_small from shop where shop_city =" + city);

		if (R == null) {
			renderText("homefail");
		} else {
			if (R.size() <= 4) {
				for (int i = 0; i < R.size(); i++) {
					System.out.println(R.get(i).getColumns());
					json.put(R.get(i).getColumns());
				}
			} else {
				for (int i = 0; i < 4; i++) {
					System.out.println(R.get(i).getColumns());
					json.put(R.get(i).getColumns());
				}
			}
			
			renderText(json.toString());
			System.out.println(json.toString());
		}
	}

	// 改变地址这点再想想，计划是改变地址只在商家端有，在客户端只存本地，每次上传的时候上传上城市信息，查找时查商家端的城市信息；
	// public void changelocale(){
	// HttpServletRequest r = getRequest();
	// String city = r.getParameter("city");
	// JSONArray json = new JSONArray();
	//
	// List<Record> R = Db.find("");
	// for (int i = 0; i < R.size(); i++) {
	// System.out.println(R.get(i).getColumns());
	// json.put(R.get(i).getColumns());
	// }
	// System.out.println(json.toString());
	// renderJson(json);
	// }
	// 搜索功能
	public void search() {
		HttpServletRequest r = getRequest();
		String city = r.getParameter("city");
		String content = r.getParameter("content");
		JSONArray json = new JSONArray();
		List<Record> R = Db.find("select id,shop_name,shop_description,shop_img_small from shop where shop_name like "+"\"%"
				+ content + "%\""+" and shop_city = " + "\"" + city+ "\"");
		

		if (R == null) {
			renderText("searchfail");
		} else {
			json.put(R.get(0).getColumns());
			renderText(json.toString());
		}
	}

	public void line_dining() {
		HttpServletRequest r = getRequest();
		String city = r.getParameter("city");

		JSONArray json = new JSONArray();

		List<Record> R = Db.find("select id,shop_name,shop_description,shop_img_small from shop where shop_city =" + "\""+city+"\""+ "and shop_type = \'1\'");

		if (R == null) {
			renderText("diningfail");
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
			renderText(json.toString());
		}
	}

	public void line_hall() {
		HttpServletRequest r = getRequest();
		String city = r.getParameter("city");

		JSONArray json = new JSONArray();
		//有错误
		List<Record> R = Db.find("select id,shop_name,shop_description,shop_img_small from shop where shop_city =" + "\""+city+"\""
				+ "and shop_type = \'2\'");

		if (R == null) {
			renderText("diningfail");
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
			renderText(json.toString());
		}
	}

	public void sao() {
		HttpServletRequest r = getRequest();
		String shopid = r.getParameter("shop_id");

		JSONArray json = new JSONArray();
		List<Record> R = Db
				.find("select shop_img_big,now_type1,now_type2,now_type3,all_type1,all_type2,all_type3,shop_address from shop,ordernum where id = shop_id ="
						+ "\""+shopid+"\"");

		if (R == null) {
			renderText("saofail");
		} else {
			for (int i = 0; i < R.size(); i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			System.out.println(json.toString());
			renderText(json.toString());
		}

	}

	public void store() {
		HttpServletRequest r = getRequest();
		String shopid = r.getParameter("shop_id");
		JSONArray json = new JSONArray();

		List<Record> R = Db
				.find("select shop_img_big,now_type1,now_type2,now_type3,all_type1,all_type2,all_type3,shop_address from shop,ordernum where id = shop_id ="
						+ "\""+shopid+"\"");

		if (R == null) {
			renderText("storefail");
		} else {
			for (int i = 0; i < R.size(); i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			System.out.println(json.toString());
			renderText(json.toString());
		}
	}

	public void join() {
		HttpServletRequest r = getRequest();
		String user_id = r.getParameter("user_id");
		String shop_id = r.getParameter("shop_id");
		String type = r.getParameter("type");

		Client_logic cl = new Client_logic(user_id, shop_id, type);
		JSONObject json = cl.join();
		if (json.toString().equals("[]")) {
			renderText("joinfail");
		} else {
			renderText(json.toString());
			System.out.println(json.toString());
		}
	}
	/**
	 * 商家端接口
	 */
	public void login() {
		HttpServletRequest r = getRequest();
		String shop_id = r.getParameter("shop_id");
		String shop_pwd = r.getParameter("shop_pwd");
		
		List<Shop> shop = Shop.dao.find("select * from shop where shop_id ="+ shop_id);
		// 判断是否是新用户
//		if (shopid.isEmpty()) {
//			Shop s = new Shop();
//			s.setShopId(Integer.parseInt(shop_id));
//			s.save();
//		}
		if (shop.isEmpty()) {
			//用户名不存在
			renderText("0");
		}else{
			//用户名存在
			List<Shop> shop2 = Shop.dao.
					find("select * from shop where shop_id" + "=" + "\"" + shop_id + "\""
							+ "and shop_pwd=" + "\"" + shop_pwd + "\"");
			if (shop2.isEmpty()) {
				//密码不正确
				renderText("1");
			}else{
				//用户名密码正确,返回商家的ID
				renderText(shop2.get(0).getId().toString());
			}
			
		}
	}
	public void isregister(){
		HttpServletRequest r = getRequest();
		String shop_id = r.getParameter("shop_id");
		
		List<Shop> shop = Shop.dao.find("select * from shop where shop_id ="+ shop_id);
		if(!shop.isEmpty()){
			//用户名存在
			renderText("1");
		}else{
			//用户名不存在
			renderText("0");
		}
	}
	
	public void register(){
		HttpServletRequest r = getRequest();
		String shop_id = r.getParameter("shop_id");
		String shop_pwd = r.getParameter("shop_pwd");
		
		List<Shop> shop = Shop.dao.find("select * from shop where shop_id ="+ shop_id);
		if(!shop.isEmpty()){
			//用户名存在
			renderText("2");
			System.out.println("2");
		}else{
			Shop s = new Shop();
			s.setShopId(shop_id);
			s.setShopPwd(shop_pwd);
			boolean b = s.save();
			if(b == true){
				Ordernum o = new Ordernum();
				o.setShopId(s.getId());
				o.setAllType1(0);
				o.setNowType1(0);
				boolean bl = o.save();
				if(bl == true){
					renderText(s.getId().toString());
					System.out.println(s.getId().toString());
					}
				else{
					renderText("0");
					System.out.println("0");
				}
			}else{
				renderText("0");
				System.out.println("0.0");
			}
		}
	}
	
	public void change_shop_smallimg(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String url = r.getParameter("smallimg_url");
		
		List<Shop> shop = Shop.dao.find("select * from shop where id ="+ id);
		shop.get(0).setShopImgSmall(url);
		boolean b = shop.get(0).update();
		if(b == true){
			//更新成功
			renderText("1");
		}else{
			//更新失败
			renderText("0");
		}
	}
	public void change_shop_bigimg(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String url = r.getParameter("bigimg_url");
		
		List<Shop> shop = Shop.dao.find("select * from shop where id ="+ id);
		shop.get(0).setShopImgBig(url);
		boolean b = shop.get(0).update();
		if(b == true){
			//更新成功
			renderText("1");
		}else{
			//更新失败
			renderText("0");
		}
	}
	public void change_shop_name(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String name = r.getParameter("name");
		
		List<Shop> shop = Shop.dao.find("select * from shop where id ="+ id);
		shop.get(0).setShopName(name);
		boolean b = shop.get(0).update();
		if(b == true){
			//更新成功
			renderText("1");
		}else{
			//更新失败
			renderText("0");
		}
	}
	public void change_shop_type(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String type = r.getParameter("type");
		
		List<Shop> shop = Shop.dao.find("select * from shop where id ="+ id);
		shop.get(0).setShopType(type);
		boolean b = shop.get(0).update();
		if(b == true){
			//更新成功
			renderText("1");
		}else{
			//更新失败
			renderText("0");
		}
	}
	public void change_shop_city(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String city = r.getParameter("city");
		
		List<Shop> shop = Shop.dao.find("select * from shop where id ="+ id);
		shop.get(0).setShopCity(city);
		boolean b = shop.get(0).update();
		if(b == true){
			//更新成功
			renderText("1");
		}else{
			//更新失败
			renderText("0");
		}
	}
	public void change_shop_address(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String address = r.getParameter("address");
		
		List<Shop> shop = Shop.dao.find("select * from shop where id ="+ id);
		shop.get(0).setShopAddress(address);
		boolean b = shop.get(0).update();
		if(b == true){
			//更新成功
			renderText("1");
		}else{
			//更新失败
			renderText("0");
		}
	}
	public void change_shop_tel(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String tel = r.getParameter("tel");
		
		List<Shop> shop = Shop.dao.find("select * from shop where id ="+ id);
		shop.get(0).setShopTel(tel);
		boolean b = shop.get(0).update();
		if(b == true){
			//更新成功
			renderText("1");
		}else{
			//更新失败
			renderText("0");
		}
	}
	//有待考虑   主要问题，数据库内没有对应列
	public void change_shop_ordertype(){
		
	}
	public void change_shop_loginname(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String loginname = r.getParameter("loginname");
		
		List<Shop> shop = Shop.dao.find("select * from shop where id ="+ id);
		shop.get(0).setShopId(loginname);
		boolean b = shop.get(0).update();
		if(b == true){
			//更新成功
			renderText("1");
		}else{
			//更新失败
			renderText("0");
		}
	}
	public void change_shop_pwd(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String pwd = r.getParameter("pwd");
		
		List<Shop> shop = Shop.dao.find("select * from shop where id ="+ id);
		shop.get(0).setShopPwd(pwd);
		boolean b = shop.get(0).update();
		if(b == true){
			//更新成功
			renderText("1");
		}else{
			//更新失败
			renderText("0");
		}
	}
	public void click(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String type = r.getParameter("type");
		
		Shop_client_logic cl = new Shop_client_logic(id,type);
		renderText(cl.click());
	}
}
