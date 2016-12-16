package com.demo.shop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.common.model.Img;
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
import com.jfinal.upload.UploadFile;

/**
 * ServerController 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller
 * 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(ShopInterceptor.class)
public class ShopController extends Controller {
	public static final String FILEURL = "http://10.7.88.49:8090/upload/";
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
				.find("select shop_name, count(shop.id), shop_address,shop_img_small from ordertab,shop where ordertab.shop_id = shop.id and shop_city = "
						+"\""+city+"\"" + " group by shop.id order by count(shop.id) DESC");
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
		UploadFile uploadFile = this.getFile();
		String content = this.getPara("scene_content");
		
		if(uploadFile == null || content == null){
			renderText("uploadfail");
		}else{
			List<Img> l = Img.dao.find("select * from img where id =\'1\'");
			int filenum = l.get(0).getNum();
			boolean i = uploadFile.getFile().renameTo(new File("F:\\eclipse\\inequality-sign-server\\WebRoot\\upload\\"+"picture"+filenum+".jpg"));
			
			if(i){
				String url = FILEURL + "picture"+filenum+".jpg";
				filenum++;
				Scene s = new Scene();
				s.setImg(url);
				s.setContent(content);
				l.get(0).setNum(filenum);
				boolean b = s.save();
				boolean b1 = l.get(0).update();
				if (b == true && b1 == true) {
					// 更新成功
					renderText(url);
				} else {
					// 更新失败
					renderText("uploadfail");
				}
			}else{
				renderText("uploadfail");
			}
		}
	}

	// 返回列表页
	public void home() {
		HttpServletRequest r = getRequest();
		String city = r.getParameter("city");
		JSONArray json = new JSONArray();
		System.out.println(city);
		// 缺少order by 排队的人数大小
		List<Record> R = Db
				.find("select id,shop_name,shop_description,shop_img_small from shop where shop_city =" + "\""+city+"\"");

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

	// 搜索功能
	public void search() {
		HttpServletRequest r = getRequest();
		String city = r.getParameter("city");
		String content = r.getParameter("content");
		JSONArray json = new JSONArray();
		List<Record> R = Db.find("select id,shop_name,shop_description,shop_img_small from shop where shop_name like "
				+ "\"%" + content + "%\"" + " and shop_city = " + "\"" + city + "\"");
		if (R.toString() == "[]"){
			renderText("searchfail");
		} else {
			for (int i = 0; i < R.size(); i++) {
				json.put(R.get(i).getColumns());
			}
			renderText(json.toString());
		}
	}

	public void line_dining() {
		HttpServletRequest r = getRequest();
		String city = r.getParameter("city");

		JSONArray json = new JSONArray();

		List<Record> R = Db.find("select id,shop_name,shop_description,shop_img_small from shop where shop_city ="
				+ "\"" + city + "\"" + "and shop_type = \'1\'");

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
		// 有错误
		List<Record> R = Db.find("select id,shop_name,shop_description,shop_img_small from shop where shop_city ="
				+ "\"" + city + "\"" + "and shop_type = \'2\'");

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
				.find("select shop_img_big,now_type1,now_type2,now_type3,all_type1,all_type2,all_type3,name_type1,name_type2,name_type3,shop_address from shop,ordernum where id = shop_id ="
						+ "\"" + shopid + "\"");

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
		JSONObject json1 = new JSONObject();
		List<Record> R = Db
				.find("select shop_img_big,now_type1,now_type2,now_type3,all_type1,all_type2,all_type3,name_type1,name_type2,name_type3,shop_address from shop,ordernum where shop.id = ordernum.shop_id and ordernum.shop_id="
						+ "\"" + shopid + "\"");

		if (R == null) {
			renderText("storefail");
		} else {
			for (int i = 0; i < R.size(); i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			String s = null;
			try {
				s = json.get(0).toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(json.toString());
			renderText(s);
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

		List<Shop> shop = Shop.dao.find("select * from shop where shop_id =" + shop_id);
		// 判断是否是新用户
		// if (shopid.isEmpty()) {
		// Shop s = new Shop();
		// s.setShopId(Integer.parseInt(shop_id));
		// s.save();
		// }
		if (shop.isEmpty()) {
			// 用户名不存在
			renderText("0");
		} else {
			// 用户名存在
			List<Shop> shop2 = Shop.dao.find("select * from shop where shop_id" + "=" + "\"" + shop_id + "\""
					+ "and shop_pwd=" + "\"" + shop_pwd + "\"");
			if (shop2.isEmpty()) {
				// 密码不正确
				renderText("fail");
			} else {
				// 用户名密码正确,返回商家的ID
				renderText(shop2.get(0).getId().toString());
			}

		}
	}

	public void isregister() {
		HttpServletRequest r = getRequest();
		String shop_id = r.getParameter("shop_id");

		List<Shop> shop = Shop.dao.find("select * from shop where shop_id =" + shop_id);
		if (!shop.isEmpty()) {
			// 用户名存在
			renderText("1");
		} else {
			// 用户名不存在
			renderText("0");
		}
	}

	public void register() {
		HttpServletRequest r = getRequest();
		String shop_id = r.getParameter("shop_id");
		String shop_pwd = r.getParameter("shop_pwd");

		List<Shop> shop = Shop.dao.find("select * from shop where shop_id =" + shop_id);
		if (!shop.isEmpty()) {
			// 用户名存在
			renderText("exist");
		} else {
			Shop s = new Shop();
			s.setShopId(shop_id);
			s.setShopPwd(shop_pwd);
			boolean b = s.save();
			if (b == true) {
				Ordernum o = new Ordernum();
				o.setShopId(s.getId());
				o.setAllType1(0);
				o.setNowType1(0);
				o.setNameType1("业务1");
				boolean bl = o.save();
				if (bl == true) {
					renderText(s.getId().toString());
					System.out.println(s.getId().toString());
				} else {
					renderText("0");
					System.out.println("0");
				}
			} else {
				renderText("0");
				System.out.println("0");
			}
		}
	}

	public void getmessage(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		JSONArray json = new JSONArray();
		
		List<Record> R = Db.find("select shop_img_small,shop_name,shop_type,shop_description,shop_city,shop_address,shop_tel,shop_img_big from shop where id ="+"\""+id+"\"");
	
		if (R == null) {
			renderText("0");
		} else {
			for (int i = 0; i < R.size(); i++) {
				System.out.println(R.get(i).getColumns());
				json.put(R.get(i).getColumns());
			}
			System.out.println(json.toString());
			renderText(json.toString());
		}
		
	}
	public void change_shop_description(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String description = r.getParameter("description");

		if (description == "" || description == null) {
			renderText("0");
		} else {
			List<Shop> shop = Shop.dao.find("select * from shop where id =" + id);
			shop.get(0).setShopDescription(description);
			boolean b = shop.get(0).update();
			if (b == true) {
				// 更新成功
				renderText("1");
			} else {
				// 更新失败
				renderText("0");
			}
		}
	}
	public void change_shop_smallimg() {
		UploadFile uploadFile = this.getFile();
		String id = this.getPara("id");
		
		if(uploadFile == null || id == null){
			renderText("0");
		}else{
			List<Img> l = Img.dao.find("select * from img where id =\'1\'");
			int filenum = l.get(0).getNum();
			boolean i = uploadFile.getFile().renameTo(new File("F:\\eclipse\\inequality-sign-server\\WebRoot\\upload\\"+"picture"+filenum+".jpg"));
			
			if(i){
				String url = FILEURL + "picture"+filenum+".jpg";
				filenum++;
				List<Shop> list = Shop.dao.find("select * from shop where id ="+"\""+id+"\"");
				list.get(0).setShopImgSmall(url);
				l.get(0).setNum(filenum);
				boolean b = list.get(0).update();
				boolean b1 = l.get(0).update();
				if (b == true && b1 == true) {
					// 更新成功
					renderText("1");
				} else {
					// 更新失败
					renderText("0");
				}
			}else{
				renderText("0");
			}
		}
		
		
	}

	public void change_shop_bigimg() {
		UploadFile uploadFile = this.getFile();
		String id = this.getPara("id");
		
		if(uploadFile == null || id == null){
			renderText("0");
		}else{
			List<Img> l = Img.dao.find("select * from img where id =\'1\'");
			int filenum = l.get(0).getNum();
			boolean i = uploadFile.getFile().renameTo(new File("F:\\eclipse\\inequality-sign-server\\WebRoot\\upload\\"+"picture"+filenum+".jpg"));
			
			if(i){
				String url = FILEURL + "picture"+filenum+".jpg";
				filenum++;
				List<Shop> list = Shop.dao.find("select * from shop where id ="+"\""+id+"\"");
				list.get(0).setShopImgBig(url);
				l.get(0).setNum(filenum);
				boolean b = list.get(0).update();
				boolean b1 = l.get(0).update();
				if (b == true && b1 == true) {
					// 更新成功
					renderText("1");
				} else {
					// 更新失败
					renderText("0");
				}
			}else{
				renderText("0");
			}
		}
	}

	public void change_shop_name() {
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String name = r.getParameter("name");

		if (name == "" || name == null) {
			renderText("0");
		} else {
			List<Shop> shop = Shop.dao.find("select * from shop where id =" + id);
			shop.get(0).setShopName(name);
			boolean b = shop.get(0).update();
			if (b == true) {
				// 更新成功
				renderText("1");
			} else {
				// 更新失败
				renderText("0");
			}
		}
	}

	public void change_shop_type() {
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String type = r.getParameter("type");

		if (type == "" || type == null) {
			renderText("0");
		} else {
			List<Shop> shop = Shop.dao.find("select * from shop where id =" + id);
			shop.get(0).setShopType(type);
			boolean b = shop.get(0).update();
			if (b == true) {
				// 更新成功
				renderText("1");
			} else {
				// 更新失败
				renderText("0");
			}
		}
	}

	public void change_shop_city() {
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String city = r.getParameter("city");
		if (city == "" || city == null) {
			renderText("0");
		} else {
			List<Shop> shop = Shop.dao.find("select * from shop where id =" + id);
			shop.get(0).setShopCity(city);
			boolean b = shop.get(0).update();
			if (b == true) {
				// 更新成功
				renderText("1");
			} else {
				// 更新失败
				renderText("0");
			}
		}
	}

	public void change_shop_address() {
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String address = r.getParameter("address");

		if (address == "" || address == null) {
			renderText("0");
		} else {
			List<Shop> shop = Shop.dao.find("select * from shop where id =" + id);
			shop.get(0).setShopAddress(address);
			boolean b = shop.get(0).update();
			if (b == true) {
				// 更新成功
				renderText("1");
			} else {
				// 更新失败
				renderText("0");
			}
		}
	}

	public void change_shop_tel() {
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String tel = r.getParameter("tel");
		if (tel == "" || tel == null) {
			renderText("0");
		} else {
			List<Shop> shop = Shop.dao.find("select * from shop where id =" + id);
			shop.get(0).setShopTel(tel);
			boolean b = shop.get(0).update();
			if (b == true) {
				// 更新成功
				renderText("1");
			} else {
				// 更新失败
				renderText("0");
			}
		}
	}

	public void change_shop_ordertype() {
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String type = r.getParameter("type");
		
		if (type == "" || type == null) {
			renderText("0");
		} else if(type.equals("业务1")){
			renderText("1");
		}else {
			List<Ordernum> order = Ordernum.dao.find("select * from Ordernum where shop_id =" + id);
			if(order.get(0).getNameType1() == type || order.get(0).getNameType1() == type || order.get(0).getNameType1() == type){
				renderText("2");
			}else{
				if(order.get(0).getNameType1().equals("业务1")){
					order.get(0).setNameType1(type);
				}else if(order.get(0).getNameType2() == null){
					order.get(0).setNameType2(type);
					order.get(0).setAllType2(0);
					order.get(0).setNowType2(0);
				}else if(order.get(0).getNameType3() == null){
					order.get(0).setNameType3(type);
					order.get(0).setAllType3(0);
					order.get(0).setNowType3(0);
				}else{
					renderText("0");
				}
				boolean b = order.get(0).update();
				if (b == true) {
					// 更新成功
					renderText("1");
				} else {
					// 更新失败
					renderText("0");
				}
			}
			}
			
	}

	public void delete_shop_ordertype(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String type = r.getParameter("type");
		if (type == "" || type == null) {
			renderText("0");
		}else{
			List<Ordernum> order = Ordernum.dao.find("select * from Ordernum where shop_id =" + id);
			if(order.get(0).getNameType1().equals(type)){
				order.get(0).setNameType1(null);
			}else if(order.get(0).getNameType2().equals(type)){
				order.get(0).setNameType2(null);
			}else if(order.get(0).getNameType3().equals(type)){
				order.get(0).setNameType3(null);
			}
			boolean i = order.get(0).update();
			if(i == true){
				renderText("1");
			}else{
				renderText("0");
			}
		}
	}
	
	
	public void change_shop_loginname() {
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String loginname = r.getParameter("loginname");
		if (loginname == "" || loginname == null) {
			renderText("0");
		} else {
			List<Shop> shop = Shop.dao.find("select * from shop where id =" + id);
			shop.get(0).setShopId(loginname);
			boolean b = shop.get(0).update();
			if (b == true) {
				// 更新成功
				renderText("1");
			} else {
				// 更新失败
				renderText("0");
			}
		}
	}

	public void change_shop_pwd() {
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String pwd = r.getParameter("pwd");
		if (pwd == "" || pwd == null) {
			renderText("0");
		} else {
			List<Shop> shop = Shop.dao.find("select * from shop where id =" + id);
			shop.get(0).setShopPwd(pwd);
			boolean b = shop.get(0).update();
			if (b == true) {
				// 更新成功
				renderText("1");
			} else {
				// 更新失败
				renderText("0");
			}
		}

	}

	public void click() {
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		String type = r.getParameter("type");
		if (type == null || type == "") {
			renderText("0");
		} else {
			Shop_client_logic cl = new Shop_client_logic(id, type);
			renderText(cl.click());
		}
	}
	
	public String filecontrol(UploadFile uploadfile){
		File file = uploadfile.getFile();
		FileInputStream in;
		String url = null;
		try {
			in = new FileInputStream(file);
			String fileName=uploadfile.getOriginalFileName();
			byte buffer[]=new byte[1024];  
		    //写入服务器端固定路径磁盘中  
		    String s = "F:\\eclipse\\inequality-sign-server\\WebRoot\\upload\\"+fileName;
		    FileOutputStream out= new FileOutputStream(s);  
		    int len=in.read(buffer, 0, 1024);  

		    //把流里的信息循环读入到file文件中  
		    while( len!=-1 ){  
		        System.out.println(len+"----------");  
		        out.write(buffer, 0, len);  
		        len=in.read(buffer, 0, 1024);  
		        }  
		      
		    out.close();
		    in.close();
		    url = FILEURL + fileName;
		    System.out.println(url);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	
}
