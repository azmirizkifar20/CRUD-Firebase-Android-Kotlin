package org.d3if4055.belajarfirebase.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.dialog_fragment_add_author.*

import org.d3if4055.belajarfirebase.R
import org.d3if4055.belajarfirebase.data.Author

class AddAuthorDialogFragment : DialogFragment() {

    private lateinit var viewModel: AuthorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // mendapatkan viewmodel dari class AuthorViewModels
        viewModel = ViewModelProviders.of(this).get(AuthorsViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_fragment_add_author, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_MinWidth)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // define result on button add author clicked
        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                getString(R.string.author_added)
            } else {
                getString(R.string.error, it.message)
            }

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            dismiss()
        })

        // on click
        btn_add_author.setOnClickListener {
            val name = et_name.text.toString().trim()

            if (name.isEmpty()) {
                input_layout_name.error = getString(R.string.error_field_required)
                return@setOnClickListener
            }

            val author = Author()
            author.name = name
            viewModel.addAuthor(author)
        }
    }

}
