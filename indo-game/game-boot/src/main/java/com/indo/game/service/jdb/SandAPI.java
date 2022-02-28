package com.indo.game.service.jdb;
import com.alibaba.fastjson.JSONObject;
import com.indo.common.config.OpenAPIProperties;
import com.indo.game.common.util.JDBAESEncrypt;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.util.ArrayList;
import java.util.List;

public class SandAPI {
    public static void main(String[] args) throws Exception {
        String dc = OpenAPIProperties.JDB_DC;
        String key = OpenAPIProperties.JDB_KEY;
        String iv = OpenAPIProperties.JDB_IV;
        String apiUrl = OpenAPIProperties.JDB_API_URL;
        // get a client
        CloseableHttpClient client = HttpClientBuilder.create().build();
        // prepare action 47 data
        JSONObject data = new JSONObject();
        data.put("action", 47);
        data.put("ts", System.currentTimeMillis());
        data.put("lang", "en");
        data.put("gType", "0");
        data.put("mType", "8001");
        data.put("windowMode", "2");
        // encrypt
        String x = JDBAESEncrypt.encrypt(data.toString(), key, iv);
        // build request
        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("dc", dc));
        paramList.add(new BasicNameValuePair("x", x));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "UTF-8");
        HttpPost httpPost = new HttpPost(apiUrl);
        httpPost.setEntity(entity);
        // gzip
        // httpPost.setHeader("Accept-Encoding", "gzip");
        HttpEntity httpEntity = null;
        try {
            // Resolve response
            HttpResponse response = client.execute(httpPost);
            httpEntity = response.getEntity();
            System.out.println(EntityUtils.toString(httpEntity));
        } finally {
            EntityUtils.consume(httpEntity);
        }
    }

}
