package com.demo.movies.ui.movies

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.demo.movies.models.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item.view.*

class MoviesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bind(context: Context, movie: Movie){

        val moviePoster = StringBuilder()
            .append(imageBase)
            .append(movie.posterPath)
            .toString()

        val posterView: ImageView = itemView.poster
        Picasso.with(context).load(moviePoster).into(posterView)
    }

    companion object {
        val imageBase = "https://image.tmdb.org/t/p/w300"
    }
}

