package com.demo.push;

public class AndroidUnicast extends AndroidNotification {
	
	public AndroidUnicast(String appkey,String appMasterSecret) throws Exception {
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "unicast");
//			单播(unicast): 向指定的设备发送消息，包括向单个device_token或者单个alias发消息。
//			列播(listcast): 向指定的一批设备发送消息，包括向多个device_token或者多个alias发消息。
//			广播(broadcast): 向安装该App的所有设备发送消息。
//			组播(groupcast): 向满足特定条件的设备集合发送消息，例如: "特定版本"、"特定地域"等。友盟消息推送所支持的维度筛选和友盟统计分析所提供的数据展示维度是一致的，后台数据也是打通的
//			文件播(filecast): 开发者将批量的device_token或者alias存放到文件, 通过文件ID进行消息发送。
//			自定义播(customizedcast): 开发者通过自有的alias进行推送, 可以针对单个或者一批alias进行推送，也可以将alias存放到文件进行发送。
	}
	
	public AndroidUnicast(String appkey,String appMasterSecret,String type) throws Exception {
		setAppMasterSecret(appMasterSecret);
		setPredefinedKeyValue("appkey", appkey);
		this.setPredefinedKeyValue("type", type);
//		单播(unicast): 向指定的设备发送消息，包括向单个device_token或者单个alias发消息。
//		列播(listcast): 向指定的一批设备发送消息，包括向多个device_token或者多个alias发消息。
//		广播(broadcast): 向安装该App的所有设备发送消息。
//		组播(groupcast): 向满足特定条件的设备集合发送消息，例如: "特定版本"、"特定地域"等。友盟消息推送所支持的维度筛选和友盟统计分析所提供的数据展示维度是一致的，后台数据也是打通的
//		文件播(filecast): 开发者将批量的device_token或者alias存放到文件, 通过文件ID进行消息发送。
//		自定义播(customizedcast): 开发者通过自有的alias进行推送, 可以针对单个或者一批alias进行推送，也可以将alias存放到文件进行发送。
}
	
	
	public void setDeviceToken(String token) throws Exception {
    	setPredefinedKeyValue("device_tokens", token);
    	System.out.println(rootJson.toString());
    }

}