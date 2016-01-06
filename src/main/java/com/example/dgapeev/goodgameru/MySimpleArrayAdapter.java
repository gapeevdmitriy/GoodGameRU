package com.example.dgapeev.goodgameru;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dgapeev.vo.RssItem;

import java.text.SimpleDateFormat;
import java.util.List;

public class MySimpleArrayAdapter extends ArrayAdapter<RssItem> {
    private final Context context;
    private final List<RssItem> values;

    public MySimpleArrayAdapter(Context context, List<RssItem> values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textViewDate = (TextView) rowView.findViewById(R.id.date);
        TextView textViewTitle = (TextView) rowView.findViewById(R.id.title);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
        String pubDate = sdf.format(values.get(position).getDatePub());
        textViewDate.setText(pubDate);
        textViewTitle.setText(values.get(position).getTitle());
        return rowView;
    }

}
