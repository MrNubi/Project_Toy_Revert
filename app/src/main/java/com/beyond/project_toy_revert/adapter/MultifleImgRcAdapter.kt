package com.beyond.project_toy_revert.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.beyond.project_toy_revert.R
import com.beyond.project_toy_revert.datas.MultifleImgDataModel
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.gun0912.tedpermission.provider.TedPermissionProvider
import com.gun0912.tedpermission.provider.TedPermissionProvider.context
import kotlinx.coroutines.withContext

class MultifleImgRcAdapter(val multiList: MutableList<MultifleImgDataModel>): RecyclerView.Adapter<MultifleImgRcAdapter.ViewHolder>() {
    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val multiImg = itemView.findViewById<ImageView>(R.id.img_itemrc_multifile) // 이미지
        val multiVidio = itemView.findViewById<PlayerView>(R.id.exo_itemrc_multifle)// 비디오
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewMulti= LayoutInflater.from(parent.context).inflate(R.layout.item_rv_multifile, parent, false )
        return(ViewHolder(viewMulti)).apply {
            multiVidio.setOnClickListener {
                val url = multiList[position].url
                var player = SimpleExoPlayer.Builder(context).build()
                multiVidio.player = player
                val dataSourceFactory = DefaultDataSourceFactory(context)
                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
                player?.setMediaSource(mediaSource)
                player?.prepare()
                player?.play()

            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val multiURL = multiList.get(position).url

        if(multiURL!=""){
            Glide.with(TedPermissionProvider.context).load(multiList.get(position).url).into(holder.multiImg)}
        else{holder.multiImg.isVisible = false}


//        val t = multiList.get(position).playerUrl
//        if(t!=null){
    //
    //    }
//
    }

    override fun getItemCount(): Int {
      return  multiList.size
    }

}