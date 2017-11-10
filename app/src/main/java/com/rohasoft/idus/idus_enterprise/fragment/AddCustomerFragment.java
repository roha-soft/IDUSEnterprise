package com.rohasoft.idus.idus_enterprise.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.rohasoft.idus.idus_enterprise.R;
import com.rohasoft.idus.idus_enterprise.imageUpload.ConnectionDetector;
import com.rohasoft.idus.idus_enterprise.imageUpload.HttpFileUpload;
import com.rohasoft.idus.idus_enterprise.other.Customer;
import com.rohasoft.idus.idus_enterprise.other.GetCustomerCallBack;
import com.rohasoft.idus.idus_enterprise.other.GetUserCallback;
import com.rohasoft.idus.idus_enterprise.other.Loan;
import com.rohasoft.idus.idus_enterprise.other.ServerRequest;
import com.rohasoft.idus.idus_enterprise.other.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddCustomerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddCustomerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCustomerFragment extends Fragment{

    EditText editText_cusName,editText_phone,editText_addr1,editText_city,editText_pincode,editText_lacMap,editText_lanMap,
    editText_remarks;
    Button button_addCustomer,button_reset;

    ImageView imgcustum, imgshop, imgidproof, imgaddrproof;
    String cusName,phone,addr1,city,pincode,lanMap,lacMap,remark;


    String imagepath = "";
    String fname;
    File file;
    private Uri selectedImage = null;
    private Bitmap bitmap, bitmapRotate;
    private Boolean upflag = false;
    private ProgressDialog pDialog;
    private ConnectionDetector cd;

    String customerImage,shopImage,idProofImage,addressProofImage,longTime;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddCustomerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCustomerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCustomerFragment newInstance(String param1, String param2) {
        AddCustomerFragment fragment = new AddCustomerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_add_customer, container, false);


        editText_cusName= (EditText) v.findViewById(R.id.edt_addcus_custmname);
        editText_phone= (EditText) v.findViewById(R.id.edt_addcus_phnno);
        editText_addr1= (EditText) v.findViewById(R.id.edt_addcus_addr1);

        editText_city= (EditText) v.findViewById(R.id.edt_addcus_city);
        editText_pincode= (EditText) v.findViewById(R.id.edt_addcus_pincode);
        editText_lacMap= (EditText) v.findViewById(R.id.edt_addcus_maplac);
        editText_lanMap= (EditText) v.findViewById(R.id.edt_addcus_maplan);
        editText_remarks= (EditText) v.findViewById(R.id.edt_addcus__remark);

        cd = new ConnectionDetector(getContext());

        imgcustum=(ImageView) v.findViewById(R.id.img_addcus_custm);
        imgshop=(ImageView) v.findViewById(R.id.img_addcus_shop1);
        imgidproof=(ImageView) v.findViewById(R.id.img_addcus_idproof);
        imgaddrproof=(ImageView) v.findViewById(R.id.img_addcus_addrproof);


        imgcustum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerImg();
            }
        });

        imgshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopImage();
            }
        });

        imgidproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IdProof();
            }
        });

        imgaddrproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressProof();
            }
        });




        button_addCustomer= (Button) v.findViewById(R.id.btn_addcus_submit);
        button_reset= (Button) v.findViewById(R.id.btn_addcus_reset);

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });






         longTime = String.valueOf(System.currentTimeMillis());






        button_addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cusName=editText_cusName.getText().toString().trim();
                phone=editText_phone.getText().toString().trim();
                addr1=editText_addr1.getText().toString().trim();

                city=editText_city.getText().toString().trim();
                pincode=editText_pincode.getText().toString().trim();
                lanMap=editText_lanMap.getText().toString().trim();
                lacMap=editText_lacMap.getText().toString().trim();
                 remark=editText_remarks.getText().toString().trim();


                if (cusName.length() > 0){
                    if (phone.length() == 10){
                        if(pincode.length()==6){
                            if(city.length() > 0){
                                User user=new User(phone);
                                authenticate(user);
                            }
                            else{
                                editText_city.setError("Please enter the City");
                            }


                        }
                        else {
                            editText_pincode.setError("please enter valid pincode");
                        }

                    }
                    else {
                        editText_phone.setError("please enter valid phone no");
                    }
                }

                else {
                    editText_cusName.setError("please fill customer name");
                }


            }
        });

        return v;
    }



    private void authenticate(User user){
        ServerRequest serverRequest=new ServerRequest(getContext());
        serverRequest.fetchPhoneDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returedUser) {
                if (returedUser == null){

                    Customer customer=new Customer(cusName,phone,addr1,city,pincode,lanMap,lacMap,customerImage,shopImage,idProofImage,addressProofImage,remark);
                    addCustomer(customer);
                    reset();


                }else {
                    editText_phone.setError("phone exists");
                }
            }
        });
    }

    private void reset() {




                editText_cusName.setText("");
                editText_phone.setText("");
                editText_addr1.setText("");
                editText_city.setText("");
                editText_pincode.setText("");
                editText_lacMap.setText("");
                editText_lanMap.setText("");
                editText_remarks.setText("");

    }



    private void addCustomer(Customer customer) {

        ServerRequest serverRequest=new ServerRequest(getContext());
        serverRequest.storeCustomerDataInBackground(customer, new GetCustomerCallBack() {
            @Override
            public void done(Customer returedCustomer) {
                Toast.makeText(getContext(),"new Customer add sucessfully",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially com.rohasoft.idus.idus_enterprise.other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void customerImg() {
        Intent cameraintent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraintent, 101);
    }
    private void ShopImage() {
        Intent cameraintent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraintent, 102);
    }
    private void IdProof() {
        Intent cameraintent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraintent, 103);
    }
    private void AddressProof() {
        Intent cameraintent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraintent, 104);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {

                case 101:
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            selectedImage = data.getData(); // the uri of the image taken
                            if (String.valueOf((Bitmap) data.getExtras().get("data")).equals("null")) {
                                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            } else {
                                bitmap = (Bitmap) data.getExtras().get("data");
                            }
                            if (Float.valueOf(getImageOrientation()) >= 0) {
                                bitmapRotate = rotateImage(bitmap, Float.valueOf(getImageOrientation()));
                            } else {
                                bitmapRotate = bitmap;
                                bitmap.recycle();
                            }


                            imgcustum.setImageBitmap(bitmapRotate);

//                            Saving image to mobile internal memory for sometime
                            String root = getContext().getFilesDir().toString();
                            File myDir = new File(root + "/androidlift");
                            myDir.mkdirs();

                            Random generator = new Random();
                            int n = 10000;
                            n = generator.nextInt(n);

//                            Give the file name that u want
                            fname = "CUS_IMG_" + longTime + ".jpg";
                            customerImage=fname;
                            imagepath = root + "/androidlift/" + fname;
                            file = new File(myDir, fname);
                            upflag = true;
                        }
                        if (cd.isConnectingToInternet()) {
                            if (!upflag) {
                                Toast.makeText(getContext(), "Image Not Captured..!", Toast.LENGTH_LONG).show();
                            } else {
                                saveFile(bitmapRotate, file);
                            }
                        } else {
                            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_LONG).show();
                        }
                    }

                    break;
                case 102:
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            selectedImage = data.getData(); // the uri of the image taken
                            if (String.valueOf((Bitmap) data.getExtras().get("data")).equals("null")) {
                                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            } else {
                                bitmap = (Bitmap) data.getExtras().get("data");
                            }
                            if (Float.valueOf(getImageOrientation()) >= 0) {
                                bitmapRotate = rotateImage(bitmap, Float.valueOf(getImageOrientation()));
                            } else {
                                bitmapRotate = bitmap;
                                bitmap.recycle();
                            }


                            imgshop.setImageBitmap(bitmapRotate);

