package com.example.mua_ban_xe_cu.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mua_ban_xe_cu.API.ApiClient;
import com.example.mua_ban_xe_cu.API.ApiService;
import com.example.mua_ban_xe_cu.Adapter.ChatAdapter;
import com.example.mua_ban_xe_cu.R;
import com.example.mua_ban_xe_cu.database.Message;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private Button buttonSendMessage;
    private ChatAdapter chatAdapter;
    private List<Message> chatMessages = new ArrayList<>();
    private String currentUserEmail;
    private String receiver;
    private String carId;  // ID của xe đang được chọn
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ChatActivity", "ChatActivity started");
        setContentView(R.layout.activity_chat);

        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);

        // Lấy thông tin receiver và carId từ Intent
        receiver = getIntent().getStringExtra("receiver");
        carId = getIntent().getStringExtra("carId");
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserEmail = preferences.getString("user_email", "");

        Log.d("ChatActivity", "Sender: " + currentUserEmail + ", Receiver: " + receiver + ", CarId: " + carId);

        apiService = ApiClient.getClient().create(ApiService.class);

        chatAdapter = new ChatAdapter(chatMessages, currentUserEmail);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        // Load tin nhắn của cuộc trò chuyện liên quan đến xe được chọn
        loadChatMessages();

        buttonSendMessage.setOnClickListener(v -> {
            String messageContent = editTextMessage.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                // Tạo tin nhắn mới bao gồm carId
                Message newMessage = new Message(currentUserEmail, receiver, carId, messageContent);
                apiService.sendMessage(newMessage).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            chatMessages.add(newMessage);
                            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                            recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
                            editTextMessage.setText("");
                            Log.d("ChatActivity", "Tin nhắn đã được gửi thành công.");
                        } else {
                            Log.w("ChatActivity", "Không gửi được tin nhắn, mã lỗi: " + response.code());
                            Toast.makeText(ChatActivity.this, "Lỗi khi gửi tin nhắn", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("ChatActivity", "Lỗi gửi tin nhắn: " + t.getMessage());
                        Toast.makeText(ChatActivity.this, "Lỗi kết nối đến server", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadChatMessages() {
        Log.d("ChatActivity", "Bắt đầu load tin nhắn với sender: " + currentUserEmail
                + ", receiver: " + receiver + ", carId: " + carId);
        apiService.getMessages(currentUserEmail, receiver, carId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Message> messages = response.body();
                    Log.d("ChatActivity", "API trả về " + messages.size() + " tin nhắn");
                    if (messages.size() == 0) {
                        Log.d("ChatActivity", "Danh sách tin nhắn trả về rỗng. Kiểm tra query sender="
                                + currentUserEmail + ", receiver=" + receiver + ", carId=" + carId);
                    } else {
                        for (Message m : messages) {
                            Log.d("ChatActivity", "Tin nhắn: sender=" + m.getSender()
                                    + ", receiver=" + m.getReceiver() + ", content=" + m.getContent());
                        }
                    }
                    chatMessages.clear();
                    chatMessages.addAll(messages);
                    chatAdapter.notifyDataSetChanged();
                    recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
                } else {
                    Log.w("ChatActivity", "Không tải được tin nhắn, mã lỗi: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e("ChatActivity", "Lỗi khi tải tin nhắn: " + t.getMessage());
            }
        });
    }
}
