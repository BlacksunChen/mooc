package cmr.mooc;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Formatter;
import java.util.Locale;
import java.util.Random;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;


/**
 * Created by 28364 on 2017/4/17.
 */

public class DanmuActivity extends AppCompatActivity{
    private boolean showDanmaku;
    private DanmakuView danmakuView;
    private DanmakuContext danmakuContext;
    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };//弹幕的解析器
    private boolean isDanmakuHide = false;//判断弹幕是否被暂停
    private boolean isPlaying = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danmuplayer);
        final VideoView videoView = (VideoView) findViewById(R.id.videoView);
        final SeekBar seekBar_player = (SeekBar)findViewById(R.id.sb_player);
        final TextView timeCurrent = (TextView)findViewById(R.id.time_current);
        final TextView timeTotal = (TextView)findViewById(R.id.time_total);

        /*开始播放视频*/
        videoView.setVideoPath(Environment.getExternalStorageDirectory() + "/testVideo2.3gp");
        videoView.start();
        videoView.seekTo(0);
        //System.out.println("!!！"+videoView.getDuration());

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            //播放器准备完成时触发
            public void onPrepared(MediaPlayer mediaPlayer) {
                //给seekBar赋予视频正确的最大时长
                seekBar_player.setMax(videoView.getDuration());
                timeTotal.setText(stringForTime(videoView.getDuration()));
                //System.out.println("!!！"+videoView.getDuration());
            }
        });

        /*线程更新进度条*/
        new Thread(){
            @Override
            public void run(){
                try{
                    isPlaying = true;
                    while (isPlaying)
                    {
                        // 如果正在播放，每0.5.毫秒更新一次进度条
                        int current = videoView.getCurrentPosition();
                        seekBar_player.setProgress(current);
                        //timeCurrent.setText(stringForTime(current));
                        System.out.println("我执行了！"+current);

                        sleep(500);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();

        /*使用Handler更新视频播放时间*/
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable()
        {
            public void run(){
                timeCurrent.setText(stringForTime(videoView.getCurrentPosition()));
            }
        };
        new Thread(){
            @Override
            public void run(){
                try
                {
                    handler.post(runnable);
                    //timeCurrent.setText(stringForTime(videoView.getCurrentPosition()));

                    sleep(500);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();


        /*弹幕相关控制*/
        danmakuView = (DanmakuView) findViewById(R.id.danmakuView);
        danmakuView.enableDanmakuDrawingCache(true);//提升绘制效率
        danmakuView.setCallback(new DrawHandler.Callback()
        {
            @Override
            public void prepared() {
                showDanmaku = true;
                danmakuView.start();
                generateSomeDanmaku();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuContext = DanmakuContext.create();//用于对弹幕的各种全局配置进行设定
        danmakuView.prepare(parser, danmakuContext);
        isDanmakuHide = false;

        /*播放控制器设置*/
        //final LinearLayout operationLayout = (LinearLayout) findViewById(R.id.layout_danmuOpt);
        final RelativeLayout layout_opt = (RelativeLayout)findViewById(R.id.layout_operation);
        final Button send = (Button) findViewById(R.id.btn_send);
        final Button close = (Button)findViewById(R.id.btn_close);
        final Button btn_play = (Button)findViewById(R.id.btn_play);
        final EditText editText = (EditText) findViewById(R.id.et_danmu);

        danmakuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (layout_opt.getVisibility() == View.GONE) {
                    layout_opt.setVisibility(View.VISIBLE);
                } else {
                    layout_opt.setVisibility(View.GONE);
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = editText.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    addDanmaku(content, true);
                    editText.setText("");
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener(){ //控制弹幕的开关
            @Override
            public void onClick(View view){
                if(danmakuView != null && danmakuView.isPrepared() && !isDanmakuHide)
                {
                    danmakuView.hide();
                    isDanmakuHide = true;
                    //System.out.println("123!!!");
                }
                else if(danmakuView != null && danmakuView.isPrepared() &&isDanmakuHide)
                {
                    danmakuView.show();
                    isDanmakuHide = false;
                }
            }
        });
        btn_play.setOnClickListener(new View.OnClickListener()
        { //播放开关
            @Override
            public void onClick(View view){
                if(videoView.isPlaying())
                {
                    videoView.pause();
                    if(danmakuView != null && danmakuView.isPrepared())
                    {
                        danmakuView.pause();
                    }
                }
                else
                {
                    videoView.start();
                    if(danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused())
                    {
                        danmakuView.resume();
                    }
                }
            }
        });
        seekBar_player.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /*添加进度条相关事件*/
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                timeCurrent.setText(stringForTime(videoView.getCurrentPosition()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 当进度条停止修改的时候触发
                int progress = seekBar.getProgress();// 取得当前进度条的刻度
                if(videoView != null && videoView.isPlaying())
                {
                    videoView.seekTo(progress);// 设置当前播放的位置
                    timeCurrent.setText(stringForTime(videoView.getCurrentPosition()));
                }

            }
        });


        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == View.SYSTEM_UI_FLAG_VISIBLE) {
                    onWindowFocusChanged(true);
                }
            }
        });
    }

    /**
     * 重新封装play()函数，实现播放功能
     */
    protected void play(int msec)
    {

    }

    /**
     * 向弹幕View中添加一条弹幕
     * @param content
     *          弹幕的具体内容
     * @param  withBorder
     *          弹幕是否有边框
     */
    private void addDanmaku(String content, boolean withBorder) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = sp2px(20);
        danmaku.textColor = ContextCompat.getColor(this,R.color.danmu_text);
        danmaku.setTime(danmakuView.getCurrentTime());
        if (withBorder) {
            danmaku.borderColor = ContextCompat.getColor(this,R.color.danmu_border);
        }
        danmakuView.addDanmaku(danmaku);
    }

    /**
     * 随机生成一些弹幕内容以供测试
     */
    private void generateSomeDanmaku() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(showDanmaku) {
                    int time = new Random().nextInt(300);
                    String content = "" + time + time;
                    addDanmaku(content, false);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * sp转px的方法。
     */
    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将长度转换为时间
     */
    public String stringForTime(int msec)
    {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        int totalSec = msec/1000;
        int min = totalSec / 60;
        int sec = totalSec % 60;

        mFormatBuilder.setLength(0);
        return mFormatter.format("%02d:%02d",min,sec).toString();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (danmakuView != null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (danmakuView != null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showDanmaku = false;
        if (danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
        isPlaying = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
