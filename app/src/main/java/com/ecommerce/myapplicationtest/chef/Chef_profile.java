package com.ecommerce.myapplicationtest.chef;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.Homepage;
import com.ecommerce.myapplicationtest.MainActivity;
import com.ecommerce.myapplicationtest.R;
import com.ecommerce.myapplicationtest.Refresh_Home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chef_profile extends Fragment implements chef_interface{
    RecyclerView recyclerView;
    ArrayList<MainModel> mainModels;
    MainAdapter mainAdapter;
    private List<Integer> logo;
    private List<String> name;
    DatabaseReference databaseReference,databaseReference2;
    StorageReference storageReference;
    TextView textView,phone_tv,address_tv;
    ImageView imageView,edit_dp;
    String category="", s="",user_name="",photo_url="",auto_id="",edit_name="",phone="",address="",last_address="";
    List<String> f_name;
    List<String> f_price;
    List<String> f_details;
    List<String> f_photo;
    List<String> f_email;
    List<String> f_key;
    Dialog dialog;
    Refresh_Home refresh_home;

    private int pic_image_request=1;
    private Uri filepath;
    private Bitmap bitmap;
    Iterable<DataSnapshot> dataSnapshot1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chef_profile, container, false);

        //initial resource
        imageView=view.findViewById(R.id.chef_profile_photo_id);
        edit_dp=view.findViewById(R.id.edit_profile_name);
        textView=view.findViewById(R.id.chef_hotel_name_id);
        phone_tv=view.findViewById(R.id.chef_hotel_phone_id);
        address_tv=view.findViewById(R.id.chef_hotel_address_id);

        registerForContextMenu(imageView);

        s=getArguments().getString("email");
        category=getArguments().getString("cat");
        auto_id=getArguments().getString("id");
        last_address=getArguments().getString("address");


        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference2= FirebaseDatabase.getInstance().getReference("food_item_add_chef");
        storageReference= FirebaseStorage.getInstance().getReference("user_profile_photo");


        //to update chef location address
        update_address(last_address);

        //initial list
        f_name=new ArrayList<>();
        f_price=new ArrayList<>();
        f_details=new ArrayList<>();
        f_photo=new ArrayList<>();
        f_email=new ArrayList<>();
        f_key=new ArrayList<>();


        //dialog code

        dialog=new Dialog(getContext());


        Query query = databaseReference.child(category).orderByChild("email").equalTo(s);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        name = snapshot1.child("name").getValue(String.class);
                        user_name=snapshot1.child("name").getValue(String.class);
                        photo = snapshot1.child("photo").getValue(String.class);
                        photo_url = snapshot1.child("photo").getValue(String.class);
                        phone = snapshot1.child("phone").getValue(String.class);
                        address = snapshot1.child("address").getValue(String.class);
                        dataSnapshot1=snapshot.getChildren();

                    }
                    textView.setText(name);
                    phone_tv.setText("Phone: "+phone);
                    address_tv.setText("Address: "+address);
                    if(photo.isEmpty()){

                    }
                    else{
                        Picasso.get().load(photo).into(imageView);
                    }

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //to add or update chef profile image
       /* imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                open_file_chooser();
                return true;
            }
        });*/

        //to edit profile photo

        edit_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(address.isEmpty()){
                    address="Null";
                }
                Intent it=new Intent(getActivity(),Edit_Profile_name.class);
                it.putExtra("cat",category);
                it.putExtra("user",user_name);
                it.putExtra("id",auto_id);
                it.putExtra("phone",phone);
                it.putExtra("address",address);

                startActivity(it);
            }
        });


        //to set recycler items data

        recyclerView=view.findViewById(R.id.recyclerid_chef_profile);

        Query query2 = databaseReference2.child(auto_id).orderByChild("email").equalTo(s);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        f_name.add(snapshot1.child("name").getValue(String.class));
                        f_price.add(snapshot1.child("price").getValue(String.class));
                        f_details.add(snapshot1.child("details").getValue(String.class));
                        f_photo.add(snapshot1.child("filepath").getValue(String.class));
                        f_email.add(snapshot1.child("email").getValue(String.class));
                        f_key.add(snapshot1.getKey());

                    }
                    mainAdapter=new MainAdapter(f_name,f_price,f_details,f_photo,f_email,f_key,getContext(),Chef_profile.this);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(mainAdapter);

                } else {
                    Toast.makeText(getContext(), "There is no food Item.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /*
        //To set horizontal recycler view
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(
                getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainAdapter=new MainAdapter(getContext(),logo,name);
        recyclerView.setAdapter(mainAdapter);*/



        return view;

    }

    private void update_address(String last_address) {
        HashMap hashMap=new HashMap();
        hashMap.put("address",""+last_address);
        databaseReference.child(category).child(auto_id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

            }
        });
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
                imageView.setImageBitmap(bitmap);

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

    private void upload_photo() {
        if(filepath!=null){

        StorageReference file_reference=storageReference.child(category).child(auto_id+"_"+user_name+System.currentTimeMillis()
                +"."+File_extension(filepath));
        file_reference.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        file_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                /*String key=databaseReference.push().getKey();
                                /*Query query = databaseReference.orderByChild("email").equalTo(e);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String pass_db = "",cat="";
                                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                pass_db = snapshot1.child("password").getValue(String.class);
                                            }
                                            for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                                                cat = snapshot2.child("category").getValue(String.class);
                                            }
                                            if (pass_db.equals(pa)) {
                                                if(cat.equals(category)){
                                                    Intent it = new Intent(MainActivity.this, Homepage.class);
                                                    it.putExtra("val1", "" + value);
                                                    it.putExtra("email",e);
                                                    startActivity(it);
                                                    finish();
                                                }
                                                else {
                                                    Toast.makeText(MainActivity.this, "Category isn't matching. For change category, Rerun the apps.", Toast.LENGTH_SHORT).show();
                                                }


                                            } else {
                                                pass.setError("Wrong Password");
                                                pass.requestFocus();
                                            }
                                        } else {
                                            email.setError("No such user exits.");
                                            email.requestFocus();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                                HashMap hashMap=new HashMap();
                                hashMap.put("photo",uri);
                                databaseReference.child(""+dataSnapshot1).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {

                                    }
                                });*/
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
        databaseReference.child(category).child(auto_id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        if(edit_name.isEmpty()){

        }
        else {
            textView.setText(edit_name);
        }
    }

    @Override
    public void edit(String s,String n,String p,String d) {
        openFirstDialog(s,n,p,d);
    }

    @Override
    public void delete(String s) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Message")
                .setMessage("Are you want to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference2.child(auto_id).child(s).removeValue();
                        Toast.makeText(getContext(), "Delete Success", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();


    }

    private void openFirstDialog(String id,String name2,String price2,String details2){
        dialog.setContentView(R.layout.edit_food_item);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button save;
        TextInputEditText name,price,details;
        save=dialog.findViewById(R.id.save_edit_id);
        name=dialog.findViewById(R.id.edit_fn_id);
        price=dialog.findViewById(R.id.edit_fp_id);
        details=dialog.findViewById(R.id.edit_fd_id);

        name.setText(name2);
        price.setText(price2);
        details.setText(details2);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname=name.getText().toString();
                String fprice=price.getText().toString();
                String fdetails=details.getText().toString();

                if(fname.isEmpty()){
                    name.setError("Please enter a food name");
                    name.requestFocus();
                }
                else if(fprice.isEmpty()){
                    price.setError("Please enter food price");
                    price.requestFocus();
                }
                else if(fdetails.isEmpty()){
                    details.setError("Please enter food details");
                    details.requestFocus();
                }
                else {


                    HashMap hashMap = new HashMap();
                    hashMap.put("name", fname);
                    hashMap.put("price", fprice);
                    hashMap.put("details", fdetails);

                    databaseReference2.child(auto_id).child(id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(getContext(), "Save Success", Toast.LENGTH_SHORT).show();
                            //refresh_home.setPage_fresh();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        dialog.show();

    }
}