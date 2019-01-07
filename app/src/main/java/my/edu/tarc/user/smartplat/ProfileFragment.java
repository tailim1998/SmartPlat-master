package my.edu.tarc.user.smartplat;

import android.content.Intent;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private View view;
    static private TextView textViewUser, textViewName, textViewContact, textViewEmail, textViewAddress, Email;
    private ImageButton btnName, btnEmail, btnContact, btnAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.profile_fragment, container, false);
        textViewUser = (TextView) view.findViewById(R.id.tv_Username);
        textViewName = (TextView) view.findViewById(R.id.tv_Name);
        textViewContact = (TextView) view.findViewById(R.id.tv_Phone);
        textViewEmail = (TextView) view.findViewById(R.id.tv_Email);
        textViewAddress = (TextView) view.findViewById(R.id.tv_Address);

        btnName = (ImageButton) view.findViewById(R.id.btnName);
        btnContact = (ImageButton) view.findViewById(R.id.btnPhone);
        btnEmail = (ImageButton) view.findViewById(R.id.btnEmail);
        btnAddress = (ImageButton) view.findViewById(R.id.btnAddress);

        textViewUser.setText(SharedPrefManager.getInstance(getContext()).getUsername());
        textViewEmail.setText(SharedPrefManager.getInstance(getContext()).getUserEmail());
        textViewName.setText(SharedPrefManager.getInstance(getContext()).getName());
        textViewContact.setText(SharedPrefManager.getInstance(getContext()).getUserContact());
        textViewAddress.setText(SharedPrefManager.getInstance(getContext()).getUserAddress());

        displayProfile();

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName();
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddress();
            }
        });

        return view;
        //return inflater.inflate(R.layout.profile_fragment,container,false);
    }

    private void displayProfile() {
        final String username = SharedPrefManager.getInstance(getContext()).getUsername();
        final String email = SharedPrefManager.getInstance(getContext()).getUserEmail();
        try {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_PROFILE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    textViewUser.setText(SharedPrefManager.getInstance(getContext()).getUsername());
                                    textViewEmail.setText(SharedPrefManager.getInstance(getContext()).getUserEmail());
                                    textViewName.setText(jsonObject.getString("name"));
                                    textViewContact.setText(jsonObject.getString("contact"));
                                    textViewAddress.setText(jsonObject.getString("address"));
                                    SharedPrefManager.getInstance(getContext())
                                            .userProfile(jsonObject.getInt("id"),
                                                    jsonObject.getString("name"),
                                                    jsonObject.getString("email"),
                                                    jsonObject.getString("contact"),
                                                    jsonObject.getString("address"));
                                    Toast.makeText(getContext(),
                                            jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(),
                                            jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(),
                                    error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", SharedPrefManager.getInstance(getContext()).getUsername());
                    params.put("email", SharedPrefManager.getInstance(getContext()).getUserEmail());
                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateName() {
        final String name = textViewName.getText().toString().trim();
        try {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_UPDATENAME,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    SharedPrefManager.getInstance(getContext())
                                            .setUserName(jsonObject.getString("name"));
                                    Toast.makeText(getContext(),
                                            jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(),
                                            jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(),
                                    error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", SharedPrefManager.getInstance(getContext()).getUsername());
                    params.put("name", name);
                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateContact() {
        final String contact = textViewContact.getText().toString().trim();
        try {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_UPDATECONTACT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    SharedPrefManager.getInstance(getContext())
                                            .setUserContact(jsonObject.getString("contact"));
                                    Toast.makeText(getContext(),
                                            jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(),
                                            jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(),
                                    error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", SharedPrefManager.getInstance(getContext()).getUsername());
                    params.put("contact", contact);
                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEmail() {
        final String email = textViewEmail.getText().toString().trim();
        try {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_UPDATEEMAIL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    SharedPrefManager.getInstance(getContext())
                                            .setUserEmail(jsonObject.getString("email"));
                                    MainActivity.textViewEmail.setText(jsonObject.getString("email"));
                                    Toast.makeText(getContext(),
                                            jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(),
                                            jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(),
                                    error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", SharedPrefManager.getInstance(getContext()).getUsername());
                    params.put("email", email);
                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAddress() {
        final String address = textViewAddress.getText().toString().trim();
        try {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_UPDATEADDRESS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    SharedPrefManager.getInstance(getContext())
                                            .setUserAddress(jsonObject.getString("address"));
                                    Toast.makeText(getContext(),
                                            jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getContext(),
                                            jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(),
                                    error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", SharedPrefManager.getInstance(getContext()).getUsername());
                    params.put("address", address);
                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
