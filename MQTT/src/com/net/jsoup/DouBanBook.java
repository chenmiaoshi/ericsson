package com.net.jsoup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * ����������з���ĸ߷��鼮
 * @time 2015��7��6�� ����5:43:58
 * @author CheWenliang
 */
public class DouBanBook {
     
    /**
     * ö�ٳ���
     * @time 2015��7��8�� ����4:39:31
     * @author CheWenliang
     */
    public enum Constants{
         
        ��ǩҳ("http://book.douban.com/tag/?icn=index-nav"),
        ��ϸҳ("http://www.douban.com/tag/:type/book"),
        ÿҳ����("15"),
        ��ҳ��Ϣ("?start=");
         
         private String name;
          
         private Constants(String name){
          this.name = name;
         }
          
         public String getValue(){
          return name;
         }
    }
     
    public static void main(String[] args) {
        List<String> tagLists = getAllType();
        getAllHighPointBook(tagLists);
    }
    /*�õ����еı�ǩ*/
    private static List<String> getAllType(){
        List<String> tagLists = new ArrayList<String>();
        try {
            URL paseUrl = new URL(Constants.��ǩҳ.getValue());
            //����URL,�������ӳ�ʱ��ʱ��
            Document document = Jsoup.parse(paseUrl, 30000);
            Elements allTag = document.select("table.tagCol");
            Iterator<Element> tags = allTag.iterator();
            while(tags.hasNext()){
                Element tag = tags.next();
                Elements href = tag.select("a");
                Iterator<Element> hrefs = href.iterator();
                while(hrefs.hasNext()){
                    Element hrefE = hrefs.next();
                    tagLists.add(hrefE.text());
                    System.out.println(hrefE.text());
                   // wirteTagToFile("E:/doubanbook", hrefE.text(), "��������ͼ���ǩ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tagLists;
    }
    /**
     * ��ȡ�߷��鼮��д���ļ�
     * @param tagLists
     */
    private static  void getAllHighPointBook(List<String> tagLists){
        ThreadPoolExecutor executor = ThreadPool.getInstance();
        try {
            for(String tagName : tagLists){
                String url = Constants.��ϸҳ.getValue().replaceAll(":type", tagName);
                DouBanBookTask task = new DouBanBookTask();
                task.setUrl(url);
                task.setFileName(tagName);
                tagName = URLEncoder.encode(tagName,"utf-8");
                task.setTagName(tagName);
                executor.execute(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * �����е�ͼ�����д���ļ�
     * @param writeFolder
     * @param title
     * @param tagName
     * @throws IOException
     */
    public void wirteTagToFile(String writeFolder,String title,String tagName) throws IOException{
        File folder = new File(writeFolder);
        if(!folder.exists()){
            folder.mkdir();
        }
        File file = new File(writeFolder+"/"+tagName+".txt");
        String line = title + "\n";
        BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
        bw.write(line);
        bw.flush();
        bw.close();
    }
}