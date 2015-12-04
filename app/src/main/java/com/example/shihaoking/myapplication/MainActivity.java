package com.example.shihaoking.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shihaoking.myapplication.entity.ShopEntity;
import com.example.shihaoking.myapplication.listadapter.ShopListViewAdapter;
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
    private List<ShopEntity> shopEntities;

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            boolean successful = bundle.getBoolean("successful");

            if (successful) {
                List<ShopEntity> result = (ArrayList<ShopEntity>) bundle.getSerializable("result");

                ShopListViewAdapter shopListViewAdapter = new ShopListViewAdapter((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));

                for (ShopEntity shopItem : result) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", shopItem.getId());
                    hashMap.put("name", shopItem.getName());
                    hashMap.put("address", shopItem.getAddress());

                    shopListViewAdapter.addItem(hashMap);
                }


                listView.setAdapter(shopListViewAdapter);

                for (ShopEntity shopItem: result) {
                    new Thread(new ShopImageRunnable(shopItem.getId(), shopItem.getImageUrl())).start();
                }

            } else {
                Exception exception = (Exception) bundle.getSerializable("exception");
                Snackbar.make(MainActivity.this.callView, "请求失败：" + exception.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                exception.printStackTrace();
            }
        }
    };

    final Handler shopImageLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            int shopId = bundle.getInt("shopId");
            Bitmap bitmap = (Bitmap) bundle.get("bitMap");

            for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                if (listView.getAdapter().getItemId(i) == shopId) {
                    BaseAdapter shopListViewAdapter = (BaseAdapter)listView.getAdapter();
                    HashMap<String, Object> hashMap  = (HashMap<String, Object>)shopListViewAdapter.getItem(i);
                    hashMap.put("imageBitMap", bitmap);
                    shopListViewAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    };

    final Runnable shopDataRunnable = new Runnable() {
        @Override
        public void run() {

            Message msg = new Message();
            Bundle bundle = new Bundle();

            try {
                ShopRemoteService shopRemoteService = new ShopRemoteServiceImpl();
                ShopEntity shopEntity1 = new ShopEntity();
                shopEntity1.setCategoryId(1);

                shopEntities = shopRemoteService.getShops(shopEntity1);


                bundle.putBoolean("successful", true);
                bundle.putSerializable("result", (Serializable) shopEntities);

            } catch (Exception e) {
                bundle.putBoolean("successful", false);
                bundle.putSerializable("exception", e);
            }

            msg.setData(bundle);
            handler.sendMessage(msg);
        }
    };

    class ShopImageRunnable implements Runnable {
        private int shopId;
        private String imageUrl;

        public ShopImageRunnable(int shopId, String imageUrl) {
            this.shopId = shopId;
            this.imageUrl = imageUrl;
        }

        @Override
        public void run() {

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                Bundle bundle = new Bundle();
                bundle.putInt("shopId", shopId);
                bundle.putParcelable("bitMap", bitmap);

                Message msg = new Message();
                msg.setData(bundle);
                shopImageLoadHandler.sendMessage(msg);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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

        listView = (ListView) findViewById(R.id.mainListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShopListViewAdapter.ViewHolder viewHolder = (ShopListViewAdapter.ViewHolder)view.getTag();
                TextView shopIdTextView = viewHolder.shopIdTextView;

                Snackbar.make(MainActivity.this.callView, shopIdTextView.getText().toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
