package com.ecommerce.myapplicationtest.customer;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.R;
import com.ecommerce.myapplicationtest.chef.Edit_Profile_name;
import com.ecommerce.myapplicationtest.chef.Show_profile_photo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Customer_profile extends Fragment {
    CircleImageView circleImageView;
    ImageView edit;
    TextView name,phone,email,address;
    String photo_url="",e,cat,id,user_name="",user_address,phone_go="";

    DatabaseReference databaseReference;
    StorageReference storageReference;

    private int pic_image_request=1;
    private Uri filepath;
    private Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_customer_profile, container, false);

        //initial widget
        circleImageView=view.findViewById(R.id.view_Cus_profile_id);
        name=view.findViewById(R.id.view_cus_name_id);
        phone=view.findViewById(R.id.view_cus_phone_id);
        email=view.findViewById(R.id.view_cus_email_id);
        address=view.findViewById(R.id.view_cus_address_id);
        edit=view.findViewById(R.id.edit_customer_details);

        //set menu
        registerForContextMenu(circleImageView);


        //initial firebase reference
        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        storageReference= FirebaseStorage.getInstance().getReference("user_profile_photo");


        //get argument from homepage
         e=getArguments().getString("email");
         cat=getArguments().getString("cat");
         id=getArguments().getString("id");
         user_address=getArguments().getString("address");
       // Toast.makeText(getContext(), "address: "+user_address, Toast.LENGTH_SHORT).show();


        ///set email in textview
        email.setText(e);
        address.setText(user_address);
        update_address(user_address);


        //start query for get data from firebase database
        Query query = databaseReference.child(cat).orderByChild("email").equalTo(e);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name_ = "",photo_="",address_="",phone_="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        name_ = snapshot1.child("name").getValue(String.class);
                        user_name = snapshot1.child("name").getValue(String.class);
                        photo_ = snapshot1.child("photo").getValue(String.class);
                        photo_url = snapshot1.child("photo").getValue(String.class);
                        phone_ = snapshot1.child("phone").getValue(String.class);
                        phone_go = snapshot1.child("phone").getValue(String.class);
                        address_ = snapshot1.child("address").getValue(String.class);

                    }
                    name.setText(name_);
                    phone.setText(phone_);
                    address.setText(address_);
                    if(photo_.isEmpty()){

                    }
                    else{
                        Picasso.get().load(photo_).into(circleImageView);
                    }

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //end process of get data from firebase database

        //edit process start
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getActivity(), Edit_Profile.class);
                it.putExtra("cat",cat);
                it.putExtra("user",user_name);
                it.putExtra("id",id);
                it.putExtra("email",e);
                it.putExtra("phone",phone_go);

                startActivity(it);
            }
        });
        //edit process end







        return view;
    }


    //set menu option
    @Override
    public void onCreateContextMenu( ContextMenu menu,  View v,  ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu1:
                Intent it=new Intent(getActivity(),Show_profile_photo.class);
                it.putExtra("url",""+photo_url);
                startActivity(it);
                return true;
            case R.id.menu2:
                open_file_chooser();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void open_file_chooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), pic_image_request);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pic_image_request && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                circleImageView.setImageBitmap(bitmap);

                upload_photo();
                // Toast.makeText(getApplicationContext(),getPath(filepath),Toast.LENGTH_LONG).show();
            } catch (Exception ex) {

            }
        }
    }
    private String File_extension(Uri uri){
        ContentResolver cr=getActivity().getContentResolver();
        MimeTypeMap mine=MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(uri));
    }


    private void upload_photo() {
        if(filepath!=null){

            StorageReference file_reference=storageReference.child(cat).child(id+"_"+user_name+System.currentTimeMillis()
                    +"."+File_extension(filepath));
            file_reference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            file_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    update_data(uri);
                                    Toast.makeText(getContext(), "Data Add success."+uri, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }
    void update_data(Uri uri){
        HashMap hashMap=new HashMap();
        hashMap.put("photo",""+uri);
        databaseReference.child(cat).child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
            }
        });



    }
    void update_address(String address){
        HashMap hashMap=new HashMap();
        hashMap.put("address",""+address);
        databaseReference.child(cat).child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });



    }

}