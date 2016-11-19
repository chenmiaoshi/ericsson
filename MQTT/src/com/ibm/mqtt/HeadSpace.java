package com.ibm.mqtt;

import java.util.HashMap;
import java.util.Map;

public class HeadSpace {
	
	public static void main(String[] arg){
		
		Map<String,String> map = new HashMap<String,String>();
		for(int i=0;i<100000;i++){
			String name = "";
			map.put("i", "ÄãºÃ");
			//name += name + "ÄãºÃ£¡" + i;
		}
		//System.out.println(name);
	}

}
