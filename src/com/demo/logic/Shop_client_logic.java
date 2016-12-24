package com.demo.logic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.demo.common.model.Ordernum;
import com.demo.common.model.Ordertab;
import com.demo.common.model.Shop;
import com.demo.common.model.User;
import com.demo.push.Push;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class Shop_client_logic {
	private String shop_id = null;
	private String type = null;
	
	public Shop_client_logic(String shop_id, String type) {
		super();
		this.shop_id = shop_id;
		this.type = type;
	}
	
	/**
	 * return 0表示叫号失败，1代表叫号成功，2代表队列人数为0；
	 * @return
	 */
	public String click(){
		if(shop_id == null || type == null){
			return "0";
		}
		
		
		int now;
		int all;
		int num = 0;
		System.out.println(shop_id);
		System.out.println(type);
		List<Ordernum> list = Ordernum.dao.find("select * from ordernum where shop_id=" + shop_id);
		Ordernum on = list.get(0);
		if(type.equals("1")){
			num = on.getNowType1();
		}else if(type.equals("2")){
			num = on.getNowType2();
		}else if(type.equals("3")){
			num = on.getNowType3();
		}
		List<Ordertab> list1 = Ordertab.dao.find("select * from ordertab where shop_id=" +"\""+ shop_id+"\""+"and type ="+"\""+type+"\""+"and num ="+"\""+(num+1)+"\"");
		if(list1.toString() != "[]"){
			if(list1.get(0).getStatus().equals("2")||list1.get(0).getStatus().equals("3")){
				if(type.equals("1")){
					on.setNowType1(++num);
				}else if(type.equals("2")){
					on.setNowType2(++num);
				}else if(type.equals("3")){
					on.setNowType3(++num);
				}
				on.update();
				System.out.println("3");
				return "3";
			}else{
				switch (type) {
				case "1":
					now = on.getNowType1();
					all = on.getAllType1();

					if((all - now) <= 0){
						return "2";	
					}else{
						now = now + 1;
						on.setNowType1(now);
					}
					break;
				case "2":
					now = on.getNowType2();
					all = on.getAllType2();

					if((all - now) <= 0){
						return "2";
					}else{
						now = now + 1;
						on.setNowType2(now);
					}
					break;
				case "3":
					now = on.getNowType3();
					all = on.getAllType3();
					if((all - now) <= 0){
						return "2";
					}else{
						now = now + 1;
						on.setNowType3(now);
					}
					break;
				default:
					return "0";
				}
				on.update();
				if(PushController(now)){
					if(PushController(now,all)){
						
					}else{
						System.out.println("PushControllers");
						return "0";
					}
				}else{
					System.out.println("PushController");
					return "0";
					
				}
				if(StatusController(now,shop_id,type)){
					return "1";
				}else{
					System.out.println("StatusController");
					return "0";
				}
			}
		}else{
			return "2";
		}
	}
	private boolean StatusController(int now,String shop_id,String type) {
		// TODO Auto-generated method stub
		List<Ordertab> list = Ordertab.dao.find("select * from ordertab where shop_id=" + "\""+shop_id+"\""+"and type = "+"\""+type+"\""+"and num = "+"\""+now+"\""+"and status = \'1\'");
		list.get(0).setStatus("0");
		System.out.println(list.get(0).toString());
		return list.get(0).update();
	}

	private boolean PushController(int now){
		System.out.println(now);
		List<Record> R = Db.find("select push_id from user,ordertab where user.id = ordertab.user_id and shop_id = " + "\""+shop_id+"\"" +" and type = "+ "\""+type+"\"" +" and num = "+"\""+now+"\"" + "and status = \'1\'");
		if(R != null){
			String s = R.get(0).getColumns().toString();
			try {
				JSONObject json = new JSONObject(s);
				String token = json.getString("push_id");
				System.out.println(token);
				if(token.equals(null)){
					return false;
				}else{
					Push p = new Push();
					p.sendAndroidUnicast(token);
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}else{
			return false;
		}
		

	}
private boolean PushController(int now,int all){
		
		List<User> o = User.dao.find("select push_id from user,ordertab where user.id = ordertab.user_id and shop_id = " +"\""+shop_id+"\"" +" and type = "+ "\""+type+"\"" +" and num > "+"\""+now+"\"" + "and num <=" + "\""+all +"\""+ "and status = \'1\'");
		String s = "";
		if(o.size()>0){
			for (int i = 0; i < o.size(); i++) {
				if(i == o.size()-1)
					if(o.get(i).getPushId() == "" || o.get(i).getPushId() == null){
						
					}else
						s = s + o.get(i).getPushId();
				else
					if(o.get(i).getPushId() == "" || o.get(i).getPushId() == null){
						
					}else
						s = s + o.get(i).getPushId()+",";
			}
			Push p = new Push();
			try {
				p.sendAndroidUnicastS(s,now);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}else{
			return true;
		}
		
	}
}
