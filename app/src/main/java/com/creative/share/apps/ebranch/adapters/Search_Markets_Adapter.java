package com.creative.share.apps.ebranch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_markets.MarketActivity;
import com.creative.share.apps.ebranch.databinding.MarketRowBinding;
import com.creative.share.apps.ebranch.databinding.SearchMarketRowBinding;
import com.creative.share.apps.ebranch.models.Catogries_Market_Model;
import com.creative.share.apps.ebranch.models.Markets_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Search_Markets_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Markets_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;
    public Search_Markets_Adapter(List<Markets_Model.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

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
