package com.demo.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Attr;

import com.demo.common.model.Img;
import com.demo.common.model.Ordernum;
import com.demo.common.model.Ordertab;
import com.demo.common.model.Shop;
import com.demo.common.model.User;
import com.demo.push.Push;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

/**
 * UserController 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller
 * 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(UserInterceptor.class)
public class UserController extends Controller {
	public static final String FILEURL = "http://10.7.88.113:8090/upload/";
	public void index() {
		setAttr("userPage", User.dao.paginate(getParaToInt(0, 1), 10));
		render("user.html");
	}

	public void add() {
	}

//	@Before(UserValidator.class)
	public void save() {
		UploadFile files = getFile();
		
		// 拼接文件上传的完整路径
				String fileUrl = "http://" + this.getRequest().getRemoteAddr() + ":"
						+ this.getRequest().getLocalPort() + "/upload/"
						+ files.getFileName();
				
				System.out.println(fileUrl);

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
//		HttpServletRequest r = getRequest();
//		String username = r.getParameter("username");
//		List<User> user = User.dao.find("select * from user where user_tel" + "=" + "\"" + username + "\"");
//		System.out.println(user.size());
//		if (user.isEmpty()) {
//			// 用户名不存在
//			renderText("0");
//			System.out.println("0");
//		} else {
//			// 用户名存在
//			renderText("1");
//			System.out.println("1");
//			Push push = new Push();
//			try {
//				push.sendAndroidUnicast("Ap1QhU2l9SL-sL_rlygx0AV9tmHzTACX-6TaTPsmW4ii");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
        
	}

	public void login() {
		HttpServletRequest r = getRequest();
		String user_tel;
		String user_id;
		String push_id;
		push_id = r.getParameter("push_id");
		// 判断是第三方登录还是手机登录
		if (r.getParameter("user_tel") == null) {
			user_id = r.getParameter("user_id");
			System.out.println(user_id+"");
			List<User> userid = User.dao.find("select * from user where open_id" + "=" + "\"" + user_id + "\"");
//			 判断是否是新用户
			 if(userid.isEmpty()){
				 User u = new User();
				 u.setOpenId(user_id);
				 u.setUserName("");
				 u.setUserImg(FILEURL+"touxiang.jpg");
				 u.save();
			 }
			List<User> id = User.dao.find("select * from user where open_id" + "=" + "\"" + user_id + "\"");
			if (id.isEmpty()) {
				renderText("loginfail");
			} else {
				System.out.println(id.get(0).getId().toString());
				renderText(id.get(0).getId().toString());
				id.get(0).setPushId(push_id);
				id.get(0).update();
			}
		} else if (r.getParameter("user_id") == null) {
			user_tel = r.getParameter("user_tel");//
			System.out.println(user_tel);
			List<User> userid = User.dao.find("select * from user where user_tel" + "=" + "\"" + user_tel + "\"");
			// 判断是否是新用户
			if (userid.isEmpty()) {
				 User u = new User();
				 u.setUserTel(user_tel);
				 u.setUserName("");
				 u.setUserImg(FILEURL+"touxiang.jpg");
				 u.save();
			}

			List<User> id = User.dao.find("select * from user where user_tel" + "=" + "\"" + user_tel + "\"");
			if (id.isEmpty()) {
				renderText("loginfail");
			} else {
				renderText(id.get(0).getId().toString());
			}
			id.get(0).setPushId(push_id);
			id.get(0).update();

		} else {
			renderText("loginfail");
		}
	}

	public void getmessage(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		JSONObject json = new JSONObject();
		List<User> list = User.dao.find("select * from user where id" + "=" + "\"" + id + "\"");
		
		if (list.isEmpty()) {
			renderText(json.toString());
		} else {
			try {
				json.put("name", list.get(0).getUserName());
				json.put("sex", list.get(0).getUserSex());
				json.put("img", list.get(0).getUserImg());
				System.out.println(json.toString());
				renderText(json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	public void changeimg() {
		UploadFile uploadFile = this.getFile();
		String id = this.getPara("id");
		
		if(uploadFile == null || id == null){
			renderText("changeimgfail");
			System.out.println("1");
		}else{
			List<Img> l = Img.dao.find("select * from img where id =\'1\'");
			int filenum = l.get(0).getNum();
			boolean i = uploadFile.getFile().renameTo(new File("F:\\eclipse\\inequality-sign-server\\WebRoot\\upload\\"+"picture"+filenum+".jpg"));
			
			if(i){
				String url = FILEURL + "picture"+filenum+".jpg";
				List<User> list = User.dao.find("select * from user where id ="+"\""+id+"\"");
				list.get(0).setUserImg(url);
				l.get(0).setNum(filenum);
				boolean b = list.get(0).update();
				boolean b1 = l.get(0).update();
				if (b == true && b1 == true) {
					// 更新成功
					renderText(url);
					System.out.println("ok");
					filenum++;
				} else {
					// 更新失败
					filenum++;
					System.out.println("fail");
					renderText("changeimgfail");
				}
			}else{
				renderText("changeimgfail");
				filenum++;
				System.out.println("2");
			}
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
	
	public void order(){
		HttpServletRequest r = getRequest();
		String id = r.getParameter("id");
		JSONArray json = new JSONArray();
		List<Record> R = Db.find("select shop.id,shop_img_small,shop_name,num,status,type from shop,ordertab where shop.id = ordertab.shop_id and ordertab.user_id ="+"\'"+id+"\'"+"and status != \'2\' and status != \'3\'"+"order by time DESC");
		if (R.toString() == "[]" ||R == null){
			renderText("orderfail");
		} else {
			for (int i = 0; i < R.size(); i++) {
				json.put(R.get(i).getColumns());
			}
			renderText(json.toString());
			System.out.println(json.toString());
		}
	
	}
	public void deleteorder(){
		HttpServletRequest r = getRequest();
		String user_id = r.getParameter("user_id");
		String shop_id = r.getParameter("shop_id");
		String type = r.getParameter("type");
		String num = r.getParameter("num");
		List<Ordertab> list = Ordertab.dao.find("select * from ordertab where user_id = "+"\""+user_id+"\""+"and shop_id = "+"\""+shop_id+"\""+"and type ="+"\""+type+"\""+"and num ="+"\""+num+"\"");
		list.get(0).setStatus("3");
		boolean b = list.get(0).update();
		if(b == true){
			renderText("ok");
			System.out.println("ok");
		}else{
			renderText("fail");
			System.out.println("ok");
		}
	}
	public void cancelorder(){
		HttpServletRequest r = getRequest();
		String user_id = r.getParameter("user_id");
		String shop_id = r.getParameter("shop_id");
		String type = r.getParameter("type");
		String num = r.getParameter("num");
		List<Ordertab> list = Ordertab.dao.find("select * from ordertab where user_id = "+"\""+user_id+"\""+"and shop_id = "+"\""+shop_id+"\""+"and type ="+"\""+type+"\""+"and num ="+"\""+num+"\"");
		list.get(0).setStatus("2");
		boolean b = list.get(0).update();
		if(b == true){
			renderText("ok");
			System.out.println("ok");
		}else{
			renderText("fail");
			System.out.println("ok");
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
