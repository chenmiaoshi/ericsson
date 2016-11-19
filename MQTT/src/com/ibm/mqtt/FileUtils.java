package com.ibm.mqtt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils {

	public static ArrayList<File> getFileList(File file){
		ArrayList<File> list = new ArrayList<File>();
		if(!file.isDirectory()){
			list.add(file);
		}else{
			File[] fileList = file.listFiles();
			for(int i=0, size=fileList.length; i < size; i ++){
				list.add(fileList[i]);
			}
		}
		return list;
	}
	
	public static void write(String content, String path)
	{
		FileWriter writer = null;
		try
		{
			File file = new File(path);
			writer = new FileWriter(file,true);
			writer.write("\n" + content);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				if(writer != null)
					writer.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		//getFileList(new File("F:\\桌面文档\\爱立信\\File\\test01"));
		write("test","F:\\桌面文档\\爱立信\\File\\test01");
	}

}
