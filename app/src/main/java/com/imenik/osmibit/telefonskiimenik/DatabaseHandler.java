package com.imenik.osmibit.telefonskiimenik;

/**
 * Created by Student on 31.5.2017..
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION =3;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_GROUP = "groups";//TABLE GROUP
    private static final String KEY_ID = "id";
    private static final String KEY_GROUP = "idGroup";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_PH_NO = "phone_number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }



    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Table for groups
        String CREATE_CONTACTS_GROUP = "CREATE TABLE " + TABLE_GROUP + "("
                + KEY_GROUP + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT UNIQUE" + ");";

         //Table for contacts
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_GROUP + " INTEGER,"
                + KEY_NAME + " TEXT,"
                + KEY_SURNAME + " TEXT,"
                + KEY_PH_NO + " TEXT,"
                + "FOREIGN KEY(" + KEY_GROUP + ") REFERENCES " + TABLE_GROUP + "(" + KEY_GROUP  +")"
                + ");";
      // db.execSQL(CREATE_CONTACTS_TABLE);



        db.execSQL(CREATE_CONTACTS_GROUP);
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);

        // Create tables again
        onCreate(db);
    }


    // code to add the new contact
     public boolean addContact(Contact contact) {
         SQLiteDatabase db = this.getWritableDatabase();

         ContentValues values = new ContentValues();
         values.put(KEY_GROUP, contact.getGroupID()); //Contact groupId
         values.put(KEY_NAME, contact.getName());   // Contact Name
         values.put(KEY_SURNAME, contact.getSurname()); // Contact Surname
         values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone

        // Inserting Row
         Long results = db.insert(TABLE_CONTACTS, null, values);
         if (results != -1) return true;
         else return false;
        //2nd argument is String containing nullColumnHack
       //  db.close(); // Closing database connection
    }

    public boolean addGroup(Group group){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, group.getName());   // Group Name
        // Inserting Row
        Long results = db.insert(TABLE_GROUP, null, values);
        if(results == -1)
            return false;
        else
            return true;

        //2nd argument is String containing nullColumnHack
        //db.close(); // Closing database connection

    }


    // code to get the single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID, KEY_GROUP,
                        KEY_NAME, KEY_SURNAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)),
                cursor.getString(2), cursor.getString(3),cursor.getString(4));
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setGroupID(Integer.parseInt(cursor.getString(1)));
                contact.setName(cursor.getString(2));
                contact.setSurname(cursor.getString(3));
                contact.setPhoneNumber(cursor.getString(4));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }



    //GET ALL RECORD FROM CONATACT AND GROUP TABLE
    public Cursor getAllRecords() {
      //  List<Cursor> cursorList = new ArrayList<Cursor>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " AS" + " tc"
                             + " INNER JOIN " + TABLE_GROUP + " AS"
                             + " tg" + " ON" + " tc." + KEY_GROUP + "=" + "tg." + KEY_GROUP;

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectQuery, null);

    }




    public List<Group> getAllGroups() {
        List<Group> groupList = new ArrayList<Group>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GROUP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Group group = new Group();
                group.setID(Integer.parseInt(cursor.getString(0)));
                group.setName(cursor.getString(1));
                // Adding contact to list
                groupList.add(group);
            } while (cursor.moveToNext());
        }
        // return contact list
        return groupList;
    }



    // code to update the single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_SURNAME, contact.getSurname());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    // Deleting single contact
    public void deleteGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROUP, KEY_GROUP + " = ?",
                new String[] { String.valueOf(group.getID()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
