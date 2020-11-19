package com.demo.movies.ui.allmovies

import android.content.Context
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.movies.R
import com.demo.movies.models.Movie
import com.demo.movies.util.ImgPathBuilder
import com.demo.movies.util.PrefsHelper

class AllMoviesAdapter(
  val movieListener: (Movie) -> Unit,
  private val prefsHelper: PrefsHelper,
  private val pathBuilder: ImgPathBuilder
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

  override fun onBindViewHolder(holder: AllMoviesViewHolder, position: Int) {

    val imageUrl = prefsHelper.read()

    imageUrl?.let {
      holder.bind(
        context = context,
        movieListener = movieListener,
        pathBuilder,
        movie = movies[position])
    }
  }

  override fun getItemCount() = movies.size
}