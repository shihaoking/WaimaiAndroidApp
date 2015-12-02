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

import com.example.shihaoking.myapplication.entity.ShopEntity;
import com.example.shihaoking.myapplication.service.ShopRemoteService;
import com.example.shihaoking.myapplication.service.impl.ShopRemoteServiceImpl;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private View callView;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            boolean successful = bundle.getBoolean("successful");


            if (successful) {
                String result = bundle.getString("result");
                MainActivity.this.textView.setText(result);

            } else {
                Exception exception = (Exception) bundle.getSerializable("exception");
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

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Message msg = new Message();
                        Bundle bundle = new Bundle();

                        try {
                            ShopRemoteService shopRemoteService = new ShopRemoteServiceImpl();
                            ShopEntity shopEntity1 = new ShopEntity();
                            shopEntity1.setCategoryId(1);
                            shopRemoteService.getShops(shopEntity1);

                            ShopEntity shopEntity = shopRemoteService.getShop(23);

                            bundle.putBoolean("successful", true);
                            bundle.putString("result", shopEntity.getId() + ":" + shopEntity.getName());

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
