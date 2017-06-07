package ilyeshammadi.booklib.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ilyeshammadi.booklib.R;
import ilyeshammadi.booklib.activities.BookDetailActivity;
import ilyeshammadi.booklib.models.Book;
import ilyeshammadi.booklib.models.Comment;

/**
 * Created by ilyes on 6/7/17.
 */

public class SimilarBooksAdapter extends RecyclerView.Adapter<SimilarBooksAdapter.MyViewHolder> {

    private List<Book> books;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.book_similar_name_tv);
            image = (ImageView) view.findViewById(R.id.book_similar_image_iv);
        }
    }


    public SimilarBooksAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public SimilarBooksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.similar_books_list_item, parent, false);

        return new SimilarBooksAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Book book = books.get(position);

        // Set the name
        if(book.getName().length() > 20) {
            String name = (book.getName().substring(0, 19)) + "...";
            holder.name.setText(name);
        } else {
            holder.name.setText(book.getName());
        }

        // Set the image
        Picasso.with(context).load(book.getThumbnail_url()).into(holder.image);

        // On image click
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Book detail activity with the book id in the extra
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("book-id", book.getId());
                intent.putExtra("book-name", book.getName());
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return books.size();
    }


    public void swap(ArrayList<Book> datas){
        books = datas;
        notifyDataSetChanged();
    }
}