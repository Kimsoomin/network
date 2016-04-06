package kr.mintech.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    // Called when send button is clicked
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Called when send button is clicked
    public void sendData(View view) {

        // set the server URL
//        String url = "http://www.word.pe.kr/keyword/testJson.php";
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Seoul&mode=json&units=metric&cnt=7&APPID=20bca9bd534d04520d1a51a054f86e50";

        // call data from web URL
        try {
            ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conManager.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                new DownloadJson().execute(url);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "네트워크를 확인하세요", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadJson extends AsyncTask<String, String, String> {
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
            String time = "";

            try {
                Log.e("try", "진입은 하니?");

                JSONObject jsonresult = new JSONObject(result.toString());
                JSONObject city = jsonresult.getJSONObject("city");

                for (int i = 0; i < city.length(); i++) {
                    JSONArray name = city.getJSONArray("name");
                    String test = name.getString(i);
                    Log.e("mini", "test:" + test);
                }

//                for (int i = 0; i < name.length(); i++) {
//                    JSONObject object = name.getJSONObject(i);
//                    JSONArray  lon = object.getJSONArray("lon");
//                    String test = lon.getString(i);
//                    Log.e("mini", "test:" + test);
//                }

//                Log.d("obj", "obj:" + obj.toString());
//                JSONArray arr = obj.getJSONArray("country");
//                Log.d("arr", "arr:" + arr.toString());
//                for (int i = 0; i < arr.length(); i++) {
//                    time = arr.getString(i);
//                    Log.e("mini", "time:" + time);
//                }

            } catch (JSONException e) {
                Log.e("catch", "catch진입");
                e.printStackTrace();
            }
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
