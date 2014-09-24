package com.example.procon2;

import java.util.List;
import org.msgpack.annotation.MessagePackMessage;


/********************************************************************/
/*DTN�ʐM�ƃ`���b�g�����������Q*/
/*ID�ԍ��̐U�蕪���ɂ���Ăǂ̋@�\�ɓn�������߂�*/

/********************************************************************/
@MessagePackMessage
public class MessageInfo{
	public int id;                       
	public String[] deviceName;  
	public String[] deviceIP;    
	public String[] chatMessage; 
	public String[] time;       
	public String[] hash;      
	public String[] latitude;  
	public String[] longitude;
	public boolean  isMoving;
	
	public MessageInfo(){

	}
	
	public MessageInfo(int id,List<String> deviceName,List<String> deviceIP,List<String> chatMessage,List<String> time,List<String> hash,List<String> latitude,List<String> longitude, boolean isMoving){
		this.id = id;
		this.deviceName  = (String[])deviceName.toArray(new String[0]);
		this.deviceIP    = (String[])deviceIP.toArray(new String[0]);
		this.chatMessage = (String[])chatMessage.toArray(new String[0]);
		this.time        = (String[])time.toArray(new String[0]);
		this.hash        = (String[])hash.toArray(new String[0]);
		this.latitude    = (String[])latitude.toArray(new String[0]);
		this.longitude   = (String[])longitude.toArray(new String[0]);
		this.isMoving    = isMoving;
	}
}