//                            Saving image to mobile internal memory for sometime
                            String root = getContext().getFilesDir().toString();
                            File myDir = new File(root + "/androidlift");
                            myDir.mkdirs();

                            Random generator = new Random();
                            int n = 10000;
                            n = generator.nextInt(n);

//                            Give the file name that u want
                            fname = "SHOP_IMG_" + longTime + ".jpg";
                            shopImage=fname;
                            imagepath = root + "/androidlift/" + fname;
                            file = new File(myDir, fname);
                            upflag = true;
                        }
                        if (cd.isConnectingToInternet()) {
                            if (!upflag) {
                                Toast.makeText(getContext(), "Image Not Captured..!", Toast.LENGTH_LONG).show();
                            } else {
                                saveFile(bitmapRotate, file);
                            }
                        } else {
                            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_LONG).show();
                        }
                    }

                    break;
                case 103:
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            selectedImage = data.getData(); // the uri of the image taken
                            if (String.valueOf((Bitmap) data.getExtras().get("data")).equals("null")) {
                                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            } else {
                                bitmap = (Bitmap) data.getExtras().get("data");
                            }
                            if (Float.valueOf(getImageOrientation()) >= 0) {
                                bitmapRotate = rotateImage(bitmap, Float.valueOf(getImageOrientation()));
                            } else {
                                bitmapRotate = bitmap;
                                bitmap.recycle();
                            }


                            imgidproof.setImageBitmap(bitmapRotate);

