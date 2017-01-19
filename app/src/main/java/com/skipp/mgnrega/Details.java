package com.skipp.mgnrega;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String cardNumber;
    JSONParser jsonParser = new JSONParser();
    static Context mContext;
    static RecyclerView recyclerView;
    WorkAdapter wa;
    RecyclerView.LayoutManager layoutManager;
    String name,dob,gender,doe;
    ArrayList<String> confirmDialog;
    int length;
    static ArrayList<String> works;
    String work;
    static int success;
    JSONArray products = null;
    static JSONParser jParser = new JSONParser();
    private static String url_all_products = "http://forceclose.org/skipp/readcard.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        works = new ArrayList<>();
        mContext = this.getApplicationContext();
        confirmDialog = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.addItemDecoration(new LineDivider(this));
        sharedPreferences = getSharedPreferences("skipp", Context.MODE_PRIVATE);
        cardNumber = sharedPreferences.getString("cardnumber","123");
        getSupportActionBar().setTitle("Jobs Available");
        new GetDetails().execute();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public static void click(int position, final Context context) {
        new MaterialDialog.Builder(context)
                .title("Confirm")
                .content("Are you sure to apply for the job "+works.get(position))
                .positiveText("Next")
                .negativeText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        new MaterialDialog.Builder(context)
                                .title("Enter number")
                                .content("Please enter your mobile number")
                                .inputType(InputType.TYPE_CLASS_NUMBER)
                                .negativeText("Cancel")
                                .positiveText("Done")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .input("Enter mobile number","", new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                            new SendNumber(""+input).execute();
                                    }
                                }).show();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    List<WorkAdapter.Info> info() {
        List<WorkAdapter.Info> data = new ArrayList<>();
        for (int i = 0; i<length;i++)
            data.add(new WorkAdapter.Info(works.get(i)));
        return data;
    }

    public class LineDivider extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public LineDivider(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.line);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

    }

    class GetDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("cardnumber",cardNumber));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);
                    length = products.length();
                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        name = c.getString("name");
                        dob = c.getString("dob");
                        gender = c.getString("gender");
                        doe = c.getString("doe");
                        System.out.println("name is:"+name);
                        confirmDialog.add("Name: "+name);
                        confirmDialog.add("DOB: "+dob);
                        confirmDialog.add("Gender: "+gender);
                        confirmDialog.add("Expiry: "+doe);

                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    //add();
                    // Closing all previous activities
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            //pDialog.dismiss();
            new MaterialDialog.Builder(Details.this)
                    .title("Confirm your Details")
                    .items(confirmDialog)
                    .positiveText("Confirm")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            new GetWorks().execute();
                        }
                    })
                    .show();

        }
    }

    class GetWorks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest("http://forceclose.org/skipp/readwork.php", "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);
                    length = products.length();
                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        work = c.getString("work");
                        works.add(work);
                    }

                } else {
                    // no products found
                    // Launch Add New product Activity
                    //add();
                    // Closing all previous activities
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            //pDialog.dismiss();
            System.out.println("Works are: "+works);
            layoutManager = new LinearLayoutManager(Details.this);
            recyclerView.setLayoutManager(layoutManager);
            wa = new WorkAdapter(info());
            recyclerView.setAdapter(wa);
        }
    }


    private static class SendNumber extends AsyncTask<String, String, String> {
        String no;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        SendNumber(String number) {
            no = number;
        }
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("number","91"+no));


            // getting JSON Object
            // Note that create product url accepts POST method
            String url_create_product = "http://forceclose.org/skipp/test.php";
            JSONObject json = jParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    System.out.println("Done");
                    // closing this screen
                } else {
                    // failed to create product
                    System.out.println("Not done");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

        }

    }


}
