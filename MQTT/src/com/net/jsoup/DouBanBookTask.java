package com.net.jsoup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
 
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 抓取
 * @time 2015年7月6日 下午5:40:15
 * @author CheWenliang
 */
public class DouBanBookTask extends Thread{
     
    public String url;
    public String tagName;
    public String fileName;
     
    @Override
    public void run() {
        try {
            String url_ = "";
            //0-100页  根据自己的需要页数可以多设一点
            for(int i = 0 ;i < 101 ; i++){
                int start = i * Integer.valueOf(DouBanBook.Constants.每页条数.getValue());
                if(start > 0){
                    url_ = url + DouBanBook.Constants.分页信息.getValue() + start;
                }else{
                    url_ = url;
                }
//              URL paseUrl = new URL(url);
//              //解析URL,设置连接超时的时间
//              Document document = Jsoup.parse(paseUrl, 30000);
                Document document = Jsoup.connect(url_).get();
                        /*.userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) "
                                + "AppleWebKit/537.36 (KHTML, like Gecko) "
                                + "Chrome/42.0.2311.152 Safari/537.36").get();*/
                Elements dl = document.select("dl");
                Iterator<Element> dlIter = dl.iterator();
                while(dlIter.hasNext()){
                    try {
                        Element dl_x = dlIter.next();
                        Elements titleElement = dl_x.select("a.title");
                        String title = titleElement.text();
                        Elements pointElement = dl_x.select("span.rating_nums");
                        String pointString = pointElement.text();
                        if(!StringUtil.isBlank(pointString)){
                            Double point = Double.valueOf(pointString);
                            System.out.println(title+"\t"+point);
                            if(point > 9) writeToFile("E:/doubanbook",title,point);
                            System.out.println("title=" + title + "  point=" + point);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 写入本地文件
     * @param writeFolder
     * @param title
     * @param point
     * @throws Exception
     */
    public void writeToFile(String writeFolder,String title,Double point) throws Exception{
        File folder = new File(writeFolder);
        if(!folder.exists()){
            folder.mkdir();
        }
        File file = new File(writeFolder+"/"+fileName+".txt");
        String line = title + "\t" +point+"\n";
        BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
        bw.write(line);
        bw.flush();
        bw.close();
    }
 
    public String getUrl() {
        return url;
    }
 
    public void setUrl(String url) {
        this.url = url;
    }
 
    public String getTagName() {
        return tagName;
    }
 
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
 
    public String getFileName() {
        return fileName;
    }
 
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
     
}