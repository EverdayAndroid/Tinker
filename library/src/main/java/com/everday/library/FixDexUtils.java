package com.everday.library;

import android.content.Context;

import com.everday.library.utils.ArrayUtils;
import com.everday.library.utils.Constants;
import com.everday.library.utils.ReflectUtils;

import java.io.File;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class FixDexUtils {

    //存放需要修复的dex集合
    private static HashSet<File> loadeDex = new HashSet<>();
    static {
        loadeDex.clear();
    }
    public static void loadFixedDex(Context context) {
        if(context == null){return;}
        //Dex文件目录
        File fileDir = context.getDir(Constants.DEX_DIR, Context.MODE_PRIVATE);

        //遍历私有目录中的所有文件
        File[] listFiles = fileDir.listFiles();
        for (File file:listFiles) {
            if (file.getName().endsWith(Constants.DEX_SUFFIX) && !"classes.dex".equals(file.getName())) {
                //找到修复包dex文件，加入集合
                loadeDex.add(file);
            }
        }
        //模拟类加载器
        createDexClassLoader(context,fileDir);

    }

    private static void createDexClassLoader(Context context, File fileDir) {
        //临时解压目录
        String optimizedDir = fileDir.getAbsolutePath()+File.separator+"opt_dex";
        //创建该目录
        File fopt = new File(optimizedDir);
        if(!fopt.exists()){
            fopt.mkdirs();
        }
        for (File dex:loadeDex){
            //初始化DexClassLoader
            DexClassLoader dexClassLoader = new DexClassLoader(dex.getAbsolutePath(),optimizedDir,null,context.getClassLoader());
            //每遍历一个需要修复的dex文件，就需要插队一次
            hotfix(dexClassLoader,context);
        }
    }

    private static void hotfix(DexClassLoader dexClassLoader, Context context) {
        //获取系统PathClassLoader
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        try {
            //获取自有的dexElements数组
            Object myDexElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(dexClassLoader));
            //获取系统的dexElements数组
            Object systemDexElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(pathClassLoader));
            //合并成新的dexElements数组，并设置自有的优先级
            Object dexElements = ArrayUtils.combineArray(myDexElements,systemDexElements);
            //获取系统的pathList对象
            Object systemPathList = ReflectUtils.getPathList(pathClassLoader);
            //重新复制系统的pathList属性值 --修改的dexElements数组（新合成的）
            ReflectUtils.setField(systemPathList,systemPathList.getClass(),dexElements);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
