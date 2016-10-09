package com.bashalex.cityquest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NextStepFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.next_btn)
    Button nextBtn;

    @BindView(R.id.lost_btn)
    Button lostBtn;

    @BindView(R.id.bg_image)
    ImageView bgImage;

    private String mImageLink;
    private int stepNum;

    public NextStepFragment() {
        // Required empty public constructor
    }


    public static NextStepFragment newInstance(String imageLink) {
        NextStepFragment fragment = new NextStepFragment();
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
        View view = inflater.inflate(R.layout.next_point, container, false);
        ButterKnife.bind(this, view);
        Picasso.with(getContext())
                .load(mImageLink)
                .placeholder(R.drawable.header)
                .error(R.drawable.header)
                .into(bgImage);
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

    @OnClick(R.id.next_btn)
    public void loadNextStep() {
        API.getRoute(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getError() != null) {
                        --API.objectNum;
                        return;
                    }
                    Log.d("response", "" + response);
                    Intent intent = new Intent(getContext(), RouteActivity.class);
                    intent.putExtra("name", response.getName());
                    intent.putExtra("images", response.getImages());
                    intent.putExtra("way", response.getWay());
                    intent.putExtra("address", response.getAddress());
                    intent.putExtra("image", response.getImage());
                    intent.putExtra("lastPoint", response.isLast());
                    startActivity(intent);
                    this.getActivity().finish();
                });
    }
}