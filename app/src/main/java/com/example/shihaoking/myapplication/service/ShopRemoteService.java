package com.example.shihaoking.myapplication.service;

import com.zhaishifu.service.db.model.ShopEntity;

/**
 * Created by shihaoking on 2015/11/30.
 */
public interface ShopRemoteService {
    ShopEntity getShop(int id, ClassLoader classLoader);
}
