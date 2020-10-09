package ru.mikhailskiy.intensiv.ui.tvshows

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.network.TvResponse


class TVItem(
    private val content: TvResponse.TVShow,
    private val onClick: (tv: TvResponse.TVShow) -> Unit
) : Item() {


    override fun getLayout() = R.layout.item_tv_show

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.name //content.title
        viewHolder.movie_rating.rating = (content.voteAverage.toFloat()/2.0).toFloat()
        viewHolder.content.setOnClickListener {
            onClick.invoke(content)
        }

        // TODO Получать из модели
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" + content.posterPath)
            .into(viewHolder.image_preview)
    }
}

private operator fun Any.invoke(content: TvResponse.TVShow) {

}
