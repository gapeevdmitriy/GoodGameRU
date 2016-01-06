package com.example.dgapeev.goodgameru;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;

import com.example.dgapeev.vo.RssItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {
    String pathToGoodgameRss = "http://goodgame.ru/rss";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetGoodgameRURssFeedTask().execute(pathToGoodgameRss);

    }


    private class GetGoodgameRURssFeedTask extends AsyncTask<String, Void, List<RssItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<RssItem> doInBackground(String... params) {
            List<RssItem> result = null;
            try {
                result = getGoodGameRURssFeed(params[0]);
            } catch (IOException | XmlPullParserException | ParseException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<RssItem> rssItems) {
            MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(MainActivity.this, rssItems);
            setListAdapter(adapter);
        }
    }

    private List<RssItem> getGoodGameRURssFeed(String urlOfRss) throws IOException, XmlPullParserException, ParseException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setFeature(Xml.FEATURE_RELAXED, true);
        InputStream in;
        URL url = new URL(urlOfRss);
        List<RssItem> resultList = new ArrayList<>();
        RssItem rssItem = null;
        String text = null;


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        in = conn.getInputStream();
        xpp.setInput(in, null);

        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = xpp.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagName.equalsIgnoreCase("item")) {
                        rssItem = new RssItem();
                    }
                    break;
                case XmlPullParser.TEXT:
                    text = xpp.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if (tagName.equalsIgnoreCase("item")) {
                        resultList.add(rssItem);
                    } else {
                        if (rssItem != null) {
                            setDataToItem(tagName, text, rssItem);
                        }
                    }
                    break;
            }
            eventType = xpp.next();
        }
        return resultList;

    }

    private void setDataToItem(String tagName, String tagText, RssItem rssItem) throws ParseException {

        switch (tagName) {
            case "title":
                rssItem.setTitle(tagText);
                break;
            case "link":
                rssItem.setUrl(tagText);
                break;
            case "pubDate":
                String dateParseFormat = "cc, dd MMM yyyy kk:mm:ss Z";
                SimpleDateFormat sdf = new SimpleDateFormat(dateParseFormat);
                rssItem.setDatePub(sdf.parse(tagText));
                break;
            case "description":
                rssItem.setDescription(tagText);
                break;
        }
    }
}
