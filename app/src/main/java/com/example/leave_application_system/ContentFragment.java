package com.example.leave_application_system;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.leave_application_system.R;

public final class ContentFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";
    private String mContent = "";

    public static ContentFragment newInstance(String content) {
        ContentFragment fragment = new ContentFragment();
        fragment.mContent =content;
        return fragment;
    }

    // ???
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    // Layout 用程式產生....
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ImageView img = new ImageView(getActivity());

        //add image here , and don't forget to change "StrArray" in LoginActivity
        switch (mContent){
            case "1":
                img.setImageResource(R.drawable.png);
                break;
            case "2":
                img.setImageResource(R.drawable.dq);
                break;
           /* case "3":
                img.setImageResource(R.drawable.a3);
                break;*/
        }

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(img);
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
}