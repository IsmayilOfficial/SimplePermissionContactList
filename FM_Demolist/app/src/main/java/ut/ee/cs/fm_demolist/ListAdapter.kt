package ut.ee.cs.fm_demolist

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.contact_data.*
import kotlinx.android.synthetic.main.contact_data.view.*
import java.text.FieldPosition

class ListAdapter(val context: Context,val list: ArrayList<Contact>):BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val view:View =LayoutInflater.from(context).inflate(R.layout.contact_data,parent,false)
        view.contact_name.text=list[position].name
        view.contact_phone_number.text=list[position].phoneNumber
        view.contact_email.text=list[position].email
        view.contact_photo.setImageBitmap(list[position].photo)



        var email_fur_sent= view.contact_email.text
        view.my_button.setOnClickListener {




            val intent = Intent(view.context, Write::class.java)
            intent.putExtra("key", "$email_fur_sent")

            startActivity(view.context,intent,null);

        }
        view.CALL.setOnClickListener(){
            var phoneNumber =list[position].phoneNumber
            val intent = Intent(Intent.ACTION_DIAL).apply {

                data = Uri.parse("tel:$phoneNumber")
            }


            startActivity(view.context,intent,null);



        }

        return  view
            }

    override fun getItem(position: Int): Any {
       return list[position]
    }

    override fun getItemId(position: Int): Long {
      return  position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

}