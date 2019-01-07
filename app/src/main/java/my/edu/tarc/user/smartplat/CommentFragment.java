package my.edu.tarc.user.smartplat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentFragment extends Fragment {


    public static final String TAG = "my.edu.tarc.user.smartplat";
    private ListView listView;
    private ProgressDialog pDialog;



    EditText editTextAddComment;
    Button buttonComment;

    private List<Comment> CommentList;
    private CommentFragment.CommentAdapter adapter;
    private RequestQueue queue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.comment_fragment,container,false);

        editTextAddComment=(EditText)view.findViewById(R.id.addComment);
        buttonComment=(Button)view.findViewById(R.id.commentBtn);

        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        listView = (ListView)view.findViewById(R.id.commentmenu);
        pDialog = new ProgressDialog(getContext());
        CommentList = new ArrayList<>();
        downloadComment(getContext(), Constants.URL_SELECTCOMMENT);


        return view;
    }

    private void downloadComment(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

        if (!pDialog.isShowing())
            pDialog.setMessage("Syn with server...");
        pDialog.show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            CommentList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject eventResponse = (JSONObject) response.get(i);
                                String CommentUser = eventResponse.getString("CommentUser");
                                int CommentTime = eventResponse.getInt("CommentTime");
                                String CommentDescription = eventResponse.getString("CommentDescription");
                                int Commentlike = eventResponse.getInt("CommentLike");
                                Comment comment = new Comment(CommentUser,CommentTime,CommentDescription,Commentlike);
                                CommentList.add(comment);
                            }
                            loadComment();
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void loadComment() {
        adapter = new CommentFragment.CommentAdapter(getActivity(), R.layout.comment_page, CommentList);
        listView.setAdapter(adapter);
        if(CommentList != null){
            int size = CommentList.size();
            if(size > 0)
                Toast.makeText(getContext(), "No. of record : " + size + ".", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "No record found.", Toast.LENGTH_SHORT).show();
        }
    }


    class CommentAdapter extends ArrayAdapter<Comment> {

        private CommentAdapter(Activity context, int resource, List<Comment> list){
            super(context,resource,list);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup){
            Comment comment = getItem(i);
            view =  getLayoutInflater().inflate(R.layout.comment_page, viewGroup,false);

            TextView textViewCommentUser = (TextView)view.findViewById(R.id.coment_user_name);
            TextView textViewCommentTime = (TextView)view.findViewById(R.id.comment_time);
            TextView textViewCommentDescription=(TextView)view.findViewById(R.id.comment_description);
            TextView textViewCommentLike=(TextView)view.findViewById(R.id.comment_like);

            textViewCommentUser.setText(comment.getCommentUser());
            textViewCommentTime.setText(""+comment.getCommentTime()+" min ago...");
            textViewCommentDescription.setText(comment.getCommentDescription());
            textViewCommentLike.setText(""+comment.getCommentLike()+" likes");




            return view;
        }
    }

    private void addComment(){
        editTextAddComment=(EditText)view.findViewById(R.id.addComment);



        final String newComment = editTextAddComment.getText().toString().trim();


        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.URL_REGISTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(getContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", newComment);
                    return params;
                }
            };

            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
