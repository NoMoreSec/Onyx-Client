package net.onyx.client.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLUtils {


    public static boolean checkHWIDonURL(String urlRaw, String HWID) {

        try {
            URL url = new URL(urlRaw);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(HWID)) return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

}