package com.newsolutions.newsyapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.newsolutions.newsyapp.BR;
import com.newsolutions.newsyapp.data.room.post.PostsGet;
import com.newsolutions.newsyapp.ui.home.HomeViewModel;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class testAdapter extends RecyclerView.Adapter<testAdapter.GenericViewHolder>  {
    private int layoutId;
    private List<PostsGet> userList;
    private HomeViewModel viewModel;
    private ViewGroup parent;
    private int viewType;

    public testAdapter(@LayoutRes int layoutId, HomeViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    @NotNull
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    public void setPosts(List<PostsGet> list) {
        this.userList = list;
    }
    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(HomeViewModel viewModel, Integer position) {
            binding.setVariable(BR.viewModel, viewModel);
            binding.setVariable(BR.position, position);
            binding.executePendingBindings();
        }
    }
}
