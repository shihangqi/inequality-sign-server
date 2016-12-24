package com.demo.logic;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.common.model.Ordernum;
import com.demo.common.model.Ordertab;
import com.demo.common.model.Shop;
import com.demo.push.Push;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class Client_logic {
	
	private String user_id = null;
	private String shop_id = null;
	private String type = null;
	
	public Client_logic(String user_id, String shop_id, String type) {
		super();
		this.user_id = user_id;
		this.shop_id = shop_id;
		this.type = type;
	}

	public JSONObject join(){
		List<Ordertab> l = Ordertab.dao.find("select * from ordertab where shop_id=" + "\""+shop_id+"\""+"and user_id = "+"\""+user_id+"\""+"and status = \'1\'" );
		if(user_id == null || shop_id == null || type == null){
			return(new JSONObject());
		}else if(!l.isEmpty()){
			try {
				return(new JSONObject().put("result", "fail"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int all = 0;
		int now;
		String name_type;
		JSONObject json = new JSONObject();

		List<Ordernum> list = Ordernum.dao.find("select * from ordernum where shop_id=" + shop_id);
		Ordernum on = list.get(0);
		switch (type) {
		case "1":
			name_type = on.getNameType1();
			// 已取号
			all = on.getAllType1() + 1;
			// 当前号
			now = on.getNowType1();
			on.setAllType1(all);
			break;
		case "2":
			name_type = on.getNameType2();
			all = on.getAllType2() + 1;
			now = on.getNowType2();
			on.setAllType2(all);
			break;
		case "3":
			name_type = on.getNameType3();
			all = on.getAllType3() + 1;
			now = on.getNowType3();
			on.setAllType3(all);
			break;
		default:
			return(new JSONObject());
		}
		if (all == 0) {
			return(new JSONObject());
		} else {
			on.update();

			Date date = new Date();
			String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			Timestamp goodsC_date = Timestamp.valueOf(nowTime);
			Ordertab ot = new Ordertab();
			ot.setUserId(Integer.parseInt(user_id));
			ot.setShopId(Integer.parseInt(shop_id));
			ot.setTime(goodsC_date);
			ot.setType(type);
			ot.setNum(all);
			ot.setStatus("1");
			ot.save();
		}
		List<Shop> shop = Shop.dao.find("select * from shop where id=" + shop_id);
		Shop s = shop.get(0);
		try {
			json.put("shop_name",s.getShopName());
			json.put("all", all);
			json.put("type", type);
			json.put("now",now);
			json.put("shop_address", s.getShopAddress());
			json.put("result", "success");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		PushController(all,name_type);
		return json;
	}
	
	private boolean PushController(int all,String name_type){
		List<Shop> list = Shop.dao.find("select * from shop where id ="+"\""+shop_id+"\"");
		List<Ordernum> list1 = Ordernum.dao.find("select * from ordernum where shop_id = "+"\""+shop_id +"\"");
		try {
			String token = list.get(0).getShopPushId();
			System.out.println(token);
			
			if(token.equals(null)){
				return false;
			}else{
				Push p = new Push();
				p.sendAndroidUnicast_server(token,all,type);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
	
}
