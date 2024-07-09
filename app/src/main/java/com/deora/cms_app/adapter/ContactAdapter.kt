package com.deora.cms_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deora.cms_app.contact.Contact
import com.deora.cms_app.databinding.ItemContactBinding
import com.deora.cms_app.listener.ListAction

class ContactAdapter(
    private val context: Context,
     var dataSource: MutableList<Contact>,
    private val action: ListAction
) : RecyclerView.Adapter<ContactAdapter.ContactVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactVH {
        val itemBinding = ItemContactBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContactVH(itemBinding)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: ContactVH, position: Int) {
        holder.bind(dataSource[position])
    }

    fun updateList(newContacts: List<Contact>) {
        dataSource.clear()
        dataSource.addAll(newContacts)
        notifyDataSetChanged()
    }

    inner class ContactVH(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.txName.text = contact.name
            binding.txMobileNo.text = contact.mobileNo
            binding.txEmailAddress.text = contact.email

            binding.linContactItem.setOnClickListener {
                action.onClick(contact)
            }
        }
    }
}
