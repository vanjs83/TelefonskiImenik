package com.imenik.osmibit.telefonskiimenik;

import android.util.Log;

import java.security.acl.*;

/**
 * Created by Student on 31.5.2017..
 */
public class Contact {
    int _id;
    int _groupId;
    String _name;
    String _surname;
    String _phone_number;
    public Contact(){}

    public Contact(int id, int groupId, String name, String _surname, String _phone_number){
        this._id = id;
        this._groupId = groupId;
        this._name = name;
        this._surname=_surname;
        this._phone_number = _phone_number;
        Log.d( "Constructor" , "id AND Group id" );
    }


    public Contact(int groupId, String name, String _surname, String _phone_number){
        this._groupId = groupId;
        this._name = name;
        this._surname=_surname;
        this._phone_number = _phone_number;
        Log.d( "Constructor" , "Group id" );
    }


    public Contact(String name, String surname, String phone_number){
        this._name = name;
        this._surname = surname;
        this._phone_number = phone_number;
        Log.d( "Constructor" , "NOT ID" );
    }



    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public int getGroupID(){
        return this._groupId;
    }

    public void setGroupID(int groupId){ this._groupId = groupId;}

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
    }

    public String getSurname(){
        return this._surname;
    }

    public void setSurname(String name){
        this._surname = name;
    }

    public String getPhoneNumber(){
        return this._phone_number;
    }

    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }
};


