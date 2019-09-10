package com.force.click.cfaadmobnativesample;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.clickforce.ad.CFNativeAd;
import com.clickforce.ad.Listener.AdNativeListener;

import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper;
import com.google.android.gms.ads.mediation.customevent.CustomEventNative;
import com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdMobNativeCustomAD  implements CustomEventNative{
    private CFNativeAd cfNativeAd;
    private CustomEventNativeListener listenerCF;
    private  ImageView adImageView;
    @Override
    public void requestNativeAd(final Context context, CustomEventNativeListener customEventNativeListener,
                                String serverParameter,
                                NativeMediationAdRequest nativeMediationAdRequest,
                                Bundle bundle) {


//        Log.d("Clickforce",""+serverParameter);
        cfNativeAd = new CFNativeAd(context);
        cfNativeAd.setAdID(serverParameter);
        cfNativeAd.outputDebugInfo=true;
        listenerCF = customEventNativeListener;
        cfNativeAd.setOnNativeListener(new AdNativeListener() {
            @Override
            public void onNativeAdResult(CFNativeAd cfNativeAd) {
                Log.d("Clickforce","onNativeAdResult");

                adImageView = new ImageView(context);
                cfNativeAd.downloadAndDisplayImage(cfNativeAd.getAdCoverImage(),adImageView);
                CfUnifiedNativeAdMapper cfUnifiedNativeAdMapper = new CfUnifiedNativeAdMapper(cfNativeAd);
                listenerCF.onAdLoaded(cfUnifiedNativeAdMapper);
            }

            @Override
            public void onNativeAdClick() {
                Log.d("Clickforce","onNativeAdClick");
                listenerCF.onAdClicked();
            }

            @Override
            public void onNativeAdOnFailed() {
                Log.d("Clickforce","onNativeAdOnFailed");
                listenerCF.onAdFailedToLoad(2);
            }
        });
    }

    public class  CfUnifiedNativeAdMapper extends UnifiedNativeAdMapper{
        private final CFNativeAd cfNativeAd2;

        public CfUnifiedNativeAdMapper(CFNativeAd ad){
            cfNativeAd2 = ad;
            setHeadline(cfNativeAd2.getAdTitle());
            setBody(cfNativeAd2.getAdContent());
            setCallToAction(cfNativeAd2.getAdButtonText());
            setAdvertiser(cfNativeAd2.getAdvertiser());
            setMediaView(adImageView);

            setOverrideClickHandling(true);
            setOverrideImpressionRecording(false);

        }

        @Override
        public void trackViews(View adContainer, Map<String, View> map, Map<String, View> map1) {
            super.trackViews(adContainer, map, map1);
            List<View> CfNativeView = new ArrayList<>();
            for (Object ob : map.keySet())
            {
                CfNativeView.add(map.get(ob));
            }
            cfNativeAd2.registerViewForInteraction(adContainer,CfNativeView);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }
}
