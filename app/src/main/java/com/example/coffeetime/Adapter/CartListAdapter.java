package com.example.coffeetime.Adapter;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coffeetime.Activity.ShowDetailActivity;
import com.example.coffeetime.Domain.CafeItem;
import com.example.coffeetime.Interface.FavIdCallback;
import com.example.coffeetime.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;




public class CartListAdapter extends RecyclerView.Adapter<CartListViewHolder> {
    private Context context;
    private Integer cafe_id;
    private ArrayList<CafeItem> products;
    private String email, token;
    private Integer restorantId;

    public CartListAdapter(Context context, String email, String token) {
        this.token = token;
        this.context = context;
        this.products = new ArrayList<>();
        this.email = email;
    }

    public void updateProducts(ArrayList<CafeItem> newProducts) {
        products.clear();
        products.addAll(newProducts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.cafe_item, parent, false);
        return new CartListViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListViewHolder holder, int position) {
        ArrayList<CafeItem> sortedProducts = new ArrayList<>(products);
        Collections.sort(sortedProducts, new Comparator<CafeItem>() {
            @Override
            public int compare(CafeItem o1, CafeItem o2) {
                // Сравниваем по убыванию рейтинга
                return Double.compare(o2.getStar(), o1.getStar());
            }
        });

        CafeItem product = products.get(position);


        holder.productTitles.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));

        Picasso.get().load(product.getPicture()).into(holder.productImage);

        holder.grade.setText(String.valueOf(product.getStar()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                        new Pair<View, String>(holder.productImage, "productImage"));
                restorantId = product.getId();
                cafe_id = product.getId();
                Log.e("rest_id", String.valueOf(restorantId));
                Intent intent = new Intent(holder.itemView.getContext(), ShowDetailActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("access_token", token);
                Log.e("TAG_TOKEN_LOG",token);
                intent.putExtra("object", product);
                intent.putExtra("restorantId", restorantId);
                intent.putExtra("cafe_id",cafe_id);

                holder.itemView.getContext().startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setTouchHelper(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback touchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CafeItem deletedItem = products.get(position); // Получаем удаляемый элемент
                int cafe_id = deletedItem.getId();
                System.out.println("!!!!!!!" + deletedItem.getFavId());// Получаем ID удаляемого ресторана
                System.out.println(cafe_id);
                deletedItem.DisplayInfo(deletedItem);
                // Выполняем DELETE запрос для удаления элемента по ID
                getFavIdFromServer(cafe_id);


                // Удаление элемента из списка и уведомление адаптера
                products.remove(position);
                notifyItemRemoved(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    private void getFavIdFromServer(int cafeId) {
        String url = "https://losermaru.pythonanywhere.com/favorite/";
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Обработка успешного ответа GET запроса
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject cafeObject = response.getJSONObject(i);
                                int fetchedCafeId = cafeObject.getInt("cafe_id");

                                int favId = cafeObject.getInt("id");
                                System.out.println("1232222" + fetchedCafeId);
                                System.out.println("1232223" + cafeId);
                                Log.e("TAG_CAFE_DESC","Cafe ID: " + fetchedCafeId + ", Fav ID: " + favId);
                                if (fetchedCafeId == cafeId) {

                                    // cafe_id совпадает с заданным значением


                                    // Далее можно использовать полученный fav_id
                                    executeDeleteRequest(favId);
                                    return;
                                }else {
                                   Log.e("TAG_ПОплач","ПОПЛАЧ");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Обработка ошибки GET запроса
                        System.out.println("Не удалось получить список кафе: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }

    private void executeDeleteRequest(int favId) {
        String url = "https://losermaru.pythonanywhere.com/favorite/" + favId;
        System.out.println("Test!!!!!" + favId);
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("УДАЛИЛ");
                        // Обработка успешного ответа DELETE запроса
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Обработка ошибки DELETE запроса
                        System.out.println(" НЕ УДАЛИЛ");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(stringRequest);

    }
}

