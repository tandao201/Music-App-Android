package com.example.appnhac.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appnhac.Adapters.SearchResultAdapter;
import com.example.appnhac.Models.BaiHat;
import com.example.appnhac.R;
import com.example.appnhac.Services.APIService;
import com.example.appnhac.Services.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTimKiem extends Fragment {

    View view;
    Toolbar toolbar;
    RecyclerView recyclerViewSearch;
    TextView notiNoDataTextView;
    SearchResultAdapter searchResultAdapter;
    ArrayList<BaiHat> songs;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tim_kiem,container,false);
        init();
        return view;
    }
    private void init(){
        toolbar = view.findViewById(R.id.toolBarSearch);
        recyclerViewSearch = view.findViewById(R.id.recyclerSearchBaiHat);
        notiNoDataTextView = view.findViewById(R.id.textViewNoData);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_view,menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!s.isEmpty())
                    searchTuKhoa(s);
                if( songs!=null ){
                    if(songs.size()>0)
                        songs.clear();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void searchTuKhoa(String tukhoa){
        Dataservice dataservice  = APIService.getService();
        Call<List<BaiHat>> callback = dataservice.getBaiHatTheoSearch(tukhoa);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                songs = (ArrayList<BaiHat>) response.body();
                if (songs.size()>0){
                    searchResultAdapter = new SearchResultAdapter(getActivity(),songs);
                    recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerViewSearch.setAdapter(searchResultAdapter);
                    notiNoDataTextView.setVisibility(View.GONE);
                    recyclerViewSearch.setVisibility(View.VISIBLE);
                } else {
                    notiNoDataTextView.setVisibility(View.VISIBLE);
                    recyclerViewSearch.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });

    }
}
