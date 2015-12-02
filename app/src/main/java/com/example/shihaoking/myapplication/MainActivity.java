package com.example.shihaoking.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private View callView;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            boolean successful = bundle.getBoolean("successful");


            if(successful) {
                String result = bundle.getString("result");
                MainActivity.this.textView.setText(result);

            }else {
                Exception exception = (Exception)bundle.getSerializable("exception");
                Snackbar.make(MainActivity.this.callView, "请求失败：" + exception.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                MainActivity.this.textView.setText(exception.getMessage());
                exception.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.textView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                callView = view;
                Snackbar.make(view, "请求中...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        String url = "http://10.14.3.35:8090/ShopService?wsdl";
                        String namespace ="http://service.zhaishifu.com/";
                        String methodName = "getShop";

                        SoapObject request = new SoapObject(namespace, methodName);
                        request.addProperty("arg0", 23);

                        HttpTransportSE ht = new HttpTransportSE(url);

                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                        envelope.dotNet = false;
                        envelope.setOutputSoapObject(request);

                        Message msg = new Message();
                        Bundle bundle = new Bundle();

                        try {

                            ht.call(namespace + methodName, envelope);
                            SoapObject rpcObject = (SoapObject)envelope.bodyIn;
                            SoapObject result = (SoapObject)rpcObject.getProperty(0);

                            bundle.putBoolean("successful", true);
                            bundle.putString("result", result.getPropertyAsString("id") +":"+ result.getPropertyAsString("name")+":"+result.getPropertyAsString("categoryId"));

                        } catch (Exception e) {
                            bundle.putBoolean("successful", false);
                            bundle.putSerializable("exception", e);
                        }

                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }



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
}
