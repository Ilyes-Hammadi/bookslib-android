package ilyeshammadi.booklib.adapters;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ilyes on 5/26/17.
 */


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.models.Book;

public class ListBookAdapter extends RecyclerView.Adapter<ListBookAdapter.MyViewHolder> {

    private List<Book> bookList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_tv);
            description = (TextView) view.findViewById(R.id.description_tv);
        }
    }


    public ListBookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.name.setText(book.getName());
        holder.description.setText(book.getDescription());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}