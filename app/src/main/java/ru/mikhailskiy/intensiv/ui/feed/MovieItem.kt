package ru.mikhailskiy.intensiv.ui.feed

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.Movie
import ru.mikhailskiy.intensiv.network.MoviesResponse

class MovieItem(
    private val content: MoviesResponse.Movie,
    private val onClick: (movie: Movie) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_with_text

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.title //content.title
        viewHolder.movie_rating.rating = (content.voteAverage.toFloat()/2.0).toFloat()
        viewHolder.content.setOnClickListener {
            onClick.invoke(content)
        }

        // TODO Получать из модели
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" +content.posterPath)
            .into(viewHolder.image_preview)
    }
}

private fun Any.invoke(content: MoviesResponse.Movie) {
    println("test1")
    println(content)
    //Toast.makeText( this ,content.toString(), Toast.LENGTH_LONG)
}
