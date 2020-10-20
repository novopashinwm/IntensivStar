package ru.mikhailskiy.intensiv

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.mikhailskiy.intensiv.db.MovieDB
import ru.mikhailskiy.intensiv.db.MovieDatabase
import java.time.Instant
import java.util.*

class MainActivity : AppCompatActivity() {


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        val movie: MovieDB = MovieDB(id = 694919 ,adult = false, backdropPath = "/pq0JSpwyT2URytdFG0euztQPAyR.jpg"
            , genreIds = listOf(), originalLanguage = "en", originalTitle = "Money Plane"
            , overview = "Профессиональный вор, задолжавший сорок миллионов долларов, после угроз семье вынужден согласиться на своё последнее ограбление. Ему предстоит обчистить футуристическое казино, расположенное на борту воздушного лайнера, завсегдатаи которого - самые отпетые преступники в мире."
            , popularity = 1032.342, posterPath = "/6CoRTJTmijhBLJTUNoVSUNxZMEI.jpg", releaseDate = "2020-09-29",
            title = "Денежный самолёт", video = false, voteAverage = 6.0, voteCount = 147)
        val db = MovieDatabase.get(this).movieDao()
        db.insert(movie)

        // Set up Action Bar
        val navController = host.navController

        setupBottomNavMenu(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }
}

