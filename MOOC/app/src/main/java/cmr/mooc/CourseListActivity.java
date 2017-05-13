package cmr.mooc;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseListActivity extends AppCompatActivity {
    private ListView listView;
    //创建数据库
    private MyDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        //接收intent传来的值
        Bundle bundle = this.getIntent().getExtras();
        String subject = bundle.getString("subject");
        System.out.println("获取到的name值为"+subject);
        //准备数据库
        CopySqliteFileFromRawToDatabases("test2.db");
        dbHelper = new MyDBHelper(getApplicationContext(),"test2.db");
        //查找数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if(db==null)
        {
            System.out.println("!!!123");
        }
        Cursor cursor = db.rawQuery("select * from course where courseClass=?",new String[]{subject});
        final ArrayList<CourseInfo> courseInfos = new ArrayList<CourseInfo>();
        System.out.println("!!!"+cursor.getColumnCount());
        while (cursor.moveToNext())
        {
            CourseInfo courseInfo = new CourseInfo(); //必须放在里面，否则不会覆盖数据
            courseInfo.courseId = cursor.getInt(0);
            System.out.println("2lie is:"+cursor.getColumnName(1));
            System.out.println("2lie is:"+cursor.getString(1));
            courseInfo.courseName = cursor.getString(1);
            System.out.println("courseName:"+courseInfo.courseName); //test!!!
            courseInfo.college = cursor.getString(4);
            courseInfo.process = cursor.getString(5);
            courseInfo.studentNum = cursor.getInt(6);
            courseInfos.add(courseInfo);
        }
        cursor.close();
        db.close();

        listView = (ListView)findViewById(R.id.listView);
        SimpleAdapter adapter = new SimpleAdapter(this,getData(courseInfos),R.layout.listview_course,
                new String[] {"image","courseName","college","studentNum","progress"},
                new int[] {R.id.course_img,R.id.courseName,R.id.college,R.id.studentNum,R.id.progress});
        listView.setAdapter(adapter);
        //listview中每一项的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CourseListActivity.this,CourseIntroActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("courseId",courseInfos.get(i).courseId); //将课程id传入下一界面
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
    }

    /**
     * 该类用于存储从数据库内获得的信息
     */
    private class CourseInfo{
        String courseName;
        String college;
        int studentNum;
        String process;
        int courseId;
    }

    /**
     * 获取Map中的数据
     * @return
     */
    private ArrayList<HashMap<String,Object>> getData(ArrayList<CourseInfo> courseInfos)
    {
        ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<courseInfos.size();i++)
        {
            HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
            tempHashMap.put("image",R.drawable.course_img);
            tempHashMap.put("courseName",courseInfos.get(i).courseName);
            tempHashMap.put("college",courseInfos.get(i).college);
            tempHashMap.put("studentNum",courseInfos.get(i).studentNum);
            tempHashMap.put("progress",courseInfos.get(i).process);
            arrayList.add(tempHashMap);
        }

        return arrayList;
    }

    /**
     * 将外部数据库写入sd卡
     */
    private void CopySqliteFileFromRawToDatabases(String dbName)
    {
        // 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>
        File dir = new File("data/data/"+this.getPackageName()+"/databases");
        if (!dir.exists() || !dir.isDirectory())
        {
            dir.mkdirs();
        }

        File file = new File(dir,dbName);
        InputStream inputStream = null;
        OutputStream outputStream = null;

        //通过IO流的方式，将assets目录下的数据库文件，写入到SD卡中。
        if (!file.exists())
        {System.out.println(dbName+"不存在!!!");
            try
            {
                file.createNewFile();
                //inputStream = this.getApplicationContext().getClass().getClassLoader().getResourceAsStream("assets/"+dbName);
                //得到资源
                AssetManager am = getAssets();
                inputStream = am.open("test2.db");
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len ;
                while ((len = inputStream.read(buffer)) > 0)
                {
                    outputStream.write(buffer,0,len);
                }
                inputStream.close();
                outputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println(dbName+"已存在!!!");
        }
    }
}
