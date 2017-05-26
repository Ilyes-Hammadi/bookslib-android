package ilyeshammadi.booklib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ilyes on 5/26/17.
 */


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.models.Book;

import static ilyeshammadi.booklib.utils.Constants.TAG;

public class ListBookAdapter extends RecyclerView.Adapter<ListBookAdapter.MyViewHolder> {

    private List<Book> bookList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_tv);
            description = (TextView) view.findViewById(R.id.description_tv);
            thumbnail = (ImageView) view.findViewById(R.id.book_thumbnail_iv);
        }
    }


    public ListBookAdapter(Context context,List<Book> bookList) {
        this.bookList = bookList;
        this.context = context;
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

        // Set the book name
        holder.name.setText(book.getName());

        // Set the book description
        // If desc is longer then 120 chars then cut it
        if (book.getDescription().length() > 120) {
            String description = (book.getDescription()).substring(0, 119) + "...";
            holder.description.setText(description);
        } else {
            holder.description.setText(book.getDescription());
        }


        // Set the book thumbnail
        Picasso.with(context).load(book.getThumbnail_url()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void swap(ArrayList<Book> datas) {
        bookList.clear();
        bookList.addAll(datas);
        notifyDataSetChanged();
    }

}
