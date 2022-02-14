package com.thejango.apiclientmodule.activity;

/**
 *  @author Vedic Rishi Astro Pvt Ltd
 *  @description API Test Activity for a showing JSON output from Vedic Rishi Astro API
 */

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.thejango.apiclientmodule.APITask;
import com.thejango.apiclientmodule.IAPITaskCallBack;
import com.thejango.apiclientmodule.R;

public class TestActivity extends Activity implements IAPITaskCallBack {

    private Button btnAstroDetails;
    private TextView txtAstroDetails;
    private Handler handler;
    private static final int SUCCESS = 99;

    private String USER_ID = "<REPLACE YOUR USERID HERE>"; // eg "4545"
    private String API_KEY = "<REPLACE YOUR API KEY HERE>";  // eg "hdkbcsjcn157618678habdkjbck"
    private String API_END_POINT = "https://api.vedicrishiastro.com/v1/"; // DON'T CHANGE THIS LINE

    class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS:
                    String res = (String) msg.obj;
                    txtAstroDetails.setText(res);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        handler = new IncomingHandler();
        btnAstroDetails = (Button) findViewById(R.id.btnAstroDetails);
        txtAstroDetails = (TextView) findViewById(R.id.txtAstroDetails);
        btnAstroDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestBody formBody = new FormEncodingBuilder()
                        .add("day", "15")
                        .add("month", "8")
                        .add("year", "1987")
                        .add("hour", "00")
                        .add("min", "49")
                        .add("lat", "28.4700")
                        .add("lon", "77.0300")
                        .add("tzone", "5.5")
                        .build();
                // the first argument is the API name
                // for ex- planets or astro_details or general_house_report/sun
                // you can get the API names from https://www.vedicrishiastro.com/docs
                // NOTE: please make sure there is no / before the API name.
                // for example /astro_details will give an error
                executeAPI("astro_details", formBody);
            }
        });
    }

    @Override
    public void onSuccess(String response) {
        Log.i("Test Activity", "Response : " + response);
        Message message = Message.obtain();
        message.what = SUCCESS;
        message.obj = response;
        handler.sendMessage(message);
    }

    @Override
    public void onFailure(String error) {
        Log.e("Test Activity Error", "Response : " + error);
    }

    private void executeAPI(String apiName, RequestBody formData)
    {
        String url = API_END_POINT+apiName;
        APITask apiTask = new APITask(url, USER_ID, API_KEY, formData, TestActivity.this);
        Thread thread = new Thread(apiTask);
        thread.start();
    }
}
