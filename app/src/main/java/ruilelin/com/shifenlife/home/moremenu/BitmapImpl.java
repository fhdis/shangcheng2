package ruilelin.com.shifenlife.home.moremenu;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.graphics.Bitmap;
import android.widget.ImageView;

import ruilelin.com.shifenlife.R;

public class BitmapImpl {

    private static BitmapImpl bitmapUtils;

    private BitmapImpl(){}

    private static DisplayImageOptions options = null;

    public void display(final ImageView imageView, String url){
        if(options == null){
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .showImageForEmptyUri(R.drawable.img_error)
                    .showImageOnFail(R.drawable.img_error)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .build();
        }

        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    public void display(final ImageView imageView, String url, int errorImageId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .showImageForEmptyUri(errorImageId)
                .showImageOnFail(errorImageId)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }

    public static BitmapImpl getInstance(){

        if(bitmapUtils == null){
            bitmapUtils = new BitmapImpl();
        }
        return bitmapUtils;
    }
}
