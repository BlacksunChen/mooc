package cmr.mooc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    //UI
    private EditText et_user;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;
    private CheckBox ckb_keepPassword;
    //创建数据库
    //public static SQLiteDatabase db;
    private MyDBHelper dbHelper;
    // 创建SharedPreferences存储记住密码的用户
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //注册UI
        et_user = (EditText)findViewById(R.id.et_user);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_register = (Button)findViewById(R.id.btn_register);
        ckb_keepPassword = (CheckBox)findViewById(R.id.ckb_keepPassword);
        //准备数据库
        //db = SQLiteDatabase.openOrCreateDatabase(LoginActivity.this.getFilesDir().toString()+"/test.dbs",null);
        dbHelper = new MyDBHelper(getApplicationContext(),"test.db");

        /*判断是否有记住密码的用户*/
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (sp.getBoolean("AUTO_ISCHECK", false))
        {
            et_user.setText(sp.getString("userName",null));
            et_password.setText(sp.getString("password",null));
            ckb_keepPassword.setChecked(true);
        }

        /*添加“注册”按钮监听事件*/
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //跳转到注册界面
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        /*添加“登录”按钮监听事件*/
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = et_user.getText().toString();
                String password = et_password.getText().toString();
                if (userName.equals("") || password.equals(""))
                {
                    //toast消息
                    Toast.makeText(getApplicationContext(),"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    if(isUserinfo(userName, password)) //如果验证成功则进入系统
                    {
                        //保存设置
                        if(ckb_keepPassword.isChecked())
                        {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("userName",userName);
                            editor.putString("password",password);
                            editor.putBoolean("AUTO_ISCHECK",true);
                            editor.commit();
                        }else {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("userName",null);
                            editor.putString("password",null);
                            editor.putBoolean("AUTO_ISCHECK",false);
                            editor.commit();
                        }
                        //跳转到主页
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        // 销毁当前activity
                        finish();
                    }

                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        System.out.println("onDestroy被运行了！");
        super.onDestroy();
        //db.close();
    }

    /**
     * 判断输入的用户是否正确
     */
    public Boolean isUserinfo(String name, String pwd)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String str="select * from users where userName=? and password=?";
            Cursor cursor = db.rawQuery(str,new String[] {name, pwd});
            //String str="select * from tb_user where name=? and password=?";
            //Cursor cursor = db.rawQuery(str, new String []{name,pwd});//rawQuery()第二个参数中的字段替换str中的问号
            if(cursor.getCount()<=0)
            {
                Toast.makeText(getApplicationContext(),"账号或密码错误",Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                return true;
            }
        }catch (SQLiteException e)
        {
            //createDb();
            System.out.println("数据库对比错误");
        }
        return false;
    }

    /**
     * 创建数据库和用户表
     */
    /*public void createDb()
    {
        db.execSQL("create table tb_user( name varchar(30) primary key,password varchar(30))");
    }*/
}
