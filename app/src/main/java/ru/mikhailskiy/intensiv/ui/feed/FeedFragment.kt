package ru.mikhailskiy.intensiv.ui.feed

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.db.MovieEntity
import ru.mikhailskiy.intensiv.db.MovieDatabase
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.network.MoviesResponse
import ru.mikhailskiy.intensiv.ui.afterTextChanged
import ru.mikhailskiy.intensiv.util.init
import timber.log.Timber

class FeedFragment : Fragment() {
    val getNowPlaying by lazy { MovieApiClient.apiClient.getNowPlaying(API_KEY, getString(R.string.lang_ru)) }
    val getUpcoming by lazy { MovieApiClient.apiClient.getUpcoming(API_KEY, getString(R.string.lang_ru)) }
    val getPopular by lazy { MovieApiClient.apiClient.getPopular(API_KEY, getString(R.string.lang_ru)) }

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Добавляем recyclerView
        movies_recycler_view.layoutManager = LinearLayoutManager(context)
        movies_recycler_view.adapter = adapter.apply { addAll(listOf()) }

        search_toolbar.search_edit_text.afterTextChanged {
            Timber.d(it.toString())
            if (it.toString().length > 3) {
                openSearch(it.toString())
            }
        }

        getPopular
            .init()
                //Я не понял, что я тут могу сделать с map - что нужно написать в MovieMapper?
            .map { movie -> toConvertListMoviesEntity(movie)  }
            .subscribe{ it -> val db = context?.let { it1 -> MovieDatabase.get(it1).movieDao() }
                    if (db != null) {
                        db.insertAll(it)
                    }

            }
        context?.let {
            MovieDatabase.get(it).movieDao().loadAll()
                .init()
                .subscribe  {
                    for (movie in it) {
                        println("${movie.id} ${movie.title}")
                    }

                }
        }
        Single.zip(getNowPlaying, getUpcoming, getPopular,
                Function3<MoviesResponse, MoviesResponse, MoviesResponse,
                        List<MainCardContainer>> { now, upcom, pop ->
                    listOf(
                        MainCardContainer(R.string.now_playing, toConvertListMovies(now)),
                        MainCardContainer(R.string.upcoming, toConvertListMovies(upcom)),
                        MainCardContainer(R.string.popular, toConvertListMovies(pop))
                    )
                }
            )
            .init()
            .doOnSubscribe {  progress_movies.visibility = View.VISIBLE }
            .doOnTerminate {  progress_movies.visibility = View.GONE   }
            .subscribe { it -> movies_recycler_view.adapter = adapter.apply { addAll(it) } }
    }

    fun convertMovie(dto: MoviesResponse.Movie):MovieEntity{ return MovieEntity(dto.id, dto.adult, dto.backdropPath
        /*,dto.genreIds*/, dto.originalLanguage, dto.originalTitle, dto.overview, dto.popularity, dto.posterPath
    , dto.releaseDate, dto.title, dto.video, dto.voteAverage, dto.voteCount) }

    private fun toConvertListMoviesEntity(response: MoviesResponse) =
        response.results.filter { movie -> movie.title != null  }
            .map { movie -> convertMovie(movie)  }.toList()
    private fun toConvertListMovies(now: MoviesResponse) =
        now.results.filter { movie -> movie.title != null }
            .map { movie -> MovieItem(movie) { openMovieDetails(movie) } }.toList()

    private fun openMovieDetails(movie: MoviesResponse.Movie) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putInt("MOVIE_ID", movie.id)

        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun openSearch(searchText: String) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putString("search", searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    //Single и Observable в init дублируют код друг друга - можно эту проблему решить?




    companion object {
        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
        const val BASE_URL= BuildConfig.BASE_URL
    }
}