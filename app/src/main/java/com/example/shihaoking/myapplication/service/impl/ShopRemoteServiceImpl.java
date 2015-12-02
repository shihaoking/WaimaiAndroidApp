package com.example.shihaoking.myapplication.service.impl;

import com.example.shihaoking.myapplication.entity.ShopEntity;
import com.example.shihaoking.myapplication.service.ShopRemoteService;
import com.example.shihaoking.myapplication.service.SoapService;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.List;


/**
 * Created by shihaoking on 2015/11/30.
 */
public class ShopRemoteServiceImpl implements ShopRemoteService {
    final String url = "http://192.168.1.4:8090/ShopService?wsdl";
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


        return null;
    }
}
