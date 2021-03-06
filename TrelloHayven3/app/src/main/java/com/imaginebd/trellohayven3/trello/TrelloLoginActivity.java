package com.imaginebd.trellohayven3.trello;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.imaginebd.trellohayven3.BaseActivity;


public final class TrelloLoginActivity extends BaseActivity {
    private ExclusiveLayout mExclusiveLayout;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trello_login_activity);
        setupToolbar();
        mExclusiveLayout = (ExclusiveLayout) findViewById(R.id.exclusive_layout);

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new Client());
        String url = new Uri.Builder()
                .scheme("https")
                .authority("trello.com")
                .path("1/authorize")
                .appendQueryParameter("key", TrelloCredentials.getAppKey())
                .appendQueryParameter("name", "Tomate")
                .appendQueryParameter("response_type", "token")
                .appendQueryParameter("scope", "read,write")
                .appendQueryParameter("return_url", "https://developers.trello.com")
                .appendQueryParameter("callback_method", "fragment")
                .appendQueryParameter("expiration", "never")
                .build()
                .toString();
        mWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private final class Client extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("https://developers.trello.com/#token=")) {
                String token = url.substring(url.indexOf("=") + 1);

                if (!TrelloCredentials.persistToken(view.getContext(), token)) {
                    Toast.makeText(view.getContext(), R.string.error_trello_login, Toast.LENGTH_SHORT).show();
                }

                if (SetupActivity.trelloFullySetup(TrelloLoginActivity.this)) {
                    startActivity(new Intent(TrelloLoginActivity.this, TasksActivity.class));
                } else {
                    startActivity(new Intent(TrelloLoginActivity.this, SetupActivity.class));
                }

                TrelloLoginActivity.this.finish();
            }

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mExclusiveLayout.showFirst();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mExclusiveLayout.showLast();
            super.onPageFinished(view, url);
        }
    }
}
