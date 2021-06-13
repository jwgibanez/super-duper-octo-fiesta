package io.github.jwgibanez.bitexplorer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import io.github.jwgibanez.api.models.Repository
import io.github.jwgibanez.bitexplorer.databinding.FragmentItemDetailBinding
import io.github.jwgibanez.bitexplorer.databinding.ItemListValuePairBinding
import kotlin.reflect.full.declaredMemberProperties


class ItemDetailFragment : Fragment() {

    private var item: Repository? = null

    private var _binding: FragmentItemDetailBinding? = null

    private val binding get() = _binding!!

    private var adapter: InfoItemRecyclerViewAdapter? = null

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

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onItemClickListener = View.OnClickListener { itemView ->
            // todo
        }

        setupRecyclerView(binding.itemList!!, onItemClickListener)
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onClickListener: View.OnClickListener
    ) {
        adapter = InfoItemRecyclerViewAdapter(
            item,
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

    companion object {
        const val ARG_ITEM = "item"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class InfoItemRecyclerViewAdapter(
        private val repository: Repository?,
        private val onClickListener: View.OnClickListener,
    ) : RecyclerView.Adapter<InfoItemRecyclerViewAdapter.ItemViewHolder>() {

        private val values = ArrayList<ValuePair>()

        init {
            repository?.apply {
                for (prop in Repository::class.declaredMemberProperties) {
                    values.add(ValuePair(prop.name, prop.get(repository)))
                }
            }
        }

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

            when (item.value) {
                is String -> {
                    val s = item.value as String
                    holder.value.text = if (s.isNotEmpty()) s else "n/a"
                }
                else -> holder.value.text = "n/a"
            }

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
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