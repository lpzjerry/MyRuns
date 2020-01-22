package edu.dartmouth.stressmeter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.GridView;
import android.widget.BaseAdapter;

/* reference:
 * https://stackoverflow.com/questions/35428106/android-custom-grid-view-adapter-with-image-and-text-error
 */

public class GridViewAdapter extends BaseAdapter {
    // Keep all Images in array
    public int[] mThumbIds = PSM.getGrid1();
    private Context mContext;
    // our ViewHolder.
    static class ViewHolderItem {
        ImageView imageViewItem;
    }

    // Constructor
    public GridViewAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.imageViewItem = (ImageView) convertView.findViewById(R.id.imageView);
            // store the holder with the view.
            convertView.setTag(viewHolder);
        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }
        // object item based on the position
        viewHolder.imageViewItem.setImageResource(mThumbIds[position]);
        return convertView;
    }
}
