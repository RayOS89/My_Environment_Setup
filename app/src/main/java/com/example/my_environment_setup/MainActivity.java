package com.example.my_environment_setup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {
EditText txtName, txtEmail;
TextView tvRetrieveName, tvRetrieveEmail;
Button btnSave, btnUpdate, btnRetrieve, btnClear, btnDelete;
DatabaseReference ref;
Member member;
long maxid = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Firebase connection Success",Toast.LENGTH_LONG).show();

        txtName = (EditText)findViewById(R.id.txtName);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        tvRetrieveName = (TextView)findViewById(R.id.tvRetrieveName);
        tvRetrieveEmail = (TextView)findViewById(R.id.tvRetrieveEmail);
        btnRetrieve = (Button)findViewById(R.id.btnRetrieve);
        btnClear = (Button)findViewById(R.id.btnClear);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        member = new Member();
        ref = FirebaseDatabase.getInstance().getReference().child("Member");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    maxid = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setName(txtName.getText().toString().trim());
                member.setEmail(txtEmail.getText().toString());
                ref.child((String.valueOf(maxid+1))).setValue(member);
                Toast.makeText(MainActivity.this,"Data entry successful",Toast.LENGTH_LONG).show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                HashMap hashmap = new HashMap();
                hashmap.put("name",name);
                hashmap.put("email",email);

                ref.child("1").updateChildren(hashmap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(MainActivity.this,"The Data has been successfully updated", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        btnRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference().child("Member").child("1");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name= Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                        String email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                        tvRetrieveName.setText(name);
                        tvRetrieveEmail.setText(email);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvRetrieveName.setText("");
                tvRetrieveEmail.setText("");
                txtName.setText("");
                txtEmail.setText("");
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.removeValue();
                maxid  = 0;
                Toast.makeText(MainActivity.this,"This data has been successfully deleted", Toast.LENGTH_LONG).show();
            }
        });

    }
}