package ru.mikhailskiy.intensiv.ui.movie_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.movie_details_fragment.view.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.db.MovieDatabase
import ru.mikhailskiy.intensiv.db.MovieEntity
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.network.MovieDetailsResponse
import ru.mikhailskiy.intensiv.network.MovieDetailsTeamResponse
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment
import ru.mikhailskiy.intensiv.ui.feed.MainCardContainer
import ru.mikhailskiy.intensiv.util.init
import timber.log.Timber
import kotlinx.android.synthetic.main.movie_details_fragment.*

private const val ARG_MOVIE_ID = "MOVIE_ID"

class MovieDetailsFragment : Fragment() {

    private var move_id: Int = 0

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
             move_id = it.getInt(ARG_MOVIE_ID)
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.movie_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment

        view.cast_recycler_view.layoutManager = LinearLayoutManager(context)
        view.cast_recycler_view.adapter = adapter.apply { addAll(listOf()) }
        view.crew_recycler_view.layoutManager = LinearLayoutManager(context)
        view.crew_recycler_view.adapter = adapter.apply { addAll(listOf()) }
        val getMovie by lazy { MovieApiClient.apiClient.getMovie(move_id,FeedFragment.API_KEY, getString(R.string.lang_ru)) }
        val getMovieTeam by lazy { MovieApiClient.apiClient.getMovieTeam(move_id, FeedFragment.API_KEY) }
        lateinit var movie : MovieEntity

        getMovie
            .init()
            .subscribe({
                movie = convertMovie(it)
                val db  = context?.let { it1 -> MovieDatabase.get(it1).movieDao() }
                if (db !=  null) {
                    val moviedb = db.get(movie_id = move_id)
                    if (moviedb != null) {
                        addFavoriteMovie.isChecked = true
                    }
                }
                tvFilmInfoText.text = it.overview
                Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" +it.posterPath)
                    .into(imageFilm)
            },
                { t-> Timber.e(t, t.toString())})

        //Еще вопрос - если я уже лайкнул это видео, как при повторном просмотреть, увидеть, что это видео лайкнуто?
        addFavoriteMovie.setOnCheckedChangeListener { buttonView, isChecked ->
            run {
                if (isChecked) {
                    val db = context?.let { it1 -> MovieDatabase.get(it1).movieDao() }
                    if (db != null) {
                        db.update(movie)
                    }
                }
            }
        }

        getMovieTeam
            .init()
            .subscribe({
                val listCast = listOf(MainCardContainer(R.string.cast_details, toConvertListCast( it)))
                val listCrew = listOf(MainCardContainer(R.string.movie_details_crew, toConvertListCrew( it)))

                view.cast_recycler_view.adapter = adapter.apply { addAll(listCast) }
                adapter.apply { addAll(listCrew) }
            }, { t -> Timber.e(t, t.toString()) })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MOVIE_ID, param2)
                }
            }
    }

    fun convertMovie(dto: MovieDetailsResponse):MovieEntity{ return MovieEntity(dto.id, dto.adult, dto.backdropPath
        /*,dto.genreIds*/, dto.originalLanguage, dto.originalTitle, dto.overview, dto.popularity, dto.posterPath
        , dto.releaseDate, dto.title, dto.video, dto.voteAverage, dto.voteCount) }

    private fun toConvertListCast(now: MovieDetailsTeamResponse) =
        now.cast.filter { movie -> movie.name != null }
            .map { movie -> MovieItemCastDetails(movie) }.toList()
    private fun toConvertListCrew(now: MovieDetailsTeamResponse) =
        now.crew.filter { movie -> movie.name != null }
            .map { movie -> MovieItemCrewDetails(movie) }.toList()


}