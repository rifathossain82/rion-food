package com.ecommerce.myapplicationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.chef.Add_Item_fragment;
import com.ecommerce.myapplicationtest.chef.Chef_profile;
import com.ecommerce.myapplicationtest.chef.OrderFragmentChef;
import com.ecommerce.myapplicationtest.chef.Pass_orderFragment;
import com.ecommerce.myapplicationtest.customer.Cart_Fragment;
import com.ecommerce.myapplicationtest.customer.Customer_profile;
import com.ecommerce.myapplicationtest.customer.HelpCenter_customer;
import com.ecommerce.myapplicationtest.customer.Home_Fragment_costumer;
import com.ecommerce.myapplicationtest.customer.MyOrder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,Refresh_Home{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button sign_out;
    // GoogleSignInClient mGoogleSignInClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    DatabaseReference databaseReference;
    Bundle bundle;
    String e,value, category,user_name,auto_id,user_address="";


    private static final int Storage_permission_code=4655;
    private int pic_image_request=1;
    private Uri filepath;
    private Bitmap bitmap;
    StorageReference storageReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.mynavID);
        toolbar.setTitle("Current Location");

        drawerLayout = findViewById(R.id.mydrawer_id);
        NavigationView navigationView = findViewById(R.id.mynavID);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   try {
                       BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Homepage.this);
                       bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
                       bottomSheetDialog.setCanceledOnTouchOutside(false);
                       bottomSheetDialog.show();

                       supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                               .findFragmentById(R.id.map_viewFragment_id);

                       //initial fused location
                       client = LocationServices.getFusedLocationProviderClient(Homepage.this);

                       //check permission
                       if (ActivityCompat.checkSelfPermission(Homepage.this,
                               Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                           //when permission granted
                           getlocation2();
                       } else {
                           //when permission denied
                           //request permission
                           ActivityCompat.requestPermissions(Homepage.this,
                                   new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                       }
                   }catch (Exception e){
                       Toast.makeText(Homepage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   }

            }
        });

       /* //set and update user profile image start
        storageReference2= FirebaseStorage.getInstance().getReference("uploads");
        //databaseReference2= FirebaseDatabase.getInstance().getReference("uploads");
        ImageView profile_photo=findViewById(R.id.profile_image);
        profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
                upload_image();
            }
        });
        //set and update user profile image end*/

        e=getIntent().getStringExtra("email");
         value=getIntent().getStringExtra("val1");
        databaseReference= FirebaseDatabase.getInstance().getReference("users");

        //set navigation view
        if (value.equals("1")) {
            navigationView.inflateMenu(R.menu.nav_chef);
            category="Chef";
        } else if (value.equals("2")) {
            navigationView.inflateMenu(R.menu.nav_customer);
            category="Customer";
        } else if (value.equals("3")) {
            navigationView.inflateMenu(R.menu.nav_delivery_boy);
            category="Delivery Person";
        }




        //set name in header
        Query query = databaseReference.child(category).orderByChild("email").equalTo(e);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        name = snapshot1.child("name").getValue(String.class);
                        photo = snapshot1.child("photo").getValue(String.class);
                        user_name = snapshot1.child("name").getValue(String.class);
                        auto_id=snapshot1.getKey();
                    }
                    TextView title_name_nav=findViewById(R.id.profile_name);
                    CircleImageView profile_pic=findViewById(R.id.profile_image);
                    Picasso.get().load(photo).into(profile_pic);
                    title_name_nav.setText(name);
                    Toast.makeText(Homepage.this, "Your Name: "+name, Toast.LENGTH_SHORT).show();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //end of set name in side var navigation view
        //TextView title_name_nav=findViewById(R.id.profile_name);
        //title_name_nav.setText(databaseReference.toString());