//                            Saving image to mobile internal memory for sometime
                            String root = getContext().getFilesDir().toString();
                            File myDir = new File(root + "/androidlift");
                            myDir.mkdirs();

                            Random generator = new Random();
                            int n = 10000;
                            n = generator.nextInt(n);

//                            Give the file name that u want
                            fname = "ID_IMG_" + longTime + ".jpg";
                            idProofImage=fname;
                            imagepath = root + "/androidlift/" + fname;
                            file = new File(myDir, fname);
                            upflag = true;
                        }
                        if (cd.isConnectingToInternet()) {
                            if (!upflag) {
                                Toast.makeText(getContext(), "Image Not Captured..!", Toast.LENGTH_LONG).show();
                            } else {
                                saveFile(bitmapRotate, file);
                            }
                        } else {
                            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_LONG).show();
                        }
                    }

                    break;
                case 104:
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            selectedImage = data.getData(); // the uri of the image taken
                            if (String.valueOf((Bitmap) data.getExtras().get("data")).equals("null")) {
                                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            } else {
                                bitmap = (Bitmap) data.getExtras().get("data");
                            }
                            if (Float.valueOf(getImageOrientation()) >= 0) {
                                bitmapRotate = rotateImage(bitmap, Float.valueOf(getImageOrientation()));
                            } else {
                                bitmapRotate = bitmap;
                                bitmap.recycle();
                            }


                            imgaddrproof.setImageBitmap(bitmapRotate);

//                            Saving image to mobile internal memory for sometime
                            String root = getContext().getFilesDir().toString();
                            File myDir = new File(root + "/androidlift");
                            myDir.mkdirs();

                            Random generator = new Random();
                            int n = 10000;
                            n = generator.nextInt(n);

//                            Give the file name that u want
                            fname = "ADDRESS_IMG_" + longTime + ".jpg";
                            addressProofImage=fname;
                            imagepath = root + "/androidlift/" + fname;
                            file = new File(myDir, fname);
                            upflag = true;
                        }
                        if (cd.isConnectingToInternet()) {
                            if (!upflag) {
                                Toast.makeText(getContext(), "Image Not Captured..!", Toast.LENGTH_LONG).show();
                            } else {
                                saveFile(bitmapRotate, file);
                            }
                        } else {
                            Toast.makeText(getContext(), "No Internet Connection !", Toast.LENGTH_LONG).show();
                        }
                    }

                    break;

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    //    In some mobiles image will get rotate so to correting that this code will help us
    private int getImageOrientation() {
        final String[] imageColumns = {MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.ORIENTATION};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageColumns, null, null, imageOrderBy);

        if (cursor.moveToFirst()) {
            int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
            System.out.println("orientation===" + orientation);
            cursor.close();
            return orientation;
        } else {
            return 0;
        }
    }

    //    Saving file to the mobile internal memory
    private void saveFile(Bitmap sourceUri, File destination) {
        if (destination.exists()) destination.delete();
        try {
            FileOutputStream out = new FileOutputStream(destination);
            sourceUri.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
            if (cd.isConnectingToInternet()) {
                new DoFileUpload().execute();
            } else {
                Toast.makeText(getContext(), "No Internet Connection..", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DoFileUpload extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("wait uploading Image..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // Set your file path here
                FileInputStream fstrm = new FileInputStream(imagepath);
                // Set your server page url (and the file title/description)
                HttpFileUpload hfu = new HttpFileUpload("http://www.idusmarket.com/loan-app/app/file_upload.php", "ftitle", "fdescription", fname);
                upflag = hfu.Send_Now(fstrm);
            } catch (FileNotFoundException e) {
                // Error: File not found
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (upflag) {
                Toast.makeText(getContext(), "Uploading Complete", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Unfortunately file is not Uploaded..", Toast.LENGTH_LONG).show();
            }
        }
    }
}
