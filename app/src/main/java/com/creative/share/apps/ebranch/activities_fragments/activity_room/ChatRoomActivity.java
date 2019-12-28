package com.creative.share.apps.ebranch.activities_fragments.activity_room;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.chat_activity.ChatActivity;
import com.creative.share.apps.ebranch.adapters.Chat_Adapter;
import com.creative.share.apps.ebranch.adapters.Room_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityChatBinding;
import com.creative.share.apps.ebranch.databinding.ActivityChatRoomBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.MessageModel;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.models.UserRoomModelData;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.creative.share.apps.ebranch.remote.Api;
import com.creative.share.apps.ebranch.share.Common;
import com.creative.share.apps.ebranch.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChatRoomBinding binding;
    private String lang;
    private Preferences preferences;
    private LinearLayoutManager manager;
    private UserModel userModel;
    private List<UserRoomModelData.UserRoomModel> userRoomModels;
    private Room_Adapter room_adapter;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_room);
        initView();
        if(userModel!=null){
            getRooms();}

    }

    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
       binding.setBackListener(this);
       binding.setLang(lang);
        userRoomModels=new ArrayList<>();
        room_adapter=new Room_Adapter(userRoomModels,this);
        manager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(manager);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.recView.setAdapter(room_adapter);



    }
    public void getRooms() {
        /*
        Api.getService(Tags.base_url)
                .getRooms(userModel.getUser().getId()+"")
                .enqueue(new Callback<UserRoomModelData>() {
                    @Override
                    public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {
                            if (response.body().getData().size()>0)
                            {
                                userRoomModels.clear();
                                userRoomModels.addAll(response.body().getData());
                                binding.tvNoConversation.setVisibility(View.GONE);
                                room_adapter.notifyDataSetChanged();

                            }else
                            {
                                binding.tvNoConversation.setVisibility(View.VISIBLE);
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<UserRoomModelData> call, Throwable t) {
                        try {

                            binding.progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });*/
    }

    public void gotomessage(int receiver_id, String receiver_name, String receiver_mobile) {
        Intent intent=new Intent(this, ChatActivity.class);
        intent.putExtra("data",receiver_id+"");
        intent.putExtra("name",receiver_name);
        intent.putExtra("phone",receiver_mobile);

        startActivity(intent);
    }

    @Override
    public void back() {
        finish();
    }

}
