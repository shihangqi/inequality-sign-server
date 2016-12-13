package com.demo.push;

public class Push{
	private String appkey;
	private String appMasterSecret;
	private PushClient client = new PushClient();
	
	
	public void sendAndroidUnicast(String token) throws Exception {
		AndroidUnicast unicast = new AndroidUnicast("58477e1b310c936be50003db","h1cjmlrvva0ctdtdoly337a0iimbcnbo");
		// TODO Set your device token
		unicast.setTicker("不等号APP提醒");
		unicast.setTitle("友情提示：");
		unicast.setText("您所预定的号码已被叫号！请迅速赶往前台办理业务");
		unicast.setDescription("不等号APP！！！！");
		unicast.goAppAfterOpen();
		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		unicast.setProductionMode();
		// Set customized fields
		unicast.setExtraField("test", "helloworld");
		unicast.setDeviceToken(token);
//		"device_tokens":"xx",    可选 设备唯一表示
//       当type=unicast时,必填, 表示指定的单个设备
//        当type=listcast时,必填,要求不超过500个,
//        多个device_token以英文逗号间隔
		client.send(unicast);
	}
	
	public void sendAndroidUnicastS(String token,int now) throws Exception {
		AndroidUnicast unicast = new AndroidUnicast("58477e1b310c936be50003db","h1cjmlrvva0ctdtdoly337a0iimbcnbo","listcast");
		// TODO Set your device token
		unicast.setTicker("不等号APP提醒");
		unicast.setTitle("友情提示：");
		unicast.setPlaySound(false);
		unicast.setPlayLights(false);
		unicast.setPlayVibrate(false);
		unicast.setText("您所排的队列，当前已到"+now+"号");
		unicast.setDescription("不等号APP！！！！");
		unicast.goAppAfterOpen();
		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		unicast.setProductionMode();
		// Set customized fields
		unicast.setExtraField("test", "helloworld");
		unicast.setDeviceToken(token);
//		"device_tokens":"xx",    可选 设备唯一表示
//       当type=unicast时,必填, 表示指定的单个设备
//        当type=listcast时,必填,要求不超过500个,
//        多个device_token以英文逗号间隔
		client.send(unicast);
	}
	
	
}
