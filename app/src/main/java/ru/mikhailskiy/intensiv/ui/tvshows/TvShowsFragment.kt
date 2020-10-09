package ru.mikhailskiy.intensiv.ui.tvshows

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.data.Movie
import ru.mikhailskiy.intensiv.network.MovieApiClient

import ru.mikhailskiy.intensiv.network.TvResponse
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment.Companion.API_KEY
import ru.mikhailskiy.intensiv.ui.feed.MainCardContainer
import timber.log.Timber

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TvShowsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private val getTVPopular by lazy { MovieApiClient.apiClient.getTVPopular(API_KEY, getString(R.string.lang_ru)) }
    private var param1: String? = null
    private var param2: String? = null

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        getTVPopular.enqueue(object : Callback<TvResponse> {
            override fun onResponse(call: Call<TvResponse>, response: Response<TvResponse>) {
                val movies = response.body()?.results

                val nowMoviesList = movies?.map {
                    TVItem(it) { tv -> openTVDetails(tv) }
                }?.toList()
                tv_recycler_view.adapter = adapter.apply { nowMoviesList?.let { addAll(it) } }

            }

            override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                Timber.e(call.toString(), t.toString())
            }
        })
        return inflater.inflate(R.layout.tv_shows_fragment, container, false)
    }

    private fun openTVDetails(tv: TvResponse.TVShow) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TvShowsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}