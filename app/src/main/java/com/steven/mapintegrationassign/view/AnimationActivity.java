package com.steven.mapintegrationassign.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.fabtransitionactivity.SheetLayout;
import com.steven.mapintegrationassign.R;
import com.steven.mapintegrationassign.model.Mail;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AnimationActivity extends BaseActivity implements SheetLayout.OnFabAnimationEndListener {

    @Bind(R.id.bottom_sheet)
    SheetLayout mSheetLayout;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.list_mails) ListView listMails;

    ArrayList<Mail> mailList = new ArrayList<>();

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpToolbarWithTitle("Email", false);

        mSheetLayout.setFab(mFab);
        mSheetLayout.setFabAnimationEndListener(this);

        fillMailList();
        listMails.setAdapter(new MailAdapter());
    }

    @OnClick(R.id.fab)
    void onFabClick() {
        mSheetLayout.expandFab();
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }


   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == REQUEST_CODE){
           mSheetLayout.contractFab();
       }
   }


    private void fillMailList(){
        String message = "hELLO tEST eMAIL";

        mailList.add(new Mail(1, "krupa shah", message, "Nov 5"));
        mailList.add(new Mail(2, "first name", message, "Nov 5"));
        mailList.add(new Mail(3, "second name", message, "Nov 4"));
        mailList.add(new Mail(4, "Third name", message, "Nov 3"));
        mailList.add(new Mail(5, "PQRS TEST", message, "Nov 2"));
        mailList.add(new Mail(6, "TEST Data", message, "Nov 2"));
        mailList.add(new Mail(7, "ABC NAME", message, "Nov 2"));
        mailList.add(new Mail(8, "Xyz NAME", "Yeah!", "Nov 2"));
    }

    private class MailAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mailList.size();
        }

        @Override
        public Mail getItem(int position) {
            return mailList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(AnimationActivity.this, R.layout.list_item_mail, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imageEmail.setColorFilter(setColorFilter(mailList.get(position).getCircleColor()));
            holder.textLabelEmail.setText(mailList.get(position).getTitleEmail().substring(0, 1));
            holder.textTitleEmail.setText(mailList.get(position).getTitleEmail());
            holder.textMessageEmail.setText(mailList.get(position).getMessageEmail());
            holder.textDateEmail.setText(mailList.get(position).getDateEmail());

            return convertView;
        }
    }

    static class ViewHolder {
        @Bind(R.id.image_email) ImageView imageEmail;
        @Bind(R.id.text_label_email)  TextView textLabelEmail;
        @Bind(R.id.text_title_email)  TextView textTitleEmail;
        @Bind(R.id.text_message_email)  TextView textMessageEmail;
        @Bind(R.id.text_date_email)  TextView textDateEmail;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private int setColorFilter(int color){
        if((color % 4) == 0) {
            return ContextCompat.getColor(getApplicationContext(), R.color.one_round);
        }if((color % 3) == 0) {
            return ContextCompat.getColor(getApplicationContext(), R.color.two_round);
        }if((color % 2) == 0) {
            return ContextCompat.getColor(getApplicationContext(), R.color.three_round);
        }else {
            return ContextCompat.getColor(getApplicationContext(), R.color.four_round);
        }
    }

}
