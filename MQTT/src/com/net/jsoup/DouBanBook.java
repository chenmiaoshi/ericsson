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
 * 豆瓣读书所有分类的高分书籍
 * @time 2015年7月6日 下午5:43:58
 * @author CheWenliang
 */
public class DouBanBook {
     
    /**
     * 枚举常量
     * @time 2015年7月8日 下午4:39:31
     * @author CheWenliang
     */
    public enum Constants{
         
        标签页("http://book.douban.com/tag/?icn=index-nav"),
        详细页("http://www.douban.com/tag/:type/book"),
        每页条数("15"),
        分页信息("?start=");
         
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
    /*得到所有的标签*/
    private static List<String> getAllType(){
        List<String> tagLists = new ArrayList<String>();
        try {
            URL paseUrl = new URL(Constants.标签页.getValue());
            //解析URL,设置连接超时的时间
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
                   // wirteTagToFile("E:/doubanbook", hrefE.text(), "豆瓣所有图书标签");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tagLists;
    }
    /**
     * 获取高分书籍并写入文件
     * @param tagLists
     */
    private static  void getAllHighPointBook(List<String> tagLists){
        ThreadPoolExecutor executor = ThreadPool.getInstance();
        try {
            for(String tagName : tagLists){
                String url = Constants.详细页.getValue().replaceAll(":type", tagName);
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
     * 将所有的图书分类写入文件
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