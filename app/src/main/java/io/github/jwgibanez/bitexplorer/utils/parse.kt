package io.github.jwgibanez.bitexplorer.utils

import androidx.lifecycle.MutableLiveData
import io.github.jwgibanez.api.models.*
import io.github.jwgibanez.bitexplorer.view.ItemDetailFragment
import kotlin.reflect.full.declaredMemberProperties

fun parse(
    liveData: MutableLiveData<ArrayList<ItemDetailFragment.InfoItemRecyclerViewAdapter.ValuePair>>,
    repository: Repository?
) {
    repository?.apply {
        val values = ArrayList<ItemDetailFragment.InfoItemRecyclerViewAdapter.ValuePair>()

        for (prop in Repository::class.declaredMemberProperties) {
            when (val member = prop.get(repository)) {
                is Project -> {
                    for (pProp in Project::class.declaredMemberProperties) {
                        when (val m = pProp.get(member)) {
                            is Links -> {
                                for (lProp in Links::class.declaredMemberProperties) {
                                    addValue(values, "${prop.name}.${pProp.name}.${lProp.name}", lProp.get(m))
                                }
                            }
                            else -> addValue(values, "${prop.name}.${pProp.name}", pProp.get(member))
                        }
                    }
                }
                is Links -> {
                    for (pProp in Links::class.declaredMemberProperties) {
                        if (pProp.name == "clone") {
                            (pProp.get(member) as List<Link>).forEach {
                                addValue(values, "${prop.name}.${pProp.name}.${it.name}", it)
                            }
                        } else {
                            addValue(values, "${prop.name}.${pProp.name}", pProp.get(member))
                        }
                    }
                }
                is Owner -> {
                    for (pProp in Owner::class.declaredMemberProperties) {
                        when (val m = pProp.get(member)) {
                            is Links -> {
                                for (lProp in Links::class.declaredMemberProperties) {
                                    addValue(values, "${prop.name}.${pProp.name}.${lProp.name}", lProp.get(m))
                                }
                            }
                            else -> addValue(values, "${prop.name}.${pProp.name}", pProp.get(member))
                        }
                    }
                }
                is Workspace -> {
                    for (pProp in Workspace::class.declaredMemberProperties) {
                        when (val m = pProp.get(member)) {
                            is Links -> {
                                for (lProp in Links::class.declaredMemberProperties) {
                                    addValue(values, "${prop.name}.${pProp.name}.${lProp.name}", lProp.get(m))
                                }
                            }
                            else -> addValue(values, "${prop.name}.${pProp.name}", pProp.get(member))
                        }
                    }
                }
                is Branch -> {
                    for (pProp in Branch::class.declaredMemberProperties) {
                        addValue(values, "${prop.name}.${pProp.name}", pProp.get(member))
                    }
                }
                else -> {
                    addValue(values, prop.name, member)
                }
            }
        }

        liveData.postValue(values)
    }
}

private fun addValue(
    values: ArrayList<ItemDetailFragment.InfoItemRecyclerViewAdapter.ValuePair>,
    name: String, value: Any?
) {
    value?.let {
        values.add(ItemDetailFragment.InfoItemRecyclerViewAdapter.ValuePair(name, it))
    }
}