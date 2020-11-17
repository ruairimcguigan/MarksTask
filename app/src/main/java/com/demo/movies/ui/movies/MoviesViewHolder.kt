package com.demo.movies.ui.movies

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.demo.movies.models.Movie

class MoviesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bind(context: Context, movie: Movie){
        val movieImage = movie.backdropPath
    }

    private fun processMoviePosters(movies: List<Movie>) {
        val posters = arrayListOf<String>()
        movies.forEach { movie ->
            val moviePoster = StringBuilder()
                .append(MoviesActivity.imageBase)
                .append(movie.posterPath)
                .toString()
            posters.add(moviePoster)
        }
    }
}

