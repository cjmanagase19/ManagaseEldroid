package com.example.managase_eldroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class viewStudentAdapter extends RecyclerView.Adapter<viewStudentAdapter.MyViewHolder> {
    List<viewItemModel> modelList;
    itemOnClick listener;

    public viewStudentAdapter(List<viewItemModel> modelList, itemOnClick listener) {
        this.modelList = modelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewStudentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewStudentAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.student_single,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewStudentAdapter.MyViewHolder holder, int position) {
        holder.nameTV.setText(modelList.get(position).getTitle());
        holder.authorEt.setText(modelList.get(position).getAuthor());
        holder.priceEt.setText(modelList.get(position).getPrice());
        Picasso.get().load(modelList.get(position).getURL()).into(holder.imageView);
        holder.dateEt.setText(modelList.get(position).getDate());
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemEdit(modelList.get(position));
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemDelete(holder.getAdapterPosition(),modelList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public interface itemOnClick
    {
        void itemDelete(int pos, viewItemModel model);
        void itemEdit(viewItemModel model);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTV, priceEt,authorEt,dateEt;
        Button deleteBtn, editBtn;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = (TextView) itemView.findViewById(R.id.tv_item_name);
            priceEt = (TextView) itemView.findViewById(R.id.tv_item_price);
            authorEt = (TextView) itemView.findViewById(R.id.tv_item_author);
            deleteBtn = (Button) itemView.findViewById(R.id.btn_delete_single);
            editBtn = (Button) itemView.findViewById(R.id.btn_edit_student);
            dateEt = (TextView) itemView.findViewById(R.id.tv_item_date);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }
}
