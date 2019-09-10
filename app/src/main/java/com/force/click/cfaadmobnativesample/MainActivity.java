package com.force.click.cfaadmobnativesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

public class MainActivity extends AppCompatActivity {
    private String Clickforce_ADMOB_UNIT = "ca-app-pub-7236340530869760/1920270961";
    private UnifiedNativeAd nativeAd;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = (RelativeLayout)findViewById(R.id.mRelativeLayout);

        MobileAds.initialize(this);
        AdLoader.Builder builder = new AdLoader.Builder(this, Clickforce_ADMOB_UNIT);
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (nativeAd != null){
                    nativeAd.destroy();
                }

                nativeAd = unifiedNativeAd;
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.ad_unified_official,null);
                populateUnifiedNativeAd(unifiedNativeAd,adView);
            }
        });
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener(){

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.d("UnifiedNativeAd","onAdImpression ");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("UnifiedNativeAd","AdFailedToLoad "+"Code: "+i);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.d("UnifiedNativeAd","onAdClicked");
            }
        }).build();

//        adLoader.loadAd(new PublisherAdRequest.Builder().build());
        adLoader.loadAd(new PublisherAdRequest.Builder().addTestDevice("F579CE6636912343968AEA95B49FA927").build());
    }

    private void populateUnifiedNativeAd(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView){
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));

        //tittle
        ((TextView)adView.getHeadlineView()).setText(nativeAd.getHeadline());

        //content
        if (nativeAd.getBody() == null){
            adView.getBodyView().setVisibility(View.INVISIBLE);
        }else{
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }
        //advertiser
        if (nativeAd.getAdvertiser() == null){
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        }else{
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
        }
        //coverImage
        if (nativeAd.getIcon() == null){
            adView.getIconView().setVisibility(View.GONE);
        }else{
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        //button
        if (nativeAd.getCallToAction() == null){
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        }else {
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        adView.setNativeAd(nativeAd);

        relativeLayout.removeAllViews();
        relativeLayout.addView(adView);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nativeAd.destroy();
    }
}
