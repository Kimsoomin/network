package kr.mintech.network;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    public TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("로깅", "로깅");

        mTextView = (TextView) findViewById(R.id.jsonData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void sendData(View view) {
        String url = "https://api.forecast.io/forecast/7cb42b713cdf319a3ae7717a03f36e41/37.517365,127.026112";
        new DownloadJson().execute(url);
    }

    private class DownloadJson extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //MainThread : 백그라운드 작업을 시작하기 이전에 "뷰작업"을 해라
            //프로그레스바를 보여준다
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                return (String) getData((String) arg0[0]);
            } catch (Exception e) {
                return "Json download failed";
            }
        }


        protected void onPostExecute(String result) {
            mTextView.setText(result);
            //백그라운드 작업이 완료되었을 때
            try {
                Log.e("try", "진입은 하니?");

                JSONObject jsonResult = new JSONObject(result.toString());
                JSONObject dailyObject = jsonResult.getJSONObject("daily");
                JSONArray dataArray = dailyObject.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject obj = dataArray.getJSONObject(i);
                    String summary = obj.getString("summary");
                    Log.e("test", "test:" + summary);

                }

            } catch (JSONException e) {
                Log.e("catch", "catch진입");
                e.printStackTrace();
            }

            //뷰처리 프로그레스바 안보임 /
            // =============================================
//            try {
//                Log.e("try", "진입은 하니?");
//                JSONObject obj = new JSONObject(result.toString());
//                JSONArray array = obj.getJSONArray("city");
//                Log.d("test","city"+array.toString());
//
//            } catch (JSONException e) {
//                Log.e("catch", "catch진입");
//                e.printStackTrace();
//            }

            //=================================================
//            try {
//                JSONArray jArray = new JSONArray(result);
//
//                String[] jsonName = {"name", "age", "exam"};
//                String[][] parsedData = new String[jArray.length()][jsonName.length];
//
//                JSONObject json = null;
//                for (int i = 0; i < jArray.length(); i++) {
//                    json = jArray.getJSONObject(i);
//                    if (json != null) {
//                        for (int j = 0; j < jsonName.length; j++) {
//                            parsedData[i][j] = json.getString(jsonName[j]);
//                        }
//                    }
//                }
//
//                for (int i = 0; i < parsedData.length; i++) {
//                    Log.i("mini", "name" + i + ":" + parsedData[i][0]);
//                    Log.i("mini", "age" + i + ":" + parsedData[i][1]);
//                    Log.i("mini", "exam" + i + ":" + parsedData[i][2]);
//                    Log.i("mini", "----------------------------------");
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
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
}
