package ru.mikhailskiy.intensiv.ui.movie_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_with_text.*
import ru.mikhailskiy.intensiv.R
import ru.mikhailskiy.intensiv.network.MovieApiClient
import ru.mikhailskiy.intensiv.network.MovieDetailsResponse
import ru.mikhailskiy.intensiv.ui.feed.FeedFragment
import timber.log.Timber


private const val ARG_PARAM1 = "title"
private const val ARG_PARAM2 = "MOVIE_ID"

class MovieDetailsFragment : Fragment() {

    private var param1: String? = null
    private var param2: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.movie_details_fragment, container, false)
        val image = view.findViewById<ShapeableImageView>(R.id.imageFilm)
        val txtFilmInfo = view.findViewById<TextView>(R.id.tvFilmInfoText)
        val getMovie by lazy { MovieApiClient.apiClient.getMovie(param2,FeedFragment.API_KEY, getString(R.string.lang_ru)) }

         getMovie
            .init()
            .subscribe({
                txtFilmInfo.text = it.overview
                Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" +it.posterPath)
                    .into(image)
            },
                { t-> Timber.e(t, t.toString())})

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun <T> Single<T>.init(): Single<T> {
        return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}