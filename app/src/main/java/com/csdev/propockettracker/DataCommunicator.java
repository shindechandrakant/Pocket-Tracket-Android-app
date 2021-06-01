package com.csdev.propockettracker;


import com.csdev.propockettracker.classes.FixExpense;
import com.csdev.propockettracker.classes.Transaction;

public interface DataCommunicator {
   void writeTransactionDatabase(Transaction transaction);
   void writeExpenseDataBase(FixExpense expense);
}
