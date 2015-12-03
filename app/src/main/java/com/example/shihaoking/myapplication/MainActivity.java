package com.example.shihaoking.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.shihaoking.myapplication.entity.ShopEntity;
import com.example.shihaoking.myapplication.service.ShopRemoteService;
import com.example.shihaoking.myapplication.service.impl.ShopRemoteServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View callView;
    private ListView listView;

    private List<HashMap<String, Object>> listViewData = new ArrayList<>();

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            boolean successful = bundle.getBoolean("successful");


            if (successful) {
                List<ShopEntity> result = (ArrayList<ShopEntity>) bundle.getSerializable("result");

                for (ShopEntity shopItem : result) {
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("name", shopItem.getName());
                    item.put("address", shopItem.getAddress());

                    Bitmap bitmap = null;
                    try {
                        URL url = new URL(shopItem.getImageUrl());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        InputStream inputStream = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(inputStream);

                        item.put("imageMap", bitmap);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    listViewData.add(item);
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        MainActivity.this,
                        listViewData,
                        R.layout.shop_list,
                        new String[]{"name", "address", "imageMap"},
                        new int[]{R.id.shopName, R.id.shopAddress});

                simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data, String textRepresentation) {
                        if (view.getId() == R.id.shopImg){
                            ImageView imageView = (ImageView)view;
                            imageView.setImageBitmap((Bitmap) data);
                        }
                        return false;
                    }
                });

                listView.setAdapter(simpleAdapter);

            } else {
                Exception exception = (Exception) bundle.getSerializable("exception");
                Snackbar.make(MainActivity.this.callView, "请求失败：" + exception.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                exception.printStackTrace();
            }
        }
    };

    final Runnable shopDataRunnable = new Runnable() {
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

                List<ShopEntity> shops = shopRemoteService.getShops(shopEntity1);


                bundle.putBoolean("successful", true);
                bundle.putSerializable("result", (Serializable) shops);

            } catch (Exception e) {
                bundle.putBoolean("successful", false);
                bundle.putSerializable("exception", e);
            }

            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    };

    final Runnable shopImageRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.mainListView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                callView = view;
                Snackbar.make(view, "请求中...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                new Thread(shopDataRunnable).start();
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
