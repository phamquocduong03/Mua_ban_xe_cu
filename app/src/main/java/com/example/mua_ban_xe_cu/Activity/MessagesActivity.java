package com.example.mua_ban_xe_cu.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mua_ban_xe_cu.API.ApiClient;
import com.example.mua_ban_xe_cu.API.ApiService;
import com.example.mua_ban_xe_cu.Adapter.ConversationAdapter;
import com.example.mua_ban_xe_cu.R;
import com.example.mua_ban_xe_cu.database.Conversation;
import com.example.mua_ban_xe_cu.database.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ConversationAdapter conversationAdapter;
    private List<Conversation> conversationList = new ArrayList<>();
    private ApiService apiService;
    private String currentUserEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        recyclerView = findViewById(R.id.recyclerViewMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiService = ApiClient.getClient().create(ApiService.class);

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserEmail = preferences.getString("user_email", "");

        loadConversations();
    }

    private void loadConversations() {
        apiService.getAllMessages(currentUserEmail).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Message> messages = response.body();
                    Map<String, Conversation> convMap = new HashMap<>();
                    for (Message msg : messages) {
                        // Xác định đối phương: nếu người gửi là mình -> đối phương là receiver, ngược lại
                        String otherParty = msg.getSender().equalsIgnoreCase(currentUserEmail) ? msg.getReceiver() : msg.getSender();
                        // Sử dụng key gồm carId và đối phương
                        String key = msg.getCarId() + "_" + otherParty;
                        if (!convMap.containsKey(key)) {
                            convMap.put(key, new Conversation(otherParty, msg.getCarId(), msg.getContent(), msg.getTimestamp() != null ? msg.getTimestamp() : new Date()));
                        } else {
                            Conversation conv = convMap.get(key);
                            // Cập nhật tin nhắn cuối nếu có tin nhắn mới hơn
                            if (msg.getTimestamp() != null && msg.getTimestamp().after(conv.getTimestamp())) {
                                conv.setLastMessage(msg.getContent());
                                conv.setTimestamp(msg.getTimestamp());
                            }
                        }
                    }
                    conversationList.clear();
                    conversationList.addAll(convMap.values());
                    conversationAdapter = new ConversationAdapter(conversationList);
                    recyclerView.setAdapter(conversationAdapter);
                } else {
                    Toast.makeText(MessagesActivity.this, "Không lấy được tin nhắn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(MessagesActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
