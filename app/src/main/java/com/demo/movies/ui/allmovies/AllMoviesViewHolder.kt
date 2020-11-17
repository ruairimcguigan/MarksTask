package com.demo.movies.ui.allmovies

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.demo.movies.models.Movie
import com.demo.movies.util.ImgPathBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_item.view.*

class AllMoviesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bind(
        context: Context,
        movieListener: (Movie) -> Unit,
        pathBuilder: ImgPathBuilder,
        movie: Movie
    ){
        val posterView: ImageView = itemView.poster
        val moviePoster = pathBuilder.buildPosterPath(movie.posterPath)
        Picasso.with(context).load(moviePoster).into(posterView)
        itemView.setOnClickListener { movieListener(movie)}
    }
}

