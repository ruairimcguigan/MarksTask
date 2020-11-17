package com.demo.movies.ui.moviedetails

import android.content.Context
import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.movies.R
import com.demo.movies.models.Constituent
import com.demo.movies.models.Part
import com.demo.movies.util.ImgPathBuilder
import com.demo.movies.util.PrefsHelper

class CollectionAdapter(
  private val collectionListener: (Part) -> Unit,
  private val prefsHelper: PrefsHelper,
  private val pathBuilder: ImgPathBuilder
) : RecyclerView.Adapter<CollectionViewHolder>() {

  private lateinit var context: Context
  private val collection: ArrayList<Part> = ArrayList()

  fun populate(constituent: Constituent) {

    collection.clear()
    constituent.parts.forEach { collection.add(it) }
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): CollectionViewHolder {
    context = parent.context
    return CollectionViewHolder(from(parent.context).inflate(
      R.layout.collection_item,
      parent,
      false)
    )
  }

  override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {

    val imageUrl = prefsHelper.read()

    imageUrl?.let {
      holder.bind(
        context = context,
        collectionListener = collectionListener,
        pathBuilder,
        constituent = collection[position])
    }
  }

  override fun getItemCount() = collection.size
}