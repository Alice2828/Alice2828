package com.example.movie.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movie.DetailActivity
import com.example.movie.R
import com.example.movie.model.Movie
import kotlinx.android.synthetic.main.movie_card.view.*

class MoviesAdapter(
    var context: Context,
    var moviesList: List<Movie>? = null
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MovieViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.movie_card, p0, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = moviesList?.size ?: 0

    override fun onBindViewHolder(p0: MovieViewHolder, p1: Int) {
        p0.bind(moviesList?.get(p1))
    }

    fun clearAll() {
        (moviesList as? ArrayList<Movie>)?.clear()
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(post: Movie?) {
            val title = view.findViewById<TextView>(R.id.title)
            val plotsynopsis = view.findViewById<TextView>(R.id.description)
            val userrating = view.findViewById<TextView>(R.id.userrating)
            val thumbnail = view.findViewById<ImageView>(R.id.thumbnail)


            title.text = post?.original_title
            val vote = post?.vote_average
            plotsynopsis.text= post?.overview
            userrating.text = vote.toString()

            Glide.with(context)
                .load(post?.getPosterPath())
                .into(thumbnail)
            //                .placeholder(R.drawable.load)

            view.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("original_title", post?.original_title)/*название*/
                intent.putExtra("poster_path", post?.poster_path)/*фото*/
                intent.putExtra("overview", post?.overview)/*описание*/
                intent.putExtra("vote_average", (post?.vote_average).toString())/*рейтинг*/
                intent.putExtra("relase_date", post?.release_date)/*дата создания*/
                context.startActivity(intent)
            }
        }
    }

    interface RecyclerViewItemClick {

        fun itemClick(position: Int, item: Movie)
    }
}

