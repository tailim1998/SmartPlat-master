package my.edu.tarc.user.smartplat;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class SettingFragment extends Fragment {
    private View view;
    private TextView textViewUsername, textViewPassword;
    private EditText editTextUsername, editTextOldPassword, editTextNewPassword;
    private ImageButton btnUsername, btnPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_fragment, container, false);

        textViewUsername = (TextView) view.findViewById(R.id.editUsername);

        editTextOldPassword = (EditText) view.findViewById(R.id.editPassword);
        editTextUsername = (EditText) view.findViewById(R.id.editTextChangeUsername);
        editTextNewPassword = (EditText) view.findViewById(R.id.editTextChangePassword);

        btnUsername = (ImageButton) view.findViewById(R.id.btnUsernmae);
        btnPassword = (ImageButton) view.findViewById(R.id.btnPassword);

        textViewUsername.setText(SharedPrefManager.getInstance(getContext()).getUsername());

        btnUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUsername();
                textViewUsername.setText(editTextUsername.getText());
            }
        });

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        return view;
        //return inflater.inflate(R.layout.profile_fragment,container,false);
    }



    private void updateUsername(){
        final String name = editTextUsername.getText().toString().trim();
        try {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_UPDATEUSERNAME,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    SharedPrefManager.getInstance(getContext())
                                            .setUsername(jsonObject.getString("username"));
                                    MainActivity.textViewNickName.setText(jsonObject.getString("username"));
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
                    params.put("newname", name);
                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePassword(){
        final String oldpass = editTextOldPassword.getText().toString().trim();
        final String newpass = editTextNewPassword.getText().toString().trim();
        try {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    Constants.URL_UPDATEPASSWORD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")) {
                                    Toast.makeText(getContext(),
                                            jsonObject.getString("message"),
                                            Toast.LENGTH_LONG).show();
                                    editTextOldPassword.setText("enter old password");
                                    editTextNewPassword.setText("enter new password");
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
                    params.put("oldpassword", oldpass);
                    params.put("newpassword", newpass);
                    return params;
                }
            };
            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
