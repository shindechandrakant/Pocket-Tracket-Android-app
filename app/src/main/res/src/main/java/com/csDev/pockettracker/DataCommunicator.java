package com.csDev.pockettracker;

import com.csDev.pockettracker.classes.FixExpense;
import com.csDev.pockettracker.classes.Transaction;

public interface DataCommunicator {
   void writeTransactionDatabase(Transaction transaction);
   void writeExpenseDataBase(FixExpense expense);
}
