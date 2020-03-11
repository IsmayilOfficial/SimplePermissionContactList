package ut.ee.cs.fm_demolist

import android.Manifest
import androidx.appcompat.app.AppCompatActivity


import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri

import android.os.Bundle
import android.provider.ContactsContract
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedInputStream


class MainActivity : AppCompatActivity() {

    val REQUEST_PERMISSION = 1
    lateinit var searchText:String
    lateinit var  adapter :ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun searcIn(search: SearchView) {
            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(textSearch: String?): Boolean {
                    searchText=textSearch!!
                                      return false
                }

                override fun onQueryTextChange(newtextSearch: String): Boolean {
                    searchText=newtextSearch

                    if(searchText.length>0){
                        filered()

                    }


                    if(searchText.length==0){



                         serched()
                        if(searchText.length==0){



                           getContacts()


                        }


                    }
                    else{


                      Toast.makeText(this@MainActivity,"Not Found",Toast.LENGTH_LONG).show()



                    }
                    return true
                }



            })


        }



        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),REQUEST_PERMISSION)
        }
        else{
            getContacts()
        }

        searcIn(searchView)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==REQUEST_PERMISSION)getContacts()
    }
    public fun getContacts(){
        adapter =ListAdapter(this,getContactsData())
        contact_list.adapter = adapter
    }
fun serched(){

    var zero=  ArrayList<Contact>()
    adapter =ListAdapter(this@MainActivity,zero )
    contact_list.adapter = adapter
    adapter.notifyDataSetChanged()
}
    fun filered(){


        adapter =ListAdapter(this@MainActivity,getContactsDataFiltered() )
        contact_list.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    private fun getContactsData(): ArrayList<Contact> {
        val contactList=ArrayList<Contact>()
        val contactsCursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
        if ((contactsCursor?.count ?:0)>0){
            while (contactsCursor !=null && contactsCursor.moveToNext()){
                val rowID=contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name=contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var phoneNumber=""
                if (contactsCursor.getInt(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))>0){
                    val phoneNumberCursor=contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                        arrayOf<String>(rowID),
                        null

                    )
                    while (phoneNumberCursor!!.moveToNext()){
                        phoneNumber +=phoneNumberCursor.getString(
                            phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )+"\n"
                    }
                    phoneNumberCursor.close()
                }
                var email =""
                val emailCursor=contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID+"=?",
                    arrayOf<String>(rowID),
                    null

                )
                while (emailCursor!!.moveToNext()){
                    email+=emailCursor.getString(
                        emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)

                    )+"\n"
                }
                emailCursor.close()
                val contactPhotoUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,rowID)
                val photoStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver,contactPhotoUri)
                val buffer  = BufferedInputStream(photoStream)
                val contactPhoto = BitmapFactory.decodeStream(buffer)


                contactList.add(Contact(name,phoneNumber,email,contactPhoto))


            }


        }
        contactsCursor!!.close()

        return contactList

    }
    private fun getContactsDataFiltered(): ArrayList<Contact> {
        val contactList=ArrayList<Contact>()
        val contactsCursor=contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
        if ((contactsCursor?.count ?:0)>0){
            while (contactsCursor !=null && contactsCursor.moveToNext()){
                val rowID=contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name=contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var phoneNumber=""
                if (contactsCursor.getInt(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))>0){
                    val phoneNumberCursor=contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",
                        arrayOf<String>(rowID),
                        null

                    )
                    while (phoneNumberCursor!!.moveToNext()){
                        phoneNumber +=phoneNumberCursor.getString(
                            phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )+"\n"
                    }
                    phoneNumberCursor.close()
                }
                var email =""
                val emailCursor=contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID+"=?",
                    arrayOf<String>(rowID),
                    null

                )
                while (emailCursor!!.moveToNext()){
                    email+=emailCursor.getString(
                        emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)

                    )+"\n"
                }
                emailCursor.close()
                val contactPhotoUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,rowID)
                val photoStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver,contactPhotoUri)
                val buffer  = BufferedInputStream(photoStream)
                val contactPhoto = BitmapFactory.decodeStream(buffer)

         if (searchText==name)     contactList.add(Contact(name,phoneNumber,email,contactPhoto))


            }


        }
        contactsCursor!!.close()

        return contactList

    }
}

