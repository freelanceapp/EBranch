package com.creative.share.apps.ebranch.general_ui_method;

import android.net.Uri;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class GeneralMethod {



    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter("image")
    public static void displayImage(View view,String endPoint)
    {
        if (endPoint!=null&&!endPoint.isEmpty())
        {
            if (view instanceof CircleImageView)
            {
                CircleImageView circleImageView = (CircleImageView) view;

                Picasso.with(view.getContext()).load(Uri.parse(Tags.IMAGE_URL)+endPoint).fit().into(circleImageView);
            }else if (view instanceof RoundedImageView)
            {
                RoundedImageView roundedImageView = (RoundedImageView) view;
                Picasso.with(view.getContext()).load(Uri.parse(Tags.IMAGE_URL)+endPoint).fit().into(roundedImageView);

            }else if (view instanceof ImageView)
            {
                ImageView imageView = (ImageView) view;
                Picasso.with(view.getContext()).load(Uri.parse(Tags.IMAGE_URL)+endPoint).fit().into(imageView);

            }
        }
    }

    @BindingAdapter("image")
    public static void displayImageHome(ImageView imageView, int image_resource)
    {
        imageView.setImageResource(image_resource);
    }


    @BindingAdapter("serviceImage")
    public static void serviceImage(ImageView imageView,String endPoint)
    {
        Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL+endPoint)).placeholder(R.drawable.logo).fit().into(imageView);
    }

    @BindingAdapter("profileImage")
    public static void profileImage(CircleImageView imageView, String endPoint)
    {
        Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL+endPoint)).placeholder(R.drawable.logo_txt).fit().into(imageView);
    }

    @BindingAdapter("offerImage")
    public static void offerImage(RoundedImageView imageView, String endPoint)
    {
        Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL+endPoint)).placeholder(R.drawable.logo).fit().into(imageView);
    }




    @BindingAdapter({"date"})
    public static void displayDate (TextView textView,long date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd/MMM", Locale.ENGLISH);
        String m_date = dateFormat.format(new Date(date*1000));

        textView.setText(String.format("%s",m_date));

    }


    @BindingAdapter("rate")
    public static void rate (SimpleRatingBar simpleRatingBar, double rate)
    {
        SimpleRatingBar.AnimationBuilder builder = simpleRatingBar.getAnimationBuilder()
                .setRatingTarget((float) rate)
                .setDuration(1000)
                .setRepeatCount(0)
                .setInterpolator(new LinearInterpolator());
        builder.start();
    }









}
