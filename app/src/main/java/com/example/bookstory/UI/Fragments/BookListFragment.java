package com.example.bookstory.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstory.DAO.relations.BookAuthorCrossRef.BookWithAuthors;
import com.example.bookstory.DOMAIN.DBController;
import com.example.bookstory.DOMAIN.Sortables.DefaultSort;
import com.example.bookstory.DOMAIN.Sortables.Sortable;
import com.example.bookstory.DOMAIN.SortingController;
import com.example.bookstory.DOMAIN.enums.Criterion;
import com.example.bookstory.DOMAIN.enums.Order;
import com.example.bookstory.R;
import com.example.bookstory.UI.RecyclerViewAdapters.BookList;
import com.example.bookstory.UI.elements.AlgoPreferencesDialogFragment;
import com.example.bookstory.UI.elements.SortPreferencesDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BookListFragment extends Fragment
        implements SortPreferencesDialogFragment.SortPreferencesDialogListener,
        AlgoPreferencesDialogFragment.AlgoPreferencesDialogListener {

    private List<BookWithAuthors> bookWithAuthorsList;
    private RecyclerView recyclerView;
    private Sortable currentAlgorithm = new DefaultSort();
    private BookListFragmentArgs args;
    private View root;

    private Criterion criterion = Criterion.NAME_OF_TITLE;
    private Order order = Order.ASCENDING_ORDER;
    private boolean isInclude = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_book_list, container, false);
        setHasOptionsMenu(true);
        args = BookListFragmentArgs.fromBundle(getArguments());
        setOnBackPressed();
        if (args.getCharacter() == null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                    "Все книги с " + args.getCharacter().characterName);
        }

        recyclerView = root.findViewById(R.id.recyclerView_bookList);
        FloatingActionButton floatingActionButton = root.findViewById(R.id.floatingActionButton_bookList);
        floatingActionButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            BookListFragmentDirections.ActionBookListFragmentToAddBookFragment action =
                    BookListFragmentDirections.actionBookListFragmentToAddBookFragment();
            navController.navigate(action);
        });

        DBController dbController = new DBController(getContext());
        if (args.getCharacter() == null) {
            bookWithAuthorsList = dbController.getBooksWithAuthors();
        } else {
            bookWithAuthorsList = dbController.getBooksWithAuthors(args.getCharacter().characterName);
        }
        recyclerView.setAdapter(new BookList(getContext(), bookWithAuthorsList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return root;
    }

    /**
     * This method is used for setting the behaviour when the user press
     * back button
     */
    private void setOnBackPressed() {
        if (args.getCharacter() != null) {
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    NavController navController = Navigation.findNavController(root);
                    navController.navigate(BookListFragmentDirections
                            .actionBookListFragmentToBookDescriptionFragment(args.getBookForPopUp())
                    );
                }
            };
            requireActivity().getOnBackPressedDispatcher().addCallback(callback);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.sort_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItem_sortMenu_sortBy:
                SortPreferencesDialogFragment sortDialogFragment = new SortPreferencesDialogFragment(criterion, order, isInclude);
                sortDialogFragment.show(getChildFragmentManager(), SortPreferencesDialogFragment.TAG);
                return true;
            case R.id.menuItem_sortMenu_algoPref:
                AlgoPreferencesDialogFragment algoDialogFragment = new AlgoPreferencesDialogFragment(currentAlgorithm);
                algoDialogFragment.show(getChildFragmentManager(), AlgoPreferencesDialogFragment.TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void applySortPreferences(Criterion criterion, Order order, boolean isInclude) {
        this.isInclude = isInclude;
        this.criterion = criterion;
        this.order = order;

        SortingController.sort(
                bookWithAuthorsList,
                SortingController.getComparatorForBookWithAuthors(criterion, order, isInclude),
                currentAlgorithm);
        recyclerView.setAdapter(new BookList(getContext(), bookWithAuthorsList));
    }

    @Override
    public void applyAlgoPreferences(Sortable sortable) {
        if (sortable != null) {
            currentAlgorithm = sortable;
        }
    }
}