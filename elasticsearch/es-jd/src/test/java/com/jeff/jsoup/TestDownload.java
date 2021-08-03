package com.jeff.jsoup;

import com.jeff.esjd.config.ESJDConst;
import com.jeff.vo.JDGoods;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class  TestDownload{
    public static void main(String[] args) throws Exception {
        String keyWrod = "java";
        Document docment = Jsoup.parse(new URL(ESJDConst.FETCH_URL + keyWrod), 5000);

        Element j_goodsList = docment.getElementById("J_goodsList");
        Elements elements = j_goodsList.getElementsByTag("li");
        ArrayList<JDGoods> jdGoods = new ArrayList<>();

        for (Element element : elements) {
//            "p-price"
            String img = element.getElementsByClass("p-img").eq(0).get(0).getElementsByClass("p-img").get(0).getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = element.getElementsByClass("p-price").eq(0).text();
            String name = element.getElementsByClass("p-name").eq(0).text();
           jdGoods.add(new JDGoods(name,img,price));
        }
        System.out.println(jdGoods);


//        renamefile();
    }

    private static void renamefile() {
        File dir = new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
            String fileName = file.getName();
            String newName = fileName.substring(0,6);
            newName+=".mp4";
            System.out.println(newName);
           file.renameTo(new File(newName));
        }
    }

    private static String path = "D:\\zzhDownloadFiles\\xunlei\\sdsdsds\\大江大河";


}
