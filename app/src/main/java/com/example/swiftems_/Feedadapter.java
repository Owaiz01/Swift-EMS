package com.example.swiftems_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Feedadapter extends RecyclerView.Adapter<Feedadapter.ViewHolder>{

    Context context;
    List<Feedmodel> feedmodelList;

    public Feedadapter(Context context, List<Feedmodel> feedmodelList) {
        this.context = context;
        this.feedmodelList = feedmodelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(feedmodelList.get(position).getName());
        holder.email.setText(feedmodelList.get(position).getEmail());
        holder.feedback.setText(feedmodelList.get(position).getFeedback());
    }

    @Override
    public int getItemCount() {
        return feedmodelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,email,feedback;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.ffname);
            email=itemView.findViewById(R.id.ffemail);
            feedback=itemView.findViewById(R.id.fffeed);

        }
    }
}
