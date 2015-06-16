package contactnote.example.devang.contactnote;

/**
 * Created by Devang on 3/26/2015.
 */

public class Contact {

    //private variables
    int _id;
    String _refference;
    String _contact_note;

    // Empty constructor
    public Contact(){

    }
    public Contact(int id, String name, String _phone_number){
        this._id = id;
        this._refference = name;
        this._contact_note = _phone_number;
    }

    // constructor
    public Contact(String refference, String _contact_note){
        this._refference  = refference;
        this._contact_note = _contact_note;
    }
    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    // getting name
    public String getRefference(){
        return this._refference;
    }

    // setting name
    public void setRefference(String refference){
        this._refference = refference;
    }

    // getting phone number
    public String getContactNote(){
        return this._contact_note;
    }

    // setting phone number
    public void setContactNote(String contact_note){
        this._contact_note = contact_note;
    }
}
