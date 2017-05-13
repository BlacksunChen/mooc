package cmr.mooc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCourseActivity extends Activity {
    private LinearLayout ll_notice;
    private LinearLayout ll_chapter;
    private LinearLayout ll_discuss;

    private TextView tv_notice;
    private TextView tv_chapter;
    private TextView tv_discuss;

    private Fragment chapterFragment;
    private Fragment noticeFragment;
    private Fragment discussFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);

        //初始化控件
        ll_notice = (LinearLayout)findViewById(R.id.ll_notice);
        ll_chapter = (LinearLayout)findViewById(R.id.ll_chapter);
        ll_discuss = (LinearLayout)findViewById(R.id.ll_discuss);
        tv_notice = (TextView)findViewById(R.id.tv_notice);
        tv_chapter = (TextView)findViewById(R.id.tv_chapter);
        tv_discuss = (TextView)findViewById(R.id.tv_discuss);

        //添加事件监听
        ll_notice.setOnClickListener(MyOnClickListener);
        ll_chapter.setOnClickListener(MyOnClickListener);
        ll_discuss.setOnClickListener(MyOnClickListener);

        //初始化fragment
        initFragment(0);

    }

    /**
     * 初始化fragment
     */
    private void initFragment(int index)
    {
        FragmentManager fragmentManager = getFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);

        switch (index)
        {
            case 0:
                tv_notice.setTextColor(getResources().getColor(R.color.btn_text_on));
                if(noticeFragment == null)
                {
                    noticeFragment = new Mycourse_noticeFragment();
                    transaction.add(R.id.fl_content,noticeFragment);
                }
                else
                {
                    transaction.show(noticeFragment);
                }
                break;

            case 1:
                tv_chapter.setTextColor(getResources().getColor(R.color.btn_text_on));
                if(chapterFragment == null)
                {
                    chapterFragment = new Mycourse_chapterFragment();
                    transaction.add(R.id.fl_content,chapterFragment);
                }
                else
                {
                    transaction.show(chapterFragment);
                }
                break;

            case 2:
                tv_discuss.setTextColor(getResources().getColor(R.color.btn_text_on));
                if(discussFragment == null)
                {
                    discussFragment = new Mycourse_discussFragment();
                    transaction.add(R.id.fl_content,discussFragment);
                }
                else
                {
                    transaction.show(discussFragment);
                }
                break;

            default:
                break;
        }

        transaction.commit();
    }

    /**
     * 隐藏所有fragment
     */
    private void hideFragment(FragmentTransaction transaction)
    {
        if(noticeFragment!=null)
        {
            transaction.hide(noticeFragment);
        }
        if (chapterFragment!=null)
        {
            transaction.hide(chapterFragment);
        }
        if (discussFragment != null)
        {
            transaction.hide(discussFragment);
        }
    }

    /**
     * 设置click事件监听逻辑
     */
    View.OnClickListener MyOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 在每次点击后将所有的底部按钮(TextView)颜色改为白色，然后根据点击着色
            // 重置按钮颜色
            tv_notice.setTextColor(getResources().getColor(R.color.btn_text));
            tv_chapter.setTextColor(getResources().getColor(R.color.btn_text));
            tv_discuss.setTextColor(getResources().getColor(R.color.btn_text));
            // TetxView置为绿色，页面随之跳转
            switch (view.getId())
            {
                case R.id.ll_notice:
                    initFragment(0);
                    break;

                case R.id.ll_chapter:
                    initFragment(1);
                    break;

                case R.id.ll_discuss:
                    initFragment(2);
                    break;

                default:
                    break;
            }
        }
    };
}
