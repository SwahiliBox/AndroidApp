package ke.co.swahilibox.swahilibox;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class AboutUs extends AppCompatActivity {

    private String url;
    private android.webkit.WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        url = "http://swahilibox.co.ke/blog/2015/02/welcome-to-swahilibox";

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int progress) {

//                final ProgressDialog progressDialog = new ProgressDialog(AboutUs.this);
//                progressDialog.setIndeterminate(true);
//                progressDialog.setMessage("Taking you to our site...");
//                progressDialog.show();
//
//                if (progress == 100) {
//                    progressDialog.dismiss();
//                }

            }
        });

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap bitmap) {
                progressDialog = new ProgressDialog(AboutUs.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Taking you to our site...");
                progressDialog.show();
                super.onPageStarted(view, url, bitmap);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });

        webView.loadUrl(url);
    }

}
