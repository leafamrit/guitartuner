package com.example.arkeyve.guitartunerv022;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.arkeyve.guitartunerv022.tarsos.PitchDetectionResult;

public class MainActivity extends AppCompatActivity {
    private TextView note;
    private Spinner instrument;
    private TextView standardTune;
    private SeekBar noteProbability;
    private Tuner tuner;
    private Handler pauseHandle = new Handler();
    private String noteText;
    private int noteAccuracy;
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        standardTune = (TextView) findViewById(R.id.standardTune);
        standardTune.setText(" ");
        instrument = (Spinner) findViewById(R.id.instruments);

        instrument.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (instrument.getSelectedItem().toString()) {
                    case "Guitar":
                        standardTune.setText("E A D G B E");
                        break;
                    case "Ukelele":
                        standardTune.setText("G C E A");
                        break;
                    default:
                        standardTune.setText(" ");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        note = (TextView) findViewById(R.id.note);
        noteProbability = (SeekBar) findViewById(R.id.noteProbability);
        tuner = new Tuner(new TunerUpdate(){
            public void updateNote(Note newNote, PitchDetectionResult result) {
                noteText = newNote.getNote() != "" ? newNote.getNote() : "\u266c";
                noteAccuracy = (int) (25 + newNote.getDifference());
                if(noteAccuracy > 22 && noteAccuracy < 28) {
                    noteProbability.setThumbTintList(ColorStateList.valueOf(Color.parseColor("#ff669900")));
                } else {
                    noteProbability.setThumbTintList(ColorStateList.valueOf(Color.parseColor("#ffcc0000")));
                }
                note.setText(noteText);
                noteProbability.setProgress(noteAccuracy);
            }
        });

        tuner.start();
    }

}
