package com.bashalex.cityquest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LastFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.bg_image)
    ImageView bgImage;

    private String mImageLink;

    public LastFragment() {
        // Required empty public constructor
    }


    public static LastFragment newInstance(String imageLink) {
        LastFragment fragment = new LastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, imageLink);
//        args.putInt(ARG_PARAM2, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageLink = getArguments().getString(ARG_PARAM1);
//            stepNum = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.last_screen, container, false);
        ButterKnife.bind(this, view);
        Picasso.with(getContext())
                .load(mImageLink)
                .placeholder(R.drawable.header)
                .error(R.drawable.header)
                .into(bgImage);
        return view;
    }

    @OnClick(R.id.end_btn)
    public void finishQuest() {
        getActivity().finish();
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