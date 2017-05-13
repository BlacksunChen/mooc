package cmr.mooc;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment{
	private ExpandableListView epListView;
	private List<String> group_list;
	private List<List<String>> child_list;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.activity_course,container,false);

		epListView = (ExpandableListView)view.findViewById(R.id.epListView);
		//初始化数据
		group_list = new ArrayList<String>();
		child_list = new ArrayList<List<String>>();
		initData();
		//epListView.setGroupIndicator(null);  //去掉箭头
		epListView.setAdapter(new MyExpandableListView(getActivity()));

		return view;
	}

	private class MyExpandableListView extends BaseExpandableListAdapter
	{
//		private Context context;
//		public void MyExpandableListView(Context context)
//		{
//			this.context = context;
//		}
		int[] imgs = new int[]{R.mipmap.subject_computer ,
		R.mipmap.subject_engineer ,
		R.mipmap.subject_science ,
		R.mipmap.subject_finance ,
		R.mipmap.subject_health ,
		R.mipmap.subject_language ,
		R.mipmap.subject_literature,
		R.mipmap.subject_other};

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
				convertView = getActivity().getLayoutInflater().from(activity).inflate(R.layout.eplistview_child,null);
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
				convertView = getActivity().getLayoutInflater().from(activity).inflate(R.layout.eplistview_group,null);
			}
			TextView tv_groupName = (TextView)convertView.findViewById(R.id.tv_groupName);
			tv_groupName.setText(group_list.get(groupPosition));//最后在相应的group里设置他相应的Text
			ImageView img_groupName = (ImageView)convertView.findViewById(R.id.img_groupName);
			img_groupName.setImageResource(imgs[groupPosition]);//设置图标
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
		group_list.add("计算机");
		List<String> childItem1 = new ArrayList<String>();
		childItem1.add("C/C++");
		childItem1.add("Java");
		childItem1.add("Android");
		childItem1.add("Linux");
		child_list.add(childItem1);

		group_list.add("工科");
		List<String> childItem2 = new ArrayList<String>();
		childItem2.add("电子");
		childItem2.add("化学");
		child_list.add(childItem2);

		group_list.add("理科");
		List<String> childItem3 = new ArrayList<String>();
		childItem3.add("数学");
		childItem3.add("力学");
		child_list.add(childItem3);

		group_list.add("经济学");
		List<String> childItem4 = new ArrayList<String>();
		childItem4.add("微观经济学");
		childItem4.add("宏观经济学");
		child_list.add(childItem4);

		group_list.add("健康");
		List<String> childItem5 = new ArrayList<String>();
		childItem5.add("心理学");
		childItem5.add("医学");
		childItem5.add("养生");
		child_list.add(childItem5);

		group_list.add("语言");
		List<String> childItem6 = new ArrayList<String>();
		childItem6.add("英语");
		childItem6.add("日语");
		childItem6.add("法语");
		child_list.add(childItem6);

		group_list.add("人文");
		List<String> childItem7 = new ArrayList<String>();
		childItem7.add("历史");
		childItem7.add("哲学");
		child_list.add(childItem7);

		group_list.add("其他");
		List<String> childItem8 = new ArrayList<String>();
		childItem8.add("全部");
		child_list.add(childItem8);
	}

}
