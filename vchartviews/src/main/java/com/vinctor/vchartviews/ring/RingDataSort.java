package com.vinctor.vchartviews.ring;


import java.util.Comparator;

/**
 * Created by Vinctor on 2017/4/11.
 */

public class RingDataSort implements Comparator<Data> {
    @Override
    public int compare(Data o1, Data o2) {
        int num1 = o1.getNum();
        int num2 = o2.getNum();
        if (num1 == num2) return 0;
        if (num1 < num2) return -1;
        if (num1 < num2) return 1;
        return 0;
    }
}
