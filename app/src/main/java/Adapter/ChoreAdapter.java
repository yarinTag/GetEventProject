package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.androidproject.R;
import com.appsnipp.androidproject.model.Chore;

import java.util.List;

public class ChoreAdapter extends RecyclerView.Adapter<ChoreAdapter.ViewHolder> {

    private Activity activity;
    private List<Chore> choreList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public ChoreAdapter(Activity activity,List<Chore> chores) {

        this.activity=activity;
        this.choreList=chores;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chore_row,parent,false);
        ViewHolder holder =new ViewHolder(view);
        holder.listener=listener;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chore chore = choreList.get(position);
        holder.bindData(chore,position);
    }

    public void setOnClickListener(ChoreAdapter.OnItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public int getItemCount() {
        return choreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public OnItemClickListener listener;
        TextView choreName;
        TextView choreDetails;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            choreName = itemView.findViewById(R.id.choreName);
            choreDetails=itemView.findViewById(R.id.choreDetails);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }

        public void bindData(Chore chore,int position){
            this.choreName.setText(chore.choreName);
            this.choreDetails.setText(chore.choreDetails);
            this.position=position;
        }
    }
}
