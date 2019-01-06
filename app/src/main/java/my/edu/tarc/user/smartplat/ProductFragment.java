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

public class ProductFragment extends Fragment {
    public static final String TAG = "my.edu.tarc.user.smartplat";
    private ListView listView;
    private ProgressDialog pDialog;
    private List<Product> ProductList;
    private RequestQueue queue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_fragment,container,false);

        listView = (ListView)view.findViewById(R.id.productmenu);
        pDialog = new ProgressDialog(getContext());
        ProductList = new ArrayList<>();
        downloadCourse(getContext(), Constants.URL_SELECTPRODUCT);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ProductPop.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",ProductList.get(position).getTitle());
                bundle.putString("desc",ProductList.get(position).getDescription());
                bundle.putDouble("price",ProductList.get(position).getPrice());
                bundle.putString("location",ProductList.get(position).getLocation());
                bundle.putInt("image",ProductList.get(position).getImage());
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
                            ProductList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject eventResponse = (JSONObject) response.get(i);
                                String title = eventResponse.getString("title");
                                String desc = eventResponse.getString("desc");
                                double price = eventResponse.getDouble("price");
                                String location = eventResponse.getString("location");
                                int image = eventResponse.getInt("image");
                                Product product = new Product(title,desc,price,location,image);
                                ProductList.add(product);
                            }
                            loadProduct();
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

    private void loadProduct() {
        final ProductFragment.ProductAdapter adapter = new ProductFragment.ProductAdapter(getActivity(), R.layout.product_item_layout, ProductList);
        listView.setAdapter(adapter);
        if(ProductList != null){
            int size = ProductList.size();
            if(size > 0)
                Toast.makeText(getContext(), "No. of record : " + size + ".", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "No record found.", Toast.LENGTH_SHORT).show();
        }
    }


    class ProductAdapter extends ArrayAdapter<Product> {

        private ProductAdapter(Activity context, int resource, List<Product> list){
            super(context,resource,list);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup){
            Product product = getItem(i);
            view =  getLayoutInflater().inflate(R.layout.event_item_layout, viewGroup,false);

            ImageView imageView = (ImageView)view.findViewById(R.id.imageViewEvent);
            TextView textViewTitle = (TextView)view.findViewById(R.id.tv_title);
            TextView textViewDesc = (TextView)view.findViewById(R.id.tv_description);

            imageView.setImageResource(product.getImage());
            textViewTitle.setText(product.getTitle());
            textViewDesc.setText(product.getDescription());

            return view;
        }
    }
}
