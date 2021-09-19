package com.example.bookstory.UI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookstory.R;
import com.example.bookstory.UI.RecyclerViewAdapters.MyAdapter;

import java.util.ArrayList;

public class BookListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_book_list, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        MyAdapter myAdapter = new MyAdapter(getContext(), new ArrayList<>());//TODO replase this ArrayList with real values
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }
}