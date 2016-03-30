package com.example.dgapeev.goodgameru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.example.dgapeev.vo.RssItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private static final String GOTOWEBSITE = "Go to full version";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        RssItem item = intent.getParcelableExtra("rssItem");
        fillInDetailLayout(item);

    }

    private void fillInDetailLayout(RssItem item) {
        TextView date = (TextView) findViewById(R.id.date);
        String pubDateStr = formatDate(item.getDatePub());
        date.setText(pubDateStr);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(item.getTitle());
        TextView description = (TextView) findViewById(R.id.description);
        description.setText(item.getDescription());
        TextView link = (TextView) findViewById(R.id.link);
        String linkToFullNews = "<a href=\""+item.getUrl()+"\">"+GOTOWEBSITE+"</a>";
        link.setMovementMethod(LinkMovementMethod.getInstance());
        link.setText(Html.fromHtml(linkToFullNews));
    }

    private String formatDate(Date datePub) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
        return sdf.format(datePub);
    }


}
