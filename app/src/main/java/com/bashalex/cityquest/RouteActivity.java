package com.bashalex.cityquest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alex Bash on 08.10.16.
 */

public class RouteActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private String[] images;
    private String[] steps;
    private String image;
    private String address;
    private String name;
    private boolean lastPoint;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        images = intent.getStringArrayExtra("images");
        steps = intent.getStringArrayExtra("way");
        address = intent.getStringExtra("address");
        image = intent.getStringExtra("image");
        name = intent.getStringExtra("name");
        lastPoint = intent.getBooleanExtra("lastPoint", false);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), images,
                steps, name, image, lastPoint);
        viewPager.setAdapter(mPagerAdapter);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        int num_of_steps;
        String[] images;
        String[] steps;
        String name;
        String image;
        boolean lastPoint;

        public ScreenSlidePagerAdapter(FragmentManager fm, String[] images, String[] steps,
                                       String name, String image, boolean lastPoint) {
            super(fm);
            num_of_steps = images.length + 2;
            this.images = images;
            this.steps = steps;
            this.name = name;
            this.image = image;
            this.lastPoint = lastPoint;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == num_of_steps - 1) {
                if (lastPoint) {
                    return LastFragment.newInstance(image);
                } else {
                    return NextStepFragment.newInstance(image);
                }
            } else if (position == num_of_steps - 2) {
                return StepFragment.newInstance(image, name, 0);
            } else {
                return StepFragment.newInstance(images[position],
                        steps[position], position + 1);
            }
        }

        @Override
        public int getCount() {
            return num_of_steps;
        }
    }
}
