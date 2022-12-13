package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class ExpensesDatabaseHelper extends SQLiteOpenHelper {
    private static ExpensesDatabaseHelper sInstance;

    public static synchronized ExpensesDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ExpensesDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Database Info
    private static final String DATABASE_NAME = "expensesDB";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_ACCOUNTS = "account";
    private static final String TABLE_TRANSACTION = "money_transaction";

    // Account table columns
    private static final String KEY_ACCOUNT_NO = "account_no";
    private static final String KEY_ACCOUNT_BANK_NAME = "bank_name";
    private static final String KEY_ACCOUNT_HOLDER_NAME = "account_holder_name";
    private static final String KEY_ACCOUNT_BALANCE = "balance";

    // Transaction table columns
    private static final String KEY_TRANSACTION_DATE = "date";
    private static final String KEY_TRANSACTION_ACCOUNT_NO = "account_no";
    private static final String KEY_TRANSACTION_TYPE = "type";
    private static final String KEY_TRANSACTION_AMOUNT = "amount";

    private ExpensesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase sqLiteDatabase) {
        super.onConfigure(sqLiteDatabase);
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS +
                "(" +
                    KEY_ACCOUNT_NO + " CHAR(7) PRIMARY KEY, " +
                    KEY_ACCOUNT_BANK_NAME + " VARCHAR(100), " +
                    KEY_ACCOUNT_HOLDER_NAME + " VARCHAR(100), " +
                    KEY_ACCOUNT_BALANCE + " DOUBLE(10, 2)" +
                ")";

        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTION +
                "(" +
                    KEY_TRANSACTION_DATE + " DATE," +
                    KEY_TRANSACTION_ACCOUNT_NO + " CHAR(6)," +
                    KEY_TRANSACTION_TYPE + " VARCHAR(8)," +
                    KEY_TRANSACTION_AMOUNT + " DOUBLE(10, 2)," +
                    "CONSTRAINT fk_account_no FOREIGN KEY (" + KEY_TRANSACTION_ACCOUNT_NO +
                    ") REFERENCES " + TABLE_ACCOUNTS + "(" + KEY_ACCOUNT_NO + ")" +
                ")";

        sqLiteDatabase.execSQL(CREATE_ACCOUNT_TABLE);
        sqLiteDatabase.execSQL(CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i != i1) {
            // Simplest implementation is to drop all old tables and recreate them
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
            onCreate(sqLiteDatabase);
        }
    }


    // Insert a new account
    public void newAccount(Account account) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_ACCOUNT_NO, account.getAccountNo());
            values.put(KEY_ACCOUNT_BALANCE, account.getBalance());
            values.put(KEY_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
            values.put(KEY_ACCOUNT_BANK_NAME, account.getBankName());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key columns.
            db.insertOrThrow(TABLE_ACCOUNTS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
    }

    public List<Account> getAllAccounts() {
        SQLiteDatabase db = getReadableDatabase();

        List<Account> accounts = new ArrayList<Account>();

        String ACCOUNTS_SELECT_QUERY = String.format("SELECT * FROM %s ORDER BY %s", TABLE_ACCOUNTS, KEY_ACCOUNT_NO);


        Cursor cursor = db.rawQuery(ACCOUNTS_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Account account = new Account();

                    account.setAccountNo(cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NO)));
                    account.setBalance(cursor.getDouble(cursor.getColumnIndex(KEY_ACCOUNT_BALANCE)));
                    account.setBankName(cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_BANK_NAME)));
                    account.setAccountHolderName(cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_HOLDER_NAME)));

                    accounts.add(account);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return accounts;
    }

    public List<String> getAllAccountNumbers() {
        SQLiteDatabase db = getReadableDatabase();

        List<String> accounts = new ArrayList<>();

        String ACCOUNTS_SELECT_QUERY = String.format("SELECT %s FROM %s ORDER BY %s", KEY_ACCOUNT_NO, TABLE_ACCOUNTS, KEY_ACCOUNT_NO);


        Cursor cursor = db.rawQuery(ACCOUNTS_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    accounts.add(cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NO)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return accounts;
    }

    public void removeAccount(String accountNo) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_ACCOUNTS, String.format("%s = %s", KEY_ACCOUNT_NO, accountNo), null);
    }

    public void updateBalance(String accountNo, double changeOfAmount) {
        SQLiteDatabase db = getWritableDatabase();
        SQLiteDatabase rdb = getReadableDatabase();



        db.execSQL(String.format(
                "UPDATE %s SET %s = %s + %s WHERE %s = '%s'",
                TABLE_ACCOUNTS,
                KEY_ACCOUNT_BALANCE,
                KEY_ACCOUNT_BALANCE,
                changeOfAmount,
                KEY_ACCOUNT_NO,
                accountNo
        ));
    }

    public Account getAccount(String accountNo) {
        SQLiteDatabase db = getReadableDatabase();

        String ACCOUNT_SELECT_QUERY = String.format(
                    "SELECT * FROM %s WHERE %s = '%s'",
                    TABLE_ACCOUNTS,
                    KEY_ACCOUNT_NO,
                    accountNo
                );


        Cursor cursor = db.rawQuery(ACCOUNT_SELECT_QUERY, null);

        Account account = new Account();

        if (cursor.moveToFirst()) {
            account.setAccountNo(cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_NO)));
            account.setBankName(cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_BANK_NAME)));
            account.setBalance(cursor.getDouble((cursor.getColumnIndex(KEY_ACCOUNT_BALANCE))));
            account.setAccountHolderName(cursor.getString(cursor.getColumnIndex(KEY_ACCOUNT_HOLDER_NAME)));
        }

        return account;
    }

    public List<Transaction> getAllTransactions() {
        SQLiteDatabase db = getReadableDatabase();

        List<Transaction> transactions = new ArrayList<>();

        String TRANSACTIONS_SELECT_QUERY = String.format("SELECT * FROM %s ORDER BY %s", TABLE_TRANSACTION, KEY_TRANSACTION_ACCOUNT_NO);


        Cursor cursor = db.rawQuery(TRANSACTIONS_SELECT_QUERY, null);


        Log.d("MyLog", cursor.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (cursor.moveToFirst()) {
                do {
                    Transaction transaction = new Transaction();

                    transaction.setAccountNo(cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_ACCOUNT_NO)));
                    transaction.setAmount(cursor.getDouble(cursor.getColumnIndex(KEY_TRANSACTION_AMOUNT)));

                    String dateStr = cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_DATE));

                    transaction.setDate(sdf.parse(dateStr));

                    transaction.setExpenseType(ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(KEY_TRANSACTION_TYPE))));

                    transactions.add(transaction);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return transactions;
    }

    // Insert a new account
    public void newTransaction(Transaction transaction) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TRANSACTION_ACCOUNT_NO, transaction.getAccountNo());
            values.put(KEY_TRANSACTION_AMOUNT, transaction.getAmount());
            values.put(KEY_TRANSACTION_TYPE, transaction.getExpenseType().toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


            values.put(KEY_TRANSACTION_DATE, sdf.format(transaction.getDate()));

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key columns.
            db.insertOrThrow(TABLE_TRANSACTION, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
    }
}
