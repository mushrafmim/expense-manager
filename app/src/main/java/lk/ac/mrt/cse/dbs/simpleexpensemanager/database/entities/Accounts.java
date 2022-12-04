package lk.ac.mrt.cse.dbs.simpleexpensemanager.database.entities;


import android.provider.BaseColumns;

public class Accounts {
    private Accounts() {}

    public static class AccountEntry implements BaseColumns {
        public static final String TABLE_NAME = "account";
        public static final String COLUMN_NAME_ACCOUNT_NO = "account_no";
        public static final String COLUMN_NAME_BANK_NAME = "bank_name";
        public static final String COLUMN_NAME_ACCOUNT_HOLDER_NAME = "account_holder";
        public static final String COLUMN_NAME_BALANCE = "balance";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + AccountEntry.TABLE_NAME + " (" +
                        AccountEntry.COLUMN_NAME_ACCOUNT_NO + " CHAR(7) PRIMARY KEY," +
                        AccountEntry.COLUMN_NAME_BANK_NAME + " VARCHAR(100)," +
                        AccountEntry.COLUMN_NAME_ACCOUNT_HOLDER_NAME + " VARCHAR(100)," +
                        AccountEntry.COLUMN_NAME_BALANCE + " DOUBLE(10, 2))"
                ;

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + AccountEntry.TABLE_NAME;

    }
}
