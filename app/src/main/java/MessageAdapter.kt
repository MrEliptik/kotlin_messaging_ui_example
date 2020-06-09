package com.example.kotlin_messaging_example

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item_correspondant.view.*
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

        // Get hour element
        val msgHour = rowView.findViewById(R.id.message_hour) as TextView

        // Get reaction element
        val reaction = rowView.findViewById(R.id.reaction) as ImageView

        msgText.text = item.get("Text").toString()
        msgHour.text = item.get("Hour").toString()

        when (item!!.get("Emoji").toString()) {
            "dislike" -> {
                reaction.visibility = View.VISIBLE
                reaction.setImageResource(R.drawable.dislike_48)
            }
            "like" -> {
                reaction.visibility = View.VISIBLE
                reaction.setImageResource(R.drawable.like_48)
            }
            "heart" -> {
                reaction.visibility = View.VISIBLE
                reaction.setImageResource(R.drawable.heart_48)
            }
            "surprised" -> {
                reaction.reaction.visibility = View.VISIBLE
                reaction.reaction.setImageResource(R.drawable.surprised_48)
            }
            "facepalm" -> {
                reaction.reaction.visibility = View.VISIBLE
                reaction.reaction.setImageResource(R.drawable.facepalm_48)
            }
            "smile" -> {
                reaction.reaction.visibility = View.VISIBLE
                reaction.reaction.setImageResource(R.drawable.smile_48)
            }
        }

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