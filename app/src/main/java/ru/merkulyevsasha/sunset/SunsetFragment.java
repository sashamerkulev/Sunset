package ru.merkulyevsasha.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;


public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    private boolean mSunset = true;

    AnimatorSet mAnimatorSunset;
    AnimatorSet mAnimatorSunrise;

    public static SunsetFragment newInstance(){
        return new SunsetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        mBlueSkyColor = ContextCompat.getColor(getActivity(), R.color.blu_sky);;
        mSunsetSkyColor = ContextCompat.getColor(getActivity(), R.color.sunset_sky);
        mNightSkyColor = ContextCompat.getColor(getActivity(), R.color.night_sky);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSunset) {
                    startSunsetAnimation();
                } else {
                    startSunriseAnimation();
                }
            }
        });

        return view;
    }

    private void startSunsetAnimation(){

        int startColor = mBlueSkyColor;

        mSunset = false;
        if (mAnimatorSunrise != null){
            mAnimatorSunrise.cancel();
            startColor = ((ColorDrawable)mSkyView.getBackground()).getColor();
        }

        float sunYStart = mSunView.getY();
        float sunYEnd = mSkyView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", startColor, mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(3000);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        mAnimatorSunset = new AnimatorSet();
        mAnimatorSunset.play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
        mAnimatorSunset.start();
    }

    private void startSunriseAnimation(){

        int startColor = mNightSkyColor;

        mSunset = true;
        if (mAnimatorSunset != null){
            mAnimatorSunset.cancel();
            startColor = ((ColorDrawable)mSkyView.getBackground()).getColor();
        }

        float sunYStart = mSunView.getY();
        float sunYEnd = mSunView.getTop();

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", startColor, mBlueSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        mAnimatorSunrise = new AnimatorSet();
        mAnimatorSunrise.play(heightAnimator)
                .with(sunsetSkyAnimator);
        mAnimatorSunrise.start();
    }
}
