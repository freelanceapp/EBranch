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
import com.creative.share.apps.ebranch.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_department;
import com.creative.share.apps.ebranch.activities_fragments.activitymarketprofile.MarketProfileActivity;
import com.creative.share.apps.ebranch.databinding.DepartmentMarketRowBinding;
import com.creative.share.apps.ebranch.databinding.DepartmentRowBinding;
import com.creative.share.apps.ebranch.models.Catogries_Model;
import com.creative.share.apps.ebranch.models.Single_Market_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Market_Department_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final MarketProfileActivity activity;
    private List<Single_Market_Model.Categories> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;
    public Market_Department_Adapter(List<Single_Market_Model.Categories> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
       this.activity = (MarketProfileActivity) context;

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
        eventHolder.binding.setLang(lang);
        eventHolder.binding.setMarketcatmodel(orderlist.get(position));
eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
activity.DisplayDepartdetials(orderlist.get(eventHolder.getLayoutPosition()));
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
