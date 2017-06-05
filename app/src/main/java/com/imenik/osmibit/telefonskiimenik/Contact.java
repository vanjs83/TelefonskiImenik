package com.imenik.osmibit.telefonskiimenik;

/**
 * Created by Student on 31.5.2017..
 */
public class Contact {
    int _id;
    String _name;
    String _surname;
    String _phone_number;
    public Contact(){}
    public Contact(int id, String name, String _surname, String _phone_number){
        this._id = id;
        this._name = name;
        this._surname=_surname;
        this._phone_number = _phone_number;
    }

    public Contact(String name, String surname, String phone_number){
        this._name = name;
        this._surname = surname;
        this._phone_number = phone_number;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

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
        this._name = name;
    }

    public String getPhoneNumber(){
        return this._phone_number;
    }

    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }
};


