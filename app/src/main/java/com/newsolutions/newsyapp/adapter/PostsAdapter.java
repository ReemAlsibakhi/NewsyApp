package com.newsolutions.newsyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.newsolutions.newsyapp.R;
import com.newsolutions.newsyapp.data.room.DataBase;
import com.newsolutions.newsyapp.data.room.post.PostsGet;
import com.newsolutions.newsyapp.ui.detail.DetailActivity;
import com.newsolutions.newsyapp.ui.home.HomeViewModel;
import com.newsolutions.newsyapp.ui.updatePost.UpdatePostActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.viewHolder> {
    private List<PostsGet> dataList = new ArrayList<>();
    Context context;
    private static final String TAG = "UserAdapter";
    HomeViewModel viewUsersViewModel;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onClicked(int position,PostsGet post);
    }
    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener=mListener;
    }
    public PostsAdapter() {
    }
    public PostsAdapter(List<PostsGet> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.tvTitle.setText(dataList.get(position).getTitle());
        Picasso.get().load(dataList.get(position).getThumbnailUrl()).into(holder.imgPost);
        holder.btn_readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailActivity.class);
                intent.putExtra("post",dataList.get(position));
                context.startActivity(intent);
                Log.e(TAG, "onClickDetail: "+dataList.get(position));
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
        });
        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mListener.onClicked(position,dataList.get(position));
              //  update(position);
            }
        });

    }
    private void update(int position) {
        Intent intent=new Intent(context, UpdatePostActivity.class);
        intent.putExtra("post",dataList.get(position));
        intent.putExtra("position",position);
        context.startActivity(intent);
        Log.e(TAG, "onClickUpdate: "+dataList.get(position)+position);
    }

    private void delete(int position) {
        DataBase.getInstance(context).postDao().deletePost(dataList.get(position));
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setPosts(List<PostsGet> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView imgPost;
        ImageButton btn_delete, btn_update;
        CardView cardView;
        Button btn_readMore;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgPost = itemView.findViewById(R.id.img_post);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_update = itemView.findViewById(R.id.btn_update);
            btn_readMore = itemView.findViewById(R.id.btn_readMore);
        }
    }
}
