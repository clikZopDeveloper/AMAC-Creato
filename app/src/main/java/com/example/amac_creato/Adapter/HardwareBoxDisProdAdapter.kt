package com.example.amac_creato.Adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.amac_creato.Activity.BarcodeScanActivity
import com.example.amac_creato.Model.BoxDetailBean
import com.example.amac_creato.R
import com.example.amac_creato.Model.CustOrderListBean
import com.example.amac_creato.Model.MultipleProductBean
import com.example.amac_creato.Model.SlotPackerDetailBean
import com.example.amac_creato.Model.SlotPackerListBean
import com.example.amac_creato.Utills.GeneralUtilities
import com.example.amac_creato.Utills.RvListClickListner

import com.example.amac_creato.Utills.RvStatusClickListner
import com.google.gson.Gson


class HardwareBoxDisProdAdapter(
    var context: Activity,
    var mFilteredList: List<BoxDetailBean.Data.HardwareProduct>,
    var rvClickListner: RvListClickListner
) : RecyclerView.Adapter<HardwareBoxDisProdAdapter.MyViewHolder>(){
   // public val data = mutableListOf<Int>()
    val data: MutableList<MultipleProductBean> = ArrayList()
    var multiple:MultipleProductBean?=null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hardware_prod_list, parent, false)
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

        holder.tvName.text = mFilteredList[position].productName
        holder.tvBarcode.text = mFilteredList[position].productionBarcode?.toString()
        holder.tvDate.text = mFilteredList[position].createdAt.toString()
        holder.tvQty.text = "QTY : "+mFilteredList[position].qty.toString()
        holder.tvPackedScan.text = mFilteredList[position].packed_scan.toString()
        holder.itemView.setOnClickListener {
            rvClickListner.clickPos(data, mFilteredList[position].id)
        }
        holder.ivScanner.visibility=View.GONE
        holder.ivVerifyed.visibility=View.GONE
        holder.checkBox.visibility=View.GONE

       /* if ( mFilteredList[position].isPacked==1){
            holder.ivScanner.visibility=View.GONE
            holder.ivVerifyed.visibility=View.VISIBLE
            holder.checkBox.visibility=View.VISIBLE
        }else{
            holder.ivScanner.visibility=View.VISIBLE
            holder.ivVerifyed.visibility=View.GONE
            holder.checkBox.visibility=View.GONE
        }*/

     /*   holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                 multiple = MultipleProductBean(
                    mFilteredList[position].id)
                data.add(multiple!!)

            } else {
                data.remove(multiple)

            }

            Log.d("zxczxc", Gson().toJson(data))
            //   rvClickListner.clickPos(data, mFilteredList[position].id)
        }*/

        /*holder.ivScanner.setOnClickListener {
            context.startActivityForResult(
                Intent(context, BarcodeScanActivity::class.java)
                    .putExtra("orderProductID", mFilteredList.get(position).id.toString())
                    .putExtra(
                        "variant", mFilteredList.get(position).variant.toString()
                    ), 101
            )
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
        val tvName: TextView = itemview.findViewById(R.id.tvName)
        val tvBarcode: TextView = itemview.findViewById(R.id.tvBarcode)
        val tvDate: TextView = itemview.findViewById(R.id.tvDate)
        val tvQty: TextView = itemview.findViewById(R.id.tvQty)
        val ivScanner: ImageView = itemview.findViewById(R.id.ivScanner)
        val tvPackedScan: TextView = itemview.findViewById(R.id.tvPackedScan)
        val ivVerifyed: ImageView = itemview.findViewById(R.id.ivVerifyed)
        val checkBox: CheckBox = itemview.findViewById(R.id.checkBox)

    }


}