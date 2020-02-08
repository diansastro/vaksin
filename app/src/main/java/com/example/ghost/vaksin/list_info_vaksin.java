package com.example.ghost.vaksin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ghost on 28/06/16.
 */
public class list_info_vaksin extends ArrayAdapter<String>{
    private String[] info;
    private String[] desc;
    private Integer[] imageid;
    private Activity context;

    public list_info_vaksin(Activity context, String[] info, String[] desc, Integer[] imageid) {
        super(context, R.layout.list_info_vaksin, info);
        this.context = context;
        this.info = info;
        this.desc = desc;
        this.imageid = imageid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_info_vaksin, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageView);

        textViewName.setText(info[position]);
        textViewDesc.setText(desc[position]);
        image.setImageResource(imageid[position]);
        return  listViewItem;
    }
}
