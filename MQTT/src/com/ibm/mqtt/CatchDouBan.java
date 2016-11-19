package com.ibm.mqtt;

 
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 

public class CatchDouBan {  
	  
    private CatchDouBan() {  
  
    }  
    
    //��Ŷ�ȡ����������
    List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
     
    /** 
     * ÿҳ��ʾ��¼���� 
     */  
    public static final int NUM = 20;  
  
    /** 
     * ƴ�ӷ�ҳ 
     */  
    public static final String START = "&start=";  
    
  
    private static final CatchDouBan instance = new CatchDouBan();  
  
    public static CatchDouBan getInstance() {  
        return instance;  
    }  
    
    public void createExcel() {
    	try {  
            WritableWorkbook book  =  Workbook.createWorkbook( new  File( "D:\\ǰ100��������ߵ���.xls" ));
            WritableSheet sheet  =  book.createSheet("��һҳ ", 0 );
            Label num =  new Label(0, 0,"���");
            sheet.addCell(num);
            Label title = new Label(1, 0,"����");
            sheet.addCell(title);
            Label sorce = new Label(2, 0,"����");
            sheet.addCell(sorce);
            Label people = new Label(3, 0, "��������");
            sheet.addCell(people);
            Label author = new Label(4, 0, "����");
            sheet.addCell(author);
            Label press = new Label(5, 0,"������");
            sheet.addCell(press);
            Label date = new Label(6, 0,"��������");
            sheet.addCell(date);
            Label price = new Label(7, 0, "�۸�");
            sheet.addCell(price);
            
            for(int i=0; i<(list.size()>100?100:list.size());i++) {
            	 Label lNum = new Label(0, i+1, i+1+"");
                 sheet.addCell(lNum);
                 Label lName = new Label(1, i+1, list.get(i).get("sTitle"));
                 sheet.addCell(lName);
                 Label lSorce = new Label(2, i+1, list.get(i).get("eleSorce"));
                 sheet.addCell(lSorce);
                 Label lPeople = new Label(3, i+1, list.get(i).get("sPeople"));
                 sheet.addCell(lPeople);
                 Label lAuthor = new Label(4, i+1, list.get(i).get("sAuthor"));
                 sheet.addCell(lAuthor);
                 Label lPress = new Label(5, i+1, list.get(i).get("sPress"));
                 sheet.addCell(lPress);
                 Label lDate = new Label(6, i+1, list.get(i).get("sDate"));
                 sheet.addCell(lDate);
                 Label lPrice = new Label(7, i+1, list.get(i).get("sPrice"));
                 sheet.addCell(lPrice);
            }
            
            
            book.write();
            book.close();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
      
    public void getDoubanReview(String urlold){  
        try {           
        	
            
            int i = 0;
        	int j = 0;
            while(j < 100){
                String url = urlold + START + String.valueOf(i * NUM);  
                System.out.println(url);  
                Connection connection = Jsoup.connect(url);  
                Document document = connection.get();  
                Elements subject = document.select("li.subject-item"); 
                System.out.println(subject);
                if(subject.size() == 0) {
                	break;
                }
                Iterator<Element> ulIter = subject.iterator();  
                while (ulIter.hasNext()) {  
                	HashMap<String,String> map = new HashMap<String,String>();
                    Element element = ulIter.next();  
                    Elements eleInfo = element.select("div.info"); 
                    Element eleTitle = eleInfo.select("a").first();            
                    String sTitle = eleTitle.html().replaceAll("<[^>]*>", "");   //����
                    String eleSorce = eleInfo.select(".rating_nums").size() != 0 ?eleInfo.select(".rating_nums").first().text() : "";   //����
                    String sPeople = eleInfo.select(".pl").text();  //��������
                    String regEx="[^0-9]";   
                    Pattern p = Pattern.compile(regEx);   
                    Matcher m = p.matcher(sPeople);   
                    sPeople = m.replaceAll("").trim();
                    
                   if(Integer.parseInt(sPeople) <1000) {
                    	continue;
                    }
                    
                    String[] array = eleInfo.select(".pub").text().split("/");
                    String sAuthor = array[0];             //����
                    String sPrice = array[array.length-1];  //�۸�
                    String sDate = array[array.length-2];  //��������
                    String sPress = array[array.length-3];  //������
                    
                    j++;

                    map.put("mNum", j+"");
                    map.put("sTitle", sTitle);
                    map.put("eleSorce", eleSorce);
                    map.put("sPeople", sPeople);
                    map.put("sAuthor", sAuthor);
                    map.put("sPress", sPress);
                    map.put("sDate", sDate);
                    map.put("sPrice", sPrice);
                    
                    //ȥ��
                    Boolean flag = true;
                    for(int k=0;k<list.size();k++) {
                    	if(sTitle.equals(list.get(k).get("sTitle"))) {
                    		flag = false;
                    	}
                    }
                    if(flag) {
                    	list.add(map);                    
                    }                   
                    //����
                    Collections.sort(list, new Comparator<HashMap<String,String>>(){  
                    	  
                        /*   
                         * ���ظ�����ʾ��o1 ����o2��  
                         * ����0 ��ʾ��o1��o2��ȣ�  
                         * ����������ʾ��o1С��o2��  
                         */  

						public int compare(HashMap<String, String> o1,HashMap<String, String> o2) {
							//�������ֽ��н�������  
							if(Double.parseDouble(o1.get("eleSorce")) > Double.parseDouble(o2.get("eleSorce"))) {
								return -1;
							}else if (Double.parseDouble(o1.get("eleSorce")) == Double.parseDouble(o2.get("eleSorce"))) {
								return 0;
							} else
							return 1;
						}  
                    });                  
                }
                i++;
            }    
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    

    
    public static void main(String[] args) {
    	CatchDouBan ju = CatchDouBan.getInstance();  
        ju.getDoubanReview("https://book.douban.com/tag/������?type=S"); 
        ju.getDoubanReview("https://book.douban.com/tag/���?type=S"); 
        ju.getDoubanReview("https://book.douban.com/tag/�㷨?type=S"); 
        
       // ju.createExcel();
  	}
      
}  