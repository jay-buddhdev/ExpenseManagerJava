package com.example.expensemanagerjava.Adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagerjava.Model.TransacationModel;
import com.example.expensemanagerjava.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<TransacationModel> TransactionItems;
    TextView name,date,amount;
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private static SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd");
    public TransactionAdapter(ArrayList<TransacationModel> transactionList) {
        Log.d("Transaction", "Got new List with size " + transactionList.size());
        this.TransactionItems = transactionList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Transaction", "onCreateViewHolder");
        return new TranscationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.tempdashboarditem, parent,
                        false
                )
        );
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return 1;
        String date1 = TransactionItems.get(position).getDate().split("/")[0];
        String date2 = TransactionItems.get(position - 1).getDate().split("/")[0];
        return date1.compareTo(date2) == 0 ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == 1){
            TranscationViewHolder hold = (TranscationViewHolder) holder;
            hold.setTransactionData(TransactionItems.get(position));
        }else {
            date.setVisibility(View.GONE);
            TranscationViewHolder hold = (TranscationViewHolder) holder;
            hold.setTransactionData(TransactionItems.get(position));
        }

    }

//    @Override
//    public void onBindViewHolder(@NonNull TranscationViewHolder holder, int position) {
//        Log.d("Transaction", "onBindViewHolder " + position + " -- " + TransactionItems.size());
//        holder.setTransactionData(TransactionItems.get(position));
//    }

    @Override
    public int getItemCount() {
        return TransactionItems.size();
    }


    public class TranscationViewHolder extends RecyclerView.ViewHolder{

        public TranscationViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.transaction_date);
            name = itemView.findViewById(R.id.transaction_name);
            amount = itemView.findViewById(R.id.transaction_amount);

        }

        public void setTransactionData(TransacationModel transacationModel) {
            name.setText(transacationModel.getName());
            Date curr = null;
            try {

                curr = from.parse(transacationModel.getDate().split("/")[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            date.setText(formatter.format(curr));
            if(transacationModel.getTransactionType()){
                amount.setTextColor(Color.GREEN);
            }else {
                amount.setTextColor(Color.RED);
            }
            amount.setText(transacationModel.getAmount());
        }
    }
}
