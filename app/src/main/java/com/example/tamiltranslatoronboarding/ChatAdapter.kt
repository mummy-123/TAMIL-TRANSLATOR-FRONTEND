package com.example.tamiltranslatoronboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val list: List<Pair<String,Boolean>>) :
    RecyclerView.Adapter<ChatAdapter.Holder>() {

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val user = v.findViewById<TextView>(R.id.txtUser)
        val bot = v.findViewById<TextView>(R.id.txtBot)
    }

    override fun onCreateViewHolder(p: ViewGroup, i: Int) =
        Holder(LayoutInflater.from(p.context).inflate(R.layout.item_chat, p, false))

    override fun onBindViewHolder(h: Holder, i: Int) {
        val (msg,isUser) = list[i]
        if(isUser){
            h.user.text = msg
            h.user.visibility = View.VISIBLE
            h.bot.visibility = View.GONE
        } else {
            h.bot.text = msg
            h.bot.visibility = View.VISIBLE
            h.user.visibility = View.GONE
        }
    }

    override fun getItemCount() = list.size
}