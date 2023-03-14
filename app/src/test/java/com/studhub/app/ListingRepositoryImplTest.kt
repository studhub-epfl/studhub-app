package com.studhub.app

import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import com.google.firebase.database.*
import org.junit.Test

class ListingRepositoryImplTest {

    @Test
    fun `Test if it's correctly added on firebase database`() {
        val myRef = FirebaseDatabase.getInstance().reference
        val myList = ArrayList<String>()
        myList.add("dato1")
        myList.add("dato2")
        myRef.setValue(myList)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               val value = dataSnapshot.getValue<ArrayList<String>>()
                Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }
}