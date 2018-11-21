package org.esb.test.httpclient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.esb.server.BaseServer;
import org.esb.server.Server;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * Created by 枫 on 2014/8/4.
 */
@Component
@Server
public class HttpClientServer extends BaseServer {
    @Override
    public void run() throws Exception {
        System.out.println("!!!!");
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setCookiePolicy(
                CookiePolicy.BROWSER_COMPATIBILITY);

        GetMethod get = new GetMethod("http://www.ip-adress.com/proxy_list/?k=time&d=desc");
        get.setRequestHeader("Host","www.ip-adress.com");
        get.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
        get.setRequestHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        get.setRequestHeader("Referer","http://www.ip-adress.com/proxy_list/?k=time&d=desc");
        int code = httpClient.executeMethod(get);
        if (code == 200) {
            String html = get.getResponseBodyAsString();
            Document doc = Jsoup.parse(html);
            Elements table =  doc.select("table.proxylist");
            System.out.println(table.size());
        }else {
            System.out.println(code);
        }
    }
}
