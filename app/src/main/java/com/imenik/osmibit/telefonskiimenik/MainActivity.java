package com.imenik.osmibit.telefonskiimenik;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.itextpdf.text.Element.ALIGN_LEFT;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    //AppCompatActivity
    EditText mName, mSurname, mNumber, mGroup;
    Spinner mSpinner;
    Button mButtSave, mButtPrint;
    public String name, surname, telNumber, group;
    DatabaseHandler db;
    int Position = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mName = (EditText) findViewById(R.id.editIme);
        mSurname = (EditText) findViewById(R.id.editPrezime);
        mNumber = (EditText) findViewById(R.id.editNumber);
        mGroup = (EditText) findViewById(R.id.editGroup);
        mButtSave = (Button) findViewById(R.id.button);
        mButtPrint = (Button) findViewById(R.id.buttonprint);
        mSpinner = (Spinner) findViewById(R.id.spinner);

        // creates

        db = new DatabaseHandler(this);
        StringBuffer buffer = new StringBuffer();
        List<Group> group = db.getAllGroups();
        List<String> str = new ArrayList<String>();
        for(Group cn : group) {
            String con = "Id: " + cn.getID()  + " ,Name: " + cn.getName();
            str.add(cn.getName());
            Log.w("myGroup: ", cn.getName());
            buffer.append(con).append("\n");
        }


        //edit group into spinner
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, str);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mSpinner.setAdapter(aa);
        mSpinner.setOnItemSelectedListener(this);

      //  createPDF(buffer);

        }








    public void onSaveContact(View view) {
        //insert on database

        name = mName.getText().toString();
        System.out.println("NAME:" + name);
        surname = mSurname.getText().toString();
        System.out.println("SURNAME :" + surname);
        telNumber = mNumber.getText().toString();
        System.out.println("TELNUBER:" + telNumber);
        mName.setHint("");
        // creates
        db = new DatabaseHandler(this);
        //position
        if(Position == -1) {
            boolean insert = db.addContact(new Contact(name, surname, telNumber));

            if (insert == false) {
                Toast.makeText(getApplicationContext(), "Contact not inserted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Contact inserted", Toast.LENGTH_LONG).show();
            }
        }
        else {
            boolean insert = db.addContact(new Contact(Position, name, surname, telNumber));

        if (insert == false) {
            Toast.makeText(getApplicationContext(), "Contact not inserted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Contact inserted", Toast.LENGTH_LONG).show();
           }
        }
   //     db.addContact(new Contact(name, surname, telNumber));
   //         StringBuffer buffer = new StringBuffer();
   //         List<Contact> contact = db.getAllContacts();
   //       for(Contact cn : contact) {
   //           String con = "Id: " + cn.getID() + " ,GroupID: " + cn.getGroupID()  + " ,Name: " + cn.getName() + " ,Suraname: " + cn.getSurname() + " ,PhoneNumber: " + cn.getPhoneNumber();
   //           Log.w("myContact: ",con);
   //           buffer.append(con).append("\n");
   //     }
    //    createPDF(buffer);

    }



    public void onPrintContact(View view) {

        db = new DatabaseHandler(this);
        StringBuffer buffer = new StringBuffer();
        Cursor records = db.getAllRecords();
        if(records.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No records", Toast.LENGTH_SHORT).show();
            return;
        }

            while (records.moveToNext()) {

                //    String con = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Suraname: " + cn.getSurname() + " ,PhoneNumber: " + cn.getPhoneNumber();
                  buffer.append("Id: " + records.getString(0)).append("\n");
                  buffer.append("idGroup: " + records.getString(1)).append("\n");
                  buffer.append("Name: " + records.getString(2)).append("\n");
                  buffer.append("Surname: " + records.getString(3)).append("\n");
                  buffer.append("PhoneNumber: " + records.getString(4)).append("\n");
                  buffer.append("GroupID: " + records.getString(5)).append("\n");
                  buffer.append("Group Name: " + records.getString(6)).append("\n\n");
        }
        createPDF(buffer);

    }


    public void onNewGroupContact(View view) {

        db = new DatabaseHandler(this);
        //Add new group into
        group = mGroup.getText().toString();
        System.out.println("NAME GROUP: " + group);
        mGroup.setHint("");

        //Add group
        boolean insert = db.addGroup(new Group(group));
        if (!insert) {
            Toast.makeText(getApplicationContext(), "Group: " + group + " not allowed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Group inserted", Toast.LENGTH_LONG).show();
        }
        finish();
        startActivity(getIntent());

    }





    private void viewPdf(File path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(path), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }





    public void createPDF(StringBuffer buffer) {//This method works for create pdf file

        Document doc = new Document(PageSize.A4);

        try {

           File f = File.createTempFile( "imenik", ".pdf", getExternalCacheDir());
            f.createNewFile();
            f.getAbsolutePath();
           // if(!dir.exists())
          //     dir.mkdirs();

         //   Log.d("PDFCreator", "PDF Path: " + path);
         //   File file = new File(dir, "Imenik.pdf");
            FileOutputStream fOut = new FileOutputStream(f);
            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
            doc.addTitle("Phonebook");
            doc.addAuthor("Tihomir Vanjurek");
            /* Create Paragraph and Set Font */
            Paragraph p1 = new Paragraph("This is an PhoneBook");
            p1.setAlignment(ALIGN_LEFT);
            //add paragraph to document
            doc.add(p1);
            //add new page
           // doc.newPage();
            Paragraph p2 = new Paragraph(buffer.toString());
            p2.setAlignment(ALIGN_LEFT);
            doc.add(p2);


            viewPdf(f);
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }

        finally
        {
            doc.close();

        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        Position= position + 1;

    }



    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
     // TODO Auto-generated method stub
       // Position = -1;

    }



}//End Class
