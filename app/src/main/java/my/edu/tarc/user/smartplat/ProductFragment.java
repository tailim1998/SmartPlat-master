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

public class ProductFragment extends Fragment {
    public static final String TAG = "my.edu.tarc.user.smartplat";
    private SearchView searchView;
    private ListView listView;
    private ProgressDialog pDialog;
    private List<Product> ProductList;
    private RequestQueue queue;
    private ProductFragment.ProductAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_fragment,container,false);
        setHasOptionsMenu(true);
        searchView = (SearchView) view.findViewById(R.id.searchViewProduct);

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
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String txt) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String txt) {
                final List<Product> filtered = new ArrayList<Product>();
                for(int pos = 0; pos < ProductList.size(); pos++) {
                    if(ProductList.get(pos).getTitle().toLowerCase().contains(txt)){
                        Product product =
                                new Product(ProductList.get(pos).getTitle(),
                                        ProductList.get(pos).getDescription(),
                                        ProductList.get(pos).getPrice(),
                                        ProductList.get(pos).getLocation(),
                                        ProductList.get(pos).getImage());
                        filtered.add(product);
                    }
                    adapter = new ProductFragment.ProductAdapter(getActivity(), R.layout.product_item_layout, filtered);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), ProductPop.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("title", filtered.get(position).getTitle());
                            bundle.putString("desc", filtered.get(position).getDescription());
                            bundle.putDouble("price", filtered.get(position).getPrice());
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

        private ProductAdapter(Activity context, int resource, List<Product> list) {
            super(context, resource, list);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Product product = getItem(i);
            view = getLayoutInflater().inflate(R.layout.product_item_layout, viewGroup, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageViewProduct);
            TextView textViewTitle = (TextView) view.findViewById(R.id.tv_title_product);
            TextView textViewDesc = (TextView) view.findViewById(R.id.tv_description_product);

            imageView.setImageResource(product.getImage());

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), product.getImage());
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);

            textViewTitle.setText(product.getTitle());
            textViewDesc.setText(product.getDescription());

            return view;
        }
    }


}
