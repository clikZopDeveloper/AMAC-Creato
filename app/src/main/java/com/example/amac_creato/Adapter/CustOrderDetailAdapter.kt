package com.example.amac_creato.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.amac_creato.Activity.BarcodeScanActivity
import com.example.amac_creato.Model.CustOrderDetailBean
import com.example.amac_creato.R
import com.example.amac_creato.Model.CustOrderListBean
import com.example.amac_creato.Utills.GeneralUtilities

import com.example.amac_creato.Utills.RvStatusClickListner


class CustOrderDetailAdapter(
    var context: Activity,
    var mFilteredList: List<CustOrderDetailBean.Data>,
    var rvClickListner: RvStatusClickListner
) : RecyclerView.Adapter<CustOrderDetailAdapter.MyViewHolder>()
     {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.dialog_order_detail, parent, false)
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


        holder.tvName.text = mFilteredList[position].name
        holder.tvBarcode.text = mFilteredList[position].productionBarcode?.toString()
        holder.tvQty.text ="QTY : "+ mFilteredList[position].qty.toString()
        holder.tvDate.text = mFilteredList[position].createdAt.toString()
        holder.ivScanner.setOnClickListener {
            rvClickListner.clickPos("", mFilteredList.get(position).id)
        }

        if (mFilteredList[position].isProduction==1){
            holder.ivScanner.visibility=View.GONE
            holder.ivVerifyed.visibility=View.VISIBLE
        }else{
            holder.ivScanner.visibility=View.VISIBLE
            holder.ivVerifyed.visibility=View.GONE
        }

        /*holder.ivScanner.setOnClickListener {
            val intent = Intent(context, BarcodeScanActivity::class.java)
            intent.putExtra("orderProductID",mFilteredList.get(position).id.toString())
            context.startActivityForResult(intent, 101)

        }*/
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

        val ivScanner: ImageView = itemview.findViewById(R.id.ivScanner)
        val ivVerifyed: ImageView = itemview.findViewById(R.id.ivVerifyed)
        val tvName: TextView = itemview.findViewById(R.id.tvName)
        val tvQty: TextView = itemview.findViewById(R.id.tvQty)
        val tvBarcode: TextView = itemview.findViewById(R.id.tvBarcode)
        val tvDate: TextView = itemview.findViewById(R.id.tvDate)



    }

}