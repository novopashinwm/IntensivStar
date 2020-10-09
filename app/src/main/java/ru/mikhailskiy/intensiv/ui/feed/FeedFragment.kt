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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Tag
import ru.mikhailskiy.intensiv.BuildConfig
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.Movie
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.network.MoviesResponse
import ru.mikhailskiy.intensiv.ui.afterTextChanged
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment.Companion.BASE_URL
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

        getNowPlaying
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val movies = it.results
                val nowMoviesList = listOf(movies?.map {
                    MovieItem(it) { movie -> openMovieDetails(movie) }
                }?.toList()?.let { MainCardContainer(R.string.now_playing, it) })
                movies_recycler_view.adapter = adapter.apply { addAll(nowMoviesList) }
            },
                { t->Timber.e(t, t.toString())})



        getUpcoming
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val movies = it.results
                val upCompingMovies = listOf(movies?.map {
                    MovieItem(it) { movie -> openMovieDetails(movie) }
                }?.toList()?.let { MainCardContainer(R.string.upcoming, it) })
                adapter.apply { addAll(upCompingMovies) }
            },
                { t->Timber.e(t, t.toString())})

        getPopular
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val movies = it.results
                val popular = listOf(movies?.map {
                    MovieItem(it) { movie -> openMovieDetails(movie) }
                }?.toList()?.let { MainCardContainer(R.string.popular, it) })
                adapter.apply { addAll(popular) }
            },
                { t->Timber.e(t, t.toString())})


    }

    private fun openMovieDetails(movie: Movie) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putString("title", movie.title)
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

    companion object {
        const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
        const val BASE_URL= BuildConfig.BASE_URL
    }
}