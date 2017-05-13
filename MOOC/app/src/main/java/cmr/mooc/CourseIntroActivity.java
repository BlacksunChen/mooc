package cmr.mooc;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CourseIntroActivity extends AppCompatActivity {
    /*准备数据库*/
    private MyDBHelper dbHelper;
    private String userName;
    private int courseId;
    private boolean isJoin;
    /*UI*/
    private TextView tv_courseName;
    private TextView tv_teacher;
    private TextView tv_college;
    private TextView tv_process;
    private TextView tv_studentNum;
    private TextView tv_hour;
    private TextView tv_intro;
    private Button btn_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_intro);
        isJoin = false;

        Bundle bundle = this.getIntent().getExtras();
        courseId = bundle.getInt("courseId");
        //userName = bundle.getString("userName");
        userName = "lidong";
        /*准备数据库处理课程信息*/
        dbHelper = new MyDBHelper(getApplicationContext(),"test2.db");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if(db==null)
        {
            System.out.println("数据库不存在!!!123");
        }
        Cursor cursor = db.rawQuery("select * from course where id=?",new String[]{String.valueOf(courseId)});
        CourseInfo courseInfo = new CourseInfo();
        while (cursor.moveToNext())
        {
            courseInfo.courseId = courseId;
            courseInfo.courseName = cursor.getString(1);
            courseInfo.teacher = cursor.getString(3);
            courseInfo.college = cursor.getString(4);
            courseInfo.process = cursor.getString(5);
            courseInfo.studentNum = cursor.getInt(6);
            courseInfo.hour = String.valueOf(cursor.getInt(8))+"小时/周";
            courseInfo.intro = cursor.getString(9);
        }
        cursor.close();

        /*设置UI*/
        tv_courseName = (TextView)findViewById(R.id.tv_courseName);
        tv_teacher = (TextView)findViewById(R.id.tv_teacher);
        tv_college = (TextView)findViewById(R.id.tv_college);
        tv_process = (TextView)findViewById(R.id.tv_progress);
        tv_studentNum = (TextView)findViewById(R.id.tv_studentNum);
        tv_hour = (TextView)findViewById(R.id.tv_hour);
        tv_intro = (TextView)findViewById(R.id.tv_intro);
        btn_join = (Button)findViewById(R.id.btn_join);

        tv_courseName.setText(courseInfo.courseName);
        tv_teacher.setText(courseInfo.teacher);
        tv_college.setText(courseInfo.college);
        tv_process.setText(courseInfo.process);
        tv_studentNum.setText(String.valueOf(courseInfo.studentNum));
        tv_hour.setText(courseInfo.hour);
        tv_intro.setText(courseInfo.intro);

        /*准备数据库处理用户信息*/
        Cursor cursor1 = db.rawQuery("select * from userCourse where userName=? AND userCourseId=?",new String[]{userName,String.valueOf(courseInfo.courseId)});
        if(cursor1.getCount() == 0)
        {
            btn_join.setText("立即参加");
            isJoin = false;
        }
        else
        {
            btn_join.setText("已加入");
            isJoin = true;
        }
        cursor1.close();

        //设置“加入”按钮的监听事件
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isJoin)
                {
                    //对话框处理事件
                    DialogInterface.OnClickListener ss = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //点击“是”，即“加入课程”
                            //调用数据库添加记录
                            SQLiteDatabase db_user = dbHelper.getWritableDatabase();
                            if(db_user==null)
                            {
                                System.out.println("数据库不存在!!!123");
                            }
                            ContentValues values = new ContentValues();
                            values.put("userName",userName);
                            values.put("userCourseId",courseId);
                            db_user.insert("userCourse",null,values);
                            db_user.close();
                            //Toast信息
                            Toast.makeText(getApplicationContext(),"课程加入成功",Toast.LENGTH_SHORT).show();
                            btn_join.setText("已加入");
                            isJoin = true;
                        }
                    };
                    //添加对话框
                    new AlertDialog.Builder(CourseIntroActivity.this).setTitle("加入须知")
                            .setMessage("是否确认加入该课程")
                            .setPositiveButton("是",ss)
                            .setNegativeButton("否",null).show();
                }
            }
        });
        db.close();
    }


    /*用于存储课程相关信息*/
    private class CourseInfo
    {
        int courseId;
        String courseName;
        String teacher;
        String college;
        String process;
        int studentNum;
        String hour;
        String intro;
    }
}
