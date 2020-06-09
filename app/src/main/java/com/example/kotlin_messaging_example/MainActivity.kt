package com.example.kotlin_messaging_example

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var item_selected_position: Int = -1
    private var touchPositionX: Int = 0
    private var touchPositionY: Int = 0
    private val messages  = JSONArray()
    private var is_selecting_emoji = false
    private lateinit var emoji_selection_view: View
    private val LAUNCH_EMOJI_ACTIVITY = 1

    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        message_list_view.emptyView = empty_element

        createMessages()

        displayMessages()

        message_list_view.setOnTouchListener(OnTouchListener { view, event ->
            touchPositionX = event.x.toInt()
            touchPositionY = event.y.toInt()
            false
        })

        sendBtn.setOnClickListener {
            val text = editText.text.trim()
            if (text.isNotEmpty()) {
                val msg = JSONObject()
                msg.put("Text", text)
                msg.put("Hour", SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()))
                msg.put("Speaker", "me")
                msg.put("Emoji", "")
                messages.put(msg)

                adapter.notifyDataSetChanged();

                val ring: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.send)
                ring.start()

                scrollToBottom()
                editText.setText("")
            }
        }

        attachmentBtn.setOnClickListener {

        }

        // Set title bar
        title = "Harley";
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun createMessages() {

        var msg = JSONObject()
        msg.put("Text", "Hey, it's been a long time, how are you?")
        msg.put("Hour", "20:40")
        msg.put("Speaker", "other")
        msg.put("Emoji", "")
        messages.put(msg)

        msg = JSONObject()
        msg.put("Text", "I saw your mom yesterday, she told me you were about to move?")
        msg.put("Hour", "20:40")
        msg.put("Speaker", "other")
        msg.put("Emoji", "")
        messages.put(msg)

        msg = JSONObject()
        msg.put("Text", "Hey Harley! It's been a while yes, I'm glad you took the time to send me a message!")
        msg.put("Hour", "20:42")
        msg.put("Speaker", "me")
        msg.put("Emoji", "")
        messages.put(msg)

        msg = JSONObject()
        msg.put("Text", "I'm doing great thanks. How are you?")
        msg.put("Hour", "20:43")
        msg.put("Speaker", "me")
        msg.put("Emoji", "")
        messages.put(msg)

        msg = JSONObject()
        msg.put("Text", "Indeed, I applied at Microsoft and got the job! I'm moving to Atlanta the next week.")
        msg.put("Hour", "20:43")
        msg.put("Speaker", "me")
        msg.put("Emoji", "heart")
        messages.put(msg)

        msg = JSONObject()
        msg.put("Text", "I'd love to see you before moving. Are you free this week?")
        msg.put("Hour", "20:44")
        msg.put("Speaker", "me")
        msg.put("Emoji", "")
        messages.put(msg)

        msg = JSONObject()
        msg.put("Text", "That's good to hear. I'm good too, I recently got a promotion!")
        msg.put("Hour", "20:47")
        msg.put("Speaker", "other")
        msg.put("Emoji", "")
        messages.put(msg)

        msg = JSONObject()
        msg.put("Text", "Whoooa! That's exciting, I always knew you'd be able to do something like that. I'm free on tuesday.")
        msg.put("Hour", "20:48")
        msg.put("Speaker", "other")
        msg.put("Emoji", "")
        messages.put(msg)

        msg = JSONObject()
        msg.put("Text", "There's a great coffee place that recently opened near Mc Cheese street, do you want to meet there around 3pm?")
        msg.put("Hour", "20:48")
        msg.put("Speaker", "other")
        msg.put("Emoji", "")
        messages.put(msg)

        msg = JSONObject()
        msg.put("Text", "Perfect for me! I can't wait to see, I think we have a lot to talk about!")
        msg.put("Hour", "20:49")
        msg.put("Speaker", "me")
        msg.put("Emoji", "")
        messages.put(msg)

        msg = JSONObject()
        msg.put("Text", "That's fore sure! See you on tuesday then, byyyye!")
        msg.put("Hour", "20:52")
        msg.put("Speaker", "other")
        msg.put("Emoji", "like")
        messages.put(msg)

        msg = JSONObject()
        msg.put("Text", "See you! \uD83D\uDE18")
        msg.put("Hour", "20:53")
        msg.put("Speaker", "me")
        msg.put("Emoji", "")
        messages.put(msg)
    }

    private fun scrollToBottom(){
        message_list_view.post(Runnable { // Select the last row so it will scroll into view...
            message_list_view.setSelection(adapter.count - 1)
        })
    }

    private fun displayMessages() {
        adapter = MessageAdapter(this, messages)
        message_list_view.adapter = adapter

        scrollToBottom()

        // Long press
        message_list_view.setOnItemLongClickListener { parent, view, position, id ->
            val element = adapter.getItem(position) // The item that was clicked

            val item: JSONObject = adapter.getItem(position) as JSONObject // The item that was clicked

            Log.d("MESSAGES", "Touched: $element")
            Log.d("MESSAGES", "X: $view.x, Y: $view.y")
            Log.d("MESSAGES", "X: $touchPositionX, Y: $touchPositionY")

            emoji_selection_view = view
            item_selected_position = position
            is_selecting_emoji = true

            val i = Intent(applicationContext, ReactionsActivity::class.java)
            i.putExtra("x", touchPositionX)
            i.putExtra("y", touchPositionY)
            startActivityForResult(i, LAUNCH_EMOJI_ACTIVITY)

            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_EMOJI_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getStringExtra("emoji")
                Log.d("EMOJI", "Res $result")

                val jsonObject: JSONObject = messages.getJSONObject(item_selected_position)
                jsonObject.put("Emoji", result)

                messages.put(item_selected_position, jsonObject)

                adapter.notifyDataSetChanged();
                val ring: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.pop)
                ring.start()
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}