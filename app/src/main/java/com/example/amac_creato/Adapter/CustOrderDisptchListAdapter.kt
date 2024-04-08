package com.example.amac_creato.Adapter

import android.app.Activity
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.amac_creato.Model.CustOrderDispatBean
import com.example.amac_creato.Model.GetCustomersBean
import com.example.amac_creato.R

import com.example.amac_creato.Utills.RvStatusComplClickListner
import com.example.amac_creato.databinding.ItemCustomerListBinding
import com.example.amac_creato.databinding.ItemCustorderDisptchListBinding

import com.stpl.antimatter.Utils.ApiContants


class CustOrderDisptchListAdapter(
    var context: Activity,
    var list: List<CustOrderDispatBean.Data>,
    var rvClickListner: RvStatusComplClickListner
) : RecyclerView.Adapter<CustOrderDisptchListAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: ItemCustorderDisptchListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val binding =
            ItemCustorderDisptchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //   val v = LayoutInflater.from(parent.context).inflate(R.layout.item_project_list, parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.binding.apply {
            tvName.text = list[position].customerName.toString()
            tvMobile.text = list[position].mobile.toString()
            tvGST.text = list[position].name.toString()
            tvReamrk.text = list[position].remarks.toString()
            tvAddress.text = list[position].address.toString()+"\n"+list[position].state.toString()+" "+list[position].city.toString()+" ( "+list[position].pincode.toString()+" ) "
         //   tvCity.text = list[position].city.toString()
            tvDate.text = list[position].createdAt.toString()
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