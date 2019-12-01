package com.creative.share.apps.ebranch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.ActivityMarketProfile.MarketProfileActivity;
import com.creative.share.apps.ebranch.databinding.DepartmentHomeRowBinding;
import com.creative.share.apps.ebranch.databinding.DepartmentMarketRowBinding;
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
private MarketProfileActivity marketProfileActivity;
    public offer_Adapter(List<Slider_Model.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        this.marketProfileActivity = (MarketProfileActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DepartmentMarketRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.department_market_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
        eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marketProfileActivity.displayproduct();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public DepartmentMarketRowBinding binding;

        public EventHolder(@NonNull DepartmentMarketRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
