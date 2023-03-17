package com.farasatnovruzov.recyclerlist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import az.turanbank.mlb.R
import az.turanbank.mlb.data.remote.model.response.NotificationResult
import az.turanbank.mlb.presentation.cardoperations.friendtransfer.TransferFriendListActivity
import az.turanbank.mlb.presentation.resources.loan.onlinecredit.FirstSubmittingLoanActivity
import az.turanbank.mlb.presentation.resources.loan.onlinecredit.LoanDocumentsActivity
import az.turanbank.mlb.presentation.resources.loan.onlinecredit.OnlineCreditSuccessActivity
import az.turanbank.mlb.util.Page
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.notification_list.view.*


class NotificationAdapter(
    var notificationList: MutableList<NotificationResult>,
    val context: Context,
    var isCustomerType: Int,
    private val clickListener: (position: Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var notificationFilterList = ArrayList<NotificationResult>()
    val activity = context as NotificationActivity


    // private var clickListener: ClickListener? = null
    private var isMultiSelectModeActive = false
    // exampleListFull . exampleList

    fun selectorDeleteActive(lmbdDelete: (ArrayList<NotificationResult>) -> Unit) {
        var selectList = ArrayList<NotificationResult>()
        notificationFilterList.forEach {
            if (it.isSelected == true) {
                selectList.add(it)
            }
        }
        if (selectList.size > 0) {
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.delete_notification_title))
                .setMessage(context.getString(R.string.delete_notification_content))
                .setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                    lmbdDelete(selectList)
                    selectList.forEach {
                        notificationList.remove(it)
                        notificationFilterList.remove(it)
                        isMultiSelectModeActive = false
                        notifyDataSetChanged()
                        val activity = context as NotificationActivity
                        activity.selectAllVisible(isMultiSelectModeActive)
                    }
                }
                .setNegativeButton(context.getString(R.string.no), null)
                .show()
        } else {
            isMultiSelectModeActive = !isMultiSelectModeActive
            notifyDataSetChanged()
            val activity = context as NotificationActivity
            activity.selectAllVisible(isMultiSelectModeActive)
        }


    }

    fun selectorReadActive(lmbdRead: (ArrayList<NotificationResult>) -> Unit) {
        var selectList = ArrayList<NotificationResult>()
        notificationFilterList.forEach {
            if (it.isSelected == true) {
                selectList.add(it)
                notificationList.get(notificationList.indexOf(it)).isRead = 1
                notificationFilterList.get(notificationFilterList.indexOf(it)).isRead = 1
            }
        }
        if (selectList.size > 0) {
            lmbdRead(selectList)
            deselectAll()
            isMultiSelectModeActive = false
            notifyDataSetChanged()
            val activity = context as NotificationActivity
            activity.selectAllVisible(isMultiSelectModeActive)
        } else {
            isMultiSelectModeActive = !isMultiSelectModeActive
        }

        notifyDataSetChanged()
        val activity = context as NotificationActivity
        activity.selectAllVisible(isMultiSelectModeActive)
    }

    private fun activateMultiSelection() {
        isMultiSelectModeActive = true
        val activity = context as NotificationActivity
        activity.selectAllVisible(isMultiSelectModeActive)
    }

    private fun deactivateMultiSelection() {
        isMultiSelectModeActive = true
        val activity = context as NotificationActivity
        activity.selectAllVisible(isMultiSelectModeActive)
    }


    fun setAllSelected(active: Boolean) {
        if (active) {
            for (op in notificationList) {
                op.isSelected = true
            }
            for (op in notificationFilterList) {
                op.isSelected = true
            }
            activateMultiSelection()
            notifyDataSetChanged()
        } else {
            for (op in notificationList) {
                op.isSelected = false
            }
            for (op in notificationFilterList) {
                op.isSelected = false
            }
            activateMultiSelection()
            notifyDataSetChanged()
        }
//        val activity = context as NotificationActivity
//        activity.selectAllVisible(isMultiSelectModeActive)
    }

    fun getAllSelectedItems() = notificationFilterList


    init {
        notificationFilterList = notificationList as ArrayList<NotificationResult>
    }

    fun addToAdapter(contact: NotificationResult) {
        notificationFilterList.add(contact)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(notificationFilterList.get(position))
        val authOperation = notificationFilterList[position]
        val typefaceMedium = ResourcesCompat.getFont(context, R.font.roboto_regular)
        val typefaceBold = ResourcesCompat.getFont(context, R.font.roboto_bold)

        holder.itemView.notificationTitle.text = authOperation.title
        holder.itemView.notificationBody.text = authOperation.content
        holder.itemView.notificationDate.text = authOperation.date.split(" ")[0]
        holder.itemView.notificationTime.text = authOperation.date.split(" ")[1]


        if (authOperation.isRead == 0) {
            holder.itemView.notificationTitle.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.black
                )
            )
            holder.itemView.notificationBody.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.black
                )
            )
            holder.itemView.notificationTitle.typeface = typefaceBold
        } else {
            holder.itemView.notificationTitle.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.sell_buy_color
                )
            )
            holder.itemView.notificationBody.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.sell_buy_color
                )
            )
            holder.itemView.notificationTitle.typeface = typefaceMedium
        }

        if (authOperation.notificationTypeId == 2) {
            holder.itemView.notificationImage.setImageResource(R.drawable.transfer_notification_icon)
        } else if (authOperation.image != null) {
            Picasso.with(context)
                .load(authOperation.image)
                .placeholder(R.drawable.no_profile_icon)
                .error(R.drawable.no_profile_icon)
                .resize(50, 50)
                .centerCrop()
                .into(holder.itemView.notificationImage)
        } else {
            holder.itemView.notificationImage.setImageResource(R.drawable.no_profile_icon)
        }

        holder.itemView.authOpCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isMultiSelectModeActive) {
                if (isChecked) {
                    if (!notificationFilterList.contains(authOperation))
                        notificationFilterList.add(authOperation)
                    authOperation.isSelected = true
                    //  holder.itemView.authOpCrName.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    holder.itemView.notificationContainer.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.grayish
                        )
                    )
                    clickListener.invoke(notificationFilterList.size)
                } else {
                    if (notificationFilterList.contains(authOperation))
                        authOperation.isSelected = false
                    //  notificationFilterList.remove(authOperation)
                    holder.itemView.notificationContainer.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    clickListener.invoke(notificationFilterList.size)
                }
            }
        }

        if (isMultiSelectModeActive) {
            holder.itemView.authOpCheckBox.visibility = View.VISIBLE
        } else {
            holder.itemView.authOpCheckBox.visibility = View.GONE
        }

        if (authOperation.isSelected) {
            holder.itemView.authOpCheckBox.visibility = View.VISIBLE
            /* holder.itemView.authOpCrName.setTextColor(
                 ContextCompat.getColor(context, R.color.colorPrimary)
             )*/
            holder.itemView.notificationContainer.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.grayish
                )
            )
            holder.itemView.authOpCheckBox.isChecked = true
        } else {
            //  holder.itemView.authOpCrName.setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
            holder.itemView.notificationContainer.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
            holder.itemView.authOpCheckBox.isChecked = false
        }
        holder.itemView.setOnLongClickListener {
            if (!isMultiSelectModeActive) {
                activateMultiSelection()
                authOperation.isSelected = true
                notifyDataSetChanged()
                val activity = context as NotificationActivity
                activity.selectAllVisible(isMultiSelectModeActive)
            } else {
                deactivateMultiSelection()
                deselectAll()
            }
            true
        }

        holder.itemView.setOnClickListener {
            if (isMultiSelectModeActive) {
                if (authOperation.isSelected) {
                    authOperation.isSelected = false

                    //     holder.itemView.authOpCrName.setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
                    holder.itemView.notificationContainer.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    notifyDataSetChanged()
                    val activity = context as NotificationActivity
                    activity.selectAllVisible(isMultiSelectModeActive)
                } else {
                    authOperation.isSelected = true

                    //     holder.itemView.authOpCrName.setTextColor(ContextCompat.getColor(context, R.color.dark_gray))
                    holder.itemView.notificationContainer.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.grayish
                        )
                    )
                    notifyDataSetChanged()
                    val activity = context as NotificationActivity
                    activity.selectAllVisible(isMultiSelectModeActive)
                }
            } else {
                if (authOperation.isRead == 0) {
                    authOperation.isRead = 1
                    holder.itemView.notificationTitle.typeface = typefaceMedium
                    holder.itemView.notificationTitle.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.sell_buy_color
                        )
                    )
                    holder.itemView.notificationBody.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.sell_buy_color
                        )
                    )
                    val activity = holder.itemView.context as NotificationActivity
                    val readList = ArrayList<NotificationResult>()
                    readList.add(authOperation)
                    activity.readOneOperationLmbd(readList)
                    notifyDataSetChanged()
                    activity.selectAllVisible(isMultiSelectModeActive)
                }
                if (authOperation.notificationTypeId == 2) {
                    val activity = holder.itemView.context as NotificationActivity
                    val intent = Intent(context, TransferFriendListActivity::class.java)
                    if (authOperation.notificationType == "FriendNotificationToFriend") {
                        intent.putExtra("type", "FriendNotificationToFriend")
                    } else {
                        intent.putExtra("type", "FriendNotificationToMe")
                    }
                    activity.startActivity(intent)
                }
                if (authOperation.notificationTypeId == 5) { // (notificationTypeId == 5) online credit's notification
                    val page = authOperation.page
                    when (page) {
                        Page.APPLY_İNPROGRESS.id -> {
                            val activity = holder.itemView.context as NotificationActivity
                            val intent = Intent(context, OnlineCreditSuccessActivity::class.java)
                            intent.putExtra("page", page)
                            activity.startActivity(intent)
                        }
                        Page.LOAN_WAS_APPROVED.id -> {
                            val activity = holder.itemView.context as NotificationActivity
                            val intent = Intent(context, FirstSubmittingLoanActivity::class.java)
                            intent.putExtra("loanOrderId", (authOperation.loanOrderId).toString())
                            intent.putExtra("page", page)
                            activity.startActivity(intent)
                        }
                        Page.APPLY_WAS_ACCEPTED.id -> {
                            val activity = holder.itemView.context as NotificationActivity
                            val intent = Intent(context, OnlineCreditSuccessActivity::class.java)
                            intent.putExtra("page", page)
                            intent.putExtra("iSCustomerType", isCustomerType)
                            activity.startActivity(intent)
                        }
                        Page.MUST_BE_SIGNED.id -> {
                            val activity = holder.itemView.context as NotificationActivity
                            val intent = Intent(context, LoanDocumentsActivity::class.java)
                            intent.putExtra("loanOrderId", (authOperation.loanOrderId).toString())
                            activity.startActivity(intent)
                        }
                        Page.LOAN_WAS_REJECTED.id -> { // without buttons
                            val activity = holder.itemView.context as NotificationActivity
                            val intent = Intent(context, FirstSubmittingLoanActivity::class.java)
                            intent.putExtra("page", page)
                            intent.putExtra("loanOrderId", (authOperation.loanOrderId).toString())
                            activity.startActivity(intent)
                        }
                        Page.DONT_AGREE_WITH_LOAN.id -> { // without disagree buttons
                            val activity = holder.itemView.context as NotificationActivity
                            val intent = Intent(context, FirstSubmittingLoanActivity::class.java)
                            intent.putExtra("loanOrderId", (authOperation.loanOrderId).toString())
                            intent.putExtra("page", page)
                            activity.startActivity(intent)
                        }
                    }
                }
                else {
                    val activity = holder.itemView.context as NotificationActivity
                    val intent = Intent(context, NotificationDetailActivity::class.java)
                    intent.putExtra("title", authOperation.title)
                    intent.putExtra("body", authOperation.content)
                    if (authOperation.image != null) {
                        intent.putExtra("image", authOperation.image)
                    } else {
                        intent.putExtra("image", "")
                    }
                    activity.startActivity(intent)
                }

            }
        }
        clickListener.invoke(notificationFilterList.size)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.notification_list, parent, false))
    }

    override fun getItemCount(): Int {
        return notificationFilterList.size
    }


    fun getItem(position: Int): NotificationResult? {
        //return if (mDataSet != null) mDataSet[position] else null
        return notificationFilterList.get(position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(model: NotificationResult): Unit {

        }

        init {
//            if (clickListener != null) {
//                itemView.setOnClickListener(this)
//            }
        }


        override fun onClick(v: View?) {
//            if (v != null) {
//            //    clickListener?.onItemClick(v,adapterPosition)
//            }
        }


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    activity.isRefreshing(true)
                    notificationFilterList = notificationList as ArrayList<NotificationResult>
                } else {
                    activity.isRefreshing(false)
                    val resultList = ArrayList<NotificationResult>()
                    for (row in notificationList) {
                        if (row.title != null && row.content != null) {
                            if (row.title.toLowerCase().contains(
                                    constraint.toString().toLowerCase()
                                ) || row.content.toLowerCase()
                                    .contains(constraint.toString().toLowerCase())
                            ) {
                                resultList.add(row)
                            }
                        }
                    }
                    notificationFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = notificationFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results?.values != null) {
                    notificationFilterList = results.values as ArrayList<NotificationResult>
                    notifyDataSetChanged()

                    activity.selectAllVisible(isMultiSelectModeActive)
                }
            }
        }
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        //  this.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }

    fun deselectAll() {
        //  notificationFilterList.clear()
        for (op in notificationList) {
            op.isSelected = false
        }
        clickListener.invoke(0)
        isMultiSelectModeActive = false
        notifyDataSetChanged()
//        val activity = context as NotificationActivity
//        activity.selectAllVisible(isMultiSelectModeActive)
    }

//    fun setAllSelected() {
//        notificationFilterList.clear()
//        for (op in notificationList) {
//            op.isSelected = true
//            notificationFilterList.add(op)
//        }
//        activateMultiSelection()
//        notifyDataSetChanged()
//    }
}



