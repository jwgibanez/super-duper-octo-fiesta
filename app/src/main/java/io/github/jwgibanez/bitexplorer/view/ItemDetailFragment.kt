package io.github.jwgibanez.bitexplorer.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.github.jwgibanez.api.models.*
import io.github.jwgibanez.bitexplorer.databinding.FragmentItemDetailBinding
import io.github.jwgibanez.bitexplorer.databinding.ItemListValuePairBinding
import io.github.jwgibanez.bitexplorer.viewmodel.RepositoryViewModel
import io.github.jwgibanez.bitexplorer.viewmodel.RepositoryViewModelFactory

open class ItemDetailFragment : Fragment() {

    private var _binding: FragmentItemDetailBinding? = null

    private val binding get() = _binding!!

    private var adapter: InfoItemRecyclerViewAdapter? = null

    private val viewModel: RepositoryViewModel by viewModels { RepositoryViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM)) {
                viewModel.setRepository(it.getSerializable(ARG_ITEM) as Repository)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        viewModel.apply {
            repositoryName.observe(viewLifecycleOwner) { name ->
                name?.let { binding.toolbarLayout?.title = it }
            }
            repositoryOwner.observe(viewLifecycleOwner) { owner ->
                owner?.let { binding.toolbarLayout?.title = "${repositoryName.value} by $it" }
            }
            repositoryOwnerAvatar.observe(viewLifecycleOwner) { url ->
                url?.let {
                    binding.avatar?.let {
                        Picasso.get().load(url).into(it)
                    }
                }
            }
            repositoryHtml.observe(viewLifecycleOwner) { html ->
                html?.let {
                    binding.fab?.apply {
                        setOnClickListener { open(html) }
                        visibility = VISIBLE
                    }
                }
            }
            repositoryValues.observe(viewLifecycleOwner) { repository ->
                adapter?.set(repository)
            }
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onItemClickListener = View.OnClickListener {
            // todo
        }

        setupRecyclerView(binding.itemList!!, onItemClickListener)
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener
    ) {
        adapter = InfoItemRecyclerViewAdapter(
            viewModel,
            onClickListener
        )
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun open(url: String) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(requireContext(), Uri.parse(url))
    }

    companion object {
        const val ARG_ITEM = "item"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class InfoItemRecyclerViewAdapter(
        private val viewModel: RepositoryViewModel,
        private val onClickListener: View.OnClickListener,
    ) : RecyclerView.Adapter<InfoItemRecyclerViewAdapter.ItemViewHolder>() {

        private val values = ArrayList<ValuePair>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =
                ItemListValuePairBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            return ItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = values[position]

            holder.name.text = item.name

            val s: Any?
            when (item.value) {
                is String -> {
                    s = item.value as String
                    holder.value.text = if (s.isNotEmpty()) s else "n/a"
                    if (item.name.equals("owner.display_name")) {
                        viewModel.repositoryOwner.value = item.value as String
                    }
                }
                is Boolean -> {
                    s = item.value as Boolean
                    holder.value.text = s.toString()
                }
                is Long -> {
                    s = item.value as Long
                    holder.value.text = s.toString()
                }
                is Link -> {
                    s = item.value as Link
                    holder.value.text = s.href
                    if (item.name.equals("links.html")) {
                        s.href?.let { viewModel.repositoryHtml.value = it }
                    } else if (item.name.equals("owner.links.avatar")) {
                        s.href?.let { viewModel.repositoryOwnerAvatar.value = it }
                    }
                }
                else -> holder.value.text = "n/a"
            }

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        fun set(newValues: ArrayList<ValuePair>) {
            values.apply {
                clear()
                addAll(newValues)
                notifyDataSetChanged()
            }
        }

        override fun getItemCount() = values.size

        inner class ItemViewHolder(binding: ItemListValuePairBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val name: TextView = binding.name
            val value: TextView = binding.value
        }

        data class ValuePair(
            var name: String?,
            var value: Any?
        )
    }
}