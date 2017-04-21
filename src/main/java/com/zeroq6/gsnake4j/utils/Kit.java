package com.zeroq6.gsnake4j.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 工具箱
 * 
 * @author icgeass@hotmail.com
 * @date 2015年6月1日
 * @version gsnake4j - v1.0.5
 * @url https://github.com/icgeass/gsnake4j
 */
public class Kit {

    private final static Random rand = new Random();

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static void sortArrayRandom(Object[] arr) {
        if (null == arr || arr.length < 2) {
            return;
        }
        int index1 = 0;
        int index2 = 0;
        Object o = null;
        for (int i = 0; i < arr.length / 2 + 1; i++) {
            index1 = rand.nextInt(arr.length);
            index2 = rand.nextInt(arr.length);
            o = arr[index1];
            arr[index1] = arr[index2];
            arr[index2] = o;
        }
    }

    public static String formatDate(Long date) {
        return sdf.format(new Date(date));
    }

}
