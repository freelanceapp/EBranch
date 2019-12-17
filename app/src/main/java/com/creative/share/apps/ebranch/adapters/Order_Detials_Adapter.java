package com.creative.share.apps.ebranch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_order_detials.OrderDetialsActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_orders.OrdersActivity;
import com.creative.share.apps.ebranch.databinding.CartRowBinding;
import com.creative.share.apps.ebranch.databinding.OrderDetialsRowBinding;
import com.creative.share.apps.ebranch.models.OrderDataModel;
import com.creative.share.apps.ebranch.models.OrderModel;
import com.creative.share.apps.ebranch.models.Slider_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Order_Detials_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final OrderDetialsActivity activity;
    private List<OrderModel.OrderDetails> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private int i = 0;

    public Order_Detials_Adapter(List<OrderModel.OrderDetails> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
       this.activity = (OrderDetialsActivity) context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        OrderDetialsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_detials_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
     eventHolder.binding.setLang(lang);
     eventHolder.binding.setModel(orderlist.get(position));
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public OrderDetialsRowBinding binding;

        public EventHolder(@NonNull OrderDetialsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
