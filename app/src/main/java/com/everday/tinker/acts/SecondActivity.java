package com.everday.tinker.acts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.SyncStateContract;
import android.view.View;
import android.widget.Toast;

import com.everday.library.FixDexUtils;
import com.everday.library.utils.Constants;
import com.everday.library.utils.FileUitls;
import com.everday.tinker.BaseActivity;
import com.everday.tinker.ParamsSort;
import com.everday.tinker.R;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public class SecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void show(View view){
        ParamsSort paramsSort = new ParamsSort();
        paramsSort.math(this);
    }
    //修复
    public void fix(View view){
        //通过接口去服务器下载修复好的dex文件,此处把classes2.dex放到手机本地

        File sourceFile = new File(Environment.getExternalStorageDirectory(),Constants.DEX_NAME);
        //目标文件：私有目录
        File targetFile = new File(getDir(Constants.DEX_DIR,Context.MODE_PRIVATE).getAbsolutePath()+File.separator+Constants.DEX_NAME);
        //如果存在的话，清理之前修复过的
        if(targetFile.exists()){
            targetFile.delete();
            Toast.makeText(this, "删除了", Toast.LENGTH_SHORT).show();
        }
        //复制过程
        try {
            FileUitls.copyFile(sourceFile,targetFile);
            Toast.makeText(this, "复制dex完成", Toast.LENGTH_SHORT).show();
            //开始修复dex
            FixDexUtils.loadFixedDex(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
