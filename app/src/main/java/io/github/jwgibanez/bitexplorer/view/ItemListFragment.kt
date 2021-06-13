package io.github.jwgibanez.bitexplorer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import io.github.jwgibanez.api.models.Repository
import io.github.jwgibanez.bitexplorer.R
import io.github.jwgibanez.bitexplorer.databinding.FragmentItemListBinding
import io.github.jwgibanez.bitexplorer.databinding.ItemListButtonBinding
import io.github.jwgibanez.bitexplorer.databinding.ItemListContentBinding
import io.github.jwgibanez.bitexplorer.viewmodel.RepositoryListViewModel
import io.github.jwgibanez.bitexplorer.viewmodel.RepositoryListViewModelFactory

class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null

    private val binding get() = _binding!!

    private val viewModel: RepositoryListViewModel by activityViewModels {
        RepositoryListViewModelFactory()
    }

    private var adapter: SimpleItemRecyclerViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            repositories.observe(viewLifecycleOwner) {
                it?.apply {
                    adapter?.addAll(it)
                }
            }
            next.observe(viewLifecycleOwner) {
                adapter?.setNext(it)
            }
        }

        val itemDetailFragmentContainer: View? = view.findViewById(R.id.item_detail_nav_container)

        val onItemClickListener = View.OnClickListener { itemView ->
            val item = itemView.tag as Repository?
            val bundle = Bundle()
            bundle.putSerializable(
                ItemDetailFragment.ARG_ITEM,
                item
            )
            if (itemDetailFragmentContainer != null) {
                itemDetailFragmentContainer.findNavController()
                    .navigate(R.id.fragment_item_detail, bundle)
            } else {
                itemView.findNavController().navigate(R.id.show_item_detail, bundle)
            }
        }

        val onNextClickListener = View.OnClickListener { itemView ->
            val button = itemView.findViewById<TextView>(R.id.button)
            button.text = getString(R.string.loading)
            viewModel.fetchNext()
        }

        setupRecyclerView(binding.itemList, onItemClickListener, onNextClickListener)
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener,
        onNextClickListener: View.OnClickListener
    ) {
        adapter = SimpleItemRecyclerViewAdapter(
            onClickListener,
            onNextClickListener
        )
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    class SimpleItemRecyclerViewAdapter(
        private val onClickListener: View.OnClickListener,
        private val onNextClickListener: View.OnClickListener
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val values: ArrayList<Repository?> = ArrayList()
        private var next: String? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            when (viewType) {
                TYPE_ITEM -> {
                    val binding =
                        ItemListContentBinding.inflate(
                            inflater,
                            parent,
                            false
                        )
                    return ItemViewHolder(binding)
                }
                else -> {
                    val binding =
                        ItemListButtonBinding.inflate(
                            inflater,
                            parent,
                            false
                        )
                    return ButtonViewHolder(binding)
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == values.size) TYPE_BUTTON else TYPE_ITEM
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is ItemViewHolder) {
                val item = values[position]
                holder.idView.text = position.toString()
                holder.contentView.text = item?.name

                with(holder.itemView) {
                    tag = item
                    setOnClickListener(onClickListener)
                }
            } else if (holder is ButtonViewHolder) {
                if (values.isEmpty()) {
                    holder.itemView.visibility = VISIBLE
                    holder.button.text = holder.button.context.getString(R.string.loading)
                    holder.button.setOnClickListener(null)
                } else if (next.isNullOrEmpty()) {
                    holder.itemView.visibility = GONE
                    holder.itemView.setOnClickListener(null)
                } else {
                    holder.itemView.visibility = VISIBLE
                    holder.button.text = holder.button.context.getString(R.string.next)
                    holder.button.setOnClickListener(onNextClickListener)
                }
            }
        }

        override fun getItemCount() = values.size + 1

        inner class ItemViewHolder(binding: ItemListContentBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val idView: TextView = binding.idText
            val contentView: TextView = binding.content
        }

        inner class ButtonViewHolder(binding: ItemListButtonBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val button: TextView = binding.button
        }

        fun setNext(next: String?) {
            this.next = next
        }

        fun addAll(repositories: List<Repository?>) {
            repositories.forEach {
                if (!values.contains(it)) {
                    values.add(it)
                }
            }
            notifyDataSetChanged()
        }

        companion object {
            const val TYPE_ITEM = 0
            const val TYPE_BUTTON = 1
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}