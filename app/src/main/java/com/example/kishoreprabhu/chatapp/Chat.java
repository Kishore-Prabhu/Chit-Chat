package com.example.kishoreprabhu.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat extends AppCompatActivity {



    private Button send;
    private EditText msg;
    private TextView conversation;
   private String user_name,room_name;
   private DatabaseReference root;
   public String temp_key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        send=(Button)findViewById(R.id.send_msg);
        msg=(EditText)findViewById(R.id.editText);
        conversation=(TextView)findViewById(R.id.chatings);

        user_name=getIntent().getExtras().get("user_name").toString();
        room_name=getIntent().getExtras().getString("room_name").toString();

        root= FirebaseDatabase.getInstance().getReference().child(room_name);

        send.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                Map<String,Object> map=new HashMap<String, Object>();
                temp_key=root.push().getKey();
                root.updateChildren(map);

                DatabaseReference msg_root=root.child(temp_key);
                Map<String,Object> map2=new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("message",msg.getText().toString());
                msg_root.updateChildren(map2);


            }
        });


        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
    private String chat_msg,chat_username;
    private void append_chat_conversation(DataSnapshot dataSnapshot){

        Iterator i=dataSnapshot.getChildren().iterator();

        while (i.hasNext()) {
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            chat_username = (String) ((DataSnapshot) i.next()).getValue();

            conversation.append(chat_username+" : "+chat_msg+" \n ");


        }

    }

}
