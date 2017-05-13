package cmr.mooc;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{
    private Button btnTest;
    /*声明轮播器相关控件*/
    private ViewPager viewPager;
    private List<ImageView> imgViewList;
    private LinearLayout ll_point;
    private int[] imgBanner = {R.mipmap.img_banner1,R.mipmap.img_banner2,R.mipmap.img_banner3};
    private BannerAdapter adapter;
    private BannerListener bannerListener;
    private int pointIndex = 2; //圆圈标志位
    private boolean isStop = false;//线程标志
    /*声明课程类别按钮*/
    private ImageView iv_computer;
    private ImageView iv_engineer;
    private ImageView iv_science;
    private ImageView iv_finance;
    private ImageView iv_language;
    private ImageView iv_health;
    private ImageView iv_literature;
    private ImageView iv_other;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_home,container,false);
        btnTest = (Button)view.findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent实现页面跳转*/
                Intent intent = new Intent();
                intent.setClass(getActivity(),DanmuActivity.class);
                startActivity(intent);
            }
        });

        /*初始化轮播器控件*/
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        ll_point = (LinearLayout)view.findViewById(R.id.ll_point);

        //初始化数据
        imgViewList = new ArrayList<ImageView>();
        View v;
        LinearLayout.LayoutParams params;
        for (int i = 0; i < imgBanner.length; i++)
        {
            //设置广告图
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT));
            imageView.setBackgroundResource(imgBanner[i]);
            imgViewList.add(imageView);

            //设置圆圈点
            v = new View(getActivity());
            params = new LinearLayout.LayoutParams(5, 5);
            params.leftMargin = 10;
            v.setBackgroundResource(R.drawable.point_bg);
            v.setLayoutParams(params);
            v.setEnabled(false);

            ll_point.addView(v);
        }
        adapter = new BannerAdapter(imgViewList);
        viewPager.setAdapter(adapter);

        //初始化事件
        bannerListener = new BannerListener();
        viewPager.setOnPageChangeListener(bannerListener);
        //取中间数来作为起始位置
        int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % imgViewList.size());
        viewPager.setCurrentItem(index);
        ll_point.getChildAt(pointIndex).setEnabled(true);

        //设置新线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!isStop)
                {
                    SystemClock.sleep(2000);
                    if(getActivity()!=null)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //System.out.println("!!!"+viewPager.getCurrentItem());
                                int position = viewPager.getCurrentItem();
                                int newPosition = position % imgBanner.length;
                                ll_point.getChildAt(newPosition).setEnabled(true);
                                //System.out.println("newPosition!!!"+newPosition);
                                ll_point.getChildAt(pointIndex).setEnabled(false);
                                //System.out.println("pointIndex!!!"+pointIndex);
                                //更新标志位
                                pointIndex = newPosition;
                                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                            }
                        });
                    }
                }
            }
        }).start();


        /*处理课程类别选择按钮相关事件*/
        iv_computer = (ImageView) view.findViewById(R.id.iv_computer);
        iv_engineer =(ImageView)view.findViewById(R.id.iv_engineer);
        iv_science =(ImageView)view.findViewById(R.id.iv_science);
        iv_finance =(ImageView)view.findViewById(R.id.iv_finance);
        iv_health = (ImageView)view.findViewById(R.id.iv_health);
        iv_language =(ImageView)view.findViewById(R.id.iv_language);
        iv_literature =(ImageView)view.findViewById(R.id.iv_literature);
        iv_other =(ImageView)view.findViewById(R.id.iv_other);
        MyOnClickListener myOnClickListener = new MyOnClickListener();
        iv_computer.setOnClickListener(myOnClickListener);
        iv_engineer.setOnClickListener(myOnClickListener);
        iv_science.setOnClickListener(myOnClickListener);
        iv_finance.setOnClickListener(myOnClickListener);
        iv_health.setOnClickListener(myOnClickListener);
        iv_language.setOnClickListener(myOnClickListener);
        iv_literature.setOnClickListener(myOnClickListener);
        iv_other.setOnClickListener(myOnClickListener);

		return view;


	}

	@Override
    public void onDestroyView()
    {
        // 关闭定时器
        isStop = true;
        System.out.println("onDestroyView运行了！"+isStop);
        super.onDestroyView();
    }

	class BannerAdapter extends PagerAdapter
    {
        private List<ImageView> imgViewList;

        public BannerAdapter(List<ImageView> imgViewList)
        {
            this.imgViewList = imgViewList;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            //取超大的数，实现无线循环效果
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView v = imgViewList.get(position%imgViewList.size());
            ViewGroup parent = (ViewGroup)v.getParent();
            if(parent!=null) //防止报错：IllegalStateException: The specified child already has a parent
            {
                parent.removeAllViews();
            }
            container.addView(v); //避免出现空指针
            return imgViewList.get(position%imgViewList.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView(imgViewList.get(position%imgViewList.size()));
        }
    }

    /**
     * 实现ViewPager监听器接口
     */
    class BannerListener implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position)
        {
//            int newPosition = position % imgBanner.length;
//            ll_point.getChildAt(newPosition).setEnabled(true);
//            ll_point.getChildAt(pointIndex).setEnabled(false);
//            // 更新标志位
//            pointIndex = newPosition;
        }
    }

    /**
     * 实现课程类别选择按钮监听器
     */
    class MyOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(getActivity(),CourseListActivity.class);
            Bundle bundle = new Bundle();

            switch (view.getId())
            {
                case R.id.iv_computer:
                    bundle.putString("subject","computer");
                    break;
                case R.id.iv_engineer:
                    bundle.putString("subject","engineer");
                    break;
                case R.id.iv_science:
                    bundle.putString("subject","science");
                    break;
                case R.id.iv_finance:
                    bundle.putString("subject","finance");
                    break;
                case R.id.iv_health:
                    bundle.putString("subject","health");
                    break;
                case R.id.iv_language:
                    bundle.putString("subject","language");
                    break;
                case R.id.iv_literature:
                    bundle.putString("subject","literature");
                    break;
                case R.id.iv_other:
                    bundle.putString("subject","other");
                    break;
                default:
                    break;
            }

            intent.putExtras(bundle);
            startActivity(intent);
        }
    }


}
