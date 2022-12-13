package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.ExpensesDatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DBTransactionDAO implements TransactionDAO {

    private final ExpensesDatabaseHelper dbHandle;

    public DBTransactionDAO(ExpensesDatabaseHelper dbHandle) {
        this.dbHandle = dbHandle;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);

        dbHandle.newTransaction(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return dbHandle.getAllTransactions();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return dbHandle.getAllTransactions();
    }
}
