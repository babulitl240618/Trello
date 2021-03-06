package com.imaginebd.trellohayven3.ui.timer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bastienleonard.tomate.R;

public final class TimesUpDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "TimesUpDialogFragment";

    public static TimesUpDialogFragment newInstance() {
        return new TimesUpDialogFragment();
    }

    private boolean mMoveToToDo = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.times_up_dialog_fragment, container, false);
        TextView done = (TextView) view.findViewById(R.id.done);
        done.setOnClickListener(this);
        TextView shortBreak = (TextView) view.findViewById(R.id.short_break);
        shortBreak.setOnClickListener(this);
        TextView longBreak = (TextView) view.findViewById(R.id.long_break);
        longBreak.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mMoveToToDo) {
            // When the screen is rotated, this method is called with getActivity() == null,
            // which otherwise causes a crash
            if (getActivity() != null) {
                moveToToDo();
            }
        }

        super.onDismiss(dialog);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.done:
                done();
                break;
            case R.id.short_break:
                onShortBreak();
                break;
            case R.id.long_break:
                onLongBreak();
                break;
        }
    }

    private void done() {
        mMoveToToDo = false;
        ((TimerActivity) getActivity()).onTaskMarkedAsDone();
        dismiss();
    }

    private void onShortBreak() {
        mMoveToToDo = false;
        ((TimerActivity) getActivity()).onShortBreak();
        dismiss();
    }

    private void onLongBreak() {
        mMoveToToDo = false;
        ((TimerActivity) getActivity()).onLongBreak();
        dismiss();
    }

    private void moveToToDo() {
        ((TimerActivity) getActivity()).onMoveToToDo();
    }
}
