package cmr.mooc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseListActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        //setContentView(R.layout.listview_data);

        listView = (ListView)findViewById(R.id.listView);
        SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.listview_data,
                new String[] {"image","courseName","college","studentNum","progress"},
                new int[] {R.id.course_img,R.id.courseName,R.id.college,R.id.studentNum,R.id.progress});
        listView.setAdapter(adapter);
    }

    /**
     * 获取Map中的数据
     * @return
     */
    private ArrayList<HashMap<String,Object>> getData()
    {
        ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String,Object>>();
        for(int i=0;i<8;i++)
        {
            HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
            tempHashMap.put("image",R.drawable.course_img);
            tempHashMap.put("courseName","Android从入门到放弃");
            tempHashMap.put("college","武汉理工大学");
            tempHashMap.put("studentNum",233);
            tempHashMap.put("progress","即将开课");
            arrayList.add(tempHashMap);
        }

        return arrayList;
    }
}
