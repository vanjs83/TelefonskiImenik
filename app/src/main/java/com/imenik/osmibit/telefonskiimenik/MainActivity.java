package com.imenik.osmibit.telefonskiimenik;


import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    //AppCompatActivity
    EditText mName, mSurname, mNumber, mGroup;
    Button mButtSave, mButtPrint;
    public String name, surname, telNumber;
    DatabaseHandler db;
    List<Contact> contact;
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
        boolean insert = db.addGroup(new Group("Prijatelji"));
        if (insert == false) {
            Toast.makeText(getApplicationContext(), "Data not inserted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Data inserted", Toast.LENGTH_LONG).show();
        }
        db.addGroup(new Group("Posao"));
        db.addGroup(new Group("Obitelj"));


        List<Group> group = db.getAllGroups();
        for (Group gn : group) {
            String log = "Id: "+ gn.getID()+" ,Name: " + gn.getName();
            // Writing Contacts to log
             System.out.println(log);

        }

    }

    public void onSaveContact(View view) {
        //insert on database

        name = mName.getText().toString();
        System.out.println("NAME:" + name);
        surname = mSurname.getText().toString();
        System.out.println("SURNAME :" + surname);
        telNumber = mNumber.getText().toString();
        System.out.println("TELNUBER:" + telNumber);
        boolean insert = db.addContact(new Contact(name, surname, telNumber));

        if (insert == false) {
            Toast.makeText(getApplicationContext(), "Data not inserted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Data inserted", Toast.LENGTH_LONG).show();
        }
   //     db.addContact(new Contact(name, surname, telNumber));
        List<Contact> contact = db.getAllContacts();
        for (Contact cn : contact) {
            String log = "Id: "+ cn.getID()+ " ,Name: " + cn.getName() + " , Suraname: " + cn.getSurname() + " ,PhoneNumber: " + cn.getPhoneNumber();
            // Writing Contacts to log
            System.out.println(log);

        }

    }



    public void onPrintContact(View view) {
            //  writePdf();
             createPDF();

    }

    public void writePdf() {

        // TODO Auto-generated method stub
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/vindroid";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);

            File file = new File(dir, "sample.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(document, fOut);
            //open the document
            document.open();


            Paragraph p1 = new Paragraph("Sample PDF CREATION USING IText");
            Font paraFont= new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            document.add(p1);

            Paragraph p2 = new Paragraph("This is an example of a simple paragraph");
            Font paraFont2= new Font(Font.FontFamily.COURIER,14.0f,0, CMYKColor.GREEN);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(paraFont2);

            document.add(p2);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);

            //add image to document
            document.add(myImg);


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally
        {
            document.close();
        }


    }


    private void viewPdf(File path){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(path), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }



    public void createPDF() {//This method works for create pdf file

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
            Paragraph p1 = new Paragraph("Hi pdf is here and it is first page!");
            p1.setAlignment(ALIGN_LEFT);
            //add paragraph to document
            doc.add(p1);
            //add new page
            doc.newPage();
            Paragraph p2 = new Paragraph("This is an PhoneBook");
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



}//End Class
