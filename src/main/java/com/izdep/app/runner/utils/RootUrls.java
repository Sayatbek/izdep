package com.izdep.app.runner.utils;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RootUrls {

//    public static void main(String[] args) throws IOException {
//        Queue<String> queue = getRootList();
//        Document document = null;
//        for (String s: queue) {
//            System.out.print("URL: " + s);
//            document = JsoupHelper.parseURL(s);
//            if(document!=null) {
//                System.out.println(" is OK");
//            }else {
//                System.out.println(" ERROR");
//            }
//        }
//    }

    public static Queue<String> getRootList() {
        Queue<String> urlList = new LinkedList<>();
        urlList.add("http://www.qamshy.kz/");
        urlList.add("http://arhar.kz/");
        urlList.add("http://www.iitu.kz/?lang=kz");
        urlList.add("https://baribar.kz/");
        urlList.add("https://massaget.kz/");
        urlList.add("http://kerekinfo.kz/");
        urlList.add("http://aitaber.kz/");
        urlList.add("https://kulki.jimdo.com/");
        urlList.add("https://kaz.nur.kz/");
        urlList.add("http://el.kz/");
        urlList.add("http://alashainasy.kz/");
        urlList.add("http://kaztrk.kz/");
        urlList.add("http://abai.kz/");
        urlList.add("http://anatili.kazgazeta.kz/");
        urlList.add("https://kitap.kz/");
        urlList.add("http://kitaphana.kz/");
        urlList.add("http://45minut.kz/");
        urlList.add("http://saryarka-samaly.kz/");
        urlList.add("http://zhebe.com/");
        urlList.add("http://bilim-all.kz/");
        urlList.add("https://www.azattyq.org/");
        urlList.add("http://mukhanov.ucoz.kz/");
        urlList.add("http://www.inform.kz/kaz");
        urlList.add("http://balalaralemi.kz/");
        urlList.add("http://ustaz.kz/");
        urlList.add("http://bilimdiler.kz/");
        urlList.add("http://sabaq.kz/");
        urlList.add("https://sabaqtar.kz/");
        urlList.add("http://www.metodist.kz/");
        urlList.add("http://portfoliolar.kz/");
        urlList.add("http://www.foliant.kz/kk/");
        urlList.add("http://www.physic.kz/");
        urlList.add("http://dostykbilim.kz/");
        urlList.add("http://bilimpaz.kz/");
        urlList.add("http://ped.kz/%20");
        urlList.add("https://cdo.kz/kz");
        urlList.add("http://e-history.kz/kz");
        urlList.add("http://today.kz/kz/");
        urlList.add("https://bnews.kz/kz");
        urlList.add("https://vse.kz/");
        urlList.add("https://baq.kz/kk");
        urlList.add("http://zhasorken.kz/");
        urlList.add("http://it-tirlik.kz/");
        urlList.add("https://www.qazaquni.kz/");
        urlList.add("http://mz.gov.kz/");
        urlList.add("http://www.mfa.kz/");
        urlList.add("http://www.edu.gov.kz/kz/");
        urlList.add("http://www.enbek.kz/kk");
        urlList.add("http://www.minfin.gov.kz/irj/portal/anonymous");
        urlList.add("http://egov.kz/cms/kk");
        urlList.add("http://www.akorda.kz/kz");
        urlList.add("http://www.parlam.kz/kk");
        urlList.add("http://www.government.kz/kz/");
        urlList.add("http://www.dari.kz/category/mainpage");
        urlList.add("http://prokuror.kz/kaz");
        urlList.add("https://www.election.gov.kz/kaz/");
        urlList.add("http://www.knb.kz/kk?q=/");
        urlList.add("http://elorda.info/kk");
        urlList.add("https://www.almaty.gov.kz/page.php?page_id=3143");
        urlList.add("http://kostanay.gov.kz/kz/");
        urlList.add("https://e-kyzylorda.gov.kz/?q=kk");
        urlList.add("http://www.akimvko.gov.kz/kz/");
        urlList.add("http://aktobe.gov.kz/");
        urlList.add("http://sko.gov.kz/");
        urlList.add("http://ontustik.gov.kz/kk");
        urlList.add("https://egemen.kz/");
        urlList.add("http://aikyn.kz/");
        urlList.add("http://anatili.kazgazeta.kz/");
        urlList.add("http://anatili.kazgazeta.kz/");
        urlList.add("https://turkystan.kz/public/");
        urlList.add("http://kurmet-records.kz/kz");
        urlList.add("https://www.juldizdar.kz/");
        return urlList;
    }
}
