package com.example.shihaoking.myapplication.entity;

import android.os.Parcelable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Simon on 2015/12/2.
 */
public class ShopEntity implements KvmSerializable, Serializable {
    private int id = -1;
    private String name;
    private String address;
    private String phoneNumber;
    private int categoryId = -1;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ShopEntity() {
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return this.id;
            case 1:
                return this.name;
            case 2:
                return this.address;
            case 3:
                return this.phoneNumber;
            case 4:
                return this.categoryId;
        }

        return null;
    }

    @Override
    public int getPropertyCount() {
        return 5;
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i) {
            case 0:
                this.id = Integer.parseInt(o.toString());
            break;
            case 1:
                this.name = o.toString();
            break;
            case 2:
                this.address = o.toString();
            break;
            case 3:
                this.phoneNumber = o.toString();
            break;
            case 4:
                this.categoryId = Integer.parseInt(o.toString());
            break;
        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

        switch (i) {
            case 0:
                propertyInfo.setType(int.class);
                propertyInfo.setName("id");
                propertyInfo.setValue(this.id);
                break;
            case 1:
                propertyInfo.setType(String.class);
                propertyInfo.setName("name");
                propertyInfo.setValue(this.name);
                break;
            case 2:
                propertyInfo.setType(String.class);
                propertyInfo.setName("address");
                propertyInfo.setValue(this.address);
                break;
            case 3:
                propertyInfo.setType(String.class);
                propertyInfo.setName("phoneNumber");
                propertyInfo.setValue(this.phoneNumber);
                break;
            case 4:
                propertyInfo.setType(int.class);
                propertyInfo.setName("categoryId");
                propertyInfo.setValue(this.categoryId);
                break;
        }
    }
}
