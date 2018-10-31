package com.ddf.pmay;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PendingVisit extends Fragment {

    RecyclerView grid;
    GridLayoutManager manager;
    NotificationAdapter adapter;
    List<pendingBean> list;
    ProgressBar progress;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifications, container, false);

        list = new ArrayList<>();

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progressBar3);
        manager = new GridLayoutManager(getContext(), 1);
        adapter = new NotificationAdapter(getContext(), list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        EasyLocationMod easyLocationMod = new EasyLocationMod(getContext());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        double[] l = easyLocationMod.getLatLong();
        String lat = String.valueOf(l[0]);
        String lon = String.valueOf(l[1]);

        Log.d("latitude" , lat);
        Log.d("latitude" , lon);



        progress.setVisibility(View.VISIBLE);

        bean b = (bean) Objects.requireNonNull(getActivity()).getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();

        ApiInterface cr = retrofit.create(ApiInterface.class);


        Call<List<pendingBean>> call = cr.pendingList(SharePreferenceUtils.getInstance().getString("id") , lat , lon);
        call.enqueue(new Callback<List<pendingBean>>() {
            @Override
            public void onResponse(@NonNull Call<List<pendingBean>> call, @NonNull Response<List<pendingBean>> response) {

                if (response.body() != null) {
                    adapter.setGridData(response.body());

                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<List<pendingBean>> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
        Context context;
        List<pendingBean> list;

        NotificationAdapter(Context context, List<pendingBean> list) {
            this.context = context;
            this.list = list;
        }

        void setGridData(List<pendingBean> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = null;
            if (inflater != null) {
                view = inflater.inflate(R.layout.pending_list_model, viewGroup, false);
            }
            return new ViewHolder(Objects.requireNonNull(view));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            final pendingBean item = list.get(i);

            viewHolder.name.setText(item.getName());
            viewHolder.id.setText("ID : " + item.getBid());
            viewHolder.address.setText(item.getCity() + ", " + item.getDistrict());

            viewHolder.visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context , UpdateJob.class);
                    intent.putExtra("name" , item.getName());
                    intent.putExtra("id" , item.getJobId());
                    intent.putExtra("iid" , item.getId());
                    intent.putExtra("bid" , item.getBid());
                    intent.putExtra("fname" , item.getFname());
                    intent.putExtra("state" , item.getState());
                    intent.putExtra("district" , item.getDistrict());
                    intent.putExtra("city" , item.getCity());
                    intent.putExtra("pname" , item.getPname());
                    intent.putExtra("ward" , item.getWard());
                    intent.putExtra("stage" , item.getStage());
                    context.startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView name, id, address, date;
            Button visit;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.textView14);
                id = itemView.findViewById(R.id.textView15);
                address = itemView.findViewById(R.id.textView16);
                date = itemView.findViewById(R.id.textView17);
                visit = itemView.findViewById(R.id.button5);

            }
        }
    }

}
