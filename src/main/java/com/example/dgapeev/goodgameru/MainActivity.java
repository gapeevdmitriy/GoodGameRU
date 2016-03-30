package com.example.dgapeev.goodgameru;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dgapeev.vo.RssItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ListActivity {
    String pathToGoodgameRss = "http://goodgame.ru/rss";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getListView().setEmptyView(findViewById(R.id.empty));
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetGoodgameRURssFeedTask().execute(pathToGoodgameRss);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private class GetGoodgameRURssFeedTask extends AsyncTask<String, Void, List<RssItem>> {
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
        protected void onPostExecute(final List<RssItem> rssItems) {
                final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(MainActivity.this, rssItems);
                setListAdapter(adapter);
                final ListView listview1 = getListView();
                listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        RssItem item = adapter.getItem(position);
                        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                        intent.putExtra("rssItem",item);
                        startActivity(intent);
                    }
                });
        }
    }

    private List<RssItem> getGoodGameRURssFeed(String urlOfRss) throws XmlPullParserException, ParseException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setFeature(Xml.FEATURE_RELAXED, true);
        InputStream in;

        URL url = null;
        try {
            url = new URL(urlOfRss);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            xpp.setInput(in, null);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readFeed(xpp);
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
                String dateParseFormat = "E, dd MMM yyyy kk:mm:ss Z";
                SimpleDateFormat sdf = new SimpleDateFormat(dateParseFormat, Locale.US);
                rssItem.setDatePub(sdf.parse(tagText));
                break;
            case "description":
                rssItem.setDescription(tagText);
                break;
        }
    }

    private List<RssItem> readFeed(XmlPullParser xpp) throws XmlPullParserException, IOException, ParseException {
        String tagDescription = "description";
        List<RssItem> resultList = new ArrayList<>();
        RssItem rssItem = null;
        String text = "";
        //Variable isDescription helps to parse tag 'Description' which contents more other tags such as <p>,<a> etc
        boolean isDescriptionTag = false;

        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = xpp.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagName.equalsIgnoreCase("item")) {
                        rssItem = new RssItem();
                    }
                    //Found tag 'description' in 'item'
                    if (rssItem != null && tagName.equalsIgnoreCase(tagDescription)) {
                        isDescriptionTag = true;
                    }
                    break;
                case XmlPullParser.TEXT:
                    if (isDescriptionTag) {
                        //Concat pieces of text
                        text += xpp.getText() + " ";
                    } else {
                        text = xpp.getText();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (tagName.equalsIgnoreCase("item")) {
                        resultList.add(rssItem);
                    } else {
                        if (rssItem != null) {
                            setDataToItem(tagName, text, rssItem);
                            if (tagDescription.equalsIgnoreCase(tagName)) {
                                isDescriptionTag = false;
                                text = "";
                            }
                        }
                    }
                    break;
            }
            eventType = xpp.nextToken();
        }
        return resultList;
    }
}
