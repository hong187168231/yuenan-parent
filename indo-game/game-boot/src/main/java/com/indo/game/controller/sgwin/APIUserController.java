package com.indo.game.controller.sgwin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.indo.game.common.util.SgwinEncrypt;

public class APIUserController {
  private HttpURLConnection conn;
  private String root;
  private String username;
  private String hashValue;
  private String urlLogin;
  private String urlLogout;
  private String urlTransaction;
  private String urlconfirm;
  private String urlAccount;
  private String urlOnline;
  private String urlLastBets;
  private String urlBets;
  private String postParams;
  private String apiKey;


  public void init() {
    String website = "http://rd.localhost:8080";
    root = "rd";
    username = "12345";
    apiKey = "abc";


    urlLogin = website + "/api/login";
    urlLogout = website + "/api/logout";
    urlTransaction = website + "/api/transaction";
    urlconfirm = website + "/api/confirm";
    urlAccount = website + "/api/account";
    urlOnline = website + "/api/online";
    urlLastBets = website + "/api/lastbets";
    urlBets = website + "/api/bets";

    String rawData = "root=" + root + "&username=" + username + "&" + apiKey;
    String hashValue = this.calcuHashValue(rawData);
    System.out.println(hashValue);
    postParams = "root=" + root + "&username=" + username + "&hash=" + hashValue;


  }


  public static void main(String[] args) throws Exception {

    APIUserController apiUser = new APIUserController();
    apiUser.init();

    // apiUser.login();
    //
    // apiUser.logout();

    // transaction and confirm
    // String confirm=apiUser.transaction(16);
    // System.out.println(confirm.substring(22,40));
    // apiUser.confirm(confirm.substring(22,40));

    // apiUser.account();

    // apiUser.online();


    // because company rd no data,to test bet, need change root=wt
    apiUser.lastbets();
    apiUser.bets();


  }

  public String login() throws Exception {

    String response = this.sendPost(urlLogin, postParams);

    return response;
  }

  public String logout() throws Exception {

    String response = this.sendPost(urlLogout, postParams);
    return response;
  }

  public String transaction(Integer money) throws Exception {
    // postParams = "root=rd&username=12345&amount=16&hash=92f9aed48fb1966881e42d8cd779f669";

    String rawData = "amount=" + money + "&root=" + root + "&username=" + username + "&" + apiKey;
    String hashValue = this.calcuHashValue(rawData);
    System.out.println(hashValue);
    postParams =
        "root=" + root + "&username=" + username + "&amount=" + money + "&hash=" + hashValue;

    String response = this.sendPost(urlTransaction, postParams);
    return response;
  }

  public String confirm(String transactionID) throws Exception {
    // this hash value base on
    StringBuilder rawData = new StringBuilder();
    rawData.append("id=");
    rawData.append(transactionID);
    rawData.append("&root=");
    rawData.append(root);
    rawData.append("&");
    rawData.append(apiKey);

    postParams = "root=" + root + "&hash=" + this.calcuHashValue(rawData.toString()) + "&id="
        + transactionID;

    String response = this.sendPost(urlconfirm, postParams);
    return response;
  }

  public String calcuHashValue(String rawData) {
    return SgwinEncrypt.md5(rawData);
  }

  public String account() throws Exception {

    String response = this.sendPost(urlAccount, postParams);
    return response;
  }

  public String online() throws Exception {

    String response = this.sendPost(urlOnline, postParams);
    return response;
  }

  public String lastbets() throws Exception {
    // calculate hash base on String rawData="root=wt&abc";
    // postParams = "root=wt&hash=8125af3e80775ad16b490461a8c0c9f7";

    postParams = "root=wt&hash=8125af3e80775ad16b490461a8c0c9f7";
    String response = this.sendPost(urlLastBets, postParams);
    return response;
  }

  public String bets() throws Exception {
    // caculate hash base on String rawData="root=wt&abc";
    postParams = "root=wt&hash=8125af3e80775ad16b490461a8c0c9f7";

    String response = this.sendPost(urlBets, postParams);
    return response;
  }



  private String sendPost(String url, String postParams) throws Exception {

    URL obj = new URL(url);
    conn = (HttpURLConnection) obj.openConnection();

    // Acts like a browser to post
    conn.setRequestMethod("POST");

    conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

    conn.setDoOutput(true);
    conn.setDoInput(true);

    // Send post request
    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
    wr.writeBytes(postParams);
    wr.flush();
    wr.close();

    int responseCode = conn.getResponseCode();
    System.out.println("\nSending 'POST' request to URL : " + url);
    System.out.println("Post parameters : " + postParams);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    System.out.println(response.toString());
    return response.toString();
  }



  public String getRoot() {
    return root;
  }

  public void setRoot(String root) {
    this.root = root;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getHashValue() {
    return hashValue;
  }

  public void setHashValue(String hashValue) {
    this.hashValue = hashValue;
  }

}
