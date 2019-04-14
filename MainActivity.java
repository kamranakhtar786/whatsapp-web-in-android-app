package com.technolifehacker.whatsweb;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    LinearLayout lin_ad;
    private AdView adView_medium,adView;
    private InterstitialAd interstitialAd;
WebView web;

    String Desktop_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
String url ="https://web.whatsapp.com/\uD83C\uDF10/"+ Locale.getDefault().getLanguage();


    private static final String androidCurrent = "Linux; U; Android " + Build.VERSION.RELEASE;
    private static final String chrome = "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";



    private static final String browser = chrome;
    private static final String device = androidCurrent;
    private static final String userAgent = "Mozilla/5.0 (" + device + ") " + browser;

    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA; // "android.permission.CAMERA";
    private static final String AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO; // "android.permission.RECORD_AUDIO";
    private static final String STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE; //android.permission.WRITE_EXTERNAL_STORAGE
    private static final String[] VIDEO_PERMISSION = {CAMERA_PERMISSION, AUDIO_PERMISSION};

    private static final String WHATSAPP_WEB_URL = "https://web.whatsapp.com/\uD83C\uDF10/"+ Locale.getDefault().getLanguage();

    private static final int FILECHOOSER_RESULTCODE = 200;
    private static final int CAMERA_PERMISSION_RESULTCODE = 201;
    private static final int AUDIO_PERMISSION_RESULTCODE = 202;
    private static final int VIDEO_PERMISSION_RESULTCODE = 203;
    private static final int STORAGE_PERMISSION_RESULTCODE = 204;

    private static final String DEBUG_TAG = "WAWEBTOGO";

    private ViewGroup mainView;

    private long lastTouchClick = 0;
    private long lastBackClick = 0;
    private float lastXClick = 0;
    private float lastYClick = 0;

    boolean keyboardEnabled;
    Toast clickReminder = null;

    private SharedPreferences prefs;

    private final Activity activity = this;

    private ValueCallback<Uri[]> mUploadMessage;
    private PermissionRequest currentPermissionRequest;
   // String host = "technolifehacker.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        adView = new AdView(this, getString(R.string.banner), AdSize.BANNER_HEIGHT_50);
        // Find the Ad Container
        LinearLayout adContainer =findViewById(R.id.lin_ad);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();

        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback

                adView.loadAd();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }

        });


        web = (WebView)findViewById(R.id.webview);
        web.loadUrl(url);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setDisplayZoomControls(false);
        web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        web.setScrollbarFadingEnabled(false);
        web.getSettings().setJavaScriptEnabled(true); //for wa web
        web.getSettings().setAllowContentAccess(true); // for camera
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setAllowFileAccessFromFileURLs(true);
        web.getSettings().setAllowUniversalAccessFromFileURLs(true);
        web.getSettings().setMediaPlaybackRequiresUserGesture(false); //for audio messages
        web.getSettings().setDomStorageEnabled(true); //for html5 app
        web.getSettings().setAppCacheEnabled(true); // app cache
        web.getSettings().setAppCachePath(getCacheDir().getAbsolutePath()); //app cache
        web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); //app cache
        web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        web.setScrollbarFadingEnabled(true);
        web.getSettings().setUserAgentString(Desktop_Agent);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setDisplayZoomControls(false);
       web.setWebChromeClient(new WebChromeClient(){
          @Override
           public void onPermissionRequest(final PermissionRequest request) {
               if (request.getResources()[0].equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                   if (ContextCompat.checkSelfPermission(activity, CAMERA_PERMISSION) == PackageManager.PERMISSION_DENIED
                           && ContextCompat.checkSelfPermission(activity, AUDIO_PERMISSION) == PackageManager.PERMISSION_DENIED) {
                       ActivityCompat.requestPermissions(activity, VIDEO_PERMISSION, VIDEO_PERMISSION_RESULTCODE);
                       currentPermissionRequest = request;
                   } else if (ContextCompat.checkSelfPermission(activity, CAMERA_PERMISSION) == PackageManager.PERMISSION_DENIED) {
                       ActivityCompat.requestPermissions(activity, new String[]{CAMERA_PERMISSION}, CAMERA_PERMISSION_RESULTCODE);
                       currentPermissionRequest = request;
                   } else if (ContextCompat.checkSelfPermission(activity, AUDIO_PERMISSION) == PackageManager.PERMISSION_DENIED) {
                       ActivityCompat.requestPermissions(activity, new String[]{AUDIO_PERMISSION}, AUDIO_PERMISSION_RESULTCODE);
                       currentPermissionRequest = request;
                   } else {
                       request.grant(request.getResources());
                   }
               } else if (request.getResources()[0].equals(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                   if (ContextCompat.checkSelfPermission(activity, AUDIO_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                       request.grant(request.getResources());
                   } else {
                       ActivityCompat.requestPermissions(activity, new String[]{AUDIO_PERMISSION}, AUDIO_PERMISSION_RESULTCODE);
                       currentPermissionRequest = request;
                   }
               } else {
                   try {
                       request.grant(request.getResources());
                   } catch (RuntimeException e) {
                       Log.d(DEBUG_TAG, "Granting permissions failed", e);
                   }
               }
           }

           @Override
           public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
               mUploadMessage = filePathCallback;
               Intent chooserIntent = fileChooserParams.createIntent();
               MainActivity.this.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
               return true;
           }
       });


        //Webview client for Webview
        web.setWebViewClient(new WebViewClient(){

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.onDestroy();
    }

    @Override
    public void onBackPressed() {
       if (web.canGoBack()){
           web.goBack();
       }else {
          openDialog();
       }
    }

    @Override
    protected void onResume() {
        super.onResume();
        web.onResume();


    }

    @Override
    protected void onPause() {
        web.onPause();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mUploadMessage == null){
            return;
        }
        switch (requestCode) {
            case FILECHOOSER_RESULTCODE:
                if (resultCode == RESULT_CANCELED || data.getData() == null) {
                    mUploadMessage.onReceiveValue(null);
                } else {
                    Uri result = data.getData();
                    Uri[] results = new Uri[1];
                    results[0] = result;
                    mUploadMessage.onReceiveValue(results);
                }
                break;
            default:
                Log.d(DEBUG_TAG, "Got activity result with unknown request code " + requestCode + " - " + data.toString());
        }
    }
    private void openDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
        //  dialog.setTitle("Please Confirm!!");
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.exit,null);
        dialog.setView(v);
        dialog.setCancelable(false);
        final RelativeLayout rel = v.findViewById(R.id.rel_ad);

        adView_medium = new AdView(this, getString(R.string.medium), AdSize.RECTANGLE_HEIGHT_250);
        // Find the Ad Container
        RelativeLayout adContainer =v.findViewById(R.id.lin_medium_exit);

        // Add the ad view to your activity layout
        adContainer.addView(adView_medium);

        // Request an ad
        adView_medium.loadAd();

        adView_medium.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback

                adView_medium.loadAd();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }

        });

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        dialog.show();

        Button btn_more = v.findViewById(R.id.btn_more);
        Button btn_yes = v.findViewById(R.id.btn_yes);
        Button btn_no = v.findViewById(R.id.btn_no);
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://play.google.com/store/apps/dev?id=7242335288388906155";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_inter_ad();
                finish();

            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });



    }
    public void show_inter_ad(){
        // Instantiate an InterstitialAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        interstitialAd = new InterstitialAd(this, "3133385463354250_3133390620020401");
        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }
}
