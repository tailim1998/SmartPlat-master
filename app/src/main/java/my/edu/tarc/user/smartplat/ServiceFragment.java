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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class ServiceFragment extends Fragment {
    public static final String TAG = "my.edu.tarc.user.smartplat";
    private SearchView searchView;
    private ListView listView;
    private ProgressDialog pDialog;
    private List<Service> ServiceList;
    private ServiceFragment.ServiceAdapter adapter;
    private RequestQueue queue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.service_fragment, container, false);
        setHasOptionsMenu(true);
        searchView = (SearchView) view.findViewById(R.id.searchViewService);
        listView = (ListView) view.findViewById(R.id.servicemenu);
        pDialog = new ProgressDialog(getContext());
        ServiceList = new ArrayList<>();
        downloadService(getContext(), Constants.URL_SELECTSERVICE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ServicePop.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", ServiceList.get(position).getTitle());
                bundle.putString("desc", ServiceList.get(position).getDescription());
                bundle.putString("location", ServiceList.get(position).getLocation());
                bundle.putInt("image", ServiceList.get(position).getImage());
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
                final List<Service> filtered = new ArrayList<Service>();
                for(int pos = 0; pos < ServiceList.size(); pos++) {
                    if(ServiceList.get(pos).getTitle().toLowerCase().contains(txt)){
                        Service service =
                                new Service(ServiceList.get(pos).getTitle(),
                                        ServiceList.get(pos).getDescription(),
                                        ServiceList.get(pos).getLocation(),
                                        ServiceList.get(pos).getImage());
                        filtered.add(service);
                    }
                    adapter = new ServiceAdapter(getActivity(), R.layout.event_item_layout, filtered);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), ServicePop.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("title", filtered.get(position).getTitle());
                            bundle.putString("desc", filtered.get(position).getDescription());
                            bundle.putString("location", filtered.get(position).getLocation());
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

    private void downloadService(Context context, String url) {
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
                            ServiceList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject eventResponse = (JSONObject) response.get(i);
                                String title = eventResponse.getString("title");
                                String desc = eventResponse.getString("desc");
                                String location = eventResponse.getString("location");
                                int image = eventResponse.getInt("image");
                                Service service = new Service(title, desc, location, image);
                                ServiceList.add(service);
                            }
                            loadService();
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

    private void loadService() {
        adapter = new ServiceFragment.ServiceAdapter(getActivity(), R.layout.service_item_layout, ServiceList);
        listView.setAdapter(adapter);
        if (ServiceList != null) {
            int size = ServiceList.size();
            if (size > 0)
                Toast.makeText(getContext(), "No. of record : " + size + ".", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "No record found.", Toast.LENGTH_SHORT).show();
        }
    }


    class ServiceAdapter extends ArrayAdapter<Service> {

        private ServiceAdapter(Activity context, int resource, List<Service> list) {
            super(context, resource, list);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Service service = getItem(i);
            view = getLayoutInflater().inflate(R.layout.service_item_layout, viewGroup, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewService);
            TextView textViewTitle = (TextView) view.findViewById(R.id.tv_title);
            TextView textViewDesc = (TextView) view.findViewById(R.id.tv_description);

            imageView.setImageResource(service.getImage());

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), service.getImage());
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);

            textViewTitle.setText(service.getTitle());
            textViewDesc.setText(service.getDescription());

            return view;
        }
    }
}
