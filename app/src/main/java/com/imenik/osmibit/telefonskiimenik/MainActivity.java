package com.imenik.osmibit.telefonskiimenik;
import android.graphics.Color;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.itextpdf.text.pdf.BidiOrder.PDF;


public class MainActivity extends AppCompatActivity  {

    //AppCompatActivity
    EditText mName, mSurname, mNumber,mGroup;
    Button mButtSave, mButtPrint;
    List<Contact> contact;
    List<Group> group;
    public String name, surname, telNumber;
    DatabaseHandler db;

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


        //Creates
       db = new DatabaseHandler(this);

        db.addGroup(new Group("Prijatelji"));
        db.addGroup(new Group("Posao"));
        db.addGroup(new Group("Obitelj"));




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

        db.addContact(new Contact(name, surname, telNumber));


    }

    public void onPrintContact(View view){
       // writePdf();
        createPDF();
    }


    public Boolean writePdf()  {
        try {
            String fpath = "/sdcard/" + "Imenik" + ".pdf";
            File file = new File(fpath);
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }


            // step 1
            Document document = new Document(PageSize.A4.rotate());
            document.addAuthor("Tihomir Vanjurek");
            document.addTitle("Phonebook");
            // step 2
            PdfWriter.getInstance(document, new FileOutputStream(file.getAbsoluteFile()));
            // step 3
            document.open();
            // step 4
            document.add(new Paragraph("Hello this is PhoneBook!"));
            document.add(new Paragraph("Content"));
            // step 5
            document.close();

            Log.d("Suceess", "Sucess");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }


    public void createPDF()
    {
        Document doc = new Document();

        try {
        //    String path = Environment.getExternalStorageDirectory() + "/Imenik" + ".pdf";
            File f = File.createTempFile( "imenik", ".pdf", getExternalCacheDir());
            f.createNewFile();
            f.getAbsolutePath();
          //  if(!dir.exists())
          //      dir.mkdirs();

         //   Log.d("PDFCreator", "PDF Path: " + path);
         //   File file = new File(dir, "Imenik.pdf");
            FileOutputStream fOut = new FileOutputStream(f);
            PdfWriter.getInstance(doc, fOut);
            //open the document
            doc.open();
            /* Create Paragraph and Set Font */
            Paragraph p1 = new Paragraph("Hi! I am Generating my first PDF using DroidText");
            //add paragraph to document
            doc.add(p1);
            Paragraph p2 = new Paragraph("This is an example of a simple paragraph");
            doc.add(p2);



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






}
