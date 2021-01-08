package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.androidproject.R;
import com.appsnipp.androidproject.model.Participant;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder>
{
    private Activity activity;
    private List<Participant> participantList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }


    public ParticipantAdapter() {
    }

    public ParticipantAdapter(Activity activity, List<Participant> participantList) {
        this.activity = activity;
        this.participantList = participantList;
    }

    @NonNull
    @Override
    public ParticipantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_row,parent,false);
        ParticipantAdapter.ViewHolder holder = new ParticipantAdapter.ViewHolder(view);
        holder.listener=listener;

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantAdapter.ViewHolder holder, int position) {

        Participant participant = participantList.get(position);
        holder.bindData(participant,position);

    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public OnItemClickListener listener;

        TextView participantName;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            participantName = itemView.findViewById(R.id.userParticipant);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }

        public void bindData(Participant participant, int position) {

            this.participantName.setText("Nir A + 1");
//          this.participantName.setText(participant.participantId);
            this.position=position;
        }
    }
}