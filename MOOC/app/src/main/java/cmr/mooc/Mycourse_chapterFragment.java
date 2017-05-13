package cmr.mooc;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 28364 on 2017/5/1.
 */

public class Mycourse_chapterFragment extends Fragment{

    private ExpandableListView epListView;
    private List<String> group_list;
    private List<List<String>> child_list;
    private int courseId;
    /*准备数据库*/
    private MyDBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_mycourse_chapter,container,false);
        Bundle bundle = getActivity().getIntent().getExtras();
        courseId = bundle.getInt("courseId");

        epListView = (ExpandableListView)view.findViewById(R.id.epListView);
        //初始化数据
        group_list = new ArrayList<String>();
        child_list = new ArrayList<List<String>>();
        initData();
        epListView.setGroupIndicator(null);
        epListView.setAdapter(new MyExpandableListView(getActivity()));

        return view;
    }
    private class MyExpandableListView extends BaseExpandableListAdapter
    {
        private Activity activity;
        public MyExpandableListView(Activity activity)
        {
            this.activity = activity;
        }

        //  获得某个父项的某个子项
        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            // TODO Auto-generated method stub
            return child_list.get(groupPosition).get(childPosition);
        }
        //  获得某个父项的子项数目
        @Override
        public int getChildrenCount(int groupPosition)
        {
            // TODO Auto-generated method stub
            return child_list.get(groupPosition).size();
        }
        //  获得某个父项的id
        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            // TODO Auto-generated method stub
            return childPosition;
        }
        //  获得子项显示的view
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                convertView = getActivity().getLayoutInflater().from(activity).inflate(R.layout.eplistview_child_chapter,null);
            }
            TextView tv_childName = (TextView)convertView.findViewById(R.id.tv_childName);
            tv_childName.setText(child_list.get(groupPosition).get(childPosition));
            return convertView;
        }

        //  获得某个父项
        @Override
        public Object getGroup(int groupPosition)
        {
            return group_list.get(groupPosition);
        }
        //  获得父项的数量
        @Override
        public int getGroupCount()
        {
            return group_list.size();
        }
        //  获得某个父项的id
        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }
        //  获得父项显示的view
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                //这里的convertView其实是一个起缓冲作用的工具，
                //因为当一个item从屏幕中滚出，我们把它放到convertView里
                //这样再滑回来的时候可以直接去取，不用重新创建
                convertView = getActivity().getLayoutInflater().from(activity).inflate(R.layout.eplistview_group_chapter,null);
            }
            TextView tv_groupName = (TextView)convertView.findViewById(R.id.tv_groupName);
            tv_groupName.setText(group_list.get(groupPosition));//最后在相应的group里设置他相应的Text
            return convertView;
        }

        //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
        @Override
        public boolean hasStableIds()
        {
            return false;
        }

        //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return false;
        }
    }

    /**
     * 初始化数据
     */
    private void initData()
    {
//        group_list.add("第1章 解读AndroidManifest配置文件");
//        List<String> childItem1 = new ArrayList<String>();
//        childItem1.add("1-1 AndroidManifest概述");
//        childItem1.add("1-2 全局信息配置");
//        childItem1.add("1-3 在配置文件中注册组件");
//        childItem1.add("1-4 总结");
//        child_list.add(childItem1);
//
//        group_list.add("第2章 使用ListView显示信息列表");
//        List<String> childItem2 = new ArrayList<String>();
//        childItem2.add("2-1 ListView解析");
//        childItem2.add("2-2 使用ArrayAdapter");
//        childItem2.add("2-3 使用SimpleAdapter");
//        child_list.add(childItem2);
//
//        group_list.add("第3章 Fragment基础概述");
//        List<String> childItem3 = new ArrayList<String>();
//        childItem3.add("8-1 静态加载Fragment（一）");
//        childItem3.add("8-2 静态加载Fragment（二）");
//        child_list.add(childItem3);
        /*准备数据库*/
        dbHelper = new MyDBHelper(getActivity(),"test2.db");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if(db==null)
        {
            System.out.println("数据库不存在!!!123");
        }
        //读取表Course中的数据
        Cursor cursor = db.rawQuery("select * from course where id=?",new String[]{String.valueOf(courseId)});
        System.out.println(cursor.getColumnName(7));
        cursor.moveToNext();
        String jsonStr = cursor.getString(7);
        cursor.close();
        db.close();
        /**使用json读取课程的章节**/
        //String jsonStr = "{\"chapter\":[{\"name\":\"第1章 解读AndroidManifest配置文件\",\"section\":[\"1-1 AndroidManifest概述\",\"1-2 全局信息配置\",\"1-3 在配置文件中注册组件\",\"1-4 总结\"]},{\"name\":\"第2章 使用ListView显示信息列表\",\"section\":[\"2-1 ListView解析\",\"2-2 使用ArrayAdapter\",\"2-3 使用SimpleAdapter\"]},{\"name\":\"第3章 Fragment基础概述\",\"section\":[\"3-1 静态加载Fragment（一）\",\"3-2 静态加载Fragment（二）\"]}]}";
        JSONTokener jsonTokener = new JSONTokener(jsonStr);
        JSONObject jsonObject;
        try{
            jsonObject = (JSONObject) jsonTokener.nextValue();
            JSONArray jsonArray = jsonObject.getJSONArray("chapter");
            for(int i=0;i<jsonArray.length();i++)
            {
                group_list.add(jsonArray.getJSONObject(i).getString("name"));
                JSONArray jsonArray_section = jsonArray.getJSONObject(i).getJSONArray("section");
                List<String> childItem = new ArrayList<String>();
                for(int j=0;j<jsonArray_section.length();j++)
                {
                    childItem.add(jsonArray_section.getString(j));
                }
                child_list.add(childItem);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
