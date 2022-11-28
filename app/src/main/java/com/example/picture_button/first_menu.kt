package com.example.picture_button

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization par pameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [first_menu.newInstance] factory method to
 * create an instance of this fragment.
 */
class first_menu : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.start_menu,container,false)
        view.findViewById<Button>(R.id.player_button).setOnClickListener{Navigation.findNavController(view).navigate(R.id.action_first_menu_to_secondFragment)}
        return view
    }
}