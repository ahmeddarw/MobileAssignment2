package com.example.assignment2ahmedjava;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Database dbHelper;
    //EditText components for user input
    EditText Address;
    EditText Latitude;
    EditText Longitude;
    EditText ID;
    //Textview component for displaying result
    TextView Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new Database(this);
        //referencing by ID for each component
        Address = findViewById(R.id.Address);
        Longitude = findViewById(R.id.Longitude);
        Latitude = findViewById(R.id.Latitude);
        Result = findViewById(R.id.result);
        ID = findViewById(R.id.ID);

        //setting listeners for update,query,add,and delete buttons
        findViewById(R.id.updatebtn).setOnClickListener(v -> update_Loc());
        findViewById(R.id.querybtn).setOnClickListener(v -> query_Loc());
        findViewById(R.id.addbtn).setOnClickListener(v -> add_Loc());
        findViewById(R.id.deletebtn).setOnClickListener(v -> delete_Loc());
    }
    // method for updating a location that already exists
    public void update_Loc() {
        //check if any of the required inputs are missing
        if (isFieldEmpty(ID, Address, Latitude, Longitude))
            return;
        //get id,address,latitude,longitude from user and update
        int id = Integer.parseInt(getText(ID));
        String address = getText(Address);
        float latitude = Float.parseFloat(getText(Latitude));
        float longitude = Float.parseFloat(getText(Longitude));
        boolean isUpdated = dbHelper.update_data(id, address, longitude, latitude);
        // confirm if the location was updated/not updated
        Result.setText(isUpdated ? "Location was updated!" : "Failed to update location!");
    }
    //method for adding a location
    public void add_Loc() {
        //check if address,latitude,longitude inputs are empty
        if (isFieldEmpty(Address, Latitude, Longitude)) return;
        //get address,longitude,latitude from user
        String address = getText(Address);
        float longitude = Float.parseFloat(getText(Longitude));
        float latitude = Float.parseFloat(getText(Latitude));

        //add data from user and show if location was added or not
        long result = dbHelper.insert_data(address, longitude, latitude);
        Result.setText(result != -1 ? "Location was added!" : "Failed to add location.");
    }
    //method to query location from user
    public void query_Loc() {
        //check if address input is empty
        if (isFieldEmpty(Address))
            return;
        //get address from user and check if its in database
        String address = getText(Address);
        Cursor cursor = dbHelper.getData(address);
        // if location exists, show it
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_ID));
            float latitude = cursor.getFloat(cursor.getColumnIndexOrThrow(Database.COLUMN_LAT));
            float longitude = cursor.getFloat(cursor.getColumnIndexOrThrow(Database.COLUMN_LONG));
            Result.setText("ID: " + id + " location: " + address + " latitude: " + latitude + " longitude: " + longitude);

        }
            // if location doesnt exists show that it wasnt found
        else {
            Result.setText("Location not found.");
        }
    }
    //method for deleting location
    public void delete_Loc() {
        //check if Id input is empty
        if (isFieldEmpty(ID))
            return;
        //get Id from user and delete the record
        int id = Integer.parseInt(getText(ID));
        boolean isDeleted = dbHelper.delete_data(String.valueOf(id));
        //show if location was deleted/not deleted
        Result.setText(isDeleted ? "Location was deleted" : "Failed to delete location.");
    }

    //method to get text from edittext abd trim it
    public String getText(EditText field) {
        return field.getText().toString().trim();
    }
    //check if the necessary fields are empty
    public boolean isFieldEmpty(EditText... fields) {
        for (EditText field : fields) {
            if (field.getText().toString().trim().isEmpty()) {
                //ask user ti fill the necessary field if they're not filled
                Result.setText("Please fill in all necessary inputs");
                return true;
            }
        }
        return false;
    }
}
