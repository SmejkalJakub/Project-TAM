package com.tama.movieswiper

import android.R.attr
import android.R.attr.*
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tama.movieswiper.database.User
import com.tama.movieswiper.ui.groups.GroupDetailViewModel
import com.tama.movieswiper.ui.groups.GroupModel
import com.tama.movieswiper.ui.groups.GroupsViewModel
import com.tama.movieswiper.ui.login.LoginViewModel
import com.tama.movieswiper.ui.login.UserModel
import com.tama.movieswiper.ui.profile.ProfileViewModel
import com.tama.movieswiper.ui.register.RegisterViewModel
import android.graphics.Color
import android.opengl.Visibility
import android.view.Gravity

import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.marginBottom
import com.tama.movieswiper.database.GenreModel
import com.tama.movieswiper.databinding.*
import com.tama.movieswiper.imdb.MoviesAsynchronousGet
import com.tama.movieswiper.ui.find_movie.FindMovieViewModel
import org.json.JSONArray


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var movieIndex: Int = 0

    var currentMovieGenres = JSONArray()

    var currentUserPreferences = GenreModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navView.visibility = View.GONE

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_find_movie, R.id.navigation_groups, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        var auth = Firebase.auth
        val currentUser = auth.currentUser

        get_user_genre_preferences()

        if(currentUser != null){
            show_navView(true)
            return
        }
    }

    fun get_user_genre_preferences()
    {
        var auth = Firebase.auth
        val currentUser = auth.currentUser

        val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference

        database.child("users").child(currentUser?.uid.toString()).child("genres").get().addOnSuccessListener {
            currentUserPreferences.action =         it.child("action").value.toString().toInt()
            currentUserPreferences.adult =          it.child("adult").value.toString().toInt()
            currentUserPreferences.adventure =      it.child("adventure").value.toString().toInt()
            currentUserPreferences.animation =      it.child("animation").value.toString().toInt()
            currentUserPreferences.biography =      it.child("biography").value.toString().toInt()
            currentUserPreferences.comedy =         it.child("comedy").value.toString().toInt()
            currentUserPreferences.crime =          it.child("crime").value.toString().toInt()
            currentUserPreferences.documentary =    it.child("documentary").value.toString().toInt()
            currentUserPreferences.drama =          it.child("drama").value.toString().toInt()
            currentUserPreferences.family =         it.child("family").value.toString().toInt()
            currentUserPreferences.fantasy =        it.child("fantasy").value.toString().toInt()
            currentUserPreferences.film_noir =      it.child("film_noir").value.toString().toInt()
            currentUserPreferences.game_show =      it.child("game_show").value.toString().toInt()
            currentUserPreferences.history =        it.child("history").value.toString().toInt()
            currentUserPreferences.horror =         it.child("horror").value.toString().toInt()
            currentUserPreferences.music =          it.child("music").value.toString().toInt()
            currentUserPreferences.musical =        it.child("musical").value.toString().toInt()
            currentUserPreferences.mystery =        it.child("mystery").value.toString().toInt()
            currentUserPreferences.news =           it.child("news").value.toString().toInt()
            currentUserPreferences.reality_tv =     it.child("reality_tv").value.toString().toInt()
            currentUserPreferences.romance =        it.child("romance").value.toString().toInt()
            currentUserPreferences.sci_fi =         it.child("sci_fi").value.toString().toInt()
            currentUserPreferences.short =          it.child("short").value.toString().toInt()
            currentUserPreferences.sport =          it.child("sport").value.toString().toInt()
            currentUserPreferences.talk_show =      it.child("talk_show").value.toString().toInt()
            currentUserPreferences.thriller =       it.child("thriller").value.toString().toInt()
            currentUserPreferences.war =            it.child("war").value.toString().toInt()
            currentUserPreferences.western =        it.child("western").value.toString().toInt()

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun show_navView(state: Boolean)
    {
        if(state)
        {
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.navigation_find_movie)
            binding.navView.visibility = View.VISIBLE
        }
        else
        {
            binding.navView.visibility = View.GONE
        }
    }

    fun create_user(user: UserModel)
    {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        if(user.password != user.passwordConfirmation)
        {
            Toast.makeText(baseContext, "Password Mismatch",
                Toast.LENGTH_SHORT).show()
            return
        }

        user.auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = user.auth.currentUser

                    val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference
                    if (currentUser != null) {
                        val databaseUser = User(currentUser.uid, currentUser.email)
                        database.child("users").child(currentUser.uid.toString()).setValue(databaseUser)
                        database.child("users").child(currentUser.uid.toString()).child("genres").setValue(GenreModel())
                    }
                    get_user_genre_preferences()
                    navController.navigate(R.id.navigation_find_movie)
                    show_navView(true)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                }
            }
    }

    fun update_user(userModel: UserModel)
    {
        val user = Firebase.auth.currentUser

        if(userModel.password != userModel.passwordConfirmation)
        {
            Toast.makeText(baseContext, "Password Mismatch",
                Toast.LENGTH_SHORT).show()
            return
        }

        user?.updateEmail(userModel.email)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
            }
        user?.updatePassword(userModel.password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password updated.")
                }
            }
    }

    fun join_group(groupName: String)
    {
        val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference

        database.child("groups").child(groupName).get().addOnSuccessListener {

            // Group does not exist
            if(it.value == null)
                return@addOnSuccessListener

            val currentUser = Firebase.auth.currentUser

            // User already in the group
            if(it.child("users").child(currentUser?.uid.toString()).value != null)
                return@addOnSuccessListener

            database.child("groups").child(groupName).child("users").
            child(currentUser?.uid.toString()).setValue(User(currentUser?.uid.toString(), currentUser?.email))

            database.child("users").child(currentUser?.uid.toString()).child("groups").child(groupName).child("name").setValue(groupName)

            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_groups)

            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun show_group_users(_binding: GroupDetailFragmentBinding, groupName: String?)
    {
        val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference

        val currentUser = Firebase.auth.currentUser


        database.child("groups").child(groupName!!).get().addOnSuccessListener {
            if(it.child("owner").value.toString() == currentUser?.uid.toString())
            {
                _binding.deleteButton.visibility = View.VISIBLE
                _binding.leaveGroupButton.visibility = View.GONE
            }
            else
            {
                _binding.deleteButton.visibility = View.GONE
                _binding.leaveGroupButton.visibility = View.VISIBLE
            }

            it.child("users").children.forEach {
                var text = TextView(this)
                var mail: String = it.child("email").value.toString()
                var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,

                )
                params.setMargins(0,0,0,20);
                text.setLayoutParams(params);

                text.textSize = 20.0f
                text.setPadding(15,15,15,15)
                text.textAlignment = View.TEXT_ALIGNMENT_CENTER

                text.setTextColor(Color.DKGRAY)
                text.setBackgroundColor(Color.WHITE)
                text.text = mail

                _binding.linearLayout.addView(text)
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun leave_group(groupName: String?)
    {
        val currentUser = Firebase.auth.currentUser

        val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference
        database.child("groups").child(groupName!!).get().addOnSuccessListener {

            database.child("groups").child(groupName).child("users").child(currentUser?.uid.toString()).removeValue()

            database.child("users").child(currentUser?.uid.toString()).child("groups").child(groupName).removeValue()

            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_groups)

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun delete_group(groupName: String?)
    {
        val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference
        database.child("groups").child(groupName!!).get().addOnSuccessListener {

            it.child("users").children.forEach {
                database.child("users").child(it.key.toString()).child("groups").child(groupName).removeValue()
            }

            database.child("groups").child(groupName).removeValue()

            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_groups)

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }


    }

    // FIX THIS FOR MANY GROUPS
    fun add_button(_binding: GroupsFragmentBinding, groupsViewModel: GroupsViewModel)
    {
        val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference

        val currentUser = Firebase.auth.currentUser

        database.child("users").child(currentUser?.uid.toString()).get().addOnSuccessListener {
            it.child("groups").children.forEach {
                var button = Button(this)
                var name: String = it.key.toString()
                var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                )
                params.setMargins(0,0,0,20);
                button.setLayoutParams(params);

                button.text = name
                button.textSize = 20.0f
                button.setPadding(15,15,15,15)
                button.setBackgroundColor(Color.LTGRAY)

                button.setOnClickListener{ groupsViewModel.show_detail(name, findNavController(R.id.nav_host_fragment_activity_main)) }

                _binding.linear.addView(button)
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun create_group(groupName: String)
    {
        val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference

        database.child("groups").child(groupName).get().addOnSuccessListener {
            if(it.value == null)
            {
                val currentUser = Firebase.auth.currentUser

                var group = GroupModel(groupName, currentUser?.uid.toString())

                database.child("groups").child(groupName).setValue(group);

                database.child("groups").child(groupName).child("users").
                child(currentUser?.uid.toString()).setValue(User(currentUser?.uid.toString(), currentUser?.email))

                database.child("users").child(currentUser?.uid.toString()).child("groups").child(groupName).child("name").setValue(groupName)

                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                navController.navigate(R.id.navigation_groups)
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    fun auth_user(user: UserModel)
    {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        user.auth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.navView.visibility = View.VISIBLE
                    get_user_genre_preferences()
                    navController.navigate(R.id.navigation_find_movie)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                }
            }
    }

    fun set_login_observer(loginViewModel: LoginViewModel)
    {
        loginViewModel.loginSuccesful.observe(this, Observer { event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                show_navView(it)
            }
        })

        loginViewModel.loginUser.observe(this, Observer { event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                auth_user(it)
            }
        })
    }

    fun set_profile_observer(profileViewModel: ProfileViewModel)
    {
        profileViewModel.updateUser.observe(this, Observer { event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                update_user(it)
            }
        })
    }


    fun set_register_observer(registerViewModel: RegisterViewModel)
    {
        registerViewModel.registerUser.observe(this, Observer { event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                create_user(it)
            }
        })
    }

    fun update_preferences(genre: String, liked: Boolean)
    {
        var addition = 0

        if(liked)
        {
            addition = 3
        }
        else
        {
            addition = -1
        }

        when (genre)
        {
            "Action" -> currentUserPreferences.action += addition
            "Adult" -> currentUserPreferences.adult += addition
            "Adventure" -> currentUserPreferences.adventure += addition
            "Animation" -> currentUserPreferences.animation += addition
            "Biography" -> currentUserPreferences.biography += addition
            "Comedy" -> currentUserPreferences.comedy += addition
            "Crime" -> currentUserPreferences.crime += addition
            "Documentary" -> currentUserPreferences.documentary += addition
            "Drama" -> currentUserPreferences.drama += addition
            "Family" -> currentUserPreferences.family += addition
            "Fantasy" -> currentUserPreferences.fantasy += addition
            "Film-Noir" -> currentUserPreferences.film_noir += addition
            "Game-Show" -> currentUserPreferences.game_show += addition
            "History" -> currentUserPreferences.history += addition
            "Horror" -> currentUserPreferences.horror += addition
            "Musical" -> currentUserPreferences.music += addition
            "Music" -> currentUserPreferences.musical += addition
            "Mystery" -> currentUserPreferences.mystery += addition
            "News" -> currentUserPreferences.news += addition
            "Reality-TV" -> currentUserPreferences.reality_tv += addition
            "Romance" -> currentUserPreferences.romance += addition
            "Sci-Fi" -> currentUserPreferences.sci_fi += addition
            "Short" -> currentUserPreferences.short += addition
            "Sport" -> currentUserPreferences.sport += addition
            "Talk-Show" -> currentUserPreferences.talk_show += addition
            "Thriller" -> currentUserPreferences.thriller += addition
            "War" -> currentUserPreferences.war += addition
            "Western" -> currentUserPreferences.western += addition
        }
    }

    fun set_find_movie_binding(movieBinding: FindMovieFragmentBinding, movieAsync: MoviesAsynchronousGet, findMovieViewModel: FindMovieViewModel, _movieIndex: Int)
    {
        findMovieViewModel.movieGenres.observe(this, Observer { genres ->
            currentMovieGenres = genres
        })
        movieIndex = _movieIndex
        movieBinding.movieFragmentFrame.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                //HATE
                movieIndex++

                for (i in 0 until currentMovieGenres.length()) {
                    val item = currentMovieGenres[i]
                    update_preferences(item.toString(), false)
                }
                var auth = Firebase.auth
                val currentUser = auth.currentUser

                val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference
                database.child("users").child(currentUser?.uid.toString()).child("genres").setValue(currentUserPreferences)

                movieAsync.getMovieDetails(findMovieViewModel, movieIndex)
                Toast.makeText(this@MainActivity, "Swipe Left gesture detected",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            override fun onSwipeRight() {
                super.onSwipeRight()
                //LIKE
                movieIndex++
                for (i in 0 until currentMovieGenres.length()) {
                    val item = currentMovieGenres[i]
                    update_preferences(item.toString(), true)
                }
                var auth = Firebase.auth
                val currentUser = auth.currentUser

                val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference
                database.child("users").child(currentUser?.uid.toString()).child("genres").setValue(currentUserPreferences)

                movieAsync.getMovieDetails(findMovieViewModel, movieIndex)
                Toast.makeText(
                    this@MainActivity,
                    "Swipe Right gesture detected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


}