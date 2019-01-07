package my.edu.tarc.user.smartplat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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
    private SearchView searchView;
    private ListView listView;
    private ProgressDialog pDialog;
    private List<Business> BusinessList;
    private RequestQueue queue;
    private BusinessFragment.BusinessAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.business_fragment,container,false);
        setHasOptionsMenu(true);
        searchView = (SearchView) view.findViewById(R.id.searchViewBusiness);

        listView = (ListView)view.findViewById(R.id.businessmenu);
        pDialog = new ProgressDialog(getContext());
        BusinessList = new ArrayList<>();
        downloadBusiness(getContext(), Constants.URL_SELECTBUSINESS);
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
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String txt) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String txt) {
                final List<Business> filtered = new ArrayList<Business>();
                for(int pos = 0; pos < BusinessList.size(); pos++) {
                    if(BusinessList.get(pos).getTitle().toLowerCase().contains(txt)){
                        Business business =
                                new Business(BusinessList.get(pos).getTitle(),
                                        BusinessList.get(pos).getDescription(),
                                        BusinessList.get(pos).getOperationTime(),
                                        BusinessList.get(pos).getVenue(),
                                        BusinessList.get(pos).getImage());
                        filtered.add(business);
                    }
                    adapter = new BusinessFragment.BusinessAdapter(getActivity(), R.layout.business_item_layout, filtered);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), BusinessPop.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("title", filtered.get(position).getTitle());
                            bundle.putString("desc", filtered.get(position).getDescription());
                            bundle.putString("operationTime", filtered.get(position).getOperationTime());
                            bundle.putString("venue", filtered.get(position).getVenue());
                            bundle.putInt("image", filtered.get(position).getImage());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
                return false;
            }
        });
        return view;

    }
    private void downloadBusiness(Context context, String url) {
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
                            loadBusiness();
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

    private void loadBusiness() {
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


    class BusinessAdapter extends ArrayAdapter<Business> {

        private BusinessAdapter(Activity context, int resource, List<Business> list) {
            super(context, resource, list);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Business business = getItem(i);
            view = getLayoutInflater().inflate(R.layout.business_item_layout, viewGroup, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewBusiness);
            TextView textViewTitle = (TextView) view.findViewById(R.id.tv_title_business);
            TextView textViewDesc = (TextView) view.findViewById(R.id.tv_description_business);

            imageView.setImageResource(business.getImage());

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), business.getImage());
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);

            textViewTitle.setText(business.getTitle());
            textViewDesc.setText(business.getDescription());

            return view;
        }
    }
}
