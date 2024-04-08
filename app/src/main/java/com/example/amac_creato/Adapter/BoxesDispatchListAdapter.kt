package com.example.amac_creato.Adapter

import android.app.Activity
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.amac_creato.Activity.BarcodeScanActivity
import com.example.amac_creato.Model.BoxListBean
import com.example.amac_creato.Model.GetCustomersBean
import com.example.amac_creato.R

import com.example.amac_creato.Utills.RvStatusComplClickListner
import com.example.amac_creato.databinding.ItemAllBoxesBinding
import com.example.amac_creato.databinding.ItemAllBoxesdispatchBinding
import com.example.amac_creato.databinding.ItemCustomerListBinding

import com.stpl.antimatter.Utils.ApiContants


class BoxesDispatchListAdapter(
    var context: Activity,
    var list: List<BoxListBean.Data>,
    var rvClickListner: RvStatusComplClickListner
) : RecyclerView.Adapter<BoxesDispatchListAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemAllBoxesdispatchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val binding =
            ItemAllBoxesdispatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //   val v = LayoutInflater.from(parent.context).inflate(R.layout.item_project_list, parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.binding.apply {
            tvSlotID.text = list[position].slotId.toString()
            tvBarcode.text = list[position].barcode.toString()
            tvRemark.text = list[position].remarks.toString()
            tvDate.text = list[position].createdAt.toString()

            if (list[position].isDispatched == 0) {
                ivScanner.visibility = View.VISIBLE
                ivVerifyed.visibility = View.GONE
            } else {
                ivScanner.visibility = View.GONE
                ivVerifyed.visibility = View.VISIBLE
            }

            ivScanner.setOnClickListener {
                context.startActivityForResult(
                    Intent(context, BarcodeScanActivity::class.java)
                        .putExtra("orderProductID", list.get(position).id.toString())
                        .putExtra("variant", ""), 101
                )
            }
        }

        /*     holder.tvAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
             holder.tvQtyAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
             holder.tvQtyMinus.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
             holder.tvQty.background = RoundView(Color.TRANSPARENT, RoundView.getRadius(20f), true, R.color.orange)
             holder.tvOff.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
             holder.tvAdd.visibility = View.VISIBLE*/

        holder.itemView.setOnClickListener {
            rvClickListner.clickPos("", "", "", list[position].id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}