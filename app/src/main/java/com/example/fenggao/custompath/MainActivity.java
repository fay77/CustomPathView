package com.example.fenggao.custompath;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private CustomRectView mCustomRectView;
    private int[] lastData0 = new int[] { 70000, 10000, 20000, 40000};
    private int[] arrs = new int[]{ 3 , 7 , 11 , 17 , 23 , 35 , 50};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCustomRectView = (CustomRectView) findViewById(R.id.custom_view);
        mCustomRectView.updateLastData(lastData0);
        mCustomRectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomRectView.updateLastData(lastData0);

            }
        });
        Log.d("gaofeng", "折半查找找到的是-------》" + halfSearch(arrs, 100));
    }

    /**
     * 排序
     * @param array
     * @return
     */

    private int[] sortArry(int[] array) {
        int jiaoBiao = 0;
        int temp = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[jiaoBiao]) {
                temp = array[jiaoBiao];
                array[jiaoBiao] = array[i];
                array[i] = temp;
                jiaoBiao++;
            }
        }
        return array;
    }

    /**
     * 折半查找
     * @param arrs
     * @param key
     * @return
     */
    private int halfSearch(int[] arrs, int key) {
        int min = 0;
        int max = arrs.length - 1;
        int mid = (min + max) / 2;
        while (arrs[mid] != key) {
            if (key > arrs[mid]) {
                min = mid + 1;
            } else if (key < arrs[mid]) {
                max = mid - 1;
            }
            if (min > max) {
                return -1;
            }
            mid = (min + max) / 2;

        }
        return mid;
    }
}
