package ru.mikhailskiy.intensiv

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import ru.mikhailskiy.intensiv.db.MovieDatabase
import ru.mikhailskiy.intensiv.db.MovieEntity
import ru.mikhailskiy.intensiv.util.init

class MainActivity : AppCompatActivity() {


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val host: NavHostFragment = my_nav_host_fragment as NavHostFragment?: return
        val movie: MovieEntity = MovieEntity(id = 694919 ,adult = false, backdropPath = "/pq0JSpwyT2URytdFG0euztQPAyR.jpg"
            /*, genreIds = listOf()*/, originalLanguage = "en", originalTitle = "Money Plane"
            , overview = "Профессиональный вор, задолжавший сорок миллионов долларов, после угроз семье вынужден согласиться на своё последнее ограбление. Ему предстоит обчистить футуристическое казино, расположенное на борту воздушного лайнера, завсегдатаи которого - самые отпетые преступники в мире."
            , popularity = 1032.342, posterPath = "/6CoRTJTmijhBLJTUNoVSUNxZMEI.jpg", releaseDate = "2020-09-29",
            title = "Денежный самолёт", video = false, voteAverage = 6.0, voteCount = 147)

        val db = MovieDatabase.get(this).movieDao()

        db.update(movie)
            .init()
            .subscribe ()

        db.loadAll()
            .init()
            .subscribe  {
                for (movie in it) {
                    println("${movie.id} ${movie.title}")
                }
            }
        // Set up Action Bar
        val navController = host.navController
        setupBottomNavMenu(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottom_nav_view?.setupWithNavController(navController)
    }

}

