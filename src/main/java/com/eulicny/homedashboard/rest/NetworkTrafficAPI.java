package com.eulicny.homedashboard.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
public class NetworkTrafficAPI {
    private String CONST_ROUTERURL = "http://192.168.1.1/update.cgi";

    @RequestMapping("/api/network/current")
    public Map<String,Object> network() {
        Map<String,Object> model = new HashMap<String,Object>();
        try {
            Long epoch1    = System.currentTimeMillis();
            String result1 = retrieveDataFromRouter(CONST_ROUTERURL);
            Thread.sleep(10000);
            Long epoch2    = System.currentTimeMillis();
            String result2 = retrieveDataFromRouter(CONST_ROUTERURL);
            HashMap<String,String> result1Hash = parseResults(result1);
            HashMap<String,String> result2Hash = parseResults(result2);

           // Long rx        =
            model.put("hash1", result1Hash);
            model.put("hash2", result2Hash);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return model;
    }

    /**
     * Data is in the format
     *
     * netdev = {
     * 'WIRED':{rx:0x4796211,tx:0x8e9fb694}
     * ,'INTERNET':{rx:0x587856bd,tx:0xc4591155}
     * ,'BRIDGE':{rx:0xaad8e597,tx:0x3db7f63a}
     * ,'WIRELESS0':{rx:0x887599cc,tx:0x9cbed9}
     * }
     * @param input
     * @return
     */
    private HashMap<String,String> parseResults(String input) {
        String[] segments                           = input.split(",");
        HashMap<String,String> transmissionHash     = new HashMap<>();

        for(String segment : segments) {
            if(segment.contains("INTERNET")) {
                String[] dataChunks = segment.split(":");
                String rx = dataChunks[2];
                String tx = dataChunks[4];
                transmissionHash.put("rx", rx);
                transmissionHash.put("tx",tx);
            }
        }

        return transmissionHash;

    }

    private String retrieveDataFromRouter(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        String postBody = "output=netdev&_http_id=TIDe855a6487043d70a";
        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("Origin", "http://192.168.1.1");
        con.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
        con.setRequestProperty("Accept", "*/*");
        con.setRequestProperty("Referer", "http://192.168.1.1/Main_TrafficMonitor_realtime.asp");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        con.setRequestProperty("Cookie", "traffic_warning_NaN=2018.6:1; wireless_list_14:DD:A9:F2:75:8C_temp=<AC:37:43:4B:0A:57>Yes<B8:E8:56:35:B5:70>Yes<50:1A:C5:C5:27:DB>Yes<DC:68:EB:64:5C:95>Yes<C8:3A:6B:D9:EC:AE>Yes<18:B4:30:04:B5:EA>Yes<98:01:A7:26:32:C9>Yes<B8:27:EB:91:D7:F9>Yes; bw_rtab=INTERNET; asus_token=5511619979413794739867712182367; bw_24tab=INTERNET; bw_24refresh=1");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postBody);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + postBody);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        return response.toString();
    }
}
