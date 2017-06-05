package com.imenik.osmibit.telefonskiimenik;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //AppCompatActivity
    EditText mName, mSurname, mNumber, mGroup;
    Button mButtSave;
    TextView text;
    public int ID;
    public String name, surname, telNumber, group;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mName = (EditText) findViewById(R.id.editIme);
        mSurname = (EditText) findViewById(R.id.editPrezime);
        mNumber = (EditText) findViewById(R.id.editNumber);
        mGroup = (EditText) findViewById(R.id.editGrupa);
        mButtSave = (Button) findViewById(R.id.button);

        //Creates
       db = new DatabaseHandler(this);


       // String c = db.getDatabaseName();
        //Toast.makeText(this, c, Toast.LENGTH_SHORT).show();

    }


    public void onSaveContact(View view){
        //insert on database

        name = mName.getText().toString();
        System.out.println("NAME:" + name);
        surname = mSurname.getText().toString();
        System.out.println("SURNAME :" + surname);
        telNumber = mNumber.getText().toString();
        System.out.println("TELNUBER:" + telNumber);
        group = mGroup.getText().toString();
        System.out.println("MGROUP:" + mGroup);

        db.addContact(new Contact(name, surname, telNumber));

    }

}