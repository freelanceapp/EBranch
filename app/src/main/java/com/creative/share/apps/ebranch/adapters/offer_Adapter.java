package com.creative.share.apps.ebranch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.databinding.DepartmentHomeRowBinding;
import com.creative.share.apps.ebranch.databinding.OfferHomeRowBinding;
import com.creative.share.apps.ebranch.models.Slider_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class offer_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Slider_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;

    public offer_Adapter(List<Slider_Model.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
      //  this.activity = (ProfileActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        OfferHomeRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.offer_home_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public OfferHomeRowBinding binding;

        public EventHolder(@NonNull OfferHomeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
