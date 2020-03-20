package org.d3if4055.belajarfirebase.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_authors.*

import org.d3if4055.belajarfirebase.R
import org.d3if4055.belajarfirebase.data.Author

class AuthorsFragment : Fragment(), RecyclerViewClickListener {
    // buat view model reference ke AuthorsViewModel
    private lateinit var viewModel: AuthorsViewModel
    private val adapter = AuthorsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inisialisasi viewModel
        viewModel = ViewModelProviders.of(this).get(AuthorsViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authors, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // set listener on this fragment
        adapter.listener = this
        // set adapter untuk recycleview
        recycler_view_authors.adapter = adapter

        // panggil fun yg di view model untuk fetch data
        viewModel.fetchAuthors()

        // panggil fun get realtimeupdate
        viewModel.getRealtimeUpdates()

        viewModel.authors.observe(viewLifecycleOwner, Observer {
            adapter.setAuthors(it)
            Toast.makeText(requireContext(), "author fetched!", Toast.LENGTH_SHORT).show()
        })

        viewModel.author.observe(viewLifecycleOwner, Observer {
            adapter.addAuthor(it)
        })

        // event ketika tombol add di klik
        btn_add.setOnClickListener {
            AddAuthorDialogFragment().show(childFragmentManager,"")
        }
    }

    override fun onRecyclerViewItemClicked(view: View, author: Author) {
        when (view.id) {
            R.id.button_edit -> {
                EditAuthorDialogFragment(author).show(childFragmentManager, "")
            }
            R.id.button_delete -> {
                AlertDialog.Builder(requireContext()).also {
                    it.setTitle(getString(R.string.delete_confirmation))
                    it.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        viewModel.deleteAuthor(author)
                    }
                }.create().show()
            }
        }
    }
}
