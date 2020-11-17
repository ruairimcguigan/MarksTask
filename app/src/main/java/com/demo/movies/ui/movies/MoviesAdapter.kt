package com.demo.movies.ui.movies

import android.content.Context
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.movies.R
import com.demo.movies.models.Movie

class MoviesAdapter : RecyclerView.Adapter<MoviesViewHolder>() {

  private lateinit var context: Context
  private val movies = mutableListOf<Movie>()

  fun populate(movieList: ArrayList<Movie>) {
    this.movies.clear()
    this.movies.addAll(movieList)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MoviesViewHolder {
    context = parent.context
    return MoviesViewHolder(from(parent.context).inflate(
      R.layout.movie_item,
      parent,
      false)
    )
  }

  override fun onBindViewHolder(holder: MoviesViewHolder, position: Int)
          = holder.bind(context, movies[position])

  override fun getItemCount() = movies.size
}