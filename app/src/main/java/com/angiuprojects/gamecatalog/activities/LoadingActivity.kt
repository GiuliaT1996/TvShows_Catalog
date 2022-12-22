package com.angiuprojects.gamecatalog.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ProgressBar
import android.widget.TextView
import com.angiuprojects.gamecatalog.R
import com.angiuprojects.gamecatalog.entities.implementation.Anime
import com.angiuprojects.gamecatalog.entities.implementation.Manga
import com.angiuprojects.gamecatalog.entities.implementation.TVShow
import com.angiuprojects.gamecatalog.queries.Queries
import com.angiuprojects.gamecatalog.utilities.Constants
import com.angiuprojects.gamecatalog.utilities.Utils

class LoadingActivity : AppCompatActivity() {

    private var animationLength : Long = 4000
    private var progressBarThreshold : Int = 500
    private var maxPercentage : Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        initializeSingletons()
        populateLists()
        animateImage(this)
    }

    private fun initializeSingletons() {
        Constants.initializeConstantSingleton()
        Queries.initializeQueriesSingleton()
        Utils.initializeUtilsSingleton()
    }

    private fun populateLists() {
        Queries.getInstance().select(Constants.getInstance().tvShowDbReference,
            Constants.getInstance().tvShowPath,
            TVShow::class.java,
            Constants.getInstance().getInstanceTvShows())

        Queries.getInstance().select(Constants.getInstance().animeDbReference,
            Constants.getInstance().animePath,
            Anime::class.java,
            Constants.getInstance().getInstanceAnime())

        Queries.getInstance().select(Constants.getInstance().mangaDbReference,
            Constants.getInstance().mangaPath,
            Manga::class.java,
            Constants.getInstance().getInstanceManga())
    }

    private fun animateImage(context: Context) {
        val handler = Handler(Looper.getMainLooper())
        val progressText = findViewById<TextView>(R.id.progress_text)

        val animation: Animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = animationLength

        val progressBar : ProgressBar = findViewById(R.id.progress_bar)
        var i = progressBar.progress

       val nameText = findViewById<TextView>(R.id.app_name)
        nameText.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                Thread {
                    while (i < maxPercentage) {
                        i += 1
                        handler.post {
                            progressBar.progress = i
                            progressText!!.text = getString(R.string.percentage, i)
                        }
                        try {
                            Thread.sleep((animationLength - progressBarThreshold)/maxPercentage)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }.start()
            }
            override fun onAnimationEnd(animation: Animation) {
                val intent = Intent(context, MenuActivity::class.java)
                startActivity(intent)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }
}