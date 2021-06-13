package io.github.jwgibanez.bitexplorer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.jwgibanez.api.models.Repository
import io.github.jwgibanez.bitexplorer.databinding.FragmentItemDetailBinding

class ItemDetailFragment : Fragment() {

    private var item: Repository? = null

    lateinit var itemDetailTextView: TextView

    private var _binding: FragmentItemDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM)) {
                item = it.getSerializable(ARG_ITEM) as Repository
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.toolbarLayout?.title = item?.name

        itemDetailTextView = binding.itemDetail

        item?.let {
            itemDetailTextView.text = it.created_on
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM = "item"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}