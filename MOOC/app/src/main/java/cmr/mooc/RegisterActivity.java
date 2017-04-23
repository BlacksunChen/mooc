package cmr.mooc;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    /*UI*/
    private EditText et_user;
    private EditText et_password;
    private EditText et_passwordAgain;
    private Button btn_register;
    /*SQLite*/
    //SQLiteDatabase db;
    private MyDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*注册UI*/
        et_user = (EditText)findViewById(R.id.et_user);
        et_password = (EditText)findViewById(R.id.et_password);
        et_passwordAgain = (EditText)findViewById(R.id.et_passwordAgain);
        btn_register = (Button)findViewById(R.id.btn_register);

        /*准备数据库*/
        dbHelper = new MyDBHelper(getApplicationContext(),"test.db");

        /*给“注册”按钮添加监听事件*/
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = et_user.getText().toString();
                String password = et_password.getText().toString();
                String passwordAgain = et_passwordAgain.getText().toString();
                if (!(userName.equals("") && password.equals("")))
                {
                    if(!password.equals(passwordAgain))
                    {
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setTitle("注册失败").setMessage("密码错误")
                                .setPositiveButton("确定", null).show();
                    }
                    else{
                        if(checkIsDataAlreadyInDBorNot(userName)) //检查用户名是否已存在
                        {System.out.println("123");
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("注册失败").setMessage("用户名已存在")
                                    .setPositiveButton("确定", null).show();
                            //Toast.makeText(getApplicationContext(),"用户名已存在",Toast.LENGTH_SHORT);
                        }
                        else {
                            if (registerUser(userName, password)) //注册，并判断是否注册成功
                            {
                                //为对话框添加处理事件
                                DialogInterface.OnClickListener ss = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // TODO Auto-generated method stub
                                        // 跳转到登录界面
                                        Intent in = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(in);
                                        // 销毁当前activity
                                        //RegisterActivity.this.onDestroy();
                                        finish();
                                    }
                                };
                                new AlertDialog.Builder(RegisterActivity.this)
                                        .setTitle("注册成功").setMessage("注册成功")
                                        .setPositiveButton("确定", ss).show();
                            }
                            else {
                                new AlertDialog.Builder(RegisterActivity.this)
                                        .setTitle("注册失败").setMessage("注册失败")
                                        .setPositiveButton("确定", null).show();
                            }
                        }
                    }
                }
                else {
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("帐号密码不能为空").setMessage("帐号密码不能为空")
                            .setPositiveButton("确定", null);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //db.close();
    }

    /**
     * 检查用户名是否已存在
     */
    public Boolean checkIsDataAlreadyInDBorNot(String userName)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String Query = "Select * from users where userName =?";System.out.println("我运行啦！");
        Cursor cursor = db.rawQuery(Query,new String[] { userName });
        if (cursor.getCount() > 0)
        {
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    /**
     * 注册用户
     */
    public Boolean registerUser(String userName,String password)
    {
        try
        {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put("userName",userName);
            values.put("password",password);
            db.insert("users",null,values);
            db.close();
            return true;
        }
        catch (Exception e)
        {
            System.out.println("数据库插入信息失败！");
        }
        return false;

    }

    /**
     * 添加用户
     */
//    public Boolean addUser(String name, String password)
//    {
//        String str = "insert into tb_user values(?,?) ";
//        LoginActivity loginActivity = new LoginActivity();
//        loginActivity.db = db; //???????????????????
//        try
//        {
//            db.execSQL(str, new String[] { name, password });
//            return true;
//        }
//        catch (Exception e)
//        {
//            loginActivity.createDb();
//        }
//        return false;
//    }
}
