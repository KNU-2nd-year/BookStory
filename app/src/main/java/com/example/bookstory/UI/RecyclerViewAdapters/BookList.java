package com.example.bookstory.UI.RecyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstory.DAO.Author;
import com.example.bookstory.DAO.relations.BookAuthorCrossRef.BookWithAuthors;
import com.example.bookstory.R;
import com.example.bookstory.UI.Fragments.BookListFragmentDirections;

import java.util.List;

public class BookList extends RecyclerView.Adapter<BookList.BookRowHolder> {

    Context context;
    List<BookWithAuthors> BooksWithAuthors;

    /**
     * Constructor, that needs the current Context and
     * the List<BookWithAuthors>
     *
     * @param context Context
     * @param books List<BookWithAuthors> of a specific book
     */
    public BookList(Context context, List<BookWithAuthors> books) {
        this.context = context;
        this.BooksWithAuthors = books;
    }

    @NonNull
    @Override
    public BookRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new BookRowHolder(
                layoutInflater.inflate(R.layout.book_row, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BookRowHolder holder, int position) {
        BookWithAuthors currentBookWithAuthors = BooksWithAuthors.get(position);
        holder.bookNameTv.setText(currentBookWithAuthors.book.bookName);
        holder.bookWithAuthors = currentBookWithAuthors;
        holder.authorNameTv.setText(getAuthorsDescriptionOfBook(currentBookWithAuthors.authors));
        holder.yearOfPublishingTv.setText(String.valueOf(currentBookWithAuthors.book.yearOfPublication));
    }

    /**
     * This method convert the author list to a String according
     * to such rules:<br/>
     * if there is no authors, return the "Author is not specified"<br/>
     * if there are at least one author, return a name of each author
     * by "enter" reparation
     *
     * @param authors authors of a book
     * @return
     */
    String getAuthorsDescriptionOfBook(@NonNull List<Author> authors) {
        StringBuilder sb = new StringBuilder();
        if (authors.isEmpty()) {
            sb.append("Автор не указан");
        } else {
            sb.append(authors.get(0).authorName);
            if (authors.size() > 1) {
                sb.append(" и ещё ").append(authors.size() - 1);
            }
        }
        return sb.toString();
    }

    @Override
    public int getItemCount() {
        return BooksWithAuthors.size();
    }

    public class BookRowHolder extends RecyclerView.ViewHolder {

        TextView bookNameTv, authorNameTv, yearOfPublishingTv;
        BookWithAuthors bookWithAuthors;

        public BookRowHolder(@NonNull View itemView) {
            super(itemView);
            bookNameTv = itemView.findViewById(R.id.bookName);
            authorNameTv = itemView.findViewById(R.id.authorName);
            yearOfPublishingTv = itemView.findViewById(R.id.yearOfPublishing);
            itemView.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(v);

                BookListFragmentDirections.ActionBookListFragmentToBookDescriptionFragment actionNav =
                        BookListFragmentDirections.actionBookListFragmentToBookDescriptionFragment(
                                bookWithAuthors.book
                        );
                navController.navigate(actionNav);
            });
        }
    }
}
