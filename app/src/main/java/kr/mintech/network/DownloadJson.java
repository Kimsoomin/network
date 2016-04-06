package kr.mintech.network;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mac on 16. 4. 6..
 */

public class DownloadJson extends AsyncTask<String,String,String> {
    @Override
    protected String doInBackground(String... arg0) {
        try {
            return (String)getData((String) arg0[0]);
        } catch (Exception e) {
            return "Json download failed";
        }
    }

    protected void onPostExecute(String result) {
//        mTextView.setText(result);
    }

    private String getData(String strUrl) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedInputStream bis = null;
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            int responseCode;

            con.setConnectTimeout(3000);
            con.setReadTimeout(3000);

            responseCode = con.getResponseCode();

            if (responseCode == 200) {
                bis = new BufferedInputStream(con.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "UTF-8"));
                String line = null;

                while ((line = reader.readLine()) != null)
                    sb.append(line);

                bis.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
