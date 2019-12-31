package com.creative.share.apps.ebranch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_Search;
import com.creative.share.apps.ebranch.activities_fragments.activity_markets.MarketActivity;
import com.creative.share.apps.ebranch.databinding.MarketRowBinding;
import com.creative.share.apps.ebranch.databinding.SearchMarketRowBinding;
import com.creative.share.apps.ebranch.models.Catogries_Market_Model;
import com.creative.share.apps.ebranch.models.Markets_Model;
import com.creative.share.apps.ebranch.models.Single_Market_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Search_Markets_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Single_Market_Model> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;
    private Fragment_Search fragment_search;
    public Search_Markets_Adapter(List<Single_Market_Model> orderlist, Context context, Fragment fragment) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
fragment_search=(Fragment_Search)fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        SearchMarketRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.search_market_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
      eventHolder.binding.setMarketmodel(orderlist.get(position));
      eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              fragment_search.displaymarketprofile(orderlist.get(eventHolder.getLayoutPosition()));
          }
      });
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public SearchMarketRowBinding binding;

        public EventHolder(@NonNull SearchMarketRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
