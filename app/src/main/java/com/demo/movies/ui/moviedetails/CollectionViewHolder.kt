package com.demo.movies.ui.moviedetails

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.demo.movies.R
import com.demo.movies.models.Part
import com.demo.movies.util.ImgPathBuilder
import com.squareup.picasso.Picasso

class CollectionViewHolder(view: View) : ViewHolder(view) {

    fun bind(
        context: Context,
        collectionListener: (Part) -> Unit,
        pathBuilder: ImgPathBuilder,
        constituent: Part
    ) {
        val posterView: ImageView = itemView.findViewById(R.id.collectionPoster)
        val moviePoster = pathBuilder.buildPosterPath(constituent.posterPath)
        Picasso.with(context).load(moviePoster).into(posterView)
        itemView.setOnClickListener { collectionListener(constituent) }
    }
}