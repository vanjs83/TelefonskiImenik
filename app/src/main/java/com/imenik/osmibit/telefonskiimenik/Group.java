package com.imenik.osmibit.telefonskiimenik;

/**
 * Created by Student on 5.6.2017..
 */

public class Group {
    int id;
    String name;
   public Group(){} //Default constructor
   public Group (int _id, String _name){
        this.id= _id;
        this.name = _name;
    }
   public Group(String _name){
        this.name = _name;
    }


    public int getID(){
        return this.id;
    }

    public void setID(int _id){
        this.id = _id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String _name){
        this.name = _name;
    }



};
