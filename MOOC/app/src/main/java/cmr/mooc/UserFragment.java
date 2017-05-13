package cmr.mooc;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class UserFragment extends Fragment{
	private ListView listView;
	private TextView tv_userName;
	/*准备数据库*/
	private MyDBHelper dbHelper;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.activity_user,container,false);

		/*调用数据库*/
		//Bundle bundle = getActivity().getIntent().getExtras();
		//String userName = bundle.getString("userId");
		String userName = "lidong";
		dbHelper = new MyDBHelper(getActivity(),"test2.db");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if(db==null)
		{
			System.out.println("数据库不存在!!!123");
		}
		//读取表userCourse中的数据
		Cursor cursor = db.rawQuery("select * from userCourse where userName=?",new String[]{userName});
		int amount = cursor.getCount();
		int[] userCourseIds = new int[amount];
		final ArrayList<CourseInfo> courseInfos = new ArrayList<CourseInfo>();
		for(int i=0;i<amount;i++)
		{
			cursor.moveToNext();
			userCourseIds[i] = cursor.getInt(2);
			//读取表course中的数据
			Cursor cursor1 = db.rawQuery("select * from course where id=?",new String[]{String.valueOf(userCourseIds[i])});
			cursor1.moveToNext();
			CourseInfo courseInfo = new CourseInfo();
			//System.out.println(cursor1.getColumnName(1));
			courseInfo.courseId = cursor1.getInt(0);
			courseInfo.courseName = cursor1.getString(1);
			courseInfo.college = cursor1.getString(4);
			courseInfo.process = cursor1.getString(5);
			courseInfos.add(courseInfo);
			cursor1.close();
		}
		cursor.close();
		db.close();

		tv_userName = (TextView)view.findViewById(R.id.userName);
		tv_userName.setText(userName);
		listView = (ListView)view.findViewById(R.id.listView);
		SimpleAdapter adapter = new SimpleAdapter(view.getContext(),getData(courseInfos),R.layout.listview_course_user,
				new String[] {"image","courseName","college","progress"},
				new int[] {R.id.course_img,R.id.courseName,R.id.college,R.id.progress});
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intent = new Intent(getActivity(),MyCourseActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("courseId",courseInfos.get(i).courseId);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		System.out.println("onStart被调用了");
	}

	@Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause被调用了");
    }

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("onResume被调用了");
	}

	@Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop被调用了");
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
			tempHashMap.put("progress",courseInfos.get(i).process);
			arrayList.add(tempHashMap);
		}

		return arrayList;
	}


	/**
	 * 该类用于存储课程信息
	 */
	private class CourseInfo
	{
		int courseId;
		String courseName;
		String college;
		String process;
	}

}
