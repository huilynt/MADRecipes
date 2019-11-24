package com.madrecipes.app.madrecipes;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

//Muhd Ikmal Hakim Bin Abdullah
//Team 6
//10177616F

//timer page
public class TimerFragment extends Fragment {

    //Timer
    private EditText mEditTextInput;
    private TextView mTextViewCountDown;
    private Button mButtonSet;
    private Button mButtonStartPause;
    private Button mButtonReset;
    MediaPlayer mAlarm;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;

    //on create set layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_layout, container, false);

        mAlarm = MediaPlayer.create(getActivity(), R.raw.alarm_sound);

        mEditTextInput = view.findViewById(R.id.edit_text_input);
        mTextViewCountDown = view.findViewById(R.id.countdown_text);

        mButtonSet = view.findViewById(R.id.button_set);
        mButtonStartPause = view.findViewById(R.id.countdown_startpause);
        mButtonReset = view.findViewById(R.id.countdown_reset);

        //set button on click
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0){ //ensure minutes not empty
                    Toast.makeText(getActivity(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0){ //ensure number entered is positive
                    Toast.makeText(getActivity(), "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });

        //start or pause on click
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning){
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        //reset button on click
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        return view;
    }

    //set the timer after manual enter
    private void setTime(long milliseconds){
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    //start timer
    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            //timer finished and text change to 00:00
            @Override
            public void onFinish() {
                mTimerRunning = false;
                mTextViewCountDown.setText("00:00");
                updateWatchInterface();
                mAlarm.start();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    //pause timer
    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    //reset timer to previously set time
    private void resetTimer(){
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    //change timer text
    private void updateCountDownText(){
        int hours = (int)(mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int)((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int)(mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0){
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d",hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    //toggle visibility of buttons
    private void updateWatchInterface(){
        if (mTimerRunning){
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("pause");
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("start");

            if (mTimeLeftInMillis < 1000){
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis){
                mButtonReset.setVisibility(View.VISIBLE);
            } else{
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    //hide keyboard
    private void closeKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //when timer is stopped
    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);

        editor.apply();

        if (mCountDownTimer != null){
            mCountDownTimer.cancel();
        }
    }

    //when timer is started
    @Override
    public void onStart() {
        super.onStart();

        mTextViewCountDown.setText("00:00");

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if(mTimerRunning){
            if (mTimeLeftInMillis < 0){
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }
}
