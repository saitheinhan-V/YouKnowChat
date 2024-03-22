package com.chat.youknow.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chat.youknow.MyApplication
import com.chat.youknow.R
import com.chat.youknow.data.response.MessageHistoryResponse
import com.chat.youknow.utils.AppConstant
import com.chat.youknow.utils.millsecondToDate

class MessageAdapter(
    val context: Context,
    private val userId: String,
    private val messageHistory: MessageHistoryResponse,
    private val messageList: MutableList<MessageHistoryResponse.MessageInfo>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_RECEIVE = 1
    private val ITEM_SENT = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return if (viewType == 1) {
            // inflate receive
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            ReceiveViewHolder(view)
        } else {
            // inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            SentViewHolder(view)
        }


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            // do the stuff for sent view holder

            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.mediaData.content.data.text
            holder.tvSendTime.text = millsecondToDate(currentMessage.timeMillisecond!!,AppConstant.TIME_FORMAT)

            val sendDateString = millsecondToDate(currentMessage.timeMillisecond!!,AppConstant.DATE_FORMAT)
            holder.tvDate.text = sendDateString
            if(position == 0){
                holder.tvDate.visibility = View.VISIBLE
            }else{
                val previousMsg = messageList[position-1]
                val previousDateString = millsecondToDate(previousMsg.timeMillisecond!!,AppConstant.DATE_FORMAT)
                if(sendDateString == previousDateString){
                    holder.tvDate.visibility = View.GONE
                }else{
                    holder.tvDate.visibility = View.VISIBLE
                }
            }

        } else {
            // do the stuff for receive view holder
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.mediaData.content.data.text
            holder.tvReceiveTime.text = millsecondToDate(currentMessage.timeMillisecond!!,AppConstant.TIME_FORMAT)

            val sendDateString = millsecondToDate(currentMessage.timeMillisecond!!,AppConstant.DATE_FORMAT)
            holder.tvDate.text = sendDateString
            if(position == 0){
                holder.tvDate.visibility = View.VISIBLE
            }else{
                val previousMsg = messageList[position-1]
                val previousDateString = millsecondToDate(previousMsg.timeMillisecond!!,AppConstant.DATE_FORMAT)
                if(sendDateString == previousDateString){
                    holder.tvDate.visibility = View.GONE
                }else{
                    holder.tvDate.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if(userId == currentMessage.mediaData.sender.id){
            ITEM_SENT
        } else {
         ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage: TextView = itemView.findViewById<TextView>(R.id.txt_sent_message)
        val tvSendTime: TextView = itemView.findViewById(R.id.txt_send_time)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById<TextView>(R.id.txt_receive_message)
        val tvReceiveTime: TextView = itemView.findViewById(R.id.txt_receive_time)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)

    }

}