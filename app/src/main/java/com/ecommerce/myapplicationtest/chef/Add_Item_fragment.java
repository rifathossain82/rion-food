package com.ecommerce.myapplicationtest.chef;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.R;
import com.ecommerce.myapplicationtest.Signup_page;
import com.ecommerce.myapplicationtest.Upload;
import com.ecommerce.myapplicationtest.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Add_Item_fragment extends Fragment {
    EditText fname,fprice,fdetails;
    Button button,save,clear;
    ImageView fimage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String user_name,category,email_,food_name,auto_id;
    int id_=0;

    private static final int Storage_permission_code=4655;
    private int pic_image_request=1;
    private Uri filepath;
    String path;
    private Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_add__item_fragment, container, false);

        fname=view.findViewById(R.id.food_name_chef);
        fprice=view.findViewById(R.id.food_price_chef);
        fdetails=view.findViewById(R.id.food_details_chef);
        button=view.findViewById(R.id.button_image_id);
        fimage=view.findViewById(R.id.imageview_add_id);
        save=view.findViewById(R.id.savedata_add_id);
        clear=view.findViewById(R.id.cleardata_add_id);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

       String email=getArguments().getString("email");
        Toast.makeText(getContext(), email, Toast.LENGTH_SHORT).show();

        //initial this for firebase database
        databaseReference= FirebaseDatabase.getInstance().getReference("food_item_add_chef");
        storageReference= FirebaseStorage.getInstance().getReference("food_item_add_chef");


        email_=getArguments().getString("email");
        category=getArguments().getString("cat");
        user_name=getArguments().getString("name");
        auto_id=getArguments().getString("id");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=fname.getText().toString();
                String price=fprice.getText().toString();
                String details=fdetails.getText().toString();
                if(name.isEmpty()){
                    fname.setError("Food Name is Empty!!");
                    fname.requestFocus();
                }
                else if(price.isEmpty()){
                    fprice.setError("Food Price is Empty!!");
                    fprice.requestFocus();
                }
                else if(details.isEmpty()){
                    fdetails.setError("Food Details is Empty!!");
                    fdetails.requestFocus();
                }
                else if(path.isEmpty()){
                    Toast.makeText(getContext(), "Food Image was not selected.", Toast.LENGTH_SHORT).show();
                    button.requestFocus();
                }
                else{
                    //find auto id
                    Query query2 = databaseReference.child(auto_id);
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    id_ = Integer.parseInt(snapshot1.getKey());

                                }
                                id_=id_+1;
                                StorageReference file_reference=storageReference.child(auto_id).child(name+System.currentTimeMillis()
                                        +"."+File_extension(filepath));
                                file_reference.putFile(filepath)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                file_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String key=databaseReference.push().getKey();
                                                        add_data ad=new add_data(name,price,details,uri.toString(),email);
                                                        databaseReference.child(auto_id).child(""+id_).setValue(ad);
                                                        Toast.makeText(getContext(), "Add Data Successfully.", Toast.LENGTH_SHORT).show();
                                                        fname.setText("");
                                                        fprice.setText("");
                                                        fdetails.setText("");
                                                        fimage.setImageResource(R.drawable.ic_wave_2);
                                                        fname.requestFocus();
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
                            else {

                                StorageReference file_reference=storageReference.child(auto_id).child(name+System.currentTimeMillis()
                                        +"."+File_extension(filepath));
                                file_reference.putFile(filepath)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                file_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String key=databaseReference.push().getKey();
                                                        add_data ad=new add_data(name,price,details,uri.toString(),email);
                                                        databaseReference.child(auto_id).child(""+id_).setValue(ad);
                                                        Toast.makeText(getContext(), "Add Data Successfully.", Toast.LENGTH_SHORT).show();
                                                        fname.setText("");
                                                        fprice.setText("");
                                                        fdetails.setText("");
                                                        fimage.setImageResource(R.drawable.ic_wave_2);
                                                        fname.requestFocus();
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

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            //end processes to find auto id




                }
            }
        });

        return view;
    }

    private String File_extension(Uri uri){
        ContentResolver cr=getActivity().getContentResolver();
        MimeTypeMap mine=MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(uri));
    }
    private void showFileChooser() {
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
            path= String.valueOf(data.getData());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
                fimage.setImageBitmap(bitmap);
                // Toast.makeText(getApplicationContext(),getPath(filepath),Toast.LENGTH_LONG).show();
            } catch (Exception ex) {

            }
        }
      }
    }