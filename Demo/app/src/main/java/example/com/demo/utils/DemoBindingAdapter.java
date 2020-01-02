package example.com.demo.utils;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class DemoBindingAdapter {

    @BindingAdapter(
            value = {"imageUrl", "placeholder"}, requireAll = false)
    public static void loadImage(
            ImageView imageView,
            String url,
            Drawable placeHolder
    ) {
        if (StringUtil.isNullOrEmpty(url)) {
            imageView.setImageDrawable(placeHolder);
        } else {
            try {
                Picasso.with(imageView.getContext())
                        .load(url)
                        .placeholder(placeHolder)
                        .error(placeHolder)
                        .resize(
                                320, 320
                        )
                        .centerCrop()
                        .onlyScaleDown()
                        .into(imageView);
            } catch (Exception e) {
                imageView.setImageDrawable(placeHolder);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @BindingAdapter(
            value = {"duration"}, requireAll = false)
    public static void duration(TextView view,
                                long milliSecond) {
        if (milliSecond <= 0) {
            view.setText("Track Duration : 0 min");
        } else {
            long min = milliSecond / 60000;
            view.setText("Track Duration :" + min + " min");
        }

    }
}
