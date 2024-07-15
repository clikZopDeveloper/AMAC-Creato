package com.example.amac_creato.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.amac_creato.R
import com.example.amac_creato.Model.CustOrderListBean
import com.example.amac_creato.Utills.GeneralUtilities

import com.example.amac_creato.Utills.RvStatusClickListner


class CustOrderListAdapter(
    var context: Activity,
    var list: List<CustOrderListBean.Data>,
    var rvClickListner: RvStatusClickListner
) : RecyclerView.Adapter<CustOrderListAdapter.MyViewHolder>(),
    Filterable {
    var mFilteredList: MutableList<CustOrderListBean.Data> =
        list as MutableList<CustOrderListBean.Data>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cust_order_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        /*     holder.tvAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
             holder.tvQtyAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
             holder.tvQtyMinus.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
             holder.tvQty.background = RoundView(Color.TRANSPARENT, RoundView.getRadius(20f), true, R.color.orange)
             holder.tvOff.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
             holder.tvAdd.visibility = View.VISIBLE*/

        holder.tvOrderID.text = mFilteredList[position].order_id.toString()
        holder.tvCustomer.text = mFilteredList[position].customerName
        holder.tvMobNo.text = mFilteredList[position].mobile
        holder.tvLocation.text = mFilteredList[position].address+"\n"+mFilteredList[position].state+" "+mFilteredList[position].city+" "+mFilteredList[position].pincode
        holder.tvInvoiceDate.text = mFilteredList[position].expectedPackingDate.toString()
        holder.tvDate.text = mFilteredList[position].expectedDeliveryDate.toString()
        holder.itemView.setOnClickListener {
            rvClickListner.clickPos("", mFilteredList[position].id)
        }

/*holder.ivCall.setOnClickListener {
    GeneralUtilities.makeCall(context, mFilteredList[position].phone)

}*/

        // holder.ivImage.setImageDrawable(context.resources.getDrawable(list[position].drawableId))

        /*     Glide.with(context)
                 .load(ApiContants.ImgBaseUrl+list[position].imgUrl)
                 .into(holder.ivImage)*/

    }

    override fun getItemCount(): Int {
        return mFilteredList.size
    }

    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val tvCustomer: TextView = itemview.findViewById(R.id.tvCustomer)
        val tvMobNo: TextView = itemview.findViewById(R.id.tvMobNo)
        val tvLocation: TextView = itemview.findViewById(R.id.tvLocation)
        val tvInvoiceDate: TextView = itemview.findViewById(R.id.tvInvoiceDate)
        val tvDate: TextView = itemview.findViewById(R.id.tvDate)
        val tvOrderID: TextView = itemview.findViewById(R.id.tvOrderID)

        val cardView: CardView = itemview.findViewById(R.id.cardView)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    mFilteredList = list as MutableList<CustOrderListBean.Data>
                } else {
                    val filteredList = ArrayList<CustOrderListBean.Data>()
                    for (serviceBean in list) {
                        if (serviceBean.customerName.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(serviceBean)
                        }
                    }
                    mFilteredList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mFilteredList = filterResults.values as ArrayList<CustOrderListBean.Data>
                android.os.Handler().postDelayed(Runnable { notifyDataSetChanged() }, 200)
            }
        }
    }
}