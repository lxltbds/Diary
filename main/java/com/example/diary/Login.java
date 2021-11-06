package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
  //用于创建SharedPreference文件
    //private SharedPreferences pref;
    //private SharedPreferences.Editor editor;
    private EditText accountEdit;
    Button login;
    Button default_login;
    //Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//获取控件，设置监听器
        //pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.sign);
        login = (Button) findViewById(R.id.login);
        default_login = (Button)findViewById(R.id.default_login);
        //save = (Button)findViewById(R.id.save);

        login.setOnClickListener(new MyOnClickListener2());
        default_login.setOnClickListener(new MyOnClickListener2());
        //save.setOnClickListener(new MyOnClickListener2());

    }

//响应函数
    class MyOnClickListener2 implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login:
//获取SharedPreference文件中的作者名
                    SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                    String account = accountEdit.getText().toString();
                    String master = pref.getString("author","");
                    Log.d("Login","author="+account);
                    Log.d("Login","master="+master);
//如果SharedPrefences表中有相应的作者名则登录
                    if(account.equals(master)) {
                        Toast.makeText(Login.this, "欢迎你"+account, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        intent.putExtra("author_name",account);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "作者不存在，请使用默认名字登录", Toast.LENGTH_SHORT).show();
                    }
                    break;
         //默认用户登录
                case R.id.default_login:
                    Intent intentput = new Intent(Login.this,MainActivity.class);
                    intentput.putExtra("author_name","l1");
                    startActivity(intentput);
                    break;
         //用于创建SharedPreferences文件
/*                case R.id.save:
                    SharedPreferences.Editor editor2 = getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor2.putString("author","l3");
                    editor2.apply();
                    Toast.makeText(Login.this, "you click save", Toast.LENGTH_SHORT).show();
                    break;*/

            }

        }
    }
}