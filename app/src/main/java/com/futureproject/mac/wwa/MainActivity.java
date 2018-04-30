package com.futureproject.mac.wwa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    AsyncHttpClient client;
    String url = "http://techkorner.ca/wp-json/wp/v2/product";
    TextView textView;
    ListView productList;
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> idList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();


    ArrayAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.json);
        productList = findViewById(R.id.productList);

        client = new AsyncHttpClient();

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String myjson = new String(responseBody);

                try {

                    JSONArray mainJsonArray = new JSONArray(myjson);



                    for (int i=0; i < mainJsonArray.length(); i++){

                        JSONObject singleObject = mainJsonArray.getJSONObject(i);

                        String str_id = singleObject.getString("id");
                        String str_date = singleObject.getString("date");
                        String str_date_gmt = singleObject.getString("date_gmt");

                        idList.add(singleObject.getString("id"));
                        dateList.add(singleObject.getString("date"));



                        JSONObject titleObj = singleObject.getJSONObject("title");
                        titleList.add(titleObj.getString("rendered"));


                        textView.setText(textView.getText()+"\n"+"this is ID :" + str_id);
                        textView.setText(textView.getText()+"\n"+"this is date: " + str_date);
                        textView.setText(textView.getText()+"\n"+"this is date GMT: " + str_date_gmt);



                    }

                    listAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,titleList);
                    productList.setAdapter(listAdapter);

                    productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                            Log.i("details", idList.get(position)+"\n"+dateList.get(position));
                            Toast.makeText(MainActivity.this,""+idList.get(position)+"\n"+dateList.get(position),Toast.LENGTH_LONG).show();

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();


                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String myjson = new String(responseBody);

                textView.setText(myjson);
            }
        };

        client.get(url,responseHandler);








    }
}
