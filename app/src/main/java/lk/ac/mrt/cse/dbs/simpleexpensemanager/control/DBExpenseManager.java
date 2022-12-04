package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.database.ExpenseManagerDBHelper;

public class DBExpenseManager extends ExpenseManager {

    public DBExpenseManager() {
    }

    @Override
    public void setup(Context context) {
        ExpenseManagerDBHelper dbHelper = new ExpenseManagerDBHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();


    }
}
