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
import com.creative.share.apps.ebranch.activities_fragments.activitymarketprofile.MarketProfileActivity;
import com.creative.share.apps.ebranch.databinding.DepartmentMarketRowBinding;
import com.creative.share.apps.ebranch.databinding.SpinnerFilterRowBinding;
import com.creative.share.apps.ebranch.models.Filter_model;
import com.creative.share.apps.ebranch.models.Single_Market_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Filter_Rec_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final DepartmentDetialsActivity activity;
    private List<Filter_model> dataList;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;
    public Filter_Rec_Adapter(List<Filter_model> orderlist, Context context) {
        this.dataList = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
       this.activity = (DepartmentDetialsActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        SpinnerFilterRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_filter_row, parent, false);

        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
        eventHolder.binding.setLang(lang);
        eventHolder.binding.setFilterModel(dataList.get(position));
eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
activity.Selectitem(eventHolder.getLayoutPosition());
    }
});
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public SpinnerFilterRowBinding binding;

        public EventHolder(@NonNull SpinnerFilterRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
