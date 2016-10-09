package com.bashalex.cityquest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";


    private String mImageLink;
    private String mText;
    private int mStepId;
    private String mDistance;

    @BindView(R.id.step_image)
    ImageView imageView;

    @BindView(R.id.step_name)
    TextView textName;

    @BindView(R.id.step_description)
    TextView textDescription;

    @BindView(R.id.step_distance)
    TextView textDistance;

    @BindView(R.id.progress_bar)
    ProgressBar prBarView;

    public StepFragment() {
        // Required empty public constructor
    }


    public static StepFragment newInstance(String imageLink, String text, int stepNumber, String distance) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, imageLink);
        args.putString(ARG_PARAM2, text);
        args.putInt(ARG_PARAM3, stepNumber);
        args.putString(ARG_PARAM4, distance);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageLink = getArguments().getString(ARG_PARAM1);
            mText = getArguments().getString(ARG_PARAM2);
            mStepId = getArguments().getInt(ARG_PARAM3);
            mDistance = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_step, container, false);
        ButterKnife.bind(this, view);
        textDescription.setText(mText);
        if (mStepId == 0) {
            textName.setText("Сейчас вы находитесь рядом с");
        } else {
            Log.d("step", "" + mDistance);
            textName.setText(String.format(Locale.getDefault(), "ШАГ %d", mStepId));
            textDistance.setText(mDistance);
        }
        Picasso.with(getContext())
                .load(mImageLink)
                .placeholder(R.drawable.header)
                .error(R.drawable.header)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        prBarView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        prBarView.setVisibility(View.GONE);
                    }
                });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
