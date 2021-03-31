package com.miyuki.firstapp.kitchentimer;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView mTimerText;
    ImageView mStartButton;
    ImageView mStopButton;

    SoundPool mSoundPool;
    int mSoundResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTimerText = (TextView) findViewById(R.id.timer_text);
        mStartButton = (ImageView) findViewById(R.id.timer_start);
        mStopButton = (ImageView) findViewById(R.id.timer_stop);

        final CountDownTimer timer = new CountDownTimer(3 * 60 * 1000, 100) {
            @Override
            public void onTick(long l) {
                long minute = l / 1000 / 60;
                long second = l / 1000 % 60;

                mTimerText.setText(String.format("%1$d:%2$02d", minute, second));
            }

            @Override
            public void onFinish() {
                mTimerText.setText("0:00");
                mSoundPool.play(mSoundResId, 1.0f, 1.0f, 0,0,1.0f);
            }
        };

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.start();//timer変数はこのメソッド外で宣言されているため宣言しているタイマー変数をfinalにする
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                mSoundPool.stop(mSoundResId);
            }
        });
    }

    //画面が表示されたときに呼ばれるメソッド
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()).build();
        } else {
            mSoundPool = new SoundPool(1, AudioManager.STREAM_ALARM,0);
        }
        mSoundResId = mSoundPool.load(this, R.raw.bellsound, 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundPool.release();
    }
}