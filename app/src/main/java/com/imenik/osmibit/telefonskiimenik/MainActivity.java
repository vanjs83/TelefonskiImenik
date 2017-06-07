package com.imenik.osmibit.telefonskiimenik;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Environment;

import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.itextpdf.text.Element.ALIGN_JUSTIFIED;
import static com.itextpdf.text.Element.ALIGN_LEFT;
import static com.itextpdf.text.pdf.BidiOrder.PDF;


public class MainActivity extends AppCompatActivity {

    //AppCompatActivity
    EditText mName, mSurname, mNumber, mGroup;
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

    public void onSaveContact(View view) {
        //insert on database

        name = mName.getText().toString();
        System.out.println("NAME:" + name);
        surname = mSurname.getText().toString();
        System.out.println("SURNAME :" + surname);
        telNumber = mNumber.getText().toString();
        System.out.println("TELNUBER:" + telNumber);

        db.addContact(new Contact(name, surname, telNumber));

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


        Document doc = new Document(PageSize.A4.rotate());

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
            Paragraph p1 = new Paragraph("Hi!");
            p1.setAlignment(ALIGN_LEFT);
            //add paragraph to document
            doc.add(p1);
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
