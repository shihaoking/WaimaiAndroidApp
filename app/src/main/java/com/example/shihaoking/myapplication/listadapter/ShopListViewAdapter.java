package com.example.shihaoking.myapplication.listadapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shihaoking.myapplication.R;
import com.example.shihaoking.myapplication.entity.ShopEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shihaoking on 2015/12/4.
 */
public class ShopListViewAdapter extends BaseAdapter {
    private List<HashMap<String, Object>> data = new ArrayList();
    private LayoutInflater mInflater;

    public ShopListViewAdapter(LayoutInflater layoutInflater) {
        this.mInflater = layoutInflater;
    }

    public void addItem(HashMap<String, Object> object) {
        data.add(object);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(data.get(position).get("id").toString());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.shop_list, null);

            viewHolder = new ViewHolder();
            viewHolder.shopIdTextView = (TextView) convertView.findViewById(R.id.shopId);
            viewHolder.shopNameTextView = (TextView) convertView.findViewById(R.id.shopName);
            viewHolder.shopAddressTextView = (TextView) convertView.findViewById(R.id.shopAddress);
            viewHolder.shopImageView = (ImageView) convertView.findViewById(R.id.shopImg);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, Object> curVal = data.get(position);
        viewHolder.shopIdTextView.setText(curVal.get("id").toString());
        viewHolder.shopNameTextView.setText(curVal.get("name").toString());
        viewHolder.shopAddressTextView.setText(curVal.get("address").toString());

        Object imageBitMapObj = curVal.get("imageBitMap");

        if (imageBitMapObj != null && imageBitMapObj.toString().equals("") == false) {
            Bitmap imageBitMap = (Bitmap) imageBitMapObj;
            viewHolder.shopImageView.setImageBitmap(imageBitMap);
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView shopIdTextView;
        public TextView shopNameTextView;
        public TextView shopAddressTextView;
        public ImageView shopImageView;
    }
}
