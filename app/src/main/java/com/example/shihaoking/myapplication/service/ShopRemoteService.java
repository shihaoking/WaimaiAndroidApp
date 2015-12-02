package com.example.shihaoking.myapplication.service;

import com.example.shihaoking.myapplication.entity.ShopEntity;

import java.util.List;

/**
 * Created by shihaoking on 2015/11/30.
 */
public interface ShopRemoteService {
    ShopEntity getShop(int id);

    List<ShopEntity> getShops(ShopEntity shopEntity);
}
