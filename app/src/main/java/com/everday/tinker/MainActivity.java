package com.everday.tinker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.everday.tinker.acts.SecondActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if(checkCallingPermission(perms[0]) == PackageManager.PERMISSION_DENIED){
                requestPermissions(perms,200);
            }
        }
    }

    public void jump(View view){
        startActivity(new Intent(this,SecondActivity.class));
    }
}
