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
import com.creative.share.apps.ebranch.activities_fragments.activity_cart.CartActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_department;
import com.creative.share.apps.ebranch.databinding.CartRowBinding;
import com.creative.share.apps.ebranch.databinding.DepartmentRowBinding;
import com.creative.share.apps.ebranch.models.Add_Order_Model;
import com.creative.share.apps.ebranch.models.Catogries_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Cart_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Add_Order_Model.Order_details> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
private CartActivity cartActivity;
    public Cart_Adapter(List<Add_Order_Model.Order_details> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CartRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_row, parent, false);

        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
       eventHolder.binding.setLang(lang);
        eventHolder.binding.setModel(orderlist.get(position));
eventHolder.binding.imageDelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(context instanceof  CartActivity){
            cartActivity=(CartActivity)context;
            cartActivity.removeitem(eventHolder.getLayoutPosition());
        }
    }
});
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public CartRowBinding binding;

        public EventHolder(@NonNull CartRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
