package com.deora.cms_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.deora.cms_app.adapter.ContactAdapter
import com.deora.cms_app.contact.Contact
import com.deora.cms_app.contact.Request
//import com.deora.cms_app.contact.Response
import com.deora.cms_app.databinding.ActivityContactListBinding
import com.deora.cms_app.listener.ListAction
import com.deora.cms_app.network.NetworkClient
import com.deora.cms_app.utils.Constant
import com.deora.cms_app.utils.DataProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ContactListActivity : AppCompatActivity(), ListAction, Callback<com.deora.cms_app.contact.Response> {
    private lateinit var binding: ActivityContactListBinding
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initializeRV()

        binding.btnManageContact.setOnClickListener {
            addContact()
        }
    }

    private fun initializeRV() {
        adapter = ContactAdapter(this, mutableListOf(), this)
        binding.rvContactList.layoutManager = LinearLayoutManager(this)
        binding.rvContactList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        val request = Request(action = Constant.GET_CONTACT)
        val callResponse = NetworkClient.api.makeApiCall(request)
        callResponse.enqueue(this)
    }

    override fun onResponse(call: Call<com.deora.cms_app.contact.Response>, response: Response<com.deora.cms_app.contact.Response>) {
        if (response.isSuccessful && response.body() != null) {
            val serverResponse = response.body()!!
            if (serverResponse.status) {
                adapter.updateList(serverResponse.contacts)
            } else {
                showToast("Contacts are not available")
            }
        } else {
            showToast("Failed to retrieve contacts")
        }
    }

    override fun onFailure(call: Call<com.deora.cms_app.contact.Response>, t: Throwable) {
        showToast("Server is not responding. Please contact your system administrator.")
    }

    private fun addContact() {
        DataProvider.contact = Contact();
        navigateToManageContact()
    }

    private fun navigateToManageContact() {
        Intent(this, ContactManageActivity::class.java).apply {
            startActivity(this)
        }
    }

    override fun onClick(contact: Contact) {
        AlertDialog.Builder(this)
            .setTitle("Action required")
            .setMessage("Please choose relevant action for contact named ${contact.name}")
            .setPositiveButton("Update") { _, _ ->
                updateContact(contact)
            }
            .setNegativeButton("Delete") { _, _ ->
                deleteContact(contact)
            }
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun updateContact(contact: Contact) {
        DataProvider.contact = contact
        navigateToManageContact()
    }

    private fun deleteContact(contact: Contact) {
        val request = Request(
            action = Constant.DELETE_CONTACT,
            id = contact.id
        )
        val callResponse = NetworkClient.api.makeApiCall(request)
        callResponse.enqueue(object : Callback<com.deora.cms_app.contact.Response> {
            override fun onResponse(call: Call<com.deora.cms_app.contact.Response>, response: Response<com.deora.cms_app.contact.Response>) {
                if (response.isSuccessful && response.body() != null) {
                    val serverResponse = response.body()!!
                    if (serverResponse.status) {
                        adapter.updateList(adapter.dataSource.filter { it.id != contact.id }.toMutableList())
                        showToast(serverResponse.message)
                    } else {
                        showToast(serverResponse.message)
                    }
                } else {
                    showToast("Failed to delete contact")
                }
            }

            override fun onFailure(call: Call<com.deora.cms_app.contact.Response>, t: Throwable) {
                showToast("Server is not responding. Please contact your system administrator.")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
