package ruilelin.com.shifenlife.home.loadmore;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> views;

    public View convertView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        Log.d("loadmore","BaseViewHolder");
        this.views = new SparseArray<>();
        convertView = itemView;
    }

    public View getConvertView() {
        Log.d("loadmore","getConvertView");
        return convertView;
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            Log.d("loadmore","viewId=="+viewId);
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setText(int viewId, int resId) {
        TextView view = getView(viewId);
        Log.d("loadmore","setText==");
        view.setText(resId);
        return this;
    }

    public BaseViewHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        Log.d("loadmore","setText11111==");
        view.setText(text);
        return this;
    }

    public BaseViewHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        Log.d("loadmore","setTextColor==");
        view.setTextColor(color);
        return this;
    }

    public BaseViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Log.d("loadmore","linkify==");
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        Log.d("loadmore","setImageResource==");
        view.setImageResource(resId);
        return this;
    }

    public BaseViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        Log.d("loadmore","setImageDrawable11111==");
        view.setImageDrawable(drawable);
        return this;
    }

    public BaseViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        Log.d("loadmore","setImageBitmap2222==");
        view.setImageBitmap(bitmap);
        return this;
    }


    public BaseViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        Log.d("loadmore","setBackgroundColor==");
        view.setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBackground(int viewId, int resId) {
        View view = getView(viewId);
        Log.d("loadmore","setBackground==");
        view.setBackgroundResource(resId);
        return this;
    }

    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        Log.d("loadmore","setVisible==");
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public BaseViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        Log.d("loadmore","setProgress==");
        return this;
    }

    public BaseViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        Log.d("loadmore","setProgress1111==");
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public BaseViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        Log.d("loadmore","setRating==");
        view.setRating(rating);
        return this;
    }

    public BaseViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        Log.d("loadmore","setRating11111==");
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        Log.d("loadmore","setOnClickListener==");
        view.setOnClickListener(listener);
        return this;
    }

    public BaseViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        Log.d("loadmore","setOnLongClickListener==");
        view.setOnLongClickListener(listener);
        return this;
    }
}
