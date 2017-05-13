package cmr.mooc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

    // 底部菜单3个Linearlayout
    private LinearLayout ll_home;
    private LinearLayout ll_course;
    private LinearLayout ll_user;

    // 底部菜单3个ImageView
    private ImageView iv_home;
    private ImageView iv_course;
    private ImageView iv_user;

    // 底部菜单3个菜单标题
    private TextView tv_home;
    private TextView tv_course;
    private TextView tv_user;

    //3个Fragment
    private Fragment homeFragment;
    private Fragment courseFragment;
    private Fragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        initView();
        // 初始化底部按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        initFragment(0);
    }

    private void initView()
    {
        // 底部菜单3个Linearlayout
        ll_home = (LinearLayout)findViewById(R.id.ll_home);
        ll_course = (LinearLayout)findViewById(R.id.ll_course);
        ll_user = (LinearLayout)findViewById(R.id.ll_user);

        // 底部菜单3个ImageView
        iv_home = (ImageView)findViewById(R.id.iv_home);
        iv_course = (ImageView)findViewById(R.id.iv_course);
        iv_user = (ImageView)findViewById(R.id.iv_user);

        // 底部菜单3个菜单标题
        tv_home = (TextView)findViewById(R.id.tv_home);
        tv_course = (TextView)findViewById(R.id.tv_course);
        tv_user = (TextView)findViewById(R.id.tv_user);
    }

    private void initEvent()
    {
        ll_home.setOnClickListener(this);
        ll_course.setOnClickListener(this);
        ll_user.setOnClickListener(this);
    }

    private void initFragment(int index)
    {
        FragmentManager fragmentManager = getFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        //hideFragment(transaction);

        switch(index)
        {
            case 0:
                tv_home.setTextColor(getResources().getColor(R.color.btn_text_on));
                iv_home.setImageResource(R.mipmap.bottom_home_on);
                if(homeFragment == null)
                {
                    homeFragment = new HomeFragment();
                    //transaction.add(R.id.fl_content,homeFragment);
                    transaction.replace(R.id.fl_content,homeFragment);
                }
                else
                {
                    //transaction.show(homeFragment);
                    transaction.replace(R.id.fl_content,homeFragment);
                }
                break;
            case 1:
                tv_course.setTextColor(getResources().getColor(R.color.btn_text_on));
                iv_course.setImageResource(R.mipmap.bottom_course_on);
                if(courseFragment == null)
                {
                    courseFragment = new CourseFragment();
                    //transaction.add(R.id.fl_content,courseFragment);
                    transaction.replace(R.id.fl_content,courseFragment);
                }
                else
                {
                    //transaction.show(courseFragment);
                    transaction.replace(R.id.fl_content,courseFragment);
                }
                break;
            case 2:
                tv_user.setTextColor(getResources().getColor(R.color.btn_text_on));
                iv_user.setImageResource(R.mipmap.bottom_user_on);
                if(userFragment == null)
                {
                    userFragment = new UserFragment();
                    //transaction.add(R.id.fl_content,userFragment);
                    transaction.replace(R.id.fl_content,userFragment);
                }
                else
                {
                    //transaction.show(userFragment);
                    transaction.replace(R.id.fl_content,userFragment);
                }
                break;
            default:
                break;
        }

        // 提交事务
        transaction.commit();

    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction)
    {
        if(homeFragment!=null)
        {
            transaction.hide(homeFragment);
        }
        if(courseFragment != null)
        {
            transaction.hide(courseFragment);
        }
        if(userFragment != null)
        {
            transaction.hide(userFragment);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        // 在每次点击后将所有的底部按钮(ImageView,TextView)颜色改为白色，然后根据点击着色
        restartButton();
        // ImageView和TetxView置为绿色，页面随之跳转
        switch(v.getId())
        {
            case R.id.ll_home:
                //tv_home.setTextColor(getResources().getColor(R.color.btn_text_on));
                initFragment(0);
                break;
            case R.id.ll_course:
                //tv_course.setTextColor(getResources().getColor(R.color.btn_text_on));
                initFragment(1);
                break;
            case R.id.ll_user:
                //tv_user.setTextColor(getResources().getColor(R.color.btn_text_on));
                initFragment(2);
                break;
            default:
                break;
        }

    }

    private void restartButton()
    {
        // ImageView置为灰色
        // TextView置为白色
        tv_home.setTextColor(getResources().getColor(R.color.btn_text));
        tv_course.setTextColor(getResources().getColor(R.color.btn_text));
        tv_user.setTextColor(getResources().getColor(R.color.btn_text));

        iv_home.setImageResource(R.mipmap.bottom_home);
        iv_course.setImageResource(R.mipmap.bottom_course);
        iv_user.setImageResource(R.mipmap.bottom_user);
    }


}