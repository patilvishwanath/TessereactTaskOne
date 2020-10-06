package com.vp.launcherapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vp.launchersdk.model.AppInfo
import com.vp.launcherapp.R
import kotlinx.android.synthetic.main.list_item.view.*

/**
 * Created by Vishwanath Patil on 02/10/20.
 */

class LauncherAdapter(var listeners: OnClickListeners, var list: List<AppInfo>) : RecyclerView.Adapter<LauncherAdapter.AppViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
       return AppViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.setInfo(list[position])

        holder.itemView.setOnClickListener{
            listeners.onItemClick(position,holder.itemView,list[position])
        }

        holder.itemView.setOnLongClickListener{
            listeners.onLongItemClick(position,holder.itemView,list[position])
            return@setOnLongClickListener false
        }
    }

    fun updateData(t: List<AppInfo>?) {
        if (t != null ) {
            list = t
            notifyDataSetChanged()
        }
    }




    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        fun setInfo(appInfo: AppInfo){
            itemView.appName.text = appInfo.appName
            itemView.packageName.text = appInfo.packageName
            itemView.activityName.text = appInfo.mainActivity
            itemView.versionCode.text = appInfo.versionCode
            itemView.versionName.text = appInfo.versionName

            itemView.appIcon.setImageDrawable(appInfo.icon)

        }

    }

    interface OnClickListeners {
        fun onItemClick(position: Int, parent: View, appInfo: AppInfo)
        fun onLongItemClick(position: Int, parent: View, appInfo: AppInfo)
    }
}