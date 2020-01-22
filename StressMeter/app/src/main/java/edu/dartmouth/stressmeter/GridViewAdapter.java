package edu.dartmouth.stressmeter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.BaseAdapter;

/* reference:
 * https://stackoverflow.com/questions/35428106/android-custom-grid-view-adapter-with-image-and-text-error
 */

public class GridViewAdapter extends BaseAdapter {
    // Keep all Images in array
    public int[] mGridImages;
    private Context mContext;

    static class ViewHolderItem {
        ImageView imageViewItem;
    }

    public GridViewAdapter(Context c, int gridImgID){
        mContext = c;
        if (gridImgID == 0) {
            mGridImages = PSM.getGrid1();
        } else if (gridImgID == 1) {
            mGridImages = PSM.getGrid2();
        } else if (gridImgID == 2) {
            mGridImages = PSM.getGrid3();
        }
    }

    @Override
    public int getCount() {
        return mGridImages.length;
    }

    @Override
    public Object getItem(int position) {
        return mGridImages[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.imageViewItem = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }
        viewHolder.imageViewItem.setImageResource(mGridImages[position]);
        return convertView;
    }
}
