package com.example.opinionpoll;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ResultListAdapter extends ArrayAdapter<ResultHelper> {
    private static final String TAG = "ResultListAdapter";
    private Context rContext;
    private int rResource;

    private static class ViewHold{
        public TextView Question,CreationTime,UserId,QuesId;
    }

    public ResultListAdapter(@NonNull Context context, int resource, @NonNull List<ResultHelper> objects) {
        super(context, resource, objects);
        rContext = context;
        rResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String question = getItem(position).getQuestion();
        String creation_time = getItem(position).getCreationDate()+ ", " +getItem(position).getCreationTime();
        String quesId = getItem(position).getQuestionId();
        String user_id = getItem(position).getOwnerUserID();

        final View result;

        ViewHold hold;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(rContext);
            convertView = inflater.inflate(rResource, parent, false);
            hold = new ViewHold();
            hold.Question = (TextView) convertView.findViewById(R.id.res_question);
            hold.CreationTime = (TextView) convertView.findViewById(R.id.res_creation_time);
            hold.QuesId = (TextView) convertView.findViewById(R.id.res_ques_id);
            hold.UserId = (TextView) convertView.findViewById(R.id.res_user_id);

            result = convertView;
            convertView.setTag(hold);
        }
        else{
            hold = (ResultListAdapter.ViewHold) convertView.getTag();
            result = convertView;
        }
        hold.Question.setText(question);
        hold.CreationTime.setText(creation_time);
        hold.QuesId.setText(quesId);
        hold.UserId.setText(user_id);
        return result;
    }
}