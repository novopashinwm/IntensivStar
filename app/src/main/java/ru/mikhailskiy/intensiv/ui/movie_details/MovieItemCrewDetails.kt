package ru.mikhailskiy.intensiv.ui.movie_details

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_artist.*
import kotlinx.android.synthetic.main.item_artist.description
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.network.MovieDetailsTeamResponse

class MovieItemCrewDetails(
    private val content: MovieDetailsTeamResponse.Crew
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.name
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" +content.profilePath)
            .into(viewHolder.foto_preview)
    }

    override fun getLayout(): Int = R.layout.item_artist
}
