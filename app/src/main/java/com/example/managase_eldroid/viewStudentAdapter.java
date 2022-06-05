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
    List<viewStudentSingleModel> modelList;
    itemOnClick listener;

    public viewStudentAdapter(List<viewStudentSingleModel> modelList, itemOnClick listener) {
        this.modelList = modelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewStudentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewStudentAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_student,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewStudentAdapter.MyViewHolder holder, int position) {
        holder.nameTV.setText(modelList.get(position).getName());
        holder.addressTV.setText(modelList.get(position).getAddress());
        holder.IDTV.setText(modelList.get(position).getID());
        Picasso.get().load(modelList.get(position).getUrl()).into(holder.imageView);
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemEdit(modelList.get(position));
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemDelete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public interface itemOnClick
    {
        void itemDelete(int pos);
        void itemEdit(viewStudentSingleModel model);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTV, addressTV,IDTV;
        Button deleteBtn, editBtn;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = (TextView) itemView.findViewById(R.id.tv_name_student);
            addressTV = (TextView) itemView.findViewById(R.id.tv_address_student);
            IDTV = (TextView) itemView.findViewById(R.id.tv_id_student);
            deleteBtn = (Button) itemView.findViewById(R.id.btn_delete_single);
            editBtn = (Button) itemView.findViewById(R.id.btn_edit_student);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }
}
