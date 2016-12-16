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
		JSONObject json = new JSONObject();

		List<Ordernum> list = Ordernum.dao.find("select * from ordernum where shop_id=" + shop_id);
		Ordernum on = list.get(0);
		switch (type) {
		case "1":
			// 已取号
			all = on.getAllType1() + 1;
			// 当前号
			now = on.getNowType1();
			on.setAllType1(all);
			break;
		case "2":
			all = on.getAllType2() + 1;
			now = on.getNowType2();
			on.setAllType2(all);
			break;
		case "3":
			all = on.getAllType3() + 1;
			now = on.getNowType3();
			on.setAllType3(all);
			System.out.println("------------");
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
		return json;
	}
}
