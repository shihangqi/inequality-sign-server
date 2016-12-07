package com.demo.push;

public class Push{
	private String appkey;
	private String appMasterSecret;
	private PushClient client = new PushClient();
	
	
	public void sendAndroidUnicast() throws Exception {
		AndroidUnicast unicast = new AndroidUnicast("58477e1b310c936be50003db","h1cjmlrvva0ctdtdoly337a0iimbcnbo");
		// TODO Set your device token
		unicast.setTicker( "这是一个标题");
		unicast.setTitle(  "不等号");
		unicast.setText(   "不等号App");
		unicast.setPlayLights(true);
		unicast.setPlaySound(true);
		unicast.setPlayVibrate(true);
		unicast.setDescription("不等号APP！！！！");
		unicast.goAppAfterOpen();
		unicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		unicast.setProductionMode();
		// Set customized fields
		unicast.setExtraField("test", "helloworld");
		unicast.setDeviceToken( "Ap1QhU2l9SL-sL_rlygx0AV9tmHzTACX-6TaTPsmW4ii");
		client.send(unicast);
	}
}
