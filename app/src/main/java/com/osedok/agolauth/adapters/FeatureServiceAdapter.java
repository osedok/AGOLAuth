package com.osedok.agolauth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.osedok.agolauth.R;
import com.osedok.agolauth.models.FeatureService;

import java.util.List;

public class FeatureServiceAdapter extends ArrayAdapter<FeatureService> {


    public FeatureServiceAdapter(Context context, int resource, List<FeatureService> items) {
        super(context, resource, items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item, null);
        }

        FeatureService s = getItem(position);

        if (s != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.item_label);
            TextView tt2 = (TextView) v.findViewById(R.id.item_desc);

            if (tt1 != null) {
                tt1.setText(s.getTitle());
            }

            if (tt2 != null) {
                tt2.setText(s.getDescription());
            }


        }

        return v;
    }

}