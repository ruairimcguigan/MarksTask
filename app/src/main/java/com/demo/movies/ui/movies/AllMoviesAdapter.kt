package com.demo.movies.ui.movies

import android.content.Context
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.movies.R
import com.demo.movies.models.Movie
import com.demo.movies.util.PrefsHelper

class AllMoviesAdapter constructor(
  private val prefsHelper: PrefsHelper
) : RecyclerView.Adapter<AllMoviesViewHolder>() {

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
  ): AllMoviesViewHolder {
    context = parent.context
    return AllMoviesViewHolder(from(parent.context).inflate(
      R.layout.movie_item,
      parent,
      false)
    )
  }

  override fun onBindViewHolder(holderAll: AllMoviesViewHolder, position: Int) {

    val imageUrl = prefsHelper.read(context)

    imageUrl?.let {
      holderAll.bind(
        context = context,
        imageUrl = it,
        movie = movies[position])
    }
  }

  override fun getItemCount() = movies.size
}