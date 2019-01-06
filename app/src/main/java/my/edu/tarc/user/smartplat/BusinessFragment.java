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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusinessFragment extends Fragment {
    public static final String TAG = "my.edu.tarc.user.smartplat";
    private ListView listView;
    private ProgressDialog pDialog;
    private List<Business> BusinessList;
    private RequestQueue queue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.business_fragment,container,false);

        listView = (ListView)view.findViewById(R.id.businessmenu);
        pDialog = new ProgressDialog(getContext());
        BusinessList = new ArrayList<>();
        downloadCourse(getContext(), Constants.URL_SELECTBUSINESS);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),EventPop.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",BusinessList.get(position).getTitle());
                bundle.putString("desc",BusinessList.get(position).getDescription());
                bundle.putString("datetime",BusinessList.get(position).getOperationTime());
                bundle.putString("venue",BusinessList.get(position).getVenue());
                bundle.putInt("image",BusinessList.get(position).getImage());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    private void downloadCourse(Context context, String url) {
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
                            BusinessList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject eventResponse = (JSONObject) response.get(i);
                                String title = eventResponse.getString("title");
                                String desc = eventResponse.getString("desc");
                                String operationTime = eventResponse.getString("operationTime");
                                String venue = eventResponse.getString("venue");
                                int image = eventResponse.getInt("image");
                                Business business = new Business(title,desc,operationTime,venue,image);
                                BusinessList.add(business);
                            }
                            loadEvent();
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

    private void loadEvent() {
        final BusinessFragment.BusinessAdapter adapter = new BusinessFragment.BusinessAdapter(getActivity(), R.layout.business_item_layout, BusinessList);
        listView.setAdapter(adapter);
        if(BusinessList != null){
            int size = BusinessList.size();
            if(size > 0)
                Toast.makeText(getContext(), "No. of record : " + size + ".", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "No record found.", Toast.LENGTH_SHORT).show();
        }
    }


    class BusinessAdapter extends ArrayAdapter<Business>{

        private BusinessAdapter(Activity context, int resource, List<Business> list){
            super(context,resource,list);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup){
            Business business = getItem(i);
            view =  getLayoutInflater().inflate(R.layout.business_item_layout, viewGroup,false);

            ImageView imageView = (ImageView)view.findViewById(R.id.imageViewBusiness);
            TextView textViewTitle = (TextView)view.findViewById(R.id.tv_title_business);
            TextView textViewDesc = (TextView)view.findViewById(R.id.tv_description_business);

            imageView.setImageResource(business.getImage());
            textViewTitle.setText(business.getTitle());
            textViewDesc.setText(business.getDescription());

            return view;
        }
    }
}
