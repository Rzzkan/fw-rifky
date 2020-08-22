package com.proyekakhir.pelaporan.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.proyekakhir.pelaporan.R;
import com.proyekakhir.pelaporan.Utils.SpacingItemDecoration;
import com.proyekakhir.pelaporan.Utils.Tools;
import com.proyekakhir.pelaporan.Utils.ViewAnimation;
import com.proyekakhir.pelaporan.adapter.AdapterHomeMenu;
import com.proyekakhir.pelaporan.model.HomeMenu;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends Fragment {
    private RecyclerView recyclerView;
    private AdapterHomeMenu mAdapter;
    private List<HomeMenu> items;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ViewAnimation.fadeOutIn(view.findViewById(R.id.nested_scroll_view));
        Tools.setToolbar(getActivity(),"Dasbor");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getContext(), 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        //get data
        getData(getContext());

        //set data and list adapter
        mAdapter = new AdapterHomeMenu(getContext(), items);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterHomeMenu.OnItemClickListener() {
            @Override
            public void onItemClick(View view, HomeMenu obj, int position) {
                switch(obj.getId().trim().toLowerCase()) {
                    case "contact":
                        Tools.addFragment(getActivity(),new Contact() ,null,"contact");
                        break;
                    case"schedule":
                        Tools.addFragment(getActivity(), new Schedule() , null, "schedule");
                        break;
                    case"location":
                        Tools.addFragment(getActivity(), new Location() , null, "location");
                        break;
                    case"add-report":
                        Tools.addFragment(getActivity(), new AddReport(), null, "add-report");
                        break;
                }
            }
        });
        return view;
    }

    private void getData(Context ctx){
        items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.menu_icon);
        String title_arr[] = ctx.getResources().getStringArray(R.array.menu_title);
        String id_arr[] = ctx.getResources().getStringArray(R.array.menu_id);
        for (int i = 0; i < drw_arr.length(); i++) {
            HomeMenu obj = new HomeMenu();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.title = title_arr[i];
            obj.imageDrw = AppCompatResources.getDrawable(ctx, obj.image);
            obj.id =id_arr[i];
            items.add(obj);
        }
    }
}
