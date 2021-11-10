package com.tama.movieswiper

import android.R.attr
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
import com.tama.movieswiper.databinding.ActivityMainBinding
import com.tama.movieswiper.databinding.GroupsFragmentBinding
import com.tama.movieswiper.ui.groups.GroupDetailViewModel
import com.tama.movieswiper.ui.groups.GroupModel
import com.tama.movieswiper.ui.groups.GroupsViewModel
import com.tama.movieswiper.ui.login.LoginViewModel
import com.tama.movieswiper.ui.login.UserModel
import com.tama.movieswiper.ui.profile.ProfileViewModel
import com.tama.movieswiper.ui.register.RegisterViewModel
import android.R.attr.button
import android.graphics.Color
import android.opengl.Visibility
import android.view.Gravity

import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.tama.movieswiper.databinding.CreateGroupFragmentBinding
import com.tama.movieswiper.databinding.GroupDetailFragmentBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
        if(currentUser != null){
            show_navView(true)
            return
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
    }

    fun create_user(user: UserModel)
    {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        user.auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = user.auth.currentUser

                    val database = Firebase.database("https://tama-project-26b9d-default-rtdb.europe-west1.firebasedatabase.app/").reference
                    if (currentUser != null) {
                        val databaseUser = User(currentUser.uid, currentUser.email)
                        database.child("users").child(currentUser?.uid.toString()).setValue(databaseUser)
                    }
                    navController.navigate(R.id.navigation_find_movie)
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

        user!!.updateEmail(userModel.email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
            }
        user!!.updatePassword(userModel.password)
            .addOnCompleteListener { task ->
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
                text.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
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

            database.child("groups").child(groupName!!).child("users").child(currentUser?.uid.toString()).removeValue()

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
                button.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                button.text = name
                button.setBackgroundColor(Color.GREEN)

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


}