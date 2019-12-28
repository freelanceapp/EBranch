package com.creative.share.apps.ebranch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_departmnet_detials.DepartmentDetialsActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_Views;
import com.creative.share.apps.ebranch.activities_fragments.activitymarketprofile.MarketProfileActivity;
import com.creative.share.apps.ebranch.databinding.DepartmentMarketRowBinding;
import com.creative.share.apps.ebranch.databinding.OfferHomeRowBinding;
import com.creative.share.apps.ebranch.databinding.ProductsHomeRowBinding;
import com.creative.share.apps.ebranch.models.Products_Model;
import com.creative.share.apps.ebranch.models.Slider_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Offer_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Products_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;
private MarketProfileActivity marketProfileActivity;
    public Offer_Adapter(List<Products_Model.Data> orderlist, Context context) {
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


        ProductsHomeRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.products_home_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
        ((EventHolder) eventHolder).binding.setLang(lang);
        ((EventHolder) eventHolder).binding.setProductsmodel(orderlist.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(context instanceof  MarketProfileActivity){
                    marketProfileActivity=(MarketProfileActivity)context;
                    marketProfileActivity.displayproduct(orderlist.get(holder.getLayoutPosition()).getId()+"");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public ProductsHomeRowBinding  binding;

        public EventHolder(@NonNull ProductsHomeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
