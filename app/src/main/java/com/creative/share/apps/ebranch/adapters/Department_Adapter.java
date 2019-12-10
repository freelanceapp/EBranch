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
import com.creative.share.apps.ebranch.databinding.DepartmentRowBinding;
import com.creative.share.apps.ebranch.models.Catogries_Model;
import com.creative.share.apps.ebranch.models.Slider_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Department_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final HomeActivity activity;
    private List<Catogries_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;
private Fragment_department fragment_department;
private Fragment fragment;
    public Department_Adapter(List<Catogries_Model.Data> orderlist, Context context, Fragment fragment) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
       this.activity = (HomeActivity) context;
       this.fragment=fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        DepartmentRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.department_row, parent, false);

        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
       eventHolder.binding.setLang(lang);
        eventHolder.binding.setCatogrymodel(orderlist.get(position));
eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(fragment instanceof  Fragment_department){
            fragment_department=(Fragment_department)fragment;
            fragment_department.DisplayDepartmentMarket(orderlist.get(eventHolder.getLayoutPosition()));
        }
    }
});
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public DepartmentRowBinding binding;

        public EventHolder(@NonNull DepartmentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
