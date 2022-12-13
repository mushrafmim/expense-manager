package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.ExpensesDatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DBAccountDAO implements AccountDAO {

    private final ExpensesDatabaseHelper dbHandle;

    public DBAccountDAO(ExpensesDatabaseHelper dbHandle) {
        this.dbHandle = dbHandle;
    }

    @Override
    public List<String> getAccountNumbersList() {
        return dbHandle.getAllAccountNumbers();
    }

    @Override
    public List<Account> getAccountsList() {
        return dbHandle.getAllAccounts();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return dbHandle.getAccount(accountNo);
    }

    @Override
    public void addAccount(Account account) {
        dbHandle.newAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        dbHandle.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if (expenseType == ExpenseType.EXPENSE)
            dbHandle.updateBalance(accountNo, -amount);
        else if (expenseType == ExpenseType.INCOME)
            dbHandle.updateBalance(accountNo, amount);
    }
}
