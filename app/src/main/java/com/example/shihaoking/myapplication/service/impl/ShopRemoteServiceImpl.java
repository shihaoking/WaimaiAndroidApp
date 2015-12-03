package com.example.shihaoking.myapplication.service.impl;

import com.example.shihaoking.myapplication.entity.ShopEntity;
import com.example.shihaoking.myapplication.service.ShopRemoteService;
import com.example.shihaoking.myapplication.service.SoapService;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;


/**
 * Created by shihaoking on 2015/11/30.
 */
public class ShopRemoteServiceImpl implements ShopRemoteService {
//    final String url = "http://192.168.1.4:8090/ShopService?wsdl";
    final String url = "http://10.14.3.35:8090/ShopService?wsdl";
    final String namespace ="http://service.zhaishifu.com/";

    @Override
    public ShopEntity getShop(int id) {
        SoapService soapService = new SoapService(url, namespace, "getShop");
        soapService.addRequestPropertyInfo("arg0", id);

        SoapObject rpcObject = null;

        try {
            rpcObject = (SoapObject)soapService.requestResult();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        if(rpcObject == null || rpcObject.getPropertyCount() == 0){
            return null;
        }

        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setId(Integer.parseInt(rpcObject.getPropertyAsString("id")));
        shopEntity.setName(rpcObject.getPropertyAsString("name"));
        shopEntity.setAddress(rpcObject.getPropertyAsString("address"));
        shopEntity.setPhoneNumber(rpcObject.getPropertyAsString("phoneNumber"));
        shopEntity.setCategoryId(Integer.parseInt(rpcObject.getPropertyAsString("categoryId")));

        return shopEntity;
    }

    @Override
    public List<ShopEntity> getShops(ShopEntity shopEntity) {
        SoapService soapService = new SoapService(url, namespace, "getShops");
        soapService.addRequestPropertyInfo("arg0", shopEntity);

        Vector rpcObject = null;

        try {
            rpcObject = (Vector)soapService.requestResult();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        List<ShopEntity> result = new ArrayList<>();

        Enumeration<SoapObject> enums = rpcObject.elements();

        while (enums.hasMoreElements()){
            ShopEntity shop = new ShopEntity();
            SoapObject item = enums.nextElement();

            if(item.getPropertyCount() < 5){
                continue;
            }

            shop.setId(Integer.parseInt(item.getPropertyAsString("id")));
            shop.setName(item.getPropertyAsString("name"));
            shop.setAddress(item.getPropertyAsString("address"));
            shop.setPhoneNumber(item.getPropertyAsString("phoneNumber"));
            shop.setCategoryId(Integer.parseInt(item.getPropertyAsString("categoryId")));
            shop.setImageUrl(item.getPropertyAsString("imageUrl"));

            result.add(shop);
        }

        return result;
    }
}
