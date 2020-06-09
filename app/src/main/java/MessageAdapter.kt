package com.example.kotlin_messaging_example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

class MessageAdapter(private val context: Context,
                     private val dataSource: JSONArray
) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = getItem(position) as JSONObject
        val speaker = item.get("Speaker").toString()

        val rowView: View

        // Get view for row item
        if(speaker == "me") rowView = inflater.inflate(R.layout.list_item_me, parent, false)
        else{
            rowView = inflater.inflate(R.layout.list_item_correspondant, parent, false)
            // Get thumbnail element
            val userIcon = rowView.findViewById(R.id.user_icon) as ImageView
        }

        // Get title element
        val msgText = rowView.findViewById(R.id.message_text) as TextView

        // Get subtitle element
        val msgHour = rowView.findViewById(R.id.message_hour) as TextView

        msgText.text = item.get("Text").toString()
        msgHour.text = item.get("Hour").toString()

        // TODO: set icon based on user

        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.length()
    }
}