package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.ExpensesDatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DBTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InMemoryTransactionDAO;

public class DBExpenseManager extends ExpenseManager {

    public DBExpenseManager() {
    }

    @Override
    public void setup(ExpensesDatabaseHelper dbHandle) throws ExpenseManagerException {


        TransactionDAO dbTransactionDAO = new DBTransactionDAO(dbHandle);
        setTransactionsDAO(dbTransactionDAO);

        AccountDAO dbAccountDAO = new DBAccountDAO(dbHandle);
        setAccountsDAO(dbAccountDAO);

    }

    @Override
    public void setup() throws ExpenseManagerException {

    }
}
