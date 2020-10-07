//Copyright by Thai Dang thaidangminhbk3i@gmail.com
//         Permission is hereby granted, free of charge, to any person obtaining a copy
//        of this software and associated documentation files (the "Software"), to deal
//        in the Software without restriction, including without limitation the rights
//        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//        copies of the Software, and to permit persons to whom the Software is
//        furnished to do so, subject to the following conditions:
//
//        The above copyright notice and this permission notice shall be included in all
//        copies or substantial portions of the Software.
//
//        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//        SOFTWARE.
package com.thaidang.webparser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText result;
    private TextView tv_bid, tv_ask, tv_change, tv_percentage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.tv);
        tv_bid = findViewById(R.id.tv_bid);
        tv_ask = findViewById(R.id.tv_ask);
        tv_change = findViewById(R.id.tv_add_minus);
        tv_percentage = findViewById(R.id.tv_percentage);
//        getWebsite();
        new DownloadData().execute("");
    }


    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
//                    https://cafef.vn/gia-vang.html
//                    https://tuoitre.vn/gia-vang.html
                    Document doc = Jsoup.connect("https://www.kitco.com").get();
                    String title = doc.title();
                    Elements gold_ask = doc.select("td#lgq-ask");
                    Elements gold_bid = doc.select("td#lgq-bid");
                    Elements gold_change = doc.select("td#lgq-chg");
                    Elements gold_percentage = doc.select("td#lgq-chg-percent");

                    builder.append(gold_bid.get(0).text()+","+gold_ask.get(0).text()+","+gold_change.get(0).text()+","+gold_percentage.get(0).text());

                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String temp= builder.toString();
                        result.setText(builder.toString());
                        String[] arrOfStr = temp.split(",");
                        if(arrOfStr.length==4)
                        {
                            String bid = arrOfStr[0];
                            String ask = arrOfStr[1];
                            String change=arrOfStr[2];
                            String percentage=arrOfStr[3];
                            tv_ask.setText(ask);
                            tv_bid.setText(bid);
                            tv_change.setText(change);
                            tv_percentage.setText(percentage);
                            if(change.contains("+"))
                            {
                                tv_change.setTextColor(Color.parseColor("#00FF00"));
                                tv_percentage.setTextColor(Color.parseColor("#00FF00"));
                            }
                            else
                            {
                                tv_change.setTextColor(Color.parseColor("#FF0000"));
                                tv_percentage.setTextColor(Color.parseColor("#FF0000"));
                            }
                        }

                    }
                });
            }
        }).start();
    }

    public void onClick(View view) {
//        getWebsite();
        new DownloadData().execute("");
    }

    class DownloadData extends AsyncTask<String, Integer, String> {
        ProgressDialog pd = null;
        StringBuilder builder ;
        @Override
        protected String doInBackground(String... strings) {
            try {
//                    https://cafef.vn/gia-vang.html
//                    https://tuoitre.vn/gia-vang.html
                Document doc = Jsoup.connect("https://www.kitco.com").get();
                String title = doc.title();
                Elements gold_ask = doc.select("td#lgq-ask");
                Elements gold_bid = doc.select("td#lgq-bid");
                Elements gold_change = doc.select("td#lgq-chg");
                Elements gold_percentage = doc.select("td#lgq-chg-percent");

                builder.append(gold_bid.get(0).text()+","+gold_ask.get(0).text()+","+gold_change.get(0).text()+","+gold_percentage.get(0).text());

            } catch (IOException e) {
                builder.append("Error : ").append(e.getMessage()).append("\n");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            builder= new StringBuilder();
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle(getResources().getString(R.string.updating));
            pd.setMessage(getResources().getString(R.string.loading));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (null != pd && pd.isShowing()) {
                pd.dismiss();
            }

            String temp= builder.toString();
//            result.setText(builder.toString());
            if(temp.length()>3) {
                String[] arrOfStr = temp.split(",");
                if (arrOfStr.length == 4) {
                    String bid = arrOfStr[0];
                    String ask = arrOfStr[1];
                    String change = arrOfStr[2];
                    String percentage = arrOfStr[3];
                    tv_ask.setText(ask);
                    tv_bid.setText(bid);
                    tv_change.setText(change);
                    tv_percentage.setText(percentage);
                    if (change.contains("+")) {
                        tv_change.setTextColor(Color.parseColor("#00FF00"));
                        tv_percentage.setTextColor(Color.parseColor("#00FF00"));
                    } else {
                        tv_change.setTextColor(Color.parseColor("#FF0000"));
                        tv_percentage.setTextColor(Color.parseColor("#FF0000"));
                    }
                }
            }
        }
        }
    }


