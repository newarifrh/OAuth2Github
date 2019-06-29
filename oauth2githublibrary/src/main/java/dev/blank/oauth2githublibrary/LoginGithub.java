package dev.blank.oauth2githublibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

public class LoginGithub extends Button {
    private String kode;
    private LoginListener listener;

    public LoginGithub(Context context) {
        super(context);
        this.listener = null;
        init(context, null);
    }

    public LoginGithub(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.listener = null;
        init(context, attrs);
    }

    public LoginGithub(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.listener = null;
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoginGithub(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.listener = null;
        init(context, attrs);
    }

    public void setLoginListener(LoginListener listener) {
        this.listener = listener;
    }

    private void setKode(String kode) {
        this.kode = kode;
    }

    public String getKode() {
        return kode;
    }

    public interface LoginListener {
        public void onError(String error);
        public void onFinish(String kode);
    }

    private void init(final Context context, AttributeSet attrs) {


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Text,
                0, 0);
        final String clientId = a.getString(R.styleable.Text_clientId);

        TypedArray b = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Text,
                0, 0);
        final String redirectUri = b.getString(R.styleable.Text_redirectUri);


        final float scale = ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        setPadding((int) (13 * scale), (int) (13 * scale), (int) (13 * scale), (int) (13 * scale));
        setCompoundDrawablePadding((int) (10 * scale));
        setTextColor(Color.parseColor("#171516"));
        setTextSize((int) (6 * scale));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(ContextCompat.getDrawable(context, R.drawable.bg_stroke));
        } else {
            setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_stroke));
        }


        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_github, 0, 0, 0);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.helveticaneuemedium);
        setTypeface(typeface);
        setAllCaps(false);
        setText("Login GitHub");
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder q3 = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_webview, null);
                final WebView webView = view.findViewById(R.id.web);
                final ProgressBar progressBar = view.findViewById(R.id.loading);
                q3.setView(view);
                final AlertDialog q3Dialog = q3.create();
                q3Dialog.show();
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webView.loadUrl("https://github.com/login/oauth/authorize?client_id=" + clientId + "&scope=repo&redirect_uri=" + redirectUri);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        if (url.contains("http://45.32.101.230/callback?code=")) {
                            webView.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            String[] data = url.split("code=");
                            String kode = data[1];
                            setKode(kode);

                            if (listener != null) {
                                listener.onFinish(kode);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    q3Dialog.dismiss();
                                }
                            }, 2000);
                        }
                    }

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);

                    }

                    public void onPageFinished(WebView view, String url) {

                    }
                });
            }
        });

    }


}
