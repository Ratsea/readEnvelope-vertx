package com.ratsea.envelope.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ratsea on 2017/7/5.
 */
public class DbStorage {


    public static ConcurrentHashMap getStorage(){
        return Storage.storage;
    }

    static  class Storage{
        private static ConcurrentHashMap storage=new ConcurrentHashMap();
    }

}
