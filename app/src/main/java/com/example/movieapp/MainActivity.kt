package com.example.movieapp
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.movieapp.databinding.ActivityFragmentBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFragmentBinding.inflate(layoutInflater)

        // Begin the transaction
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(binding.fragmentContainer.id, MainFragment())
        ft.commit()

        setContentView(binding.root)
    }


}