//



        //initial fused location provider

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Homepage.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
            getlocation();
        } else {
            //when permission denied
            ActivityCompat.requestPermissions(Homepage.this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }

       /* //for load
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        sign_out=findViewById(R.id.sign_out_id);

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_out_id:
                        signOut();
                        break;
                }
            }
        });*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation_drawer,
                R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        toggle.syncState();

        //start page
        if (savedInstanceState == null) {
            if(category.contains("Customer")){
                bundle=new Bundle();
                bundle.putString("email",""+e);
                bundle.putString("cat",""+category);
                bundle.putString("id",""+auto_id);
                bundle.putString("address",""+user_address);
                Home_Fragment_costumer home_fragment_costumer=new Home_Fragment_costumer();
                home_fragment_costumer.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        home_fragment_costumer).commit();
                Toast.makeText(this, "Home Page", Toast.LENGTH_SHORT).show();
                getlocation();
                navigationView.setCheckedItem(R.id.nav_home);
            }
            else if(category.contains("Chef")){
                Toast.makeText(this, "Order page", Toast.LENGTH_SHORT).show();
                bundle=new Bundle();
                bundle.putString("email",""+e);
                bundle.putString("cat",""+category);
                bundle.putString("name",""+user_name);
                bundle.putString("id",""+auto_id);
                OrderFragmentChef orderFragmentChef=new OrderFragmentChef();
                orderFragmentChef.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        orderFragmentChef).commit();
                getlocation();
                navigationView.setCheckedItem(R.id.nav_order_chef);
            }

        }
    }

    private String File_extension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mine=MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cr.getType(uri));
    }


    private void upload_image() {
        if(filepath!=null){
            StorageReference file_reference=storageReference2.child(System.currentTimeMillis()
            +"."+File_extension(filepath));
            file_reference.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String s=file_reference.getDownloadUrl().toString();
                            HashMap hashMap=new HashMap();
                            hashMap.put("image",s);

                            String e=getIntent().getStringExtra("email");
                            Query query = databaseReference.orderByChild("email").equalTo(e);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String image = "";
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            image = snapshot1.child("image").getValue(String.class);
                                        }
                                        databaseReference.child(snapshot.getKey()).updateChildren(hashMap);
                                    } else {

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Homepage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        }
                    });
        }else{
            Toast.makeText(this, "No file selected.", Toast.LENGTH_SHORT).show();
        }
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
            try {
ImageView profile_photo=findViewById(R.id.profile_image);
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filepath);
                profile_photo.setImageBitmap(bitmap);
                // Toast.makeText(getApplicationContext(),getPath(filepath),Toast.LENGTH_LONG).show();
            } catch (Exception ex) {

            }
        }
    }

    //google sign_out method
    /*  private void signOut() {
          mGoogleSignInClient.signOut()
                  .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          Toast.makeText(Homepage.this, "Sign Out Successfully.", Toast.LENGTH_LONG).show();
                          finish();
                      }
                  });
      }*/
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);
            builder.setTitle("Confirm Message")
                    .setMessage("Are you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.create();
            builder.show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            //customer menu option start
            case R.id.nav_home:
                Toast.makeText(this, "Home Page", Toast.LENGTH_SHORT).show();
                bundle=new Bundle();
                bundle.putString("email",""+e);
                bundle.putString("cat",""+category);
                bundle.putString("id",""+auto_id);
                bundle.putString("address",""+user_address);
                Home_Fragment_costumer home_fragment_costumer=new Home_Fragment_costumer();
                home_fragment_costumer.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        home_fragment_costumer).commit();
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case R.id.nav_cart:
                Toast.makeText(this, "Cart page", Toast.LENGTH_SHORT).show();
                bundle=new Bundle();
                bundle.putString("email",""+e);
                bundle.putString("cat",""+category);
                bundle.putString("id",""+auto_id);
                bundle.putString("address",""+user_address);
                Cart_Fragment cart_fragment=new Cart_Fragment();
                cart_fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        cart_fragment).commit();
                navigationView.setCheckedItem(R.id.nav_cart);
                break;
            case R.id.nav_order:
                Toast.makeText(this, "Order page", Toast.LENGTH_SHORT).show();
                Intent it=new Intent(Homepage.this, MyOrder.class);
                it.putExtra("email",""+e);
                it.putExtra("cat",""+category);
                it.putExtra("id",""+auto_id);
                it.putExtra("address",""+user_address);
                startActivity(it);
                /*Cart_Fragment cart_fragment=new Cart_Fragment();
                cart_fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        cart_fragment).commit();
                //navigationView.setCheckedItem(R.id.nav_order);*/
                break;
            case R.id.nav_profile:
                Toast.makeText(this, "Profile page", Toast.LENGTH_SHORT).show();
                bundle=new Bundle();
                bundle.putString("email",""+e);
                bundle.putString("cat",""+category);
                bundle.putString("id",""+auto_id);
                bundle.putString("address",""+user_address);
                Customer_profile customer_profile=new Customer_profile();
                customer_profile.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        customer_profile).commit();
                navigationView.setCheckedItem(R.id.nav_profile);
                break;
            case R.id.nav_help:
                Toast.makeText(this, "Help Center", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Homepage.this, HelpCenter_customer.class);
                intent.putExtra("email",""+e);
                intent.putExtra("cat",""+category);
                intent.putExtra("id",""+auto_id);
                intent.putExtra("address",""+user_address);
                startActivity(intent);
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case R.id.nav_setting:
                Toast.makeText(this, "Setting page", Toast.LENGTH_SHORT).show();
                //toolbar.setSubtitle("Setting ");
                navigationView.setCheckedItem(R.id.nav_setting);
                break;

            case R.id.nav_logout_customer:
                //open dialog for logout
                log_outDialog();
                break;
            case R.id.nav_privacy:
                //open dialog for logout
                privacyDialog();
                break;
                //customer menu option end


                //chef menu option start
            case R.id.nav_order_chef:
                Toast.makeText(this, "Order page", Toast.LENGTH_SHORT).show();
                bundle=new Bundle();
                bundle.putString("email",""+e);
                bundle.putString("cat",""+category);
                bundle.putString("name",""+user_name);
                bundle.putString("id",""+auto_id);
                OrderFragmentChef orderFragmentChef=new OrderFragmentChef();
                orderFragmentChef.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        orderFragmentChef).commit();
                navigationView.setCheckedItem(R.id.nav_order_chef);
                break;
            case R.id.nav_item_chef:
                Toast.makeText(this, "Add Item page", Toast.LENGTH_SHORT).show();
                bundle=new Bundle();
                bundle.putString("email",""+e);
                bundle.putString("cat",""+category);
                bundle.putString("name",""+user_name);
                bundle.putString("id",""+auto_id);
                Add_Item_fragment addItemFragment=new Add_Item_fragment();
                addItemFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        addItemFragment).commit();
                navigationView.setCheckedItem(R.id.nav_item_chef);
                break;
            case R.id.nav_pass_Order_chef:
                Toast.makeText(this, "Pass Order page", Toast.LENGTH_SHORT).show();
                bundle=new Bundle();
                bundle.putString("email",""+e);
                bundle.putString("cat",""+category);
                bundle.putString("name",""+user_name);
                bundle.putString("id",""+auto_id);
                Pass_orderFragment pass_orderFragment=new Pass_orderFragment();
                pass_orderFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        pass_orderFragment).commit();
                navigationView.setCheckedItem(R.id.nav_pass_Order_chef);
                break;
            case R.id.nav_profile_chef:
                Toast.makeText(this, "Profile page", Toast.LENGTH_SHORT).show();
                bundle=new Bundle();
                bundle.putString("email",""+e);
                bundle.putString("cat",""+category);
                bundle.putString("id",""+auto_id);
                bundle.putString("address",""+user_address);
                Chef_profile chef_profile=new Chef_profile();
                chef_profile.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        chef_profile).commit();
                navigationView.setCheckedItem(R.id.nav_profile_chef);
                break;
            case R.id.nav_logout_chef:
                //open dialog for logout
                log_outDialog();
                break;
            case R.id.nav_privacy_chef:
                //open dialog for logout
                privacyDialog();
                break;
            case R.id.nav_help_chef:
                Toast.makeText(this, "Help Center", Toast.LENGTH_SHORT).show();
                Intent intent2=new Intent(Homepage.this, HelpCenter_customer.class);
                intent2.putExtra("email",""+e);
                intent2.putExtra("cat",""+category);
                intent2.putExtra("id",""+auto_id);
                intent2.putExtra("address",""+user_address);
                startActivity(intent2);
                navigationView.setCheckedItem(R.id.nav_help_chef);
                break;

                //chef menu option end

            //delivery boy menu option start
            case R.id.nav_logout_boy:

                break;

                //delivery boy menu option end

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location=task.getResult();
                if(location!=null){
                    try {   //initial geocoder
                    Geocoder geocoder=new Geocoder(Homepage.this
                    , Locale.getDefault());

                    //initial address list

                        List<Address> addresses=geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );
                        toolbar.setSubtitle(addresses.get(0).getAddressLine(0));
                        Toast.makeText(Homepage.this, ""+addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                        user_address=""+addresses.get(0).getAddressLine(0);
                        //to add address in firebase database
                        /*HashMap hashMap = new HashMap();
                        hashMap.put("address",add);

                        databaseReference.child(category).child(auto_id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {


                            }
                        });*/
                        // end process to add address in firebase database


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void getlocation2() {
        //initial task location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //when success
                if(location!=null){
                    //sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng=new LatLng(location.getLatitude()
                                    ,location.getLongitude());
                            //create marker option
                            MarkerOptions options=new MarkerOptions().position(latLng)
                                    .title("I am here!!");
                            //ZOOM app
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       try {
           if (requestCode == 44) {
               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   //when permission granted
                   //call method
                   getlocation2();
               }
           }
       }catch (Exception exception){
           Toast.makeText(this, "Error : "+exception, Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void setPage_fresh() {

    }

    void log_outDialog(){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.logout_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        TextView cancel=dialog.findViewById(R.id.cancel_logout);
        TextView logout=dialog.findViewById(R.id.logout_logout);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(category).child(auto_id).removeValue();
                dialog.dismiss();
                Intent intent=new Intent(Homepage.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    void privacyDialog(){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.terms_condition);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        TextView ok=dialog.findViewById(R.id.ok_terms);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}