package com.example.expensemanagerjava.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagerjava.Model.TransacationModel;
import com.example.expensemanagerjava.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TranscationViewHolder>{

    private List<TransacationModel> TransactionItems;

    public TransactionAdapter(ArrayList<TransacationModel> transactionList) {
        this.TransactionItems = transactionList;
    }

    @NonNull
    @Override
    public TranscationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TranscationViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.tempdashboarditem, parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TranscationViewHolder holder, int position) {
        holder.setTransactionData(TransactionItems.get(position));
    }

    @Override
    public int getItemCount() {
        return TransactionItems.size();
    }

    public class TranscationViewHolder extends RecyclerView.ViewHolder{
        TextView name,date,amount;
        public TranscationViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.transaction_name);
            date = itemView.findViewById(R.id.transaction_date);
            amount = itemView.findViewById(R.id.transaction_amount);

        }

        public void setTransactionData(TransacationModel transacationModel) {
            name.setText(transacationModel.getName());
            date.setText(transacationModel.getDate());
            amount.setText(transacationModel.getAmount());
        }
    }
}
