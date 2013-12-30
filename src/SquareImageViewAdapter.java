/**
 * Created with IntelliJ IDEA.
 * User: scottomalley
 * Date: 15/11/2013
 * Time: 12:43
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * Created with IntelliJ IDEA.
 * User: scottomalley
 * Date: 15/11/2013
 * Time: 12:41
 * To change this template use File | Settings | File Templates.
 */
public class SquareImageViewAdapter extends BaseAdapter {
    private Context mContext;
    private SquareImageView[] images;

    // Gets the context so it can be used later
    public SquareImageViewAdapter(Context c) {
        mContext = c;
    }

    // Total number of things contained within the adapter
    public int getCount() {
        return images.length;
    }

    // Require for structure, not really used in my code.
    public Object getItem(int position) {
        return null;
    }

    // Require for structure, not really used in my code. Can
    // be used to get the id of an item in the adapter for
    // manual control.
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SquareImageView imgView;
        imgView = (SquareImageView) convertView;
        imgView.setBackgroundColor(Color.RED);
        imgView.setId(position);


        return imgView;
    }
}