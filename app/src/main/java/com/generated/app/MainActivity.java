package com.generated.app;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final double[] FREQUENCIES = {261.63, 293.66, 329.63, 349.23, 392.00, 440.00, 493.88, 523.25};
    private final String[] LABELS = {"1 - דו", "2 - רה", "3 - מי", "4 - פה", "5 - סול", "6 - לה", "7 - סי", "8 - דו"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFFE0E0E0);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(32, 32, 32, 32);

        TextView title = new TextView(this);
        title.setText("נגן פסנתר - השתמש במקשי המספרים (1-8)");
        title.setTextSize(18);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 32);
        root.addView(title);

        for (int i = 0; i < LABELS.length; i++) {
            final int index = i;
            Button key = new Button(this);
            key.setText(LABELS[i]);
            key.setTextSize(22);
            key.setBackgroundResource(R.drawable.piano_key);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 140);
            params.setMargins(0, 10, 0, 10);
            key.setLayoutParams(params);
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(FREQUENCIES[index]);
                }
            });
            root.addView(key);
        }

        scroll.addView(root);
        setContentView(scroll);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int index = -1;
        switch (keyCode) {
            case KeyEvent.KEYCODE_1: index = 0; break;
            case KeyEvent.KEYCODE_2: index = 1; break;
            case KeyEvent.KEYCODE_3: index = 2; break;
            case KeyEvent.KEYCODE_4: index = 3; break;
            case KeyEvent.KEYCODE_5: index = 4; break;
            case KeyEvent.KEYCODE_6: index = 5; break;
            case KeyEvent.KEYCODE_7: index = 6; break;
            case KeyEvent.KEYCODE_8: index = 7; break;
        }

        if (index != -1) {
            playSound(FREQUENCIES[index]);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void playSound(double freq) {
        final int sampleRate = 44100;
        final int numSamples = 8820; 
        final byte[] generatedSnd = new byte[2 * numSamples];

        for (int i = 0; i < numSamples; ++i) {
            double angle = 2.0 * Math.PI * i / (sampleRate / freq);
            short val = (short) (Math.sin(angle) * 32767);
            generatedSnd[2 * i] = (byte) (val & 0xff);
            generatedSnd[2 * i + 1] = (byte) ((val >> 8) & 0xff);
        }

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }
}
