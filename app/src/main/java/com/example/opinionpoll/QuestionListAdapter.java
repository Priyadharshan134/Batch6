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

public class QuestionListAdapter extends ArrayAdapter<Helper>{

    private static final String TAG = "QuestionListAdapter";
    private Context mContext;
    private int mResource;

    private static class ViewHolder{
        TextView Question,Timing,QuestionID,UserID,Option1,Option2,Option3,Option4,Link,OptionNum;
    }

    public QuestionListAdapter(@NonNull Context context, int resource, @NonNull List<Helper> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String question = getItem(position).getQuestion();
        String timing =  getItem(position).getCreationDate() +", " + getItem(position).getCreationTime();
        String quesId = getItem(position).getQuestionId();
        String user_id =  getItem(position).getOwnerUserID();
        String option1 = getItem(position).getOption1();
        String option2 = getItem(position).getOption2();
        String option3 = getItem(position).getOption3();
        String option4 = getItem(position).getOption4();
        String link = getItem(position).getLink();

        final View result;

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.Question = (TextView) convertView.findViewById(R.id.list_ques);
            holder.Timing = (TextView) convertView.findViewById(R.id.list_time);
            holder.QuestionID = (TextView) convertView.findViewById(R.id.list_ques_id);
            holder.UserID = (TextView) convertView.findViewById(R.id.list_user_id);
            holder.Option1 = (TextView) convertView.findViewById(R.id.list_option1);
            holder.Option2 = (TextView) convertView.findViewById(R.id.list_option2);
            holder.Option3 = (TextView) convertView.findViewById(R.id.list_option3);
            holder.Option4 = (TextView) convertView.findViewById(R.id.list_option4);
            holder.Link = (TextView) convertView.findViewById(R.id.list_link);

            result = convertView;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        holder.Question.setText(question);
        holder.Timing.setText(timing);
        holder.QuestionID.setText(quesId);
        holder.UserID.setText(user_id);
        holder.Option1.setText(option1);
        holder.Option2.setText(option2);
        holder.Option3.setText(option3);
        holder.Option4.setText(option4);
        holder.Link.setText(link);

        return result;
    }
}

