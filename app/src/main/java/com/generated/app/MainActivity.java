package com.generated.app;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final double[] FREQUENCIES = {261.63, 293.66, 329.63, 349.23, 392.00, 440.00, 493.88, 523.25};
    private final String[] LABELS = {"1 - דו", "2 - רה", "3 - מי", "4 - פה", "5 - סול", "6 - לה", "7 - סי", "8 - דו"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFFF5F5F5);
        root.setGravity(Gravity.CENTER);
        root.setPadding(32, 32, 32, 32);

        TextView title = new TextView(this);
        title.setText("נגן לפי מספרים");
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 32);
        root.addView(title);

        LinearLayout keysContainer = new LinearLayout(this);
        keysContainer.setOrientation(LinearLayout.VERTICAL);
        
        for (int i = 0; i < LABELS.length; i++) {
            final int index = i;
            Button key = new Button(this);
            key.setText(LABELS[i]);
            key.setTextSize(20);
            key.setBackgroundResource(R.drawable.piano_key);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 120);
            params.setMargins(0, 8, 0, 8);
            key.setLayoutParams(params);
            
            key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playSound(FREQUENCIES[index]);
                }
            });
            keysContainer.addView(key);
        }

        root.addView(keysContainer);
        setContentView(root);
    }

    private void playSound(double freq) {
        final int sampleRate = 44100;
        final int duration = 1; 
        final int numSamples = duration * sampleRate;
        final double[] sample = new double[numSamples];
        final byte[] generatedSnd = new byte[2 * numSamples];

        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freq));
        }

        int idx = 0;
        for (final double dVal : sample) {
            final short val = (short) ((dVal * 32767));
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }
}
